package com.riccardocinti.walkingskeleton;

import com.riccardocinti.main.Main;
import com.riccardocinti.main.MainWindow;

import static com.riccardocinti.walkingskeleton.FakeAuctionServer.XMPP_HOSTNAME;

public class ApplicationRunner {

    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String SNIPER_XMPP_ID = "sniper@localhost/Auction";

    private AuctionSniperDriver driver;

    public void startBiddingIn(final FakeAuctionServer auction, String tName) {
        Thread thread = new Thread(tName) {
            @Override
            public void run() {
                try {
                    Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        driver = new AuctionSniperDriver(1000);
        driver.showsSniperStatus(MainWindow.STATUS_JOINING);
    }

    public void hasShownSniperIsBidding() {
        driver.showsSniperStatus(MainWindow.STATUS_BIDDING);
    }

    public void showsSniperHasLostAuction() {
        driver.showsSniperStatus(MainWindow.STATUS_LOST);
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }

}
