import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

// YOU for your name?

/**
 * The BigTwo class is used to model a Big Two card game.
 * It has private instance variables for storing a deck of cards, a list of players, a list of hands played on the table, an index of the current player, and a console for providing the user interface.
 * It has methods for getting the deck of cards, list of players, list of hands played on the table and index of current player
 * It also has methods for returning a valid hand from the specified list of cards of the player, and starting the BigTwo game which implements the BigTwo game logics
 * @author Devang
 *
 */
public class BigTwoClient implements CardGame,NetworkGame 
{
	private Deck deck; // Storing deck of cards
	private ArrayList<CardGamePlayer> playerList = new ArrayList<CardGamePlayer>(); // Storing list of players
	private ArrayList<Hand> handsOnTable = new ArrayList<Hand>(); // Storing list of hands played on the table
	private int currentIdx; // Storing an index of the current player
	private BigTwoTable table; // Storing a console for providing the user interface
	private int numOfPlayers = 0; // an integer specifying the number of players.
	private int playerID = -1; // an integer specifying the playerID (i.e., index) of the local player.
	private String playerName; // a string specifying the name of the local player.
	private String serverIP; // a string specifying the IP address of the game server.
	private int serverPort; // an integer specifying the TCP port of the game server.
	private Socket sock; // a socket connection to the game server.
	private ObjectOutputStream oos; // an ObjectOutputStream for sending messages to the server.
	private ObjectInputStream oistream;   // an ObjectInputStream for receiving messages from the server.
	
	/**
	 * Constructor of the BigTwo class
	 * It is used to create the four players of the card game and add them to the list
	 * It is also used initialise the BigTwoConsole object
	 */
	public BigTwoClient()
	{
		// Adding players to the game
		CardGamePlayer a = new CardGamePlayer();
		CardGamePlayer b = new CardGamePlayer();
		CardGamePlayer c = new CardGamePlayer();
		CardGamePlayer d = new CardGamePlayer();
		playerList.add(a);
		playerList.add(b);
		playerList.add(c);
		playerList.add(d);
		setServerIP("127.0.0.1"); // Setting ServerIP
		setServerPort(2396); // Setting Serverport
		table = new BigTwoTable(this);
		makeConnection();
	}
	
	/**
	 * A getter function for getting the number of players.
	 * Overrides the same function in CardGame
	 */
	public int getNumOfPlayers() 
	{
		return playerList.size(); // numOfPlayers, need to set it also
	}
	
	
	
	/**
	 * A getter function to return the deck of cards.
	 * Overrides the same function in CardGame
	 * @return
	 * 		The deck of cards
	 */
	public Deck getDeck()
	{
		return deck;
	}
	
	/**
	 * A getter function to return the list of players.
	 * Overrides the same function in CardGame
	 * @return
	 * 		The list of players
	 */
	public ArrayList<CardGamePlayer> getPlayerList()
	{
		return playerList;
	}
	
	
	/**
	 * A getter function to return the list of hands played on the table.
	 * Overrides the same function in CardGame
	 * @return
	 * 		The list of hands played on the table
	 */
	public ArrayList<Hand> getHandsOnTable()
	{
		return handsOnTable;
	}
	
	
	/**
	 * A getter function to return the index of the current player.
	 * Overrides the same function in CardGame
	 * @return
	 * 		The index of the current player
	 */
	public int getCurrentIdx()
	{
		return currentIdx;
	}
	
	
	/**
	 * A method for starting the game with a (shuffled) deck of cards supplied as the argument. It implements the Big Two game logics.
	 * The shuffled cards are dealt to the player
	 * The Person with the 3 of diamonds is found and the table is enabled accordingly
	 * @param deck
	 * 		The Deck of cards used in the BigTwo game
	 */
	public void start(Deck deck)
	{
		
		for(int i=0;i<4;i++)
		{
			playerList.get(i).removeAllCards();
		}
		// Dealing the cards to each of the players
		for(int i=0;i<13;i++)
		{
			playerList.get(0).addCard(deck.getCard(i));
			playerList.get(1).addCard(deck.getCard(i+13));
			playerList.get(2).addCard(deck.getCard(i+26));
			playerList.get(3).addCard(deck.getCard(i+39));
		}
		// Sorting the cards in hand to ensure easier computation
		for(int i=0;i<4;i++)
		{
			playerList.get(i).sortCardsInHand();
		}
		
		int i=0;
		for(i=0;i<4;i++) // To find the player having the 3 of diamonds
		{
			Card starting = playerList.get(i).getCardsInHand().getCard(0);
			if(starting.getSuit() == 0 && starting.getRank() == 2)
				break;
		}
		currentIdx = i;// Setting currentIdx to player having 2 of diamonds
		table.setActivePlayer(playerID); // Setting the active player of the client 
		table.printMsg(this.getPlayerList().get(currentIdx).getName()+"'s turn\n");
		if(playerID != currentIdx)
			table.disable(); // Disabling the table if it is not the player's move
		else
			table.enable(); // Else enabling it
		table.repaint();
		
	}
	
