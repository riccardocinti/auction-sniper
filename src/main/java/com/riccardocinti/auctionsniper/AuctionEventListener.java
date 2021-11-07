package com.riccardocinti.auctionsniper;

public interface AuctionEventListener {

    void auctionClosed();

    default void currentPrice(int price, int increment) {
    }

}
