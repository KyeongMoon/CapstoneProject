package org.capstone.android.checkin.activity;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import org.capstone.android.checkin.R;
import org.capstone.android.checkin.handler.FingerTestHandler;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class FingerTestActivity extends AppCompatActivity {

    final String TAG = "FingerTestActivity";

    private static final String KEY_NAME = "example_key";
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private Cipher cipher;
    private FingerprintManager.CryptoObject cryptoObject;

    private TextView fingerTestActivityTextView;
    private Switch fingerSwitch;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_test);

        fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
        keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);

        fingerTestActivityTextView = findViewById(R.id.fingerTestTextView);
        fingerSwitch = findViewById(R.id.fingerTestSwitch);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        //activity switch값 조정
        boolean useFingerPrint = preferences.getBoolean("useFingerPrint", false);
        if(useFingerPrint) {
            fingerSwitch.setChecked(true);
            fingerTestActivityTextView.setText("지문으로 로그인을 사용중입니다.");
        }
        else {
            fingerSwitch.setChecked(false);
            fingerTestActivityTextView.setText("지문으로 로그인을 사용하시려면\n 우측 상단 버튼을 눌러주세요.");
        }

        fingerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //지문인식 시작, 지문인식 가능한지
                    if (!fingerprintManager.isHardwareDetected()) {
                        fingerTestActivityTextView.setText("지문을 사용할 수 없는 디바이스 입니다.");
                    } else if (ContextCompat.checkSelfPermission(FingerTestActivity.this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                        fingerTestActivityTextView.setText("지문사용을 허용해 주세요");
                    } else if (!keyguardManager.isKeyguardSecure()) {
                        fingerTestActivityTextView.setText("잠금화면을 설정해주세요");
                    } else if (!fingerprintManager.hasEnrolledFingerprints()) {
                        fingerTestActivityTextView.setText("등록된 지문이 없습니다.");
                    } else {
                        fingerTestActivityTextView.setText("지문 인식 센서에\n손가락을 올려주세요");


                        generateKey();

                        if (cipherInit()) {
                            cryptoObject = new FingerprintManager.CryptoObject(cipher);

                            FingerTestHandler fingerTestHandler = new FingerTestHandler(FingerTestActivity.this);
                            fingerTestHandler.startAuto(fingerprintManager, cryptoObject);
                        }
                    }

                } else {
                    //지문인식 삭제
                    fingerTestActivityTextView.setText("지문으로 로그인을 사용하시려면\n 우측 상단 버튼을 눌러주세요.");
                    editor.putBoolean("useFingerPrint", false);
                    editor.commit();

                }
            }
        });
    }


    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    protected void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Faild to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_CBC).setUserAuthenticationRequired(true).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build());
            keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void FinishActivity(View view) {
        finish();
    }
}
