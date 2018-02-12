import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * The BigTwoTable class implements the CardGameTable interface. 
 * It is used to build a GUI for the Big Two card game and handle all user actions.
 * Has a panel showing the cards of each player as well as the cards played on the table. The cards should be shown in a partially overlapped manner.
 * For each player, shows his/her name and an avatar for him/her.
 * For the active player, shows the faces of his/her cards.
 * For other players, shows only the backs of his/her cards.
 * For cards played on the table, shows (the faces of) the last hand of cards played on the table and the name of the player for this hand.
 * Allows the active player to select and deselect his/her cards by mouse clicks. The selected cards should be drawn in a “raised” position with respect to the rest of the cards.
 * Builds a Play button for the active player to play the selected cards.
 * Builds a Pass button for the active player to pass his/her turn to the next player.
 * Builds a text area to show the current game status as well as one for chat messages.
 * Builds a Connect menu item for connecting the game.
 * Builds a Quit menu item for quitting the game.
 * @author Devang
 *
 */
public class BigTwoTable implements CardGameTable
{
	private BigTwoClient game; //  The card game associated with this table
	private boolean[] selected; // A boolean array indicating which cards are being selected.
	private int activePlayer; // An integer specifying the index of the active player.
	private JFrame frame; // The main window of the application.
	private JPanel bigTwoPanel; // A panel for showing the cards of each player and the cards played on the table
	private JButton playButton; // A Play button for the active player to play the selected cards
	private JButton passButton; // A Pass button for the active player to pass his/her turn to the next player
	private JTextArea msgArea; // A text area for showing the current game status.
	private Image[][] cardImages; // A 2D array storing the images for the faces of the cards.
	private Image cardBackImage; // An image for the backs of the cards
	private Image[] avatars; // An array storing the images for the avatars
	private int CardWidth; // An integer storing width of each card
	private int CardHeight; // An integer storing height of each card
	private boolean enabled; // A boolean to check if the selection of cards needs to be enabled or not
	private JTextField message; // A text field for sending messages
	private JTextArea received; // A text area for showing the received text message
	
