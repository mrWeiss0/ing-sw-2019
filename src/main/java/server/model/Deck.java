package server.model;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * <code>Deck</code> is a generic class used to simulate a deck of cards
 * with a discard pile; the discard pile can be reshuffled into the deck to
 * avoid its depletion.
 * <p>
 * It provides methods to shuffle, draw and discard from hand.
 *
 * @param <T>
 */
public class Deck<T> {
    private final ArrayList<T> discard = new ArrayList<>();
    private final ArrayDeque<T> cards = new ArrayDeque<>();

    /**
     * Shuffles into this deck the discard pile, which is then cleared.
     */
    private void shuffle() {
        Collections.shuffle(discard);
        cards.addAll(discard);
        discard.clear();
    }

    /**
     * Draws a card and removes it from the deck. Before drawing, if this
     * deck is empty, it's reshuffled using <code>shuffle</code>.
     *
     * @throws java.util.NoSuchElementException if both the deck and the discard pile are empty
     * @return the card drawn
     */
    public T draw() {
        if (cards.isEmpty()) shuffle();
        return cards.pop();
    }

    /**
     * Adds a card to this deck's discard pile.
     *
     * @param c the card to be discarded
     */
    public void discard(T c) {
        discard.add(c);
    }

    /**
     * Adds a group of cards to this deck's discard pile.
     *
     * @param c the collections of cards to be discarded
     */
    public void discard(Collection<T> c) {
        discard.addAll(c);
    }
}
