package mobile.forged.com.app.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import java.util.Random;

/**
 * Created by nchampagne on 11/19/14.
 */
public class LocalService extends Service {
    public static final int DO_WORK = 100;

    private Messenger mMmessenger;

    private Looper mLooper;

    private ServiceHandler mHandler;

    @Override
    public IBinder onBind(Intent intent) {
        return mMmessenger.getBinder();
    }

    @Override
    public void onCreate() {
        /* This is the service's main thread. */
        HandlerThread thread = new HandlerThread("ThreadedService", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        /* Get the HandlerThread's Looper and use it for our handler. */
        mLooper = thread.getLooper();
        mHandler = new ServiceHandler(mLooper);
        mMmessenger = new Messenger(mHandler);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    private void handleServiceMessage(Message msg) {
        switch(msg.what) {
            case DO_WORK: {
                spawnWorkerThread().sendMessage(Message.obtain(msg));
                break;
            }
            default: {

            }
        }
    }

    private void simulateBackgroundWork(Message msg) {
        ((Task) msg.getData().getSerializable("task")).executeTask();
    }

    private void handleWorkerMessage(Message msg) {
        switch(msg.what) {
            case DO_WORK: {
                Log.d("ThreadedService", "SIMULATING WORK!");
                simulateBackgroundWork(Message.obtain(msg));
                break;
            }
            default: {

            }
        }
    }

    private Handler spawnWorkerThread() {
        Random ran = new Random();
        HandlerThread thread = new HandlerThread("LocalServiceWorker" + ran.nextInt(100) + 1, android.os.Process.THREAD_PRIORITY_BACKGROUND);
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
                handleServiceMessage(msg);
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