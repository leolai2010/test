/** 
 * The purpose of this project is to create a black jack game 
 * the game plays like a normal card game and assumes that multiple 
 * deck of cards are in play and duplication is possible!
 * @author Leo Lai
 * @version 1.0
**/
// Import necessary package
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

// Create the main class of the program
public class Blackjack
{
    public static void main(String [] args)
    {
        // Define player and dealer's hand by having them both create their own empty decks
        System.out.println("Welcome to BlackJack!");
        Deck deck = new Deck();
        deck.startDeck(); 
        Deck playerDeck = new Deck();
        Deck dealerDeck = new Deck();
        double playerMoney = 100.00;
        Scanner input = new Scanner(System.in);
        // This loop will start the game
        while(playerMoney>0)
        {
            // First declare the variables that will hold player's bet and account for round ending
            System.out.println("You have $"+playerMoney+" How much would you like to bet?");
            double playerBet = input.nextDouble();
            boolean roundEnds = false;
            // This statement will account for user betting an unreasonable amount
            if(playerBet>playerMoney)
            {
                System.out.println("You can't bet more than you have!");
                break;
            }
            System.out.println("Hands are being dealt");
            // Player draw two cards 
            playerDeck.drawCard(deck);
            playerDeck.drawCard(deck);
            // Dealer draw two cards
            dealerDeck.drawCard(deck);
            dealerDeck.drawCard(deck);
            // This is the game round loop 
            while(true)
            {
                // Show player's hand and total value
                System.out.println("Hand: "+playerDeck.toString());
                System.out.println("Valued at: "+playerDeck.cardsValue());
                // Show part of dealer's hand
                System.out.println("Dealer Hand: "+dealerDeck.getCard(0).toString()+" + [Hidden]");
                // Prompt user action 
                System.out.println("[1]Hit or [2]Stay");
                int response = input.nextInt();
                // Condition for the actions
                if(response == 1)
                {
                    playerDeck.drawCard(deck);
                    System.out.println("Hit: "+playerDeck.getCard(playerDeck.deckSize()-1).toString());
                    // This nested condition will determine if the player bust or not
                    // If they do then lose the bet money and end the round!
                    if(playerDeck.cardsValue() > 21)
                    {
                        System.out.println("Bust!");
                        System.out.println("You have a total value of: "+playerDeck.cardsValue());
                        playerMoney -= playerBet;
                        roundEnds = true;
                        break;
                    }
                }
                else if(response == 2)
                {
                    break;
                }
            }
            // Player action is over at this point and dealer action will start here
            System.out.println("Dealer Cards:" +dealerDeck.toString());
            if((dealerDeck.cardsValue()>playerDeck.cardsValue()) && roundEnds == false)
            {
                System.out.println("Dealer Wins!");
                System.out.println("Dealer: "+dealerDeck.cardsValue()+" V.S. Player: "+playerDeck.cardsValue());
                playerMoney -= playerBet;
                roundEnds = true;
            }
            // Dealer's action logic starts here. hits at 16 and stands at 17
            while((dealerDeck.cardsValue() < 17) && roundEnds == false)
            {
				dealerDeck.drawCard(deck);
				System.out.println("Dealer Hit: " + dealerDeck.getCard(dealerDeck.deckSize()-1).toString());
            }
            // Display total values from dealer
			System.out.println("Dealers total value: " + dealerDeck.cardsValue());
			// Determine if dealer busted
            if((dealerDeck.cardsValue()>21) && roundEnds == false)
            {
				System.out.println("Dealer Busts. You Win!");
				playerMoney += playerBet;
				roundEnds = true;
            }
            // Determine if drawCard happens
			if((dealerDeck.cardsValue() == playerDeck.cardsValue()) && roundEnds == false){
                System.out.println("Its a drawCard!");
                roundEnds = true;
            }
            // If player does win 
            if((playerDeck.cardsValue() > dealerDeck.cardsValue()) && roundEnds == false){
				System.out.println("You win the hand.");
				playerMoney += playerBet;
				roundEnds = true;
            }
            // If dealer wins
			else if(roundEnds == false) 
			{
				System.out.println("Dealer Wins!");
				playerMoney -= playerBet;
            }
            // End of the round!
            System.out.println("End of Round.");
        }
        // Game over if player runs out of money
        System.out.println("You're out of Money");
        System.out.println("GAME OVER!");
        input.close();
    }
    /** 
     * Here is the deck class that encompass all the function call written above
     * the deck class consist of functions that describe how to get card value, draw cards
     * and so on. The randomness of the draw is done by the java utility import 
     * ArrayList is used here as well because the requirement for object-oriented design 
     * This data type makes much more sense and utilize built-in java function existed instead
     * of reinventing the wheel 
     * @return sum of deck values, card suit and card rank
    **/
    static class Deck
    {
        private ArrayList<Card> cards;
        public Deck()
        {
            this.cards = new ArrayList<Card>();
        }
        // This nested for-loop function will allow the addition of total 52 cards in a normal deck
        // since 13 ranks with 4 suits returns 52 cards
        public void startDeck()
        {
            for(Suit cardSuit:Suit.values())
            {
                for(Rank cardRank:Rank.values())
                {
                    this.cards.add(new Card(cardSuit, cardRank));
                }
            }
        }
        // Here I use get card to return the card name
        public Card getCard(int card)
        {
            return this.cards.get(card);
        }
        /** 
         * Here the draw card function will draw at random of the 52 cards
         * however since there is no limitation on the randomness duplication is possible
         * this can create an illusion of multiple decks are being played 
         * which is not against the rule of black jack!
        **/
        public void drawCard(Deck owner)
        {
            Random random = new Random();
            int randomCard = random.nextInt(51-0+1)+0;
            this.cards.add(owner.getCard(randomCard));
        }
        public int deckSize()
        {
            return this.cards.size();
        }
        /** 
         * this function is mainly created to show player and dealers hand 
         * by looping through the deck and return their suit and rank
         * @return cardlist string 
        **/
        public String toString()
        {
            String cardList = "";
            int i = 0;
            for (Card card:this.cards)
            {
                cardList += card.toString() + " ";
                i++;
            }
            return cardList;
        }
        /** 
         * this function is to assign values to the cards to be able to tell 
         * the total points of the hand
         * @param totalValue 
         * @param Ace
         * @return totalValue
        **/
        public int cardsValue()
        {
            int totalValue = 0;
            int aceValue = 0;
            for(Card Card : this.cards)
            {
                switch(Card.getRank())
                {
                    case TWO: 
                        totalValue+=2; 
                        break;
                    case THREE: 
                        totalValue+=3; 
                        break;
                    case FOUR: 
                        totalValue+=4; 
                        break;
                    case FIVE: 
                        totalValue+=5; 
                        break;
                    case SIX: 
                        totalValue+=6; 
                        break;
                    case SEVEN: 
                        totalValue+=7; 
                        break;
                    case EIGHT: 
                        totalValue+=8; 
                        break;
                    case NINE: 
                        totalValue+=9; 
                        break;
                    case TEN: 
                        totalValue+=10; 
                        break;
                    case JACK: 
                        totalValue+=10; 
                        break;
                    case QUEEN: 
                        totalValue+=10; 
                        break;
                    case KING: 
                        totalValue+=10; 
                        break;
                    case ACE: 
                        aceValue+=1; 
                        break;
                }
            }
            for(int i=0; i<aceValue; i++)
            {
                if(totalValue>10)
                {
                    totalValue+=1;
                }
                else
                {
                    totalValue+=11;
                }
            }
            return totalValue;
        }
    }
    /** 
     * this is the card class which consist of the suit and rank 
     * @param suit 
     * @param rank 
     * @return suit + rank 
    **/
    static class Card
    {
        private Suit suit;
        private Rank rank;
        public Card(Suit suit, Rank rank)
        {
            this.suit = suit;
            this.rank = rank;
        }
        public Rank getRank()
        {
            return this.rank;
        }
        public String toString()
        {
            return this.suit.toString() + "-" + this.rank.toString();
        }
    }
    /** 
     * this is the enum class of rank. Enum class serve the purpose of represetning a group of name constants
     * this is much simpler to use in a object oriented program much like Arraylist instead of creating an array 
     * of variables. The constants are enumerated as ranks of card and can be called upon by the preceeding classes
     * @param rank 
     * @return rank 
    **/
    static enum Rank
    {
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
    }
    /** 
     * this is the enum class of suit
     * @param suit 
     * @return suit 
    **/
    static enum Suit 
    {
        HEART, SPADE, DIAMOND, CLUB
    }
}