	/**
	 * Checks the move made by the player.
	 * Sends message to server regarding the move the local player makes
	 * @param playerID
	 *            the playerID of the player who makes the move
	 * @param cardIdx
	 *            the list of the indices of the cards selected by the player
	 */
	public void makeMove(int playerID, int[] cardIdx)
	{
		sendMessage(new CardGameMessage(CardGameMessage.MOVE,-1, cardIdx )); // Sending message to server on every move of the local player
	}
	
	/**
	 * Checks the move made by the player.
	 * Checks all the legality of the moves made.
	 * @param playerID
	 *            the playerID of the player who makes the move
	 * @param cardIdx
	 *            the list of the indices of the cards selected by the player
	 */
	public void checkMove(int playerID, int[] cardIdx)
	{
		int i = playerID; // i holding the playerID of the person who has made the move
		boolean f=false;
		Hand ans = null;
		int A[] = cardIdx;
		CardList chosen = playerList.get(i).play(A); // Getting the chosen cards
		if ((chosen==null || chosen.isEmpty() == true)) // Condition when the user does not select any card
		{
			// If it is the first move of the game or the player is the same who played the last hand, then it is an illegal move
			if (handsOnTable.size() == 0 || ((playerList.get(i).getName().equals(handsOnTable.get(handsOnTable.size()-1).getPlayer().getName()))==true)) 
			{
				table.printMsg("{pass} <== Not a legal move!!!\n");
				table.repaint();
				return;
			}
			else // If some other player apart from player who played the last hand chooses no cards then we break from the inner while loop and pass
			{
				f = true;
			}
		}
		
		if(f==true) // Handles the output needed when it is a legal pass as checked earlier
		{
			table.printMsg("{Pass}\n");
			i++;
			i=i%4;
			currentIdx = i;// Setting currentIdx to player having 2 of diamonds
			table.printMsg(this.getPlayerList().get(currentIdx).getName()+"'s turn\n");
			if(this.playerID != currentIdx)
				table.disable(); // Disabling table if it is not the player's move
			else
				table.enable(); // Else enabling it
			table.repaint();
			return;
		}
		
		ans = composeHand(playerList.get(i),chosen); // Checks whether a hand can be composed, provided some cards have been selected
		if (ans!=null && (handsOnTable.size()!= 0)) // If a hand can be composed it is not the first hand then we check if it beats the previous hand
		{
			Hand lastHand = handsOnTable.get(handsOnTable.size()-1);
			// If the player is the same who played the last hand, then it is always legal, thus we only check beats if the player is different from the one who played the last hand
			if (ans.getPlayer().getName().equals(lastHand.getPlayer().getName())==false) 
			{
				if ((ans.beats(lastHand)==false)) // Then we check if it beats the previous hand, if not, then the hand is not legal
				{
					table.printMsg("{"+ans.getType()+"}" + " ");
					ans = null;
				}
			}				
		}
		if (handsOnTable.size() == 0 && (chosen.contains(new BigTwoCard(0,2)) == false)) // Ensure 1st move always starts with 3 of diamonds, if not, hand is not legal
		{
			if(ans!=null)
				table.printMsg("{"+ans.getType()+"}" + " "); // Printing type of hand, if there is a possibility
			ans = null;
		}
		if(ans==null) 
		{
			table.printMsg(chosen.toString());
			table.printMsg("  <== Not a legal move!!!\n");
			table.repaint();
			return;
		}
		
		// Does the required processing once the hand is legal
		handsOnTable.add(ans);
		table.printMsg("{"+ans.getType()+"}" + " ");
		table.printMsg(ans.toString()+"\n");
		ans.getPlayer().removeCards(ans);
		 
		// Continuing if the game has bot ended
		if(!endOfGame())
		{
			i++; // Moving on to the next player
			i=i%4;
			currentIdx = i;// Setting currentIdx to new player
			table.printMsg(this.getPlayerList().get(currentIdx).getName()+"'s turn\n");
			if(this.playerID != currentIdx)
				table.disable(); // Disabling table if it is not the player's move
			else
				table.enable(); // Else enabling it
			table.repaint();
		}
	}
	
	
	/**
	 * Checks for end of game.
	 * If yes, resets activePlayer and disables the GUI. 
	 * Shows the winner and sends message that the remote player is ready
	 * @return true if the game ends; false otherwise
	 */
	public boolean endOfGame()
	{
		int i = getCurrentIdx();
		String end = "";
		if(this.numOfPlayers==4 && playerList.get(i).getCardsInHand().isEmpty() == true) // checking if the game has ended
		{
			// table.setActivePlayer(-1); // Setting active player as -1
			table.repaint();
			table.disable(); // Disables GUI on the table
			end+="Game ends\n";
			for(int j=0;j<playerList.size();j++)
			{
				if(j!=i)
					end+=this.getPlayerList().get(j).getName()+" has "+playerList.get(j).getNumOfCards()+" cards in hand.\n";
				else
					end+=this.getPlayerList().get(j).getName()+" wins the game.\n";	
				this.getPlayerList().get(j).removeAllCards();
			}
			JOptionPane.showMessageDialog(null ,end); // Showing who won the game
			getHandsOnTable().clear();
			table.clearMsgArea();
			this.numOfPlayers = 0;
			sendMessage(new CardGameMessage(CardGameMessage.READY,-1, null )); // Setting the players to get ready once again
			return true;
		}
		return false;
	}

	
	/**
	 * A method for returning a valid hand from the specified list of cards of the player. 
	 * Returns null is no valid hand can be composed from the specified list of cards.
	 * @param player
	 * 		The player of the current hand
	 * @param cards
	 * 		The list of cards played by the player
	 * @return
	 * 		A valid hand from the specified list of cards of the player. 
	 * 		Object of the type of hand that is valid
	 * 		Returns null is no valid hand can be composed from the specified list of cards		
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards)
	{
		Hand h1 = new StraightFlush(player,cards);
		if (h1.isValid()) // Checking if the hand is a straight flush and returning if so
				return h1;
		Hand h2 = new Quad(player,cards);
		if (h2.isValid()) // Checking if the hand is a quad and returning if so
				return h2;
		Hand h3 = new FullHouse(player,cards);
		if (h3.isValid()) // Checking if the hand is a full house and returning if so
				return h3;
		Hand h4 = new Flush(player,cards);
		if (h4.isValid()) // Checking if the hand is a flush and returning if so
				return h4;
		Hand h5 = new Straight(player,cards);
		if (h5.isValid()) // Checking if the hand is a straight and returning if so
				return h5;
		Hand h6 = new Triple(player,cards);
		if (h6.isValid()) // Checking if the hand is a triple and returning if so
				return h6;
		Hand h7 = new Pair(player,cards);
		if (h7.isValid()) // Checking if the hand is a pair and returning if so
				return h7;
		Hand h8 = new Single(player,cards);
		if (h8.isValid()) // Checking if the hand is a single and returning if so
				return h8;
		return null; // returning null if it is not a valid hand
	}
	
	/**
	 * Main method from where the execution begins.
	 * Creates a BigTwo game object and a BigTwoDeck object
	 * Shuffles the deck of cards
	 * Starts the game with the deck of cards
	 * @param args
	 * 		Not being used
	 */
	public static void main(String[] args)
	{
		new BigTwoClient(); // Creates a BigTwo game object
	//	BigTwoDeck d = new BigTwoDeck(); // Creates a BigTwoDeck object
	//	d.shuffle(); // Shuffles the deck of cards
	//	game.start(d); // Starts the game with the deck of cards
	}

