
/**
 * It is a subclass of the Hand class and is used to model a hand of straight in a Big two card game, i.e five cards with consecutive ranks
 * 2 and A can only form a straight with K but not with 3.
 * It overrides the getType() and the isValid() methods
 * @author Devang
 *
 */
public class Straight extends Hand
{
	private static final long serialVersionUID = 178749520333021015L;
	/**
	 * Constructor of the Straight class to initialise the player of the current hand and the cards played by the player
	 * It simply calls the super (Hand) class constructor
	 * @param player
	 * 		Use to specify the player of the current hand
	 * @param cards
	 * 		Used to specify the cards played in the current hand
	 */
	public Straight(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * A getter method for returning a string specifying the type of this hand, i.e Straight.
	 * @return
	 * 		a string value specifying the type of hand, i.e Straight
	 */
	public String getType()
	{
		return "Straight";
	}
	
	/**
	 *  An method for checking if this is a valid Straight, i.e five cards with consecutive ranks
	 *  2 and A can only form a straight with K but not with 3.
	 * @return
	 * 		boolean value specifying if the hand being a Straight is valid or not
	 */
	public boolean isValid()
	{
		if(this.size() != 5) // If the hand size is not 5 then it is not a straight
			return false;
		Card c1 = this.getCard(0);
		Card c2 = this.getCard(1);
		Card c3 = this.getCard(2);
		Card c4 = this.getCard(3);
		Card c5 = this.getCard(4);
		// Checking if the ranks of the cards are consecutive. 2 and A can only form a straight with K but not with 3 - returning true, if they are
		if((((c1.getRank()+11)%13)+1 == (c2.getRank()+11)%13)  &&  (((c2.getRank()+11)%13)+1 == (c3.getRank()+11)%13)  &&  (((c3.getRank()+11)%13)+1 == (c4.getRank()+11)%13)  &&  (((c4.getRank()+11)%13)+1 == (c5.getRank()+11)%13) )
			return true;
		return false;
	}
}
