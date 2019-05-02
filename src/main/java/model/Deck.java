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
    private ArrayDeque<T> cards = new ArrayDeque<>();
    private ArrayList<T> discard = new ArrayList<>();

    public Deck() {
    }

    /**
     * Constructs a deck with the given collection of cards, automatically
     * shuffling it.
     *
     * @param cards list of cards constituting the deck
     */
    public Deck(Collection<T> cards) {
        discard.addAll(cards);
        shuffle();
    }

    /**
     * Shuffles a new deck made from the discard pile, which is then cleared.
     */
    private void shuffle() {
        Collections.shuffle(discard);
        cards = new ArrayDeque<>(discard);
        discard.clear();
    }

    /**
     * Draws a card and removes it from the deck. If the deck is empty it's
     * reshuffled using <code>shuffle</code>.
     *
     * @return the card drawn
     */
    public T draw() {
        if (cards.isEmpty()) shuffle();
        return cards.pop();
    }

    /**
     * Adds a card to the discard pile, that can be can be reshuffled into
     * the deck once it is emptied.
     *
     * @param c the card to be discarded
     */
    public void discard(T c) {
        discard.add(c);
    }
}