	/**
	 * Returns the playerID (index) of the local player.
	 * 
	 * @return the playerID (index) of the local player
	 */
	public int getPlayerID() 
	{
		return this.playerID;
	}

	/**
	 * Sets the playerID (index) of the local player.
	 * 
	 * @param playerID
	 *            the playerID (index) of the local player.
	 */
	public void setPlayerID(int playerID) 
	{
		this.playerID = playerID;
	}
	
	/**
	 * Returns the name of the local player.
	 * 
	 * @return the name of the local player
	 */
	public String getPlayerName() 
	{
		return playerName;
	}

	/**
	 * Sets the name of the local player.
	 * 
	 * @param playerName
	 *            the name of the local player
	 */
	public void setPlayerName(String playerName) 
	{
		this.playerName = playerName;
	}

	/**
	 * Returns the IP address of the server.
	 * 
	 * @return the IP address of the server
	 */
	public String getServerIP() 
	{
		return serverIP;
	}

	/**
	 * Sets the IP address of the server.
	 * 
	 * @param serverIP
	 *            the IP address of the server
	 */
	public void setServerIP(String serverIP)
	{
		this.serverIP = serverIP;
	}

	/**
	 * Returns the TCP port of the server.
	 * 
	 * @return the TCP port of the server
	 */
	public int getServerPort() 
	{
		return serverPort;
	}

	/**
	 * Sets the TCP port of the server
	 * 
	 * @param serverPort
	 *            the TCP port of the server
	 */
	public void setServerPort(int serverPort) 
	{
		this.serverPort = serverPort;
	}

