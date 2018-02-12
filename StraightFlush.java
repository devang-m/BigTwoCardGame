
/**
 * It is a subclass of the Hand class and is used to model a hand of StraightFLush in a Big two card game,
 * i.e. five cards with consecutive ranks and the same suit
 * 2 and A can only form a straight with K but not with 3.
 * It overrides the getType() and the isValid() methods
 * @author Devang
 *
 */
public class StraightFlush extends Hand
{
	private static final long serialVersionUID = 178749520333021015L;
	/**
	 * Constructor of the Straight Flush class to initialise the player of the current hand and the cards played by the player
	 * It simply calls the super (Hand) class constructor
	 * @param player
	 * 		Use to specify the player of the current hand
	 * @param cards
	 * 		Used to specify the cards played in the current hand
	 */
	public StraightFlush(CardGamePlayer player, CardList cards)
	{
		super(player,cards);
	}
	
	/**
	 * A getter method for returning a string specifying the type of this hand, i.e StraightFlush.
	 * @return
	 * 		a string value specifying the type of hand, i.e StraightFlush
	 */
	public String getType()
	{
		return "StraightFlush";
	}
	
	/**
	 *  An method for checking if this is a valid StraightFLush, i.e five cards with consecutive ranks and the same suit
	 *  2 and A can only form a straight with K but not with 3.
	 * @return
	 * 		boolean value specifying if the hand being a StraightFlush is valid or not
	 */
	public boolean isValid()
	{
		if (this.size() != 5)
			return false;
		CardList temp = new CardList(); // Creating a temporary cardlist from the Hand
		for(int j=0;j<this.size();j++)
		{
			temp.addCard(this.getCard(j));
		}
		// StraightFlush is s Straight as well as a Flush, so checking for that
		Hand str =  new Straight(this.getPlayer(),temp);
		Hand flsh  = new Flush(this.getPlayer(),temp);
		if(str.isValid() && flsh.isValid())
			return true;
		return false;

	}
}
