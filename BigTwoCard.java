/**
 * The BigTwoCard class is a subclass of the Card class, and is used to model a card used in a Big Two card game.
 * It overrides the compareTo() method it inherited from the Card class to reflect the ordering of cards used in a Big Two card game.
 * @author Devang
 *
 */
public class BigTwoCard extends Card
{
	private static final long serialVersionUID = -5822018182891811204L;

	/**
	 * Constructor for building a card with the specified suit and rank. 
	 * Suit is an integer between 0 and 3, and rank is an integer between 0 and 12.
	 * It simply calls the super (Card) class constructor
	 * @param suit
	 * 		Used to specify the suit - an integer between 0 and 3
	 * 		0 = Diamond, 1 = Club, 2 = Heart, 3 = Spade
	 * @param rank
	 * 		Used to specify the rank according to normal card - an integer between 0 and 12
	 *		 0 = 'A', 1 = '2', 2 = '3', ..., 8 = '9', 9 = '0', 10 = 'J', 11
	 *            = 'Q', 12 = 'K'		
	 */
	public BigTwoCard(int suit, int rank)
	{
		super(suit,rank);
	}
	
	/**
	 * Compares this card with the specified card for order according to BigTwo game logic
	 * 
	 * @param card
	 *            the card to be compared
	 * @return a negative integer, zero, or a positive integer as this card is
	 *         less than, equal to, or greater than the specified card
	 */
	public int compareTo(Card card)
	{
		// Using BigTwo game logic where 2 is the highest card and 3 is the smallest
		if ((this.rank+11)%13 > (card.rank+11)%13) { 
			return 1;
		} else if ((this.rank+11)%13 < (card.rank+11)%13) {
			return -1;
		} else if (this.suit > card.suit) {
			return 1;
		} else if (this.suit < card.suit) {
			return -1;
		} else {
			return 0;
		}
	}
}
