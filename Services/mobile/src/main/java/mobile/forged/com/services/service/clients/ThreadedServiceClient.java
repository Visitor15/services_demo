package mobile.forged.com.services.service.clients;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import mobile.forged.com.services.ServicesDemo;
import mobile.forged.com.services.service.Task;
import mobile.forged.com.services.service.ThreadedService;

/**
 * Created by visitor15 on 11/17/14.
 */
public class ThreadedServiceClient implements Serializable{

    private transient Messenger _messenger;

    private boolean _isBound;

    private transient List<Message> _msgQueueList;

    private transient ServiceConnection _serviceConnection;

    private transient SimpleClientCallback _callback;

    public ThreadedServiceClient(SimpleClientCallback callback) {
        _callback = callback;
        _msgQueueList = new ArrayList<Message>();
        initializeService(ThreadedService.class);
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
}
