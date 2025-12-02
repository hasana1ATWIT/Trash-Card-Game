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


package edu.wit.scds.ds.lists.app.card_game.trash.pile ;

import static edu.wit.scds.ds.lists.app.card_game.universal_base.support.Orientation.FACE_DOWN ;

import edu.wit.scds.ds.lists.app.card_game.standard_cards.card.Card ;
import edu.wit.scds.ds.lists.app.card_game.standard_cards.pile.Pile ;
import edu.wit.scds.ds.lists.app.card_game.universal_base.support.NoCardsException ;

import java.util.Collections ;

/**
 * Representation of a hand of cards
 * <p>
 * NOTE: You probably will modify this code
 *
 * @author Dave Rosenberg
 *
 * @version 1.0 2021-12-08 Initial implementation
 * @version 2.0 2025-03-30 track changes to {@code Pile}
 * @version 3.0 2025-06-28
 *     <ul>
 *     <li>track changes to other classes
 *     <li>switch {@code remove[Highest,Lowest]Card()} from manual loops to
 *     {@code Collections.[max,min]()}
 *     </ul>
 *
 * @author Abia Hasan
 *
 * @version 4.0 2025-11-03 modifications for your implementation
 */
public final class Hand extends Pile
    {

    // no additional data fields


    /*
     * constructors
     */

    /**
     * initialize hand with {@code Card}s placed face down by default
     */
    public Hand()
        {

        super( FACE_DOWN ) ;

        }	// end no-arg constructor


    /*
     * public methods
     */


    /**
     * retrieve and remove the highest value card in the hand
     *
     * @return the highest value card
     *
     * @throws NoCardsException
     *     if the hand is empty
     */
    public Card removeHighestCard() throws NoCardsException
        {

        if ( isEmpty() )
            {
            throw new NoCardsException() ;
            }

        return removeCard( Collections.max( super.cards ) ) ;

        }  // end removeHighestCard()


    /**
     * retrieve and remove the lowest value card in the hand
     *
     * @return the lowest value card
     *
     * @throws NoCardsException
     *     if the hand is empty
     */
    public Card removeLowestCard() throws NoCardsException
        {

        if ( isEmpty() )
            {
            throw new NoCardsException() ;
            }

        return removeCard( Collections.min( super.cards ) ) ;

        }  // end removeLowestCard()


    /**
     * Converts the rank of the specified card to its corresponding Trash slot index in this hand.
     * In Trash, ACE–TEN map to slots 0–9 respectively. All other ranks (JACK, QUEEN, KING, JOKER)
     * do not map to a playable slot and return -1.
     *
     * @param currentCard
     *     the card whose rank should be mapped to a slot index
     *
     * @return the slot index (0-9) or -1 if the rank does not correspond to a playable slot
     *
     * @since 1.0
     */
    public int rankToIndex( final Card currentCard )
        {

        return switch ( currentCard.rank )
            {
            case ACE -> 0;
            case TWO -> 1;
            case THREE -> 2;
            case FOUR -> 3;
            case FIVE -> 4;
            case SIX -> 5;
            case SEVEN -> 6;
            case EIGHT -> 7;
            case NINE -> 8;
            case TEN -> 9;
            default -> -1;
            } ;

        }


    /**
     * Determines whether the Trash slot corresponding to the specified card's rank is currently
     * available in this hand. A slot is considered available if the card 's rank maps to a valid
     * slot index in this hand and the card currently at the index is face down.
     *
     * @param currentCard
     *     the card whose correspoinding slot should be checked
     *
     * @return true if slot exists and the card in that slot is face down. false otherwises
     *
     * @since 1.0
     */
    public boolean isSlotAvailable( final Card currentCard )
        {

        // Gets the rank of the card and converts to slot (where the slots represents the player's
        // hand)
        final int index = rankToIndex( currentCard ) ;

        if ( index < 0 )
            {
            return false ;
            }

        // Get's the corresponding card in the player's hand that matches the index of current card
        final Card slot = (Card) this.cards.get( index ) ;

        // Returns true if the card in player's hand is face down and thus available.
        return slot.getOrientation() == FACE_DOWN ;

        }


    /**
     * Replace's the card in this hand's Trash slot corresponding to the specificed card's rank with
     * the specified card.
     *
     * @param currentCard
     *     the card to place into its corresponding Trash slot
     * 
     * @return the card that was previosuly occupying that slot
     *
     * @since 1.0
     */
    public Card replace( final Card currentCard )
        {

        if ( !isSlotAvailable( currentCard ) )
            {
            throw new IllegalStateException( "card is not replacable." ) ;
            }

        final int index = rankToIndex( currentCard ) ;

        final Card oldCard = (Card) this.cards.get( index ) ;

        currentCard.setFaceUp() ;
        this.cards.set( index, currentCard ) ;

        return oldCard ;

        }


    /**
     * (optional) test driver
     *
     * @param args
     *     -unused-
     */
    public static void main( final String[] args )
        {
        // TODO Auto-generated method stub

        }	// end main()


    /**
     * @param pos
     * @param reveal
     *
     * @return
     *
     * @since 1.0
     */


    }	// end class Hand