/**
 * The Hand class is an abstract class and a subclass of the CardList class, and is used to model a hand of cards. 
 * It has a private instance variable for storing the player who plays this hand. 
 * It also has methods for getting the player of this hand, checking if it is a valid hand, getting the type of this hand, getting the top card of this hand, and checking if it beats a specified hand.
 * @author Devang
 *
 */
public abstract class Hand extends CardList
{
	private static final long serialVersionUID = 3708415018664975012L;
	private CardGamePlayer player = new CardGamePlayer();
	
	
	/**
	 * Constructor of the Hand class to initialise the player of the current hand and the cards played by the player
	 * Sorts the cards played by the player
	 * @param player
	 * 		Use to specify the player of the current hand
	 * @param cards
	 * 		Used to specify the cards played in the current hand
	 */	
	public Hand(CardGamePlayer player, CardList cards)
	{ 
		this.player = player;
		for(int i=0;i<cards.size();i++)
		{
			this.addCard(cards.getCard(i)); // Adding the cards selected to the hand
		}
		this.sort();
	}
	
	/**
	 * A getter method for retrieving the player of this hand
	 * @return
	 * 		The of player of the current hand
	 */
	public CardGamePlayer getPlayer()
	{
		return player;
	}
	
	
	/**
	 * A getter method for retrieving the top card of this hand.
	 * @return
	 * 		the top card of the current hand
	 */
	public Card getTopCard()
	{
		return this.getCard(this.size()-1); // Since the lists are sorted, top card is the last card in most cases unless overridden
	}
	
	
	/**
	 * A method for checking if this hand beats a specified hand
	 * @param hand
	 * 		The specified hand that the current hand is checked against
	 * @return
	 * 		boolean value suggesting if the current hand beats the specified hand or not
	 */
	public boolean beats(Hand hand)
	{
		if(this.size() != hand.size()) // if the size of the hands are not equal then the current hand does not beat the specified hand
			return false;
		if(this.getType().equals("StraightFlush"))
		{
			// If the current hand is a straight flush and the specified hand is straight, fullhouse, flush or quad then the current hand beats the specified hand
			if (hand.getType().equals("Straight") || hand.getType().equals("Flush") || hand.getType().equals("FullHouse")|| hand.getType().equals("Quad"))
				return true;
		}
		
		if(this.getType().equals("Quad"))
		{
			// If the current hand is a quad and the specified hand is straight flush then the current hand does not beat the specified hand
			if (hand.getType().equals("StraightFlush"))
				return false;
			// If the current hand is a quad and the specified hand is straight, fullhouse or flush then the current hand beats the specified hand
			if (hand.getType().equals("Straight") || hand.getType().equals("Flush") || hand.getType().equals("FullHouse"))
				return true;
		}
		
		if(this.getType().equals("FullHouse"))
		{
			// If the current hand is a full house and the specified hand is straight flush or quad then the current hand does not beat the specified hand
			if (hand.getType().equals("Quad") || hand.getType().equals("StraightFlush"))
				return false;
			// If the current hand is a full house and the specified hand is straight or flush then the current hand beats the specified hand
			if (hand.getType().equals("Straight") || hand.getType().equals("Flush")) 
				return true;
		}
		
		if(this.getType().equals("Flush"))
		{
			// If the current hand is a flush and the specified hand is straight flush, quad or fullhouse then the current hand does not beat the specified hand
			if (hand.getType().equals("Quad") || hand.getType().equals("FullHouse") || hand.getType().equals("StraightFlush"))
				return false;
			// If the current hand is a flush and the specified hand is straight then the current hand beats the specified hand
			if (hand.getType().equals("Straight"))
				return true;
			// If both the hands are flush, then we can't use compareto directly, thus checking if the current hand is larger than that of the specified hand
			if (hand.getType().equals("Flush") && (this.getTopCard().getSuit() > hand.getTopCard().getSuit()))
				return true;
			// If both the hands are flush, then we can't use compareto directly, thus checking if the current hand is smaller than that of the specified hand
			if (hand.getType().equals("Flush") && (this.getTopCard().getSuit() < hand.getTopCard().getSuit()))
				return false;
		}
		if(this.getType().equals("Straight"))
		{
			// If the current hand is a straight and the specified hand is straight flush, quad, fullhouse or flush then the current hand does not beat the specified hand
			if (hand.getType().equals("Flush") || hand.getType().equals("Quad") || hand.getType().equals("FullHouse") || hand.getType().equals("StraightFlush"))
				return false;
		}
		// In cases where the two types of hand are the same comparing using compareTo and the returning the appropriate value
		Card c1 = this.getTopCard();
		Card c2 = hand.getTopCard();
		if(c1.compareTo(c2) == 1)
			return true;
		else
			return false;
	}
	
	/**
	 *  An abstract method for checking if this is a valid hand
	 * @return
	 * 		boolean value specifying if the hand is valid or not
	 */
	public abstract boolean isValid();
	
	
	/**
	 * An abstract method for returning a string specifying the type of this hand.
	 * @return
	 * 		a string value specifying the type of hand
	 */
	public abstract String getType();
}