	/**
	 * A constructor for creating a BigTwoTable.
	 * - Creates a new frame
	 * - Stores the avatars and the cards from the path into the array
	 * - Creates a BigTwoPanel showing the name, avatar and cards of each player as well as the cards played on the table. Adds Mouse Listener to the panel
	 * - Creates a button Panel for Play and Pass button and adds Action Listener to each
	 * - Creates a text area to show the current game status as well as one for showing the received text message.
	 * - Adds the text area to a scroller for vertical scrolling
	 * - Creates a Menu Bar for Connect and Quit and adds Action Listener to each
	 * - Adds the Menu Bar, big Two Panel, Button Panel and scroller to the frame
	 * @param game
	 * 		The parameter game is a reference to a card game associates with this table.
	 */
	public BigTwoTable(BigTwoClient game)
	{
		this.game = game;
		frame = new JFrame(); // Creating a new JFrame
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(800, 800); // Setting the size of the frame
	    cardImages = new Image[4][13];
	    avatars = new Image[4];
	    // Loading the cards into an array
	    for(int i=0;i<4;i++)
	    {
	    		for(int j=0;j<13;j++)
	    		{
	    			Image cardi = new ImageIcon(getClass().getResource("Cards/"+j+"_"+i+".png")).getImage();
	    			cardImages[i][j]=cardi;
	    		}
	    }
	    
	    activePlayer = -1;
	    // Loading the back card
	    cardBackImage = new ImageIcon(getClass().getResource("Cards/cardBack.jpg")).getImage();
	    // Loading the avatars into an array
	    for(int i=0;i<4;i++)
		{
			Image avatari = new ImageIcon(getClass().getResource("Avatars/Avatar_"+(i)+".png")).getImage();
			avatars[i]=avatari;
		}
	    selected = new boolean[13];
	    for(int i=0;i<13;i++)
		{
			selected[i] = false;
		}
	    
	    bigTwoPanel = new BigTwoPanel(); // Creating a new BigTwoPanel
	    bigTwoPanel.setLayout(new BoxLayout(bigTwoPanel, BoxLayout.X_AXIS));
	    bigTwoPanel.setPreferredSize(new Dimension(600, 600));
	    bigTwoPanel.addMouseListener(new BigTwoPanel()); 
	    bigTwoPanel.setBackground(Color.BLACK); 
	    enabled=true; // Enabling selection of cards 
	    
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setLayout(new BorderLayout());
	    JPanel leftPanel = new JPanel();
	    playButton = new JButton("Play"); 
	    leftPanel.add(playButton); // Adding play button to left panel
	    playButton.addActionListener(new PlayButtonListener());
	    passButton = new JButton("Pass");
	    leftPanel.add(passButton); // Adding pass button to left panel
	    passButton.addActionListener(new PassButtonListener());
	    
	    JPanel rightPanel = new JPanel();
	    message = new JTextField (25); // Creating textfield to send messages
	    message.addKeyListener(new EnterButtonListener());
	    JLabel msgLabel = new JLabel ("Message: ");
	    rightPanel.add(msgLabel); // Adding message label to right panel
	    rightPanel.add(message); // Adding message text field to right panel
	    buttonPanel.add(leftPanel,BorderLayout.CENTER);
	    buttonPanel.add(rightPanel,BorderLayout.EAST);

	    msgArea = new JTextArea(100,30); // Creating a new JTextArea for server messages
	    msgArea.setLineWrap(true); // To ensure that the text is wrapped in the size of the text area
	    msgArea.setEditable(false);
	    JScrollPane scroller = new JScrollPane(msgArea); // Creating a new ScrollPane and adding the text area to it 
	    scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // Ensuring vertical scroll of scroller
	    scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	    
	    received = new JTextArea(100,30); // Creating a new JTextArea for chat messages
	    received.setLineWrap(true); // To ensure that the text is wrapped in the size of the text area
	    received.setEditable(false);
	    JScrollPane receivedScroller = new JScrollPane(received); // Creating a new ScrollPane and adding the text area to it 
	    receivedScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS); // Ensuring vertical scroll of scroller
	    receivedScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
	    
	    JPanel messagePanel =  new JPanel();
	    messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
	    messagePanel.add(scroller); // Adding the server message scroller to message panel
	    messagePanel.add(receivedScroller); // Adding the chat message scroller to message panel
	    
	    JMenuBar menuBar = new JMenuBar();
	    JMenu menu = new JMenu("Game");
	    JMenuItem connect = new JMenuItem("Connect");
	    connect.addActionListener(new ConnectMenuItemListener());
	    JMenuItem quit = new JMenuItem("Quit");
	    quit.addActionListener(new QuitMenuItemListener());
	    menu.add(connect); // Adding connect menu item to menu
	    menu.add(quit); // Adding quit menu item to menu
	    menuBar.add(menu); // Adding menu to menu bar
	    
	    /*
	     * Adding menu bar, message panel, big two panel and button panel appropriately to frame
	     */
	    frame.setJMenuBar(menuBar);
	    frame.add(messagePanel,BorderLayout.EAST);
	    frame.add(bigTwoPanel,BorderLayout.WEST);
	    frame.add(buttonPanel,BorderLayout.SOUTH);

	    frame.setVisible(true);
	    
