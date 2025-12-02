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
import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Card.CompareOn ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Rank ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.pile.Deck ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.pile.Pile ;
import edu.wit.scds.ds.lists.app.card_game.trash.pile.DiscardPile ;
import edu.wit.scds.ds.lists.app.card_game.trash.pile.Hand ;
import edu.wit.scds.ds.lists.app.card_game.trash.pile.Stock ;
import edu.wit.scds.ds.lists.app.card_game.universal_base.support.NoCardsException ;

import java.io.File ;
import java.io.FileNotFoundException ;
import java.util.Scanner ;

/**
 * Representation of a player
 * <p>
 * NOTE: You will modify this code
 *
 * @author Dave Rosenberg
 *
 * @version 1.0 2021-12-08 Initial implementation
 * @version 2.0 2025-06-28 track changes to other classes
 * @version 2.1 2025-11-04 track changes to other classes
 *
 * @author Cath Kreig
 *
 * @version 3.0 2025-11-03 modifications for your game
 */
public final class Player
    {

    /*
     * data fields
     */

    private final Hand hand ;
    public final String name ;


    public Player( final String playerName )
        {

        this.name = playerName ;
        this.hand = new Hand() ;

        }


    public void dealtACard( final Card dealt )
        {

        this.hand.addToBottom( dealt.hide() ) ;

        }


    /**
     * Returns true when the player's hand contains A..10 in the correct slots and each of those
     * cards is face-up.
     */
    public boolean hasWon()
        {

        for ( int slot = 0 ; slot < 10 ; slot++ )
            {
            final Card c = (Card) this.hand.getCardAt( slot ) ;

            if ( ( c == null ) || !c.isFaceUp() )
                {
                return false ;
                }

            final Rank expectedRank = switch ( slot )
                {
                case 0 -> Rank.ACE ;
                case 1 -> Rank.TWO ;
                case 2 -> Rank.THREE ;
                case 3 -> Rank.FOUR ;
                case 4 -> Rank.FIVE ;
                case 5 -> Rank.SIX ;
                case 6 -> Rank.SEVEN ;
                case 7 -> Rank.EIGHT ;
                case 8 -> Rank.NINE ;
                case 9 -> Rank.TEN ;
                default -> null ;
                } ;

            if ( ( expectedRank == null ) || ( c.rank != expectedRank ) )
                {
                return false ;
                }

            }

        return true ;

        }


    public void takeTurn( final Stock stock,
                          final DiscardPile discardPile )
        {

        Card card = null ;

        try
            {
            final Card top = discardPile.getTopCard() ;

            if ( ( top != null ) && isUsable( top ) &&
                 this.hand.isSlotAvailable( top ) )
                {
                card = discardPile.removeTopCard() ;
                }

            }
        catch ( final NoCardsException ignored )
            {
            card = null ;
            }

        if ( card == null )
            {
            card = drawCardFromStock( stock ) ;
            }

        while ( ( card != null ) && isUsable( card ) &&
                this.hand.isSlotAvailable( card ) )
            {
            final Card displaced = this.hand.replace( card ) ;

            if ( displaced == null )
                {
                card = null ;
                break ;
                }

            card = displaced.reveal() ;
            }

        if ( card != null )
            {
            discardPile.addToTop( card.reveal() ) ;
            }

        }


    public String revealHand()
        {

        if ( this.hand.cardCount() == 0 )
            {
            return "empty" ;
            }

        return this.hand.revealAll().toString() ;

        }


    public Pile turnInAllCards()
        {

        class AllCards extends Pile
            { /* temporary */ }

        final AllCards all = new AllCards() ;
        all.moveCardsToBottom( this.hand ) ;
        return all ;

        }

    /* helpers */


    private boolean isUsable( final Card c )
        {

        if ( c == null )
            {
            return false ;
            }

        return switch ( c.rank )
            {
            case ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN -> true ;
            default -> false ;
            } ;

        }


    private Card drawCardFromStock( final Stock stock )
        {

        try
            {
            return stock.drawTopCard().reveal() ;
            }
        catch ( final NoCardsException e )
            {
            return null ;
            }

        }


    @Override
    public String toString()
        {

        // return String.format( "%nPlayer: %s%n\thand: %s", this.name, revealHand() ) ;
        return String.format( "%nPlayer: %s%n\thand: %s",
                              this.name,
                              this.hand ) ;

        }


    public static void main( final String[] args )
        {

        Card.setCompareOnAttributes( CompareOn.COMPARE_RANK_ONLY ) ;
        Rank.setUseAltOrder( true ) ;

        final Deck testDeck = new Deck() ;

        final Stock testStock = new Stock( testDeck ) ;

        final Card lookupJoker = new Card( JOKER ) ;
        Card foundJoker ;

        while ( ( foundJoker = testStock.removeCard( lookupJoker ) ) != null )
            {
            testDeck.addToBottom( foundJoker ) ;
            }

        testStock.shuffle() ;

        testStock.revealAll() ;
        System.out.printf( "Stock: %s%n%n", testStock ) ;
        testStock.hideAll() ;

        testDeck.revealAll() ;
        System.out.printf( "Deck: %s%n%n", testDeck ) ;
        testDeck.hideAll() ;

        final Player testPlayer = new Player( "tester" ) ;

        System.out.printf( "start: %s%n", testPlayer ) ;

        for ( int i = 1 ; i <= 5 ; i++ )
            {
            final Card dealt = testStock.drawTopCard().reveal() ;

            testPlayer.dealtACard( dealt ) ;
            }

        System.out.printf( "%ndealt: %s%n", testPlayer ) ;

        try ( Scanner input = new Scanner( new File( "./data/readme.txt" ) ) ; )
            {

            while ( input.hasNextLine() )
                {
                System.out.printf( "%s%n", input.nextLine() ) ;
                }

            }
        catch ( final FileNotFoundException e )
            {
            System.err.printf( "failed to open readme.txt:%n%s%n", e ) ;
            }

        }

    }   // end class Player