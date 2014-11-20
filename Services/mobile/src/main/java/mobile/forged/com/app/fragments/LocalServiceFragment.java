package mobile.forged.com.app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.Random;

import mobile.forged.com.app.R;
import mobile.forged.com.app.services.Task;
import mobile.forged.com.app.services.clients.LocalServiceClient;
import mobile.forged.com.app.services.clients.SimpleClientCallback;

/**
 * Created by nchampagne on 11/19/14.
 */
public class LocalServiceFragment extends Fragment implements SimpleClientCallback {

    private Button mButton;

    private View mRootView;

    private RelativeLayout mRelativeLayout;

    private LocalServiceClient localServiceClient;

    private boolean isBound;

    public LocalServiceFragment() {
        localServiceClient = new LocalServiceClient(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.generic_fragment_layout, container, false);

        mButton = (Button) mRootView.findViewById(R.id.button);
        mRelativeLayout = (RelativeLayout) mRootView.findViewById(R.id.container);
        mRootView.findViewById(R.id.textView).setVisibility(View.GONE);

        initButtonListener();

        return mRootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onServiceConnected() {
        isBound = true;
    }

    private void initButtonListener() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBound) {
                    localServiceClient.doTask(new Task() {

                        @Override
                        public void executeTask() {
                            Random random = new Random();

                            int backgroundId = 0;
                            int num = 0;

                            while (true) {
                                num = random.nextInt(5);
                                switch (num) {
                                    case 0: {
                                        backgroundId = android.R.color.black;
                                        break;
                                    }
                                    case 1: {
                                        backgroundId = android.R.color.holo_purple;
                                        break;
                                    }
                                    case 2: {
                                        backgroundId = android.R.color.holo_red_dark;
                                        break;
                                    }
                                    case 3: {
                                        backgroundId = android.R.color.holo_blue_bright;
                                        break;
                                    }
                                    case 4: {
                                        backgroundId = android.R.color.holo_green_light;
                                        break;
                                    }
                                }

                                final int finalBackgroundId = backgroundId;
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mRelativeLayout.setBackgroundResource(finalBackgroundId);
                                    }
                                });

                                try {
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void handleServiceMessage(Message msg) {

    }
}
