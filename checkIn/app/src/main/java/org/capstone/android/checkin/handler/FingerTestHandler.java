package org.capstone.android.checkin.handler;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.preference.PreferenceManager;
import android.widget.TextView;

import org.capstone.android.checkin.R;

public class FingerTestHandler extends FingerprintManager.AuthenticationCallback
{
    CancellationSignal cancellationSignal;
    private Context context;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public FingerTestHandler(Context context){
        this.context = context;
    }

    public void startAuto(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject){
        cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(cryptoObject, cancellationSignal, 0, this, null);

    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        this.update("지문 인식 센서에\n손가락을 다시올려주세요.", false);
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        this.update("Error" + helpString, false);
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        this.update("지문이 등록되었습니다.", true);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
        editor.putBoolean("useFingerPrint", true);
        editor.commit();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        ((Activity)context).finish();
                    }
                }, 500);
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

    private void update(final String s, boolean b){
        final TextView textView = ((Activity)context).findViewById(R.id.fingerTestTextView);
        textView.setText(s);
    }
}
