package model;



import java.util.*;


public class Deck<T> {
    private ArrayDeque<T> deck;
    private ArrayList<T> discard;
    public Deck(Collection<? extends T> cards){
        discard = new ArrayList<>(cards);
        shuffle();
    }

    private void shuffle() {
        Collections.shuffle(discard);
        deck = new ArrayDeque<>(discard);
        discard.clear();
    }
    public T draw(){
        if(deck.isEmpty()) shuffle();
        return deck.pop();
    }
    public void discard(T c){
        discard.add(c);
    }
}
