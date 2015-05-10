package com.android.destranger.com.android.destranger.push;

/**
 * Created by wk on 2015/5/10.
 */
public class SendThread implements Runnable {

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            if(!MessageQueue.isEmpty()) {
                Message msg = MessageQueue.poll();

            }
        }
    }
}
