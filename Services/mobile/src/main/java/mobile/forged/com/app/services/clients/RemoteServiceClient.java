package mobile.forged.com.app.services.clients;

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

import java.util.ArrayList;
import java.util.List;

import mobile.forged.com.app.ServicesDemo;
import mobile.forged.com.app.services.RemoteService;
import mobile.forged.com.app.services.Task;
import mobile.forged.com.app.services.ThreadedService;

/**
 * Created by nchampagne on 11/19/14.
 */
public class RemoteServiceClient {
    private transient Messenger _messenger;

    private boolean _isBound;

    private transient List<Message> _msgQueueList;

    private transient ServiceConnection _serviceConnection;

    private transient SimpleClientCallback _callback;

    private Messenger responseMessenger;

    public RemoteServiceClient(SimpleClientCallback callback) {
        _callback = callback;
        _msgQueueList = new ArrayList<Message>();
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
        _serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                _isBound = true;
                _messenger = new Messenger(service);
                _callback.onServiceConnected();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                _isBound = false;
            }
        };

        return _serviceConnection;
    }

    public void simulateWork(List<Task> tasks) {
        for(Task t : tasks) {
            Bundle b = new Bundle();
            b.putSerializable("task", t);
            b.putString("id", "task" + tasks.indexOf(t));
            Message msg = new Message();
            msg.setData(b);
            msg.what = ThreadedService.DO_WORK;

            try {
                _messenger.send(msg);
            } catch (Exception e) {
                _msgQueueList.add(msg);
            }
        }
    }

    public void doTask(Task t) {
        Bundle b = new Bundle();
        b.putSerializable("task", t);
        b.putString("id", "task");
        Message msg = new Message();
        msg.setData(b);
        msg.what = ThreadedService.DO_WORK;

        try {
            _messenger.send(msg);
        } catch (Exception e) {
            _msgQueueList.add(msg);
        }
    }

    public void calculatePi() {
        Message msg = Message.obtain();
        msg.what = RemoteService.CALCULATE_PI;
        msg.replyTo = responseMessenger;

        try {
            _messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void printFibonacciSequence() {
        Message msg = Message.obtain();
        msg.what = RemoteService.FIBONACCI_NUMBER;
        msg.replyTo = responseMessenger;

        try {
            _messenger.send(msg);
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
                _callback.handleServiceMessage(Message.obtain(msg));
            }
        }
    }
}
