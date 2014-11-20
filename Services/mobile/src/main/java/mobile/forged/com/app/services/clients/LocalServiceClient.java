package mobile.forged.com.app.services.clients;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import mobile.forged.com.app.ServicesDemo;
import mobile.forged.com.app.services.LocalService;
import mobile.forged.com.app.services.Task;

/**
 * Created by nchampagne on 11/19/14.
 */
public class LocalServiceClient {
    private transient Messenger _messenger;

    private boolean _isBound;

    private transient ServiceConnection _serviceConnection;

    private transient SimpleClientCallback _callback;

    public LocalServiceClient(SimpleClientCallback callback) {
        _callback = callback;
        initializeService(LocalService.class);
    }

    protected void initializeService(Class c) {
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

    public void doTask(Task t) {
        Bundle b = new Bundle();
        b.putSerializable("task", t);
        b.putString("id", "task");
        Message msg = new Message();
        msg.setData(b);
        msg.what = LocalService.DO_WORK;

        try {
            _messenger.send(msg);
        } catch (Exception e) {
        }
    }
}
