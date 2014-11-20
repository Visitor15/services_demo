package mobile.forged.com.app.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.Random;

/**
 * Created by nchampagne on 11/19/14.
 */
public class RemoteService extends Service{

    public static final int FIBONACCI_NUMBER = 200;

    private Messenger mMessenger;

    private Looper mLooper;

    private ServiceHandler mHandler;

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        /* This is the service's main thread. */
        HandlerThread thread = new HandlerThread("RemoteServiceThread", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        /* Get the HandlerThread's Looper and use it for our handler. */
        mLooper = thread.getLooper();
        mHandler = new ServiceHandler(mLooper);
        mMessenger = new Messenger(mHandler);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    public int fibonacciRecusion(int number){
        if(number == 1 || number == 2){
            return 1;
        }
        return fibonacciRecusion(number-1) + fibonacciRecusion(number -2); //tail recursion
    }

    private void handleWorkerMessage(Message msg) {
        switch(msg.what) {
            case FIBONACCI_NUMBER: {
                Log.d("RemoteService", "Calculating fibonacci sequence...");
                printFibonacciNumbers(Message.obtain(msg));
            }
            default: { return; }
        }
    }

    private void printFibonacciNumbers(Message msg) {
        Bundle b;
        /* Print the first 20 fibonacci numbers. */
        for(int i = 1; i <= 20; i++){
            b = new Bundle();
            b.putString("fibonacci_number", Integer.toString(fibonacciRecusion(i)));
            Message replyMsg = Message.obtain();
            replyMsg.what = FIBONACCI_NUMBER;
            replyMsg.setData(b);

            try {
                msg.replyTo.send(replyMsg);
                Thread.sleep(2000);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /* Resets i = 1 if i == 20 */
            i = (i % 20 == 0) ? 1 : i;
        }
    }

    private Handler spawnWorkerThread() {
        Random ran = new Random();
        HandlerThread thread = new HandlerThread("RemoteServiceWorker" + ran.nextInt(100) + 1, android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        return new ServiceWorkerHandler(thread.getLooper());
    }

    /*
    * CLASS
    */
    protected class ServiceHandler extends Handler {

        protected ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            synchronized (this) {
                spawnWorkerThread().sendMessage(Message.obtain(msg));;
            }
        }
    }

    /*
    * CLASS
    */
    protected class ServiceWorkerHandler extends Handler {

        protected ServiceWorkerHandler(Looper looper) { super(looper); }

        @Override
        public void handleMessage(Message msg) {
            synchronized (this) {
                handleWorkerMessage(msg);
            }
        }
    }
}