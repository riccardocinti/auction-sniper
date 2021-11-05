package com.riccardocinti.walkingskeleton;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

public class AuctionSniperEndToEndTest {

    private static final FakeAuctionServer auctionServer = new FakeAuctionServer("item-54321");
    private static final ApplicationRunner applicationRunner = new ApplicationRunner();

    @Test
    public void sniperJoinsAuctionUntilAuctionCloses() throws Exception {
        auctionServer.startSellingItem();
        applicationRunner.startBiddingIn(auctionServer);
        auctionServer.hasReceivedJoinRequestFromSniper();
        auctionServer.announceClosed();
        applicationRunner.showsSniperHasLostAuction();
    }

    @AfterAll
    public static void stopAuction() {
        auctionServer.stop();
    }

    @AfterAll
    public static void stopApplication() {
        applicationRunner.stop();
    }
}
