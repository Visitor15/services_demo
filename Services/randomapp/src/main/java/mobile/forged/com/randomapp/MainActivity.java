package mobile.forged.com.randomapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import mobile.forged.com.randomapp.clients.ThreadedServiceClient;


public class MainActivity extends Activity {

    private Messenger _messenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ThreadedServiceClient threadedServiceClient = new ThreadedServiceClient(this);

//        Intent i = new Intent("com.forged.action.threadedservice");
//        bindService(i, new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//                Message msg = Message.obtain();
//                msg.what = 100;
//                _messenger = new Messenger(service);
//                try {
//                    _messenger.send(msg);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//                Log.d("TAG", "Did not work.");
//            }
//        }, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onResume() {
        super.onResume();

//        try {
//            simulateWork();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
    }

//    public void simulateWork() throws RemoteException {
//        ThreadedServiceClient threadedServiceClient = new ThreadedServiceClient(this);
//
//        List<Task> tasks = new ArrayList<Task>();
//
//        tasks.add(new Task() {
//            @Override
//            public void executeTask() {
//                Log.d("MainActivity", "Hello, I am a task originating from MainActivity.");
//                MainActivity.this.runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        findViewById(R.id.main).setBackgroundResource(android.R.color.black);
//                    }
//                });
//            }
//        });
//
//        tasks.add(new Task() {
//            @Override
//            public void executeTask() {
//                Log.d("MainActivity", "All you base are belong to us.");
//                MainActivity.this.runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        findViewById(R.id.main).setBackgroundResource(android.R.color.holo_red_dark);
//                    }
//                });
//            }
//        });
//
//        tasks.add(new Task() {
//            @Override
//            public void executeTask() {
//                Log.d("MainActivity", "Here I am!");
//                MainActivity.this.runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        findViewById(R.id.main).setBackgroundResource(android.R.color.holo_blue_bright);
//                    }
//                });
//            }
//        });
//
//        tasks.add(new Task() {
//            @Override
//            public void executeTask() {
//                Log.d("MainActivity", "Where is the normal?");
//                MainActivity.this.runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        findViewById(R.id.main).setBackgroundResource(android.R.color.holo_purple);
//                    }
//                });
//            }
//        });
//
//        tasks.add(new Task() {
//            @Override
//            public void executeTask() {
//                Log.d("MainActivity", "Hello, I am a task originating from MainActivity.");
//                MainActivity.this.runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        findViewById(R.id.main).setBackgroundResource(android.R.color.holo_green_light);
//                    }
//                });
//            }
//        });
////        threadedServiceClient.simulateWork(tasks);
//    }


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

    private void handleServiceMessage(Message msg) {
        switch(msg.what) {
            case 200: {

                break;
            }
            default: {
                Log.d("RandomApp", "Got response from remote service.");
            }
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
}
