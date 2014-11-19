package mobile.forged.com.services;

import android.app.Activity;
import android.os.Bundle;

import java.util.Random;

import mobile.forged.com.services.services.Task;
import mobile.forged.com.services.services.clients.SimpleClientCallback;
import mobile.forged.com.services.services.clients.ThreadedServiceClient;


public class MainActivity extends Activity implements SimpleClientCallback {

    private ThreadedServiceClient threadedServiceClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        threadedServiceClient = new ThreadedServiceClient(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void doTask(Task t) {
        threadedServiceClient.doTask(t);
    }

    @Override
    public void onServiceConnected() {
        /*
         * Creating a new Task to execute on a background thread within a service.
         *
         * NOTE: We cannot manipulate the UI from a thread other than the main thread. We can use
         * the MainActivity to post the UI change on the UI thread.
         */
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
