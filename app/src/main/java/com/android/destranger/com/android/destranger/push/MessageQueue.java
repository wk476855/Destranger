package com.android.destranger.com.android.destranger.push;

import java.util.PriorityQueue;

/**
 * Created by wk on 2015/5/10.
 */
public class MessageQueue {

    private PriorityQueue<Message> queue;
    public final static MessageQueue Wait_Queue = new MessageQueue();
    public final static MessageQueue Send_Queue = new MessageQueue();

    private  MessageQueue(){
        queue = new PriorityQueue<Message>();
    }

    public synchronized boolean push(Message msg) {
        return queue.add(msg);
    }

    public synchronized Message peek() {
        return queue.peek();
    }

    public synchronized Message poll() {
        return queue.poll();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}
