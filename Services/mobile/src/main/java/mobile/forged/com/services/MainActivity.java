package mobile.forged.com.services;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mobile.forged.com.services.service.Task;
import mobile.forged.com.services.service.clients.SimpleClientCallback;
import mobile.forged.com.services.service.clients.ThreadedServiceClient;


public class MainActivity extends Activity implements SimpleClientCallback {

    private ThreadedServiceClient threadedServiceClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        threadedServiceClient = new ThreadedServiceClient(this);
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

    @Override
    public void onResume() {
        super.onResume();

//        try {
//            simulateWork();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }


    }

    public void doTask(Task t) {
        threadedServiceClient.doTask(t);
    }

    public void simulateWork() throws RemoteException {


        List<Task> tasks = new ArrayList<Task>();

        tasks.add(new Task() {
            @Override
            public void executeTask() {
                Log.d("MainActivity", "Hello, I am a task originating from MainActivity.");
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        findViewById(R.id.main).setBackgroundResource(android.R.color.black);
                    }
                });
            }
        });

        tasks.add(new Task() {
            @Override
            public void executeTask() {
                Log.d("MainActivity", "All you base are belong to us.");
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        findViewById(R.id.main).setBackgroundResource(android.R.color.holo_red_dark);
                    }
                });
            }
        });

        tasks.add(new Task() {
            @Override
            public void executeTask() {
                Log.d("MainActivity", "Here I am!");
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        findViewById(R.id.main).setBackgroundResource(android.R.color.holo_blue_bright);
                    }
                });
            }
        });

        tasks.add(new Task() {
            @Override
            public void executeTask() {
                Log.d("MainActivity", "Where is the normal?");
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        findViewById(R.id.main).setBackgroundResource(android.R.color.holo_purple);
                    }
                });
            }
        });

        tasks.add(new Task() {
            @Override
            public void executeTask() {
                Log.d("MainActivity", "Hello, I am a task originating from MainActivity.");
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        findViewById(R.id.main).setBackgroundResource(android.R.color.holo_green_light);
                    }
                });
            }
        });
        threadedServiceClient.simulateWork(tasks);
    }


    @Override
    public void onServiceConnected() {
        doTask(new Task() {
            @Override
            public void executeTask() {
                Random random = new Random();
                int num = 0;
                while(true) {
                    num = random.nextInt(5);
                    switch (num) {
                        case 0: {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    findViewById(R.id.main).setBackgroundResource(android.R.color.black);
                                }
                            });
                            break;
                        }
                        case 1: {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    findViewById(R.id.main).setBackgroundResource(android.R.color.holo_blue_bright);
                                }
                            });
                            break;
                        }
                        case 2: {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    findViewById(R.id.main).setBackgroundResource(android.R.color.holo_orange_dark);
                                }
                            });
                            break;
                        }
                        case 3: {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    findViewById(R.id.main).setBackgroundResource(android.R.color.holo_purple);
                                }
                            });
                            break;
                        }
                        case 4: {
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    findViewById(R.id.main).setBackgroundResource(android.R.color.holo_green_light);
                                }
                            });
                            break;
                        }
                    }

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
