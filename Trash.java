/* @formatter:off
 *
 * © David M Rosenberg
 *
 * Topic: List App ~ Card Game
 *
 * Usage restrictions:
 *
 * You may use this code for exploration, experimentation, and furthering your
 * learning for this course. You may not use this code for any other
 * assignments, in my course or elsewhere, without explicit permission, in
 * advance, from myself (and the instructor of any other course).
 *
 * Further, you may not post (including in a public repository such as on github)
 * nor otherwise share this code with anyone other than current students in my
 * sections of this course.
 *
 * Violation of these usage restrictions will be considered a violation of
 * Wentworth Institute of Technology's Academic Honesty Policy.  Unauthorized posting
 * or use of this code may also be considered copyright infringement and may subject
 * the poster and/or the owners/operators of said websites to legal and/or financial
 * penalties.  My students are permitted to store this code in a private repository
 * or other private cloud-based storage.
 *
 * Do not modify or remove this notice.
 *
 * @formatter:on
 */


package edu.wit.scds.ds.lists.app.card_game.trash.game ;

import static edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Rank.JOKER ;

import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Card ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.pile.Deck ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.pile.Pile ;
import edu.wit.scds.ds.lists.app.card_game.trash.pile.DiscardPile ;
import edu.wit.scds.ds.lists.app.card_game.trash.pile.Stock ;
import edu.wit.scds.ds.lists.app.card_game.universal_base.support.NoCardsException ;

import java.util.ArrayList ;
import java.util.List ;
import java.util.Scanner ;


/**
 * NOTE: You will modify this code
 * <p>
 * NOTE This is a sample, fictitious card game
 * <p>
 * This is the main driver for the game of Trash. It supports three players.
 * <p>
 * Goal: to collect one card each from ace (acting as one), through ten
 * <p>
 * Rules:
 * <ul>
 * Players are dealt 10 cards total. Cards are dealt face down in two rows of five cards each. The
 * remaining cards form a draw pile. The goal is to replace or rearrange the facedown cards so you
 * have 10 faceup cards, starting with an ace in the upper left-hand corner and ending with a 10 in
 * the bottom right-hand corner. Kings, queens, and jacks are not playable. Any player can go first.
 * They draw the top card from the draw pile. If the player draws a king, jack, or queen, their turn
 * is automatically over. The player puts the card in a faceup draw pile next to the facedown draw
 * pile. If the player draws an ace through 10, they find the right spot in their rows for the card
 * and replace the facedown card. If the facedown card is the same as the card being played (e.g.,
 * if a player draws an ace and there is already an ace in the top left corner), they must discard
 * one of the aces and end their turn. If the facedown card is one the player can use in their grid,
 * they can move it to the proper spot and pick up the facedown card in that spot.
 * </ul>
 * <p>
 * NOTE You must rename this class to whatever your game is called. If the name of the game begins
 * with a number, spell out the number (can't start a class name with a digit). Replace the comments
 * above describing my game with appropriate comments for yours.
 *
 * @author David M Rosenberg
 *
 * @version 1.0 2025-03-27 Initial implementation
 * @version 2.0 2025-06-28 track changes to other classes
 * @version 2.1 2025-11-19 validate the deck(s) at the end of the game
 *
 * @author Cath Kreig
 *
 * @version 3.0 2025-11-03 modifications for your implementation
 */