	    String name = null;
	    while(name == null || name.length()==0)
	    {
	    		name = JOptionPane.showInputDialog(frame,"Please enter your name!"); // Input Dialog to input name of player
	    }
		game.setPlayerName(name);
		disable();
	}
	
	
	/**
	 * Sets the index of the active player (i.e., the current player).
	 * 
	 * @param activePlayer
	 *            an int value representing the index of the active player
	 */
	public void setActivePlayer(int activePlayer)
	{
		this.activePlayer = activePlayer;
	}
	
	/**
	 * Returns an array of indices of the cards selected.
	 * 
	 * @return an array of indices of the cards selected
	 */
	public int[] getSelected()
	{
		int[] cardIdx = null;
		int count = 0;
		for (int j = 0; j < selected.length; j++) 
		{
			if (selected[j])
				count++; // Counting number of selected cards
		}

		if (count != 0)
		{
			cardIdx = new int[count];
			count = 0;
			for (int j = 0; j < selected.length; j++)
			{
				if (selected[j])
				{
					cardIdx[count] = j; // Storing the index of the selected cards in an int array
					count++;
				}
			}
		}
		return cardIdx; // Returning the int array
	}
	
	/**
	 * Resets the list of selected cards to an empty list.
	 */
	public void resetSelected()
	{
		for(int i=0;i<13;i++)
		{
			selected[i] = false; // Ensures no cards are selected
		}
	}
	
	/**
	 * Repaints the GUI.
	 */
	public void repaint() 
	{
		resetSelected(); // Calls resetSelected(), cause whenever repaint is getting called, the selected cards are made false since the player has to make a move
		frame.repaint();
	}
	
	/**
	 * Prints the specified string to the message area of the card game table.
	 * 
	 * @param msg
	 *            the string to be printed to the server message area of the card game
	 *            table
	 */
	public void printMsg(String msg) 
	{
		msgArea.append(msg);
		msgArea.setCaretPosition(msgArea.getDocument().getLength()); // To ensure the scroller is at the end of the last appended text
	}
	
	/**
	 * Prints the specified string to the message area of the card game table.
	 * 
	 * @param msg
	 *            the string to be printed to the chat message area of the card game
	 *            table
	 */
	public void printPlayerMsg(String msg) 
	{
		received.append(msg);
		received.setCaretPosition(received.getDocument().getLength()); // To ensure the scroller is at the end of the last appended text
	}
	
	/**
	 * Clears all the message area of the card game table.
	 */
	public void clearMsgArea()
	{
		msgArea.setText("");
		received.setText("");
	}
	
	/**
	 * Resets the GUI. Used when game is restarted.
	 * Resets handsOnTable, selected cards, message area and enables GUI
	 */
	public void reset()
	{
		game.getHandsOnTable().clear(); // Clears hands on table cards
		resetSelected(); // clears selected cards
		clearMsgArea(); // Clears message area
		enable(); // enables GUI interaction
	}
	
	/**
	 * Enables user interactions with the GUI.
	 */
	public void enable()
	{
		passButton.setEnabled(true);
		playButton.setEnabled(true);
		enabled=true; // Enabling selection of cards 
	}
	
	/**
	 * Disables user interactions with the GUI.
	 */
	public void disable()
	{
		/*
		 * Disabling buttons and selection of cards
		 */
		passButton.setEnabled(false);
		playButton.setEnabled(false);
		enabled=false;
	}
	
	/**
	 *		An inner class that extends the JPanel class and implements the MouseListener interface. 
	 *		Overrides the paintComponent() method inherited from the JPanel class to draw the card game table. 
	 *		Implements the mouseClicked() method from the MouseListener interface to handle mouse click events.
	 *@author Devang
	 */
	class BigTwoPanel extends JPanel implements MouseListener
	{
		private static final long serialVersionUID = 1L;
		/**
		  * Paints the avatars, the cards of the players and the last hand played
		  */
		public void paintComponent(Graphics g) 
		{
			super.paintComponent(g);
	        g.setColor(Color.WHITE);
	        // Drawing the player number, his/her avatar and a line at the end of each player block
		    for(int i=0;i<4;i++)
		    {
		    		if(activePlayer == i)
		    			g.drawString(game.getPlayerList().get(i).getName()+ " (You)", 15, (130*i)+25);
		    		else
		    			g.drawString(game.getPlayerList().get(i).getName(), 15, (130*i)+25);
		    		g.drawImage(avatars[i], 15, (130*i)+30, getWidth()/10, getHeight()/8, this);
		    		g.drawLine(0, (130*i)+50+(getHeight()/8), getWidth(), (130*i)+50+(getHeight()/8));
		    }
		    CardWidth = getWidth()/8; // Getting and storing the width of each card
		    CardHeight = getHeight()/8; // Getting and storing the height of each card
		    for(int i=0;i<4;i++)
		    {
	    			if(activePlayer == i)
	    			{
	    				CardList cardsInHand= game.getPlayerList().get(activePlayer).getCardsInHand(); // Getting cards in hand of the active player
	    				for(int j =0;j<cardsInHand.size();j++)
	    				{
	    					Card c = cardsInHand.getCard(j);
	    					if(selected[j]==true)
	    						// Printing selected cards in raised position showing the faces of the cards 
	    						g.drawImage(cardImages[c.getSuit()][c.getRank()], (15*j)+100,(130*i)+10, CardWidth, CardHeight, this);
	    					else
	    						// Printing not selected cards in normal position showing the faces of the cards 
	    						g.drawImage(cardImages[c.getSuit()][c.getRank()], (15*j)+100,(130*i)+30, CardWidth, CardHeight, this);
	    				}
	    			}
	    			else
	    			{
	    				CardList cardsInHand= game.getPlayerList().get(i).getCardsInHand(); // Getting cards in hand of the inactive player
	    				for(int j=0;j<cardsInHand.size();j++)
	    				{
	    					// Printing cards in normal position showing the back of the cards 
	    					g.drawImage(cardBackImage, (15*j)+100,(130*i)+30, CardWidth, CardHeight, this);
	    					
	    				}
	    			}
		    }
		    if(game.getHandsOnTable().size() != 0) // Checking if a previous hand has been played on the table
		    {
	    			Hand lastHand = game.getHandsOnTable().get(game.getHandsOnTable().size()-1);
	    			g.drawString("Played by "+lastHand.getPlayer().getName(), 15, 545); // Drawing player who played the last hand
				for(int j =0;j<lastHand.size();j++)
				{
					Card c = lastHand.getCard(j);
					// Drawing cards played in the last hand
					g.drawImage(cardImages[c.getSuit()][c.getRank()], (15*(j+1)),550, CardWidth, CardHeight, this);
				}
		    }
		    else
		    {
		    		g.drawString("No hand played yet!", 15, 545); 
		    }
		}
		/**
		  * Check if the mouse listener is enabled or not.
		  * Used to select/deselect cards
		  */
		public void mouseClicked(MouseEvent e)
		{
			if(!enabled) // Checking if mouse clicked is enabled or not
				return;
			int numOfCards = game.getPlayerList().get(activePlayer).getCardsInHand().size(); // Number of cards of the player
			int playerWidth = (15*(numOfCards-1)); // Start position of last card of any player (x axis)
			int playerHeightStart = (130*activePlayer)+10; // Start of the height of the particular player, including when a card is lifted (y axis)
			int x=e.getX(); // x coordinate of mouse click
		    int y=e.getY(); // y coordinate of mouse click
		    // Checking the conditions for the mouse click to be within the the area of the cards of the player (including when it can be raised)
		    if(x>= 100 && x<= (100+playerWidth+CardWidth) && y>=playerHeightStart && y<=(playerHeightStart+CardHeight+20)) 
		    { 
			    for(int i=numOfCards-1;i>=0;i--)
			    {
			    		int IthStart = 100 + (15*(i)); // Storing x axis position of start of ith card of the player
			    		// Checking for conditions when the a card is not selected and wants to be selected
			    		if(x>= IthStart && x <= (IthStart+CardWidth) && selected[i]==false && y>=playerHeightStart+20)
			    		{
			    			selected[i]=true;
			    			frame.repaint();
			    			return;
			    		}
			    		// Checking for conditions when the a card is selected and does not want to be selected
			    		if(x>= IthStart && x <= (IthStart+CardWidth) && selected[i]==true && y<=playerHeightStart+CardHeight)
			    		{
			    			selected[i]=false;
			    			frame.repaint();
			    			return;
			    		}
			    }
		    }
		    
		}
		/**
		  * Not used in this program
		  */
		public void mousePressed(MouseEvent e)
		{		
		}
		/**
		  * Not used in this program
		  */
		public void mouseReleased(MouseEvent e)
		{			
		}
		/**
		  * Not used in this program
		  */
		public void mouseEntered(MouseEvent e)
		{
		}
		/**
		  * Not used in this program
		  */
		public void mouseExited(MouseEvent e)
		{	
		}
	}
	
	/**
	 *	An inner class that implements the ActionListener interface.
	 *  Implements the actionPerformed() method from the ActionListener interface to handle button-click events for the “Play” button. 
	 *  When the “Play” button is clicked, the makeMove() method of the CardGame object is called to make a move.
	 *  @author Devang
	 */
	class PlayButtonListener implements ActionListener
	{
		/**
		  * Used to respond to the Play button.
		  * Calls makeMove function if cards are selected and play button is pressed
		  */
		public void actionPerformed(ActionEvent e) 
		{
			if(getSelected()!= null) // Checking if cards are selected 
				game.makeMove(activePlayer,getSelected()); 			
			else
				printMsg("Please select a card to play, else pass!\n");
		}
	}
	
	/**
	 *		An inner class that implements the ActionListener interface. 
	 *		Implements the actionPerformed() method from the ActionListener interface to handle button-click events for the “Pass” button. 
	 *		When the “Pass” button is clicked, the makeMove() method of the CardGame object is called to make a move.
	 *@author Devang
	 */
	class PassButtonListener implements ActionListener
	{
		/**
		  * Used to respond to the Pass button.
		  * Calls makeMove function with null cards selected when pass button is pressed
		  */
		public void actionPerformed(ActionEvent e) 
		{
			game.makeMove(activePlayer,null); // Calling makeMove with null argument is pass is played
		}
		
	}
	
	/**
	 *		An inner class that implements the ActionListener interface. 
	 *		Implements the actionPerformed() method from the ActionListener interface to handle menu-item-click events for the “Connect” menu item. 
	 *		When the “Connect” menu item is selected, 
	 *			Calls makeConnection()
	 *@author Devang
	 */
	class ConnectMenuItemListener implements ActionListener
	{
		/**
		  * Used to respond to the Connect Menu Item Listener button.
		  * Calls makeConnection()
		  */
		public void actionPerformed(ActionEvent e) 
		{
			if(game.getPlayerID() == -1)
				game.makeConnection(); // Starts the game
			else
				printMsg("You are already connected!\n");
		}
		
	}
	
	/**
	 *		An inner class that implements the ActionListener interface. 
	 *		Implements the actionPerformed() method from the ActionListener interface to handle menu-item-click events for the “Quit” menu item. 
	 *		When the “Quit” menu item is selected, closes all the connections and it terminates the application
	 * @author Devang
	 */
	class QuitMenuItemListener implements ActionListener
	{
		/**
		  * Used to respond to the Quit Menu Item button.
		  * Terminates the application
		  */
		public void actionPerformed(ActionEvent e) 
		{
			game.closeConnection();
			System.exit(2);
		}
		
	}
	
	/**
	 *		An inner class that implements the AKeyListener interface. 
	 *		Implements the keyPressed() method from the KeyListener interface to handle enter-click events for the message area menu item. 
	 * @author Devang
	 */
	class EnterButtonListener implements KeyListener
	{

		/**
		  * Not used in this program
		  */
		public void keyTyped(KeyEvent e) 
		{
		}

		/**
		  * if the enter key is pressed, sends the message to the sendMessage function of bigTwoClient
		  * Resets the text to ""
		  */
		public void keyPressed(KeyEvent e) 
		{
			if(e.getKeyCode() == 10)
			{
				String text = message.getText();
				CardGameMessage mess = new CardGameMessage(CardGameMessage.MSG,-1, text);
				game.sendMessage(mess);
				message.setText("");
			}
		}

		/**
		  * Not used in this program
		  */
		public void keyReleased(KeyEvent e) 
		{
		}
		
	}
}
