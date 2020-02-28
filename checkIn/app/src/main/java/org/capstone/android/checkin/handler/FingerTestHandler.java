package org.capstone.android.checkin.handler;

import android.app.Activity;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import org.capstone.android.checkin.R;

@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerTestHandler extends FingerprintManager.AuthenticationCallback {
    CancellationSignal cancellationSignal;
    private Context context;

    public FingerTestHandler(Context context){
        this.context = context;
    }

    public void startAuto(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){
        cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        this.update("인증에러 발생" + errString, false);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        this.update("Error" + helpString, false);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("앱 접근 허용", true);
    }

    @Override
    public void onAuthenticationFailed() {
        this.update("인증 실패", false);
    }

    public void stopFingerAuth(){
        if(cancellationSignal != null && !cancellationSignal.isCanceled()){
            cancellationSignal.cancel();
        }
    }

    private void update(String s, boolean b){
        final TextView textView = ((Activity)context).findViewById(R.id.fingerTestTextView);

        textView.setText(s);
    }
}
