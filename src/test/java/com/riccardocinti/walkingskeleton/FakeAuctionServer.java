package com.riccardocinti.walkingskeleton;

import com.riccardocinti.main.Main;
import com.riccardocinti.main.MainWindow;
import org.hamcrest.Matcher;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class FakeAuctionServer {

    public static final String ITEM_ID_AS_LOGIN = "auction-%s";
    public static final String AUCTION_RESOURCE = "Auction";
    public static final String XMPP_HOSTNAME = "localhost";
    public static final String AUCTION_PASSWORD = "auction";

    public final SingleMessageListener messageListener = new SingleMessageListener();

    private final String itemId;
    private XMPPConnection connection;
    private Chat currentChat;

    public FakeAuctionServer(String itemId) {
        this.itemId = itemId;
        this.connection = new XMPPConnection(XMPP_HOSTNAME);
    }

    public void startSellingItem() throws XMPPException {
        connection.connect();
        connection.login(String.format(ITEM_ID_AS_LOGIN, itemId),
                AUCTION_PASSWORD, AUCTION_RESOURCE);
        connection.getChatManager().addChatListener(
                new ChatManagerListener() {
                    @Override
                    public void chatCreated(Chat chat, boolean createdLocally) {
                        chat.addMessageListener(messageListener);
                        currentChat = chat;
                    }
                }
        );
    }

    public void reportPrice(int price, int increment, String bidder) throws XMPPException {
        currentChat.sendMessage(
                String.format("SOLVersion: 1.1; Event: PRICE; CurrentPrice: %d; Increment: %d; Bidder: %s;",
                        price, increment, bidder));
    }

    public String getItemId() {
        return this.itemId;
    }

    public void hasReceivedJoinRequestFrom(String sniperId) throws InterruptedException {
        receivesAMessageMatching(
                sniperId, equalTo(MainWindow.STATUS_JOINING));
    }

    public void hasReceivedBid(int bid, String sniperId) throws InterruptedException {
        receivesAMessageMatching(
                sniperId, equalTo(String.format(Main.BID_COMMAND_FORMAT, bid)));
    }

    private void receivesAMessageMatching(String sniperId,
                                          Matcher<? super String> messageMatcher) throws InterruptedException {
        messageListener.receivesMessage(messageMatcher);
        assertThat(currentChat.getParticipant(), equalTo(sniperId));
    }

    public void announceClosed() throws XMPPException {
        currentChat.sendMessage("SOLVersion: 1.1; Event: CLOSE;");
    }

    public void stop() {
        currentChat = null;
        connection.disconnect();
        this.connection = new XMPPConnection(XMPP_HOSTNAME);
    }


}
