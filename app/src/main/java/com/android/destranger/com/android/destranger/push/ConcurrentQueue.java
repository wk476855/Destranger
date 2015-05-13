package com.android.destranger.com.android.destranger.push;

import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by wk on 2015/5/10.
 */
public class ConcurrentQueue {

    public final static LinkedBlockingQueue<ProtocolPair> Wait_Queue = new LinkedBlockingQueue<ProtocolPair>();
    public final static LinkedBlockingQueue<ProtocolPair> Receive_Queue = new LinkedBlockingQueue<ProtocolPair>();

}
