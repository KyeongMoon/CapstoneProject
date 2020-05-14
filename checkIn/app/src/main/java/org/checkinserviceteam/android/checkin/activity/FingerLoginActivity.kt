package org.checkinserviceteam.android.checkin.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_finger_login.*
import org.checkinserviceteam.android.checkin.MyApplication
import org.checkinserviceteam.android.checkin.R
import java.util.concurrent.Executor

class FingerLoginActivity : AppCompatActivity() {

    private lateinit var executor : Executor
    private lateinit var preferences : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finger_login)

        executor = ContextCompat.getMainExecutor(applicationContext)
        preferences = MyApplication.getPreference()
        editor = MyApplication.getEditor()

        /*
        * 1. switch on
        * 1-1 현재 실행중입니다.
        *   => switch off 시 tv(지문으로 로그인 설명) switch(off), pref(finger login status) 변경
        *
        * 2. switch off
        * 2-1 지문으로 로그인 설명
        *   => switch on 시 dialog생성
        *       => 성공 시 tv(현재 실행중입니다.), switch 변경, pref(finger login status) 변경
        *       => 실패 시 변경 x
        * */

        setFingerLogin(preferences.getBoolean("useFingerLoginPref", false))

        activity_finger_login_st_switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                showBiometricPrompt()
            }

            else{
                setFingerLogin(false)
                setFingerLoginPref(false)
            }
        }

//        biometric 기기에서 지원 여부
//        val biometricManager = BiometricManager.from(this)
//
//        when (biometricManager.canAuthenticate()) {
//            BiometricManager.BIOMETRIC_SUCCESS ->
//                Log.d("aa", "App can authenticate using biometrics.")
//            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
//                Log.e("aa", "No biometric features available on this device.")
//            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
//                Log.e("aa", "Biometric features are currently unavailable.")
//            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
//                Log.e("aa", "The user hasn't associated any biometric credentials " +
//                        "with their account.")
//        }

    }
    
    private fun showBiometricPrompt() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("지문으로 로그인 설정")
            .setNegativeButtonText("취소")
            .build()

        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int,
                                                   errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    if(errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON)
                        setFingerLogin(false)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    setFingerLogin(true)
                    setFingerLoginPref(true)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }
            })

        // Displays the "log in" prompt.
        biometricPrompt.authenticate(promptInfo)
    }

    private fun setFingerLoginPref(status : Boolean){
        Log.d("setFingerLoginPref", status.toString())
        if(status)
            editor.putBoolean("useFingerLoginPref", true)
        else
            editor.putBoolean("useFingerLoginPref", false)
        editor.commit()
    }

    private fun setFingerLogin(status : Boolean){
        Log.d("setFingerLogin", status.toString())
        if(status){
            activity_finger_login_tv_text.text = "지문으로 로그인을 사용중입니다."
            activity_finger_login_st_switch.isChecked = true
        }
        else{
            activity_finger_login_tv_text.text = "지문으로 로그인을 사용하시려면\n 우측 상단 버튼을 눌러주세요."
            activity_finger_login_st_switch.isChecked = false
        }
    }

    fun FinishActivity(view: View){
        finish()
    }
}
