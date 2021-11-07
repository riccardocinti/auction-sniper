package com.riccardocinti.auctionsniper;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPException;

import static com.riccardocinti.main.Main.BID_COMMAND_FORMAT;
import static java.lang.String.format;

public class XMPPAuction implements Auction {

    private final Chat chat;

    public XMPPAuction(Chat chat) {
        this.chat = chat;
    }

    public void bid(int amount) {
        sendMessage(format(BID_COMMAND_FORMAT, amount));
    }

    public void join() {
        sendMessage("joining");
    }

    private void sendMessage(final String message) {
        try {
            chat.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }
}
