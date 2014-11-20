package mobile.forged.com.app.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

import java.util.Random;

/**
 * Created by visitor15 on 11/17/14.
 */
public class ThreadedService extends Service {

    public static final int DO_WORK = 100;

    public static final int SYNC_BACKGROUND = 200;

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
        return START_NOT_STICKY;
    }

    private void handleServiceMessage(Message msg) {
        switch(msg.what) {
            case DO_WORK: {
                spawnWorkerThread().sendMessage(Message.obtain(msg));
                break;
            }
            case SYNC_BACKGROUND: {
                spawnWorkerThread().sendMessage(Message.obtain(msg));
            }
            default: {

            }
        }
    }

    private void syncBackgroundTask(Message msg) {
        Bundle b = new Bundle();

        Random random = new Random();
        int num = 0;
        while(true) {
            b = new Bundle();
            num = random.nextInt(5);
            switch(num) {
                case 0: {
                    b.putInt("background_color", android.R.color.black);
                    break;
                }
                case 1: {
                    b.putInt("background_color", android.R.color.holo_purple);
                    break;
                }
                case 2: {
                    b.putInt("background_color", android.R.color.holo_red_dark);
                    break;
                }
                case 3: {
                    b.putInt("background_color", android.R.color.holo_blue_bright);
                    break;
                }
                case 4: {
                    b.putInt("background_color", android.R.color.holo_green_light);
                    break;
                }
            }

            Message replyMsg = Message.obtain();
            replyMsg.setData(b);
            try {
                msg.what = 200;
//                Log.d("ThreadedService", "REPLYING TO REQUESTER!");
//                msg.getTarget().sendMessage(replyMsg);
                msg.replyTo.send(replyMsg);
                Thread.sleep(3000);
            }catch (InterruptedException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
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
            case SYNC_BACKGROUND: {
                Log.d("ThreadedService", "SYNCING BACKGROUNDS!");
                syncBackgroundTask(Message.obtain(msg));
                break;
            }
            default: {

            }
        }
    }

    private void postToMainThread(Message msg) {
        _handler.post(msg.getCallback());
    }

    private Handler spawnWorkerThread() {
        Random ran = new Random();
        HandlerThread thread = new HandlerThread("ThreadedServiceWorker" + ran.nextInt(100) + 1, android.os.Process.THREAD_PRIORITY_BACKGROUND);
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
