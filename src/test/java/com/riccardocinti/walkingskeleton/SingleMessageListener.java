package com.riccardocinti.walkingskeleton;

import org.hamcrest.Matcher;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SingleMessageListener implements MessageListener {
    private final ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<>(1);

    public void processMessage(Chat chat, Message message) {
        messages.add(message);
    }

    public void receivesMessage(Matcher<? super String> matcher) throws InterruptedException {
        final Message message = messages.poll(5, TimeUnit.SECONDS);
        assertThat("Message", message, is(notNullValue()));
        assertThat(message.getBody(), matcher);
    }
}
