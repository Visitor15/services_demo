package mobile.forged.com.app;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import mobile.forged.com.app.fragments.LocalServiceFragment;
import mobile.forged.com.app.fragments.RemoteServiceFragment;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragment();
    }

    private void initFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.top_container, new LocalServiceFragment(), "local_service_frag");
        ft.replace(R.id.bottom_container, new RemoteServiceFragment(), "remote_service_frag");
        ft.commit();
    }
}
