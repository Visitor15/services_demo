package mobile.forged.com.services.service.clients;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import java.util.ArrayList;
import java.util.List;

import mobile.forged.com.services.ServicesDemo;
import mobile.forged.com.services.service.ThreadedService;

/**
 * Created by visitor15 on 11/17/14.
 */
public class ThreadedServiceClient implements ServiceConnection {

    private Messenger _messenger;

    private boolean _isBound;

    private List<Message> _msgQueueList;

    public ThreadedServiceClient() {
        _msgQueueList = new ArrayList<Message>();
        initializeService(ThreadedService.class, this);
    }

    protected void initializeService(Class c, ServiceConnection serviceConnection) {
        ServicesDemo.getReference().bindService(new Intent(ServicesDemo.getReference(),
                        c),
                serviceConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        _isBound = true;
        _messenger = new Messenger(service);

        for(Message msg : _msgQueueList) {
            try {
                _messenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        _isBound = false;
    }

    public void simulateWork(Handler handler, List<Runnable> runnables) {
        for(Runnable r : runnables) {
            Bundle b = new Bundle();
            b.putString("id", "task" + runnables.indexOf(r));
            Message msg = Message.obtain(handler, r);
            msg.setData(b);
            msg.what = ThreadedService.DO_WORK;

            try {
                _messenger.send(msg);
            } catch (RemoteException e) {
                _msgQueueList.add(msg);
            }
        }
    }
}
