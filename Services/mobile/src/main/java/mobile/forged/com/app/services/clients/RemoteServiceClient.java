package mobile.forged.com.app.services.clients;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import mobile.forged.com.app.ServicesDemo;
import mobile.forged.com.app.services.RemoteService;

/**
 * Created by nchampagne on 11/19/14.
 */
public class RemoteServiceClient {
    private Messenger mMessenger;

    private ServiceConnection serviceConnection;

    private SimpleClientCallback callback;

    private Messenger responseMessenger;

    public RemoteServiceClient(SimpleClientCallback callback) {
        this.callback = callback;
        initializeService(RemoteService.class);
    }

    protected void initializeService(Class c) {
        responseMessenger = new Messenger(new ServiceHandler(Looper.myLooper()));
        ServicesDemo.getReference().bindService(new Intent(ServicesDemo.getReference(),
                        c),
                createServiceConnection(),
                Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection createServiceConnection() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mMessenger = new Messenger(service);
                callback.onServiceConnected();
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };

        return serviceConnection;
    }

    public void printFibonacciSequence() {
        Message msg = Message.obtain();
        msg.what = RemoteService.FIBONACCI_NUMBER;
        msg.replyTo = responseMessenger;

        try {
            mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
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
                callback.handleServiceMessage(Message.obtain(msg));
            }
        }
    }
}
