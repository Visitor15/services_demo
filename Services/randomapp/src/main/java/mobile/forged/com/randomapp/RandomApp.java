package mobile.forged.com.randomapp;

import android.app.Application;

/**
 * Created by visitor15 on 11/17/14.
 */
public class RandomApp extends Application {

    private static RandomApp _singleton;

    @Override
    public void onCreate() {
        RandomApp._singleton = this;
        super.onCreate();
    }

    public static RandomApp getReference() {
        return RandomApp._singleton;
    }
}
