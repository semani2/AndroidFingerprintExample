package development.sai.fingerprintpoc;

import android.app.Activity;
import android.app.DialogFragment;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by sai on 1/11/16.
 */
public class FingerprintScanDialogFragment extends DialogFragment implements FingerprintHelper.Callback{
    private Button mCancelButton;
    private View mFingerprintContent;
    private FingerprintManager.CryptoObject mCryptoObject;
    private FingerprintHelper mFingerprintUiHelper;
    private MainActivity mActivity;

    FingerprintHelper.FingerprintHelperBuilder mFingerprintHelperBuilder;

    public FingerprintScanDialogFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Do not create a new Fragment when the Activity is re-created such as orientation changes.
        setRetainInstance(true);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(getString(R.string.sign_in));
        View v = inflater.inflate(R.layout.fingerprint_dialog_container, container, false);
        mCancelButton = (Button) v.findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mFingerprintContent = v.findViewById(R.id.fingerprint_container);

        mFingerprintUiHelper = mFingerprintHelperBuilder.build(new FingerPrintServices(getActivity()).getFingerprintManager(),
                (ImageView) v.findViewById(R.id.fingerprint_icon),
                (TextView) v.findViewById(R.id.fingerprint_status), this);
        updateStage();

        return v;
    }

    private void updateStage(){
        mCancelButton.setText("Cancel");
        mFingerprintContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        mFingerprintUiHelper.startListening(mCryptoObject);
    }

    @Override
    public void onPause() {
        super.onPause();
        mFingerprintUiHelper.stopListening();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MainActivity) activity;
    }

    public void setCryptoObject(FingerprintManager.CryptoObject cryptoObject) {
        mCryptoObject = cryptoObject;
    }

    @Override
    public void onAuthenticated() {
        dismiss();
        mActivity.onScanned();
    }

    @Override
    public void onError() {
        dismiss();
    }
}
