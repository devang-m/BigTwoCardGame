

/**
 * It is a subclass of the Hand class and is used to model a hand of triple in a Big two card game, i.e three cards with the same rank
 * It overrides the getType() and the isValid() methods
 * @author Devang
 *
 */
public class Triple extends Hand
{
	private static final long serialVersionUID = 178749520333021015L;
	/**
	 * Constructor of the Triple class to initialise the player of the current hand and the cards played by the player
	 * It simply calls the super (Hand) class constructor
	 * @param player
	 * 		Use to specify the player of the current hand
	 * @param cards
	 * 		Used to specify the cards played in the current hand
	 */
	public Triple(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * A getter method for returning a string specifying the type of this hand, i.e Triple.
	 * @return
	 * 		a string value specifying the type of hand, i.e Triple
	 */
	public String getType()
	{
		return "Triple";
	}
	
	/**
	 *  An method for checking if this is a valid Triple, i.e three cards with the same rank
	 * @return
	 * 		boolean value specifying if the hand being a Triple is valid or not
	 */
	public boolean isValid()
	{
		if(this.size() != 3) // If the hand size is not 3 then it is not a triple
			return false;
		Card c1 = this.getCard(0);
		Card c2 = this.getCard(1);
		Card c3 = this.getCard(2);
		if ((c1.getRank() == c2.getRank()) && (c1.getRank() == c3.getRank())) // Else checking if the ranks of the 3 cards is the same, which means they are a triple
			return true;
		return false;
	}
}
