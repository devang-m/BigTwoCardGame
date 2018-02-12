
/**
 * It is a subclass of the Hand class and is used to model a hand of single in a Big two card game, i.e. a single card
 * It overrides the getType() and the isValid() methods
 * @author Devang
 *
 */
public class Single extends Hand
{
	private static final long serialVersionUID = 178749520333021015L;
	/**
	 * Constructor of the Single class to initialise the player of the current hand and the cards played by the player
	 * It simply calls the super (Hand) class constructor
	 * @param player
	 * 		Use to specify the player of the current hand
	 * @param cards
	 * 		Used to specify the cards played in the current hand
	 */
	public Single(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * A getter method for returning a string specifying the type of this hand, i.e Single.
	 * @return
	 * 		a string value specifying the type of hand, i.e Single
	 */
	public String getType()
	{
		return "Single";
	}
	
	/**
	 *  An method for checking if this is a valid Single, i.e. a single card
	 * @return
	 * 		boolean value specifying if the hand being a Single is valid or not
	 */
	public boolean isValid()
	{
		if (this.size() == 1 ) // Only case of validity is if the size of the hand is 1
			return true;
		return false;
	}
}
