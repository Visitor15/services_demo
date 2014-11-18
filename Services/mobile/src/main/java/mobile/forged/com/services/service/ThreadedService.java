package mobile.forged.com.services.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.util.Log;

import java.util.Random;

/**
 * Created by visitor15 on 11/17/14.
 */
public class ThreadedService extends Service {

    public static final int DO_WORK = 100;

    private Messenger _messenger;

    private Looper _looper;

    private ServiceHandler _handler;

    @Override
    public IBinder onBind(Intent intent) {
        return _messenger.getBinder();
    }

    @Override
    public void onCreate() {
        /* This is the service's main thread. */
        HandlerThread thread = new HandlerThread("ThreadedService", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        /* Get the HandlerThread's Looper and use it for our handler. */
        _looper = thread.getLooper();
        _handler = new ServiceHandler(_looper);
        _messenger = new Messenger(_handler);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void handleServiceMessage(Message msg) {
        switch(msg.what) {
            case DO_WORK: {
                simulateBackgroundWork(Message.obtain(msg));
                break;
            }
            default: {

            }
        }
    }

    private void simulateBackgroundWork(Message msg) {
        Random random = new Random();

        while(true) {
            Log.d("ThreadedService", "Running task from message: " + msg.getData().getString("id"));
            try {
                Thread.sleep(random.nextInt(1999) + 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleWorkerMessage(Message msg) {

    }

    private void postToMainThread(Message msg) {
        _handler.post(msg.getCallback());
    }

    private Handler spawnWorkerThread() {
        HandlerThread thread = new HandlerThread("ThreadedServiceWorker", android.os.Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        return new ServiceWorkerHandler(thread.getLooper());
    }

    /*
    * CLASS
    */
    public class ServiceBinder extends Binder {
        /*
         * Returns an instance of the current service.
         */
        public ThreadedService getService() {
            return ThreadedService.this;
        }
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
