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
import android.widget.TextView;
import android.widget.Toast;

import mobile.forged.com.app.R;
import mobile.forged.com.app.services.RemoteService;
import mobile.forged.com.app.services.clients.RemoteServiceClient;
import mobile.forged.com.app.services.clients.SimpleClientCallback;

/**
 * Created by nchampagne on 11/19/14.
 */
public class RemoteServiceFragment extends Fragment implements SimpleClientCallback {

    Button mButton;

    View mRootView;

    RelativeLayout mRelativeLayout;

    private TextView mTextView;

    private boolean isBound;

    private RemoteServiceClient remoteServiceClient;

    public RemoteServiceFragment() {
        remoteServiceClient = new RemoteServiceClient(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.generic_fragment_layout, container, false);

        mButton = (Button) mRootView.findViewById(R.id.button);
        mRelativeLayout = (RelativeLayout) mRootView.findViewById(R.id.container);
        mTextView = (TextView) mRootView.findViewById(R.id.textView);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBound) {
                    remoteServiceClient.printFibonacciSequence();
                }
                else {
                    Toast.makeText(getActivity(), "Service not connected.", Toast.LENGTH_LONG).show();
                }
            }
        });

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

    @Override
    public void handleServiceMessage(final Message msg) {
        switch(msg.what) {
            case RemoteService.FIBONACCI_NUMBER: {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTextView.setText(msg.getData().getString("fibonacci_number"));
                    }
                });
                break;
            }
        }
    }
}
