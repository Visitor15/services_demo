package mobile.forged.com.randomapp;

import android.app.Activity;
import android.os.Bundle;

import mobile.forged.com.randomapp.clients.RemoteServiceClient;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new RemoteServiceClient(this);
    }
}
