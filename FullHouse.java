
/**
 * It is a subclass of the Hand class and is used to model a hand of Full House in a Big two card game,
 *  i.e five cards with  two having the same rank and three having another same rank
 * It overrides the getType() and the isValid() and getTopCard() methods
 * It also has a private instance variable first which marks if the three cards having the same rank appear earlier or not in a sorted deck
 * @author Devang
 *
 */
public class FullHouse extends Hand
{
	private static final long serialVersionUID = 178749520333021015L;
	private 	boolean first = true; // Shows if the three cards having the same rank appear earlier or not in a sorted deck
	/**
	 * Constructor of the FullHouse class to initialise the player of the current hand and the cards played by the player
	 * It simply calls the super (Hand) class constructor
	 * @param player
	 * 		Use to specify the player of the current hand
	 * @param cards
	 * 		Used to specify the cards played in the current hand
	 */
	public FullHouse(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * A getter method for returning a string specifying the type of this hand, i.e FullHouse.
	 * @return
	 * 		a string value specifying the type of hand, i.e FullHouse
	 */
	public String getType()
	{
		return "FullHouse";
	}
	
	/**
	 *  An method for checking if this is a valid Full House, i.e five cards with  two having the same rank and three having another same rank
	 * @return
	 * 		boolean value specifying if the hand being a FullHouse is valid or not
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
		if (c3.getRank() == c4.getRank()) // Checking if the three cards having the same rank appear later in a sorted deck
			first = false;
		else
			first = true;
		
		if (first == true) // Condition if the three cards having the same rank appear first in a sorted deck
			if((c1.getRank() == c2.getRank()) && (c2.getRank() == c3.getRank()) && (c3.getRank() != c4.getRank()) && (c4.getRank() == c5.getRank()))
				return true;
		if (first == false) // Condition if the three cards having the same rank appear later in a sorted deck
			if((c1.getRank() == c2.getRank()) && (c2.getRank() != c3.getRank()) && (c3.getRank() == c4.getRank()) && (c4.getRank() == c5.getRank())) 
				return true;
		return false;
	}
	
	/**
	 * A getter method for retrieving the top card of a Full House.
	 * @return
	 * 		the top card of the Full House
	 */
	public Card getTopCard()
	{
		
		Card c3 = this.getCard(2);
		Card c4 = this.getCard(3);
		if (c3.getRank() == c4.getRank()) // Comparing the 3rd and 4th card in the sorted hand
			first = false;
		else
			first = true;
		if(first == true) // Condition if the three cards having the same rank appear first in a sorted deck
			return this.getCard(2);
		else
			return this.getCard(4);
	}
}
