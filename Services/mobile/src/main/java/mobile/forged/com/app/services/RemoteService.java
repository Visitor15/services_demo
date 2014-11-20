package mobile.forged.com.app.services;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by nchampagne on 11/19/14.
 */
public class RemoteService extends Service{
    public static final int DO_WORK = 10;

    public static final int SYNC_BACKGROUND = 20;

    public static final int CALCULATE_PI = 100;

    public static final int FIBONACCI_NUMBER = 200;

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
        HandlerThread thread = new HandlerThread("ThreadedService", android.os.Process.THREAD_PRIORITY_BACKGROUND);
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
        spawnWorkerThread().sendMessage(Message.obtain(msg));
    }

    public int fibonacciRecusion(int number){
        if(number == 1 || number == 2){
            return 1;
        }

        return fibonacciRecusion(number-1) + fibonacciRecusion(number -2); //tail recursion
    }

    private void calculatePi(Message msg) {
        double pi = 4;
        boolean plus = false;
        Bundle b;
        DecimalFormat formatter = new DecimalFormat();
        int count = 0;
        for (int i = 3; i < 10000000; i += 2)
        {
            ++count;
            b = new Bundle();
            if (plus)
            {
                pi += 4.0 / i;
            }
            else
            {
                pi -= 4.0 / i;
            }
            plus = !plus;

            formatter.setMaximumIntegerDigits(count);
            b.putString("pi_key", formatter.format(pi));
            Message replyMsg = Message.obtain();
            replyMsg.what = CALCULATE_PI;
            replyMsg.setData(b);

            try {
                msg.replyTo.send(replyMsg);
                Thread.sleep(2000);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {


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
//                syncBackgroundTask(Message.obtain(msg));
                break;
            }
            case CALCULATE_PI: {
                Log.d("RemoteService", "Calculating pi...");
                calculatePi(Message.obtain(msg));
            }
            case FIBONACCI_NUMBER: {
                Log.d("RemoteService", "Calculating fibonacci sequence...");
                printFibonacciNumbers(Message.obtain(msg));
            }
            default: {

            }
        }
    }

    private void printFibonacciNumbers(Message msg) {
        Bundle b;
        for(int i=1; i<=50; i++){
            b = new Bundle();
            int fibNum = fibonacciRecusion(i);
            b.putInt("fibonacci_number", fibNum);
            Message replyMsg = Message.obtain();
            replyMsg.what = FIBONACCI_NUMBER;
            replyMsg.setData(b);

            try {
                msg.replyTo.send(replyMsg);
                Thread.sleep(2000);
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {


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
        public RemoteService getService() {
            return RemoteService.this;
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