public final class Trash
    {
    /*
     * constants
     */

    /** can't play with fewer than this many decks at an absolute minimum */
    private final static int MINIMUM_NUMBER_OF_DECKS = 1 ;

    /** can't play with fewer than this many players at an absolute minimum */
    private final static int MINIMUM_PLAYER_COUNT = 3 ;

    private final static int CARDS_PER_HAND = 10 ;

    /*
     * data fields
     */

    private boolean running = false ;
    private final Scanner scanner ;
    private final List<Player> players = new ArrayList<>() ;

    private final List<Deck> decks = new ArrayList<>() ;
    private Deck deck ;
    private Stock stock ;
    private DiscardPile discardPile ;


    /*
     * constructors
     */


    /**
     * set up the game instance
     *
     * @param input
     *     used for player interactions
     */
    private Trash( final Scanner input )
        {

        this.scanner = input ;

        }   // end constructor


    /*
     * game driver
     */


    /**
     * This is the top-level driver for the game of Top This.
     *
     * @param args
     *     -unused-
     */
    public static void main( final String[] args )
        {

        try ( final Scanner input = new Scanner( System.in ) )
            {
            final Trash game = new Trash( input ) ;

            welcome() ;
            displayDivider() ;

            game.setup() ;

            while ( game.running )
                {
                game.run() ;

                if ( !game.running )
                    {
                    game.tearDown() ;
                    return ;
                    }

                displayDivider() ;
                game.summary() ;
                displayDivider() ;

                final String again = game.promptForLine( "Play again?" ) ;

                if ( ( again == null ) || again.isEmpty() ||
                     ( Character.toLowerCase( again.charAt( 0 ) ) != 'y' ) )
                    {
                    game.running = false ;
                    game.tearDown() ;
                    return ;
                    }

                game.reset() ;
                }

            game.tearDown() ;
            }   // end try (input)

        }   // end main()


    /*
     * operational methods
     */


    private static void displayDivider()
        {

        System.out.printf( "%n--------------------%n%n" ) ;

        }   // end displayDivider()


    /**
     * prepare the game to run again
     */
    private void reset()

        {

        this.stock.moveCardsToBottom( this.discardPile ) ;

        for ( final Player p : this.players )
            {
            this.stock.moveCardsToBottom( p.turnInAllCards() ) ;
            }

        }   // end reset()


    /**
     * primary driver for the game
     */
    private void run()
        {

        this.running = true ;

        int current = 0 ;

        while ( this.running )
            {
            final Player p = this.players.get( current ) ;

            displayDivider() ;
            System.out.println( "TURN: " + p.name ) ;

            
            System.out.println( "Current boards:" ) ;

            for ( final Player pl : this.players )
                {
                System.out.println( pl ) ;   // uses Player.toString()
                }

            System.out.println() ;

            try
                {
                System.out.println( "Top of discard pile: " +
                                    this.discardPile.getTopCard() ) ;
                }
            catch ( final NoCardsException e )
                {
                System.out.println( "Top of discard pile: (empty)" ) ;
                }

            System.out.println() ;

            p.takeTurn( this.stock, this.discardPile ) ;

            if ( p.hasWon() )
                {
                System.out.println( "\n" + p.name + " has completed A–10!" ) ;
                this.running = false ;
                break ;
                }

            current = ( current + 1 ) % this.players.size() ;
            }

        }   // end run()


    /**
     * prepare to play the game
     */
    private void setup()
        {

        this.running = true ;

        displayDivider() ;
        System.out.println( "Setting up Trash..." ) ;

        for ( int i = 1 ; i <= MINIMUM_PLAYER_COUNT ; i++ )
            {
            final String name = promptForLine( String.format( "Enter name of player %d:",
                                                              i ) ) ;

            if ( !this.running )
                {
                return ;
                }

            this.players.add( new Player( name ) ) ;
            }

        this.deck = new Deck() ;
        this.decks.add( this.deck ) ;

        final Pile all = this.deck.removeAllCards() ;

        final Card lookupJoker = new Card( JOKER ) ;
        final Pile jokers = all.removeAllMatchingCards( lookupJoker ) ;

        this.deck.moveCardsToBottom( jokers.revealAll() ) ;

        this.stock = new Stock() ;
        this.stock.moveCardsToBottom( all ) ;
        this.stock.shuffle() ;

        for ( int i = 0 ; i < CARDS_PER_HAND ; i++ )
            {

            for ( final Player p : this.players )
                {
                final Card dealt = this.stock.drawTopCard().hide() ;
                p.dealtACard( dealt ) ;
                }

            }

        this.discardPile = new DiscardPile() ;
        final Card first = this.stock.drawTopCard().reveal() ;
        this.discardPile.addToTop( first ) ;

        System.out.println( "Starting discard: " + first ) ;

        displayDivider() ;
        System.out.println( "Setup complete!" ) ;

        }   // end setup()


    /**
     * displays the results of playing the game
     */
    private void summary()
        {

        System.out.println( "Game Over Summary:\n" ) ;

        for ( final Player p : this.players )
            {
            System.out.println( p ) ;
            }

        System.out.println( "\nCards in discard pile:" ) ;
        System.out.println( this.discardPile.revealAll() ) ;

        System.out.println( "\nCards remaining in stock:" ) ;
        System.out.println( this.stock.revealAll() ) ;

        }   // end summary()


    /**
     * finished running the game
     */
    private void tearDown()
        {

        displayDivider() ;

        reset() ;

        this.players.clear() ;

        this.stock.sort() ;

        int deckIndex = 0 ;

        while ( !this.stock.isEmpty() )
            {
            final Card c = this.stock.removeTopCard() ;
            this.decks.get( deckIndex ).addToBottom( c ) ;
            deckIndex = ( deckIndex + 1 ) % this.decks.size() ;
            }

        for ( final Deck d : this.decks )
            {
            System.out.printf( "\t%s%n", d.revealAll().toString() ) ;
            d.hideAll() ;
            d.validateDeck() ;
            }

        this.decks.clear() ;

        System.out.printf( "Thank you for playing Trash!\n" ) ;

        }   // end tearDown()


    /**
     * display introductory message
     *
     * @since 2.0
     */
    private static void welcome()
        {

        System.out.println( """
                            Welcome to TRASH!

                            Fill positions 1–10 using cards Ace through Ten.
                            First player to complete their board wins.

                            Enter '.' at any prompt to quit.
                            """ ) ;

        }   // end welcome()


    /*
     * utility methods
     */


    /**
     * displays a formatted prompt
     *
     * @param prompt
     *     the prompt with optional formatting specifiers
     * @param arguments
     *     argument(s) used by the formatting specifiers
     */
    private static void displayPrompt( final String prompt,
                                       final Object... arguments )
        {

        System.out.printf( "%s ", String.format( prompt, arguments ) ) ;

        }   // end displayPrompt()


    private String promptForLine( final String prompt,
                                  final Object... args )
        {

        displayPrompt( prompt, args ) ;

        if ( !this.scanner.hasNextLine() )
            {
            this.running = false ;
            return null ;
            }

        final String line = this.scanner.nextLine().trim() ;

        if ( ".".equals( line ) )
            {
            this.running = false ;
            return null ;
            }

        return line ;

        }

    }   // end class YourGame