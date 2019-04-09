package model;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * <code>Deck</code> is a generic class used to simulate a deck of cards
 * with a discard pile; once emptied, can be refilled reshuffling
 * the discard pile.
 * It contains method to shuffle, draw and discard from hand.
 *
 * @param <T>
 */
public class Deck<T> {
    private ArrayDeque<T> deck;
    private ArrayList<T> discard;

    /**
     * sole constructor. Automatically shuffles the given cards.
     *
     * @param cards list of cards constituting the deck
     */
    public Deck(Collection<? extends T> cards) {
        discard = new ArrayList<>(cards);
        shuffle();
    }

    /**
     * Shuffles a new deck made from the discard pile, which is then cleared.
     */
    private void shuffle() {
        Collections.shuffle(discard);
        deck = new ArrayDeque<>(discard);
        discard.clear();
    }

    /**
     * Draws a card and removes it from the deck. If the deck is empty
     * reshuffles it using <code>shuffle</code>.
     *
     * @return the card drawn
     */
    public T draw() {
        if (deck.isEmpty()) shuffle();
        return deck.pop();
    }

    /**
     * Adds a card to the discard pile.
     *
     * @param c the card to be discard
     */
    public void discard(T c) {
        discard.add(c);
    }
}
