package com.company;

import java.util.ArrayList;

public class NightReport {
    // NightReport holds the winning and losing decklists from each session

    public Decklist getWinningDeck() {
        return winningDeck;
    }

    public void setWinningDeck(Decklist winningDeck) {
        this.winningDeck = winningDeck;
    }

    public Decklist getLosingDeck() {
        return losingDeck;
    }

    public void setLosingDeck(Decklist losingDeck) {
        this.losingDeck = losingDeck;
    }

    public ArrayList<Decklist> getBothLists(){
        ArrayList<Decklist> bothLists = new ArrayList<Decklist>();
        bothLists.add(winningDeck);
        bothLists.add(losingDeck);
        return bothLists;
    }

    public boolean hasBothLists(){
        if (winningDeck != null && losingDeck != null){
            return true;
        } else return false;
    }

    public int getLargestListSize(){
        return Math.max(winningDeck.getListSize(), losingDeck.getListSize());
    }


    private Decklist winningDeck;
    private Decklist losingDeck;
}
