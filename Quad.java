
/**
 * It is a subclass of the Hand class and is used to model a hand of Quad in a Big two card game,
 * i.e five cards with four having the same rank
 * It overrides the getType() and the isValid() and getTopCard() methods
 * It also has a private instance variable first which marks if the four cards having the same rank appear earlier or not in a sorted deck
 * @author Devang
 *
 */
public class Quad extends Hand
{
	private static final long serialVersionUID = 178749520333021015L;
	private 	boolean first = true; // Shows if the four cards having the same rank appear earlier or not in a sorted deck
	/**
	 * Constructor of the Quad class to initialise the player of the current hand and the cards played by the player
	 * It simply calls the super (Hand) class constructor
	 * @param player
	 * 		Use to specify the player of the current hand
	 * @param cards
	 * 		Used to specify the cards played in the current hand
	 */
	public Quad(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * A getter method for returning a string specifying the type of this hand, i.e Quad.
	 * @return
	 * 		a string value specifying the type of hand, i.e Quad
	 */
	public String getType()
	{
		return "Quad";
	}
	
	/**
	 *  An method for checking if this is a valid Quad, i.e five cards with four having the same rank
	 * @return
	 * 		boolean value specifying if the hand being a Quad is valid or not
	 */
	public boolean isValid()
	{
		if (this.size() != 5)
			return false;
		Card c1 = this.getCard(0);
		Card c2 = this.getCard(1);
		Card c3 = this.getCard(2);
		Card c4 = this.getCard(3);
		Card c5 = this.getCard(4);
		if (c4.getRank() == c5.getRank())  // Checking if the four cards having the same rank appear later in a sorted deck
			first = false;
		else
			first = true;
		
		if (first == true) // Condition if the four cards having the same rank appear first in a sorted deck
			if((c1.getRank() == c2.getRank()) && (c2.getRank() == c3.getRank()) && (c3.getRank() == c4.getRank()) && (c4.getRank() != c5.getRank()))
				return true;
		if (first == false)
			if((c1.getRank() != c2.getRank()) && (c2.getRank() == c3.getRank()) && (c3.getRank() == c4.getRank()) && (c4.getRank() == c5.getRank())) 
				return true;
		return false;
	}
	
	/**
	 * A getter method for retrieving the top card of a Quad.
	 * @return
	 * 		the top card of the Quad
	 */
	public Card getTopCard()
	{
		
		Card c4 = this.getCard(3);
		Card c5 = this.getCard(4);
		if (c4.getRank() == c5.getRank()) // Comparing the 4th and 5th card in the sorted hand
			first = false;
		else
			first = true;
		if(first == true) // Condition if the four cards having the same rank appear first in a sorted deck
			return this.getCard(3);
		else
			return this.getCard(4);
	}
}
