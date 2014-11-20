package mobile.forged.com.app.services.clients;

import android.os.Message;

import java.io.Serializable;

/**
 * Created by visitor15 on 11/18/14.
 */
public interface  SimpleClientCallback extends Serializable {
    public void onServiceConnected();

    public void handleServiceMessage(Message msg);
}
