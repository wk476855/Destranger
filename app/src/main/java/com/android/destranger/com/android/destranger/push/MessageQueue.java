package com.android.destranger.com.android.destranger.push;

import java.util.PriorityQueue;

/**
 * Created by wk on 2015/5/10.
 */
public class MessageQueue {

    private static PriorityQueue<Message> queue = new PriorityQueue<Message>();
    private MessageQueue(){
    }

    public synchronized static boolean push(Message msg) {
        return queue.add(msg);
    }

    public synchronized static Message peek() {
        return queue.peek();
    }

    public synchronized static Message poll() {
        return queue.poll();
    }

    public synchronized static boolean isEmpty() {
        return queue.isEmpty();
    }
}
