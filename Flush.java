
/**
 * It is a subclass of the Hand class and is used to model a hand of flush in a Big two card game, i.e five cards with the same suit
 * It overrides the getType() and the isValid() methods
 * @author Devang
 *
 */
public class Flush extends Hand
{
	private static final long serialVersionUID = 178749520333021015L;
	/**
	 * * Constructor of the Flush class to initialise the player of the current hand and the cards played by the player
	 * It simply calls the super (Hand) class constructor
	 * @param player
	 * 		Use to specify the player of the current hand
	 * @param cards
	 * 		Used to specify the cards played in the current hand
	 */
	public Flush(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * A getter method for returning a string specifying the type of this hand, i.e Flush.
	 * @return
	 * 		a string value specifying the type of hand, i.e Flush
	 */
	public String getType()
	{
		return "Flush";
	}
	
	/**
	 *  An method for checking if this is a valid Flush, i.e five cards with the same suit
	 * @return
	 * 		boolean value specifying if the hand being a Flush is valid or not
	 */
	public boolean isValid()
	{
		if(this.size() != 5) // If the hand size is not 5 then it is not a flush
			return false;
		Card c1 = this.getCard(0);
		Card c2 = this.getCard(1);
		Card c3 = this.getCard(2);
		Card c4 = this.getCard(3);
		Card c5 = this.getCard(4);
		// Checking if the cards are of the same suit or not
		if((c1.getSuit() == c2.getSuit()) && (c2.getSuit() == c3.getSuit()) && (c3.getSuit() == c4.getSuit()) && (c4.getSuit() == c5.getSuit()))
				return true;
		return false;
	}
}