	/**
	 * Makes a network connection to the server.
	 */
	public void makeConnection() 
	{
		try 
		{	
			sock = new Socket("127.0.0.1", 2396);
			oos = new ObjectOutputStream(sock.getOutputStream()); // Creates a new Output stream for the socket to write to
			Thread t = new Thread(new ServerHandler()); // Creating a new thread
			t.start();
			sendMessage(new CardGameMessage(CardGameMessage.JOIN,-1, this.getPlayerName()));
			sendMessage(new CardGameMessage(CardGameMessage.READY,-1, null ));		
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		}
	}

	/**
	 * Parses the specified message received from the server.
	 * 
	 * @param message
	 *            the specified message received from the server
	 */
	public void parseMessage(GameMessage message) 
	{
		switch (message.getType()) 
		{
			case CardGameMessage.PLAYER_LIST:
				this.setPlayerID(message.getPlayerID()); // Setting local playerID
				String[] players= (String[]) message.getData();
				for(int i=0;i<players.length;i++)
				{
					if(players[i]!=null)
					{
						this.getPlayerList().get(i).setName(players[i]); // Setting name of already added players
						this.numOfPlayers++;
					}
				}
				table.repaint();
				break;
			case CardGameMessage.JOIN:
				String name= (String) message.getData();
				this.getPlayerList().get(message.getPlayerID()).setName(name); // Setting the name of the new player
				break;
			case CardGameMessage.MOVE:
				this.checkMove(message.getPlayerID(), (int[]) message.getData()); // Checking the move made by the player by calling checkmove
				break;
			case CardGameMessage.FULL:
				table.printMsg("The server is full, can't join the game\n");
				break;
			case CardGameMessage.MSG:
				table.printPlayerMsg((String)message.getData()+"\n"); // Printing the message
				break;
			case CardGameMessage.READY:
				table.printMsg(this.getPlayerList().get(message.getPlayerID()).getName() + " is ready for the game\n");
				this.numOfPlayers++; // increasing the number of players once the player is ready
				table.repaint();
				break;
			case CardGameMessage.START:
				table.printMsg("Everyone is ready for the game.\n");
				this.start((BigTwoDeck) message.getData()); // Starting the game once evryone is ready
				break;
			case CardGameMessage.QUIT:
				table.printMsg(this.getPlayerList().get(message.getPlayerID()).getName()+" left the game.\n");
				this.getPlayerList().get(message.getPlayerID()).setName(""); // Setting name as "" for the player who has left the game
				if(this.numOfPlayers==4 && this.endOfGame() == false) // If the game was on and is not the end of the game
				{
					getHandsOnTable().clear();
					table.clearMsgArea();
					table.disable();
					this.numOfPlayers = 0;
					for(int i=0;i<4;i++)
					{
						playerList.get(i).removeAllCards(); // Removing all cards of the players
					}
					sendMessage(new CardGameMessage(CardGameMessage.READY,-1, null )); // Sending that the local player is ready
				}
				else
				{
					this.numOfPlayers--; // if the game had not yet started
				}
				table.repaint();
				break;
			default:
				break;
		}	
	}

	/**
	 * Sends the specified message to the server.
	 * 
	 * @param message
	 *            the specified message to be sent the server
	 */
	public void sendMessage(GameMessage message) 
	{
		try
		{
			oos.writeObject(message); // Writing message to the output stream
			oos.flush();
		}
		catch (Exception ex) 
		{
			System.out.println("Error sending message");
			ex.printStackTrace();
		}
	}
	
	/**
	 * Used to close the connection once the client has quit
	 */
	public void closeConnection()
	{
		try
		{
			oistream.close(); // Closing object input stream
			oos.close(); // Closing object output stream
			sock.close(); // Closing socket
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	/**
	 * @author Devang
	 *	An inner class that implements the Runnable interface
	 */
	public class ServerHandler implements Runnable 
	{
		/*
		public ServerHandler()
		{
			try {
				// creates an ObjectInputStream and chains it to the InputStream
				// of the client socket
				oistream = new ObjectInputStream(sock.getInputStream());
			} 
			catch (Exception ex) 
			{
				System.out.println("Error in creating an ObjectInputStream for the client at " + sock.getRemoteSocketAddress());
				ex.printStackTrace();
			}
		} 
		*/
		
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() 
		{
			CardGameMessage message;
			try 
			{
				oistream = new ObjectInputStream(sock.getInputStream());
				// waits for messages from the server
				while ((message = (CardGameMessage) oistream.readObject()) != null) 
				{
					System.out.println("Message received");
					parseMessage(message); // Used to parse the received messages
				} 
			} 
			catch (Exception ex) 
			{
				// System.out.println("Error in receiving messages.");
				ex.printStackTrace();
			}
		}
	}
	
}
