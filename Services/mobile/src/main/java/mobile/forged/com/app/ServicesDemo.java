package mobile.forged.com.app;

import android.app.Application;

/**
 * Created by visitor15 on 11/17/14.
 */
public class ServicesDemo extends Application {

    private static ServicesDemo _singleton;

    @Override
    public void onCreate() {
        ServicesDemo._singleton = this;
        super.onCreate();
    }

    public static ServicesDemo getReference() {
        return ServicesDemo._singleton;
    }
}
