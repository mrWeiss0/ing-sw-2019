package model.board;


import model.Deck;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    @Test
    void testCreation() {
        Integer[] source = new Integer[]{1, 2, 5};
        Deck<Integer> deck = new Deck<>(Arrays.asList(source));
        assertTrue(Arrays.asList(source).contains(deck.draw()));
        assertTrue(Arrays.asList(source).contains(deck.draw()));
        assertTrue(Arrays.asList(source).contains(deck.draw()));
    }

    @Test
    void testDiscard() {
        Deck<Integer> deck = new Deck<>(Collections.emptyList());
        deck.discard(4);
        assertEquals(4, deck.draw());
    }

    @Test
    void testException() {
        Deck<Integer> deck = new Deck<>(Collections.emptyList());
        assertThrows(NoSuchElementException.class, deck::draw);
    }

}