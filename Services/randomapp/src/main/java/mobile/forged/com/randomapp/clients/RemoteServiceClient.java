package mobile.forged.com.randomapp.clients;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.TextView;

import mobile.forged.com.randomapp.R;
import mobile.forged.com.randomapp.RandomApp;

/**
 * Created by visitor15 on 11/17/14.
 */
public class RemoteServiceClient {

    private Messenger mMessenger;

    private Messenger replyMessenger;

    private ServiceConnection serviceConnection;

    private RemoteServiceHandler remoteServiceHandler;

    private Activity mActivity;

    public RemoteServiceClient(Activity activity) {
        this.mActivity = activity;
        initializeService();
    }

    protected void initializeService() {
        remoteServiceHandler = new RemoteServiceHandler(Looper.getMainLooper());
        replyMessenger = new Messenger(remoteServiceHandler);
        Intent _intent = new Intent("com.forged.action.remote_service");
        RandomApp.getReference().bindService(
                _intent,
                createServiceConnection(),
                Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection createServiceConnection() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mMessenger = new Messenger(service);
                printFibonacciSequence();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        return serviceConnection;
    }

    private void printFibonacciSequence() {
        Bundle b = new Bundle();
        Message msg = Message.obtain(remoteServiceHandler);
        msg.setData(b);
        msg.what = 200;
        msg.replyTo = replyMessenger;
        try {
            mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void handleRemoteResponse(Message msg) {
        ((TextView) mActivity.findViewById(R.id.textView)).setText(msg.getData().getString("fibonacci_number"));
    }

    /*
     * CLASS
     */
    private class RemoteServiceHandler extends Handler {

        public RemoteServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            synchronized (this) {
                handleRemoteResponse(Message.obtain(msg));
            }
        }
    }
}
