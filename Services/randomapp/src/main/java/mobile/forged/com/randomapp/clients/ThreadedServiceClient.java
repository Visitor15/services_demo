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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mobile.forged.com.randomapp.R;
import mobile.forged.com.randomapp.RandomApp;
import mobile.forged.com.randomapp.Task;

/**
 * Created by visitor15 on 11/17/14.
 */
public class ThreadedServiceClient {

    private Messenger _messenger;

    private Messenger _replyMessenger;

    private boolean _isBound;

    private List<Message> _msgQueueList;

    private ServiceConnection _serviceConnection;

    private RemoteServiceHandler _remoteServiceHandler;

    private Activity _activity;

    public ThreadedServiceClient(Activity activity) {
        this._activity = activity;
        _msgQueueList = new ArrayList<Message>();
        initializeService();
    }

    protected void initializeService() {
        _remoteServiceHandler = new RemoteServiceHandler(Looper.getMainLooper());
        _replyMessenger = new Messenger(_remoteServiceHandler);
        Intent _intent = new Intent("com.forged.action.remote_service");
        RandomApp.getReference().bindService(
                _intent,
                createServiceConnection(),
                Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection createServiceConnection() {
        _serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                _isBound = true;
                _messenger = new Messenger(service);


                simulateWork(null);

//                for(Message msg : _msgQueueList) {
//                    try {
//                        _messenger.send(msg);
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
//                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                _isBound = false;
            }
        };

        return _serviceConnection;
    }

    public void simulateWork(List<Task> tasks) {
//        for(Task t : tasks) {
            Bundle b = new Bundle();
            b.putString("id", "task");
//            b.putString("id", "task" + tasks.indexOf(t));
            Message msg = Message.obtain(_remoteServiceHandler);
            msg.setData(b);
            msg.what = 200;
            msg.replyTo = _replyMessenger;
            try {
                _messenger.send(msg);
            } catch (Exception e) {
                _msgQueueList.add(msg);
            }
//        }
    }

    private void handleRemoteResponse(Message msg) {
        ((TextView) _activity.findViewById(R.id.textView)).setText(msg.getData().getString("fibonacci_number"));
    }

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
