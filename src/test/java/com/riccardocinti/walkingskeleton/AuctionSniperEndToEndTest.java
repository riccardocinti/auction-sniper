package com.riccardocinti.walkingskeleton;


import org.junit.After;
import org.junit.Test;

public class AuctionSniperEndToEndTest {

    private static final FakeAuctionServer auctionServer = new FakeAuctionServer("item-54321");
    private static final ApplicationRunner applicationRunner = new ApplicationRunner();

    @Test
    public void sniperJoinsAuctionUntilAuctionCloses() throws Exception {
        auctionServer.startSellingItem();

        applicationRunner.startBiddingIn(auctionServer, "JoinCloses");
        auctionServer.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);

        auctionServer.announceClosed();
        applicationRunner.showsSniperHasLostAuction();
    }

    @Test
    public void sniperMakesAHigherBidButLoses() throws Exception {
        auctionServer.startSellingItem();

        applicationRunner.startBiddingIn(auctionServer, "HigherBid");
        auctionServer.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);

        auctionServer.reportPrice(1000, 98, "other bidder");
        applicationRunner.hasShownSniperIsBidding();

        auctionServer.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);

        auctionServer.announceClosed();
        applicationRunner.showsSniperHasLostAuction();
    }

    @After
    public void stopApplication() {
        applicationRunner.stop();
    }

    @After
    public void stopAuction() {
        auctionServer.stop();
    }
}
