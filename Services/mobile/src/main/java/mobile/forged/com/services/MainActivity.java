package mobile.forged.com.services;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import mobile.forged.com.services.service.clients.ThreadedServiceClient;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void simulateWork() throws RemoteException {
        ThreadedServiceClient threadedServiceClient = new ThreadedServiceClient();

        List<Runnable> runnables = new ArrayList<Runnable>();

        runnables.add(new Runnable() {
            @Override
            public void run() {
                Log.d("MainActivity", "Hello, I am a task originating from MainActivity.");
            }
        });

        runnables.add(new Runnable() {
            @Override
            public void run() {
                Log.d("MainActivity", "All you base are belong to us.");
            }
        });

        runnables.add(new Runnable() {
            @Override
            public void run() {
                Log.d("MainActivity", "Here I am!");
            }
        });

        runnables.add(new Runnable() {
            @Override
            public void run() {
                Log.d("MainActivity", "Where is the normal?");
            }
        });

        runnables.add(new Runnable() {
            @Override
            public void run() {
                Log.d("MainActivity", "Hello, I am a task originating from MainActivity.");
            }
        });


        threadedServiceClient.simulateWork(new ServiceHandler(Looper.getMainLooper()), runnables);
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
                /* Handle message here. */
            }
        }
    }
}
