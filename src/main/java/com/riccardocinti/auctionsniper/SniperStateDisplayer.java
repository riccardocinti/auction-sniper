package com.riccardocinti.auctionsniper;

import com.riccardocinti.main.MainWindow;

import javax.swing.*;

import static com.riccardocinti.main.MainWindow.STATUS_BIDDING;
import static com.riccardocinti.main.MainWindow.STATUS_LOST;

public class SniperStateDisplayer implements SniperListener {

    private final MainWindow ui;

    public SniperStateDisplayer(MainWindow ui) {
        this.ui = ui;
    }

    @Override
    public void sniperLost() {
        showStatus(STATUS_LOST);
    }

    @Override
    public void sniperBidding() {
        showStatus(STATUS_BIDDING);
    }

    private void showStatus(final String status) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ui.showStatus(status);
            }
        });
    }

}
