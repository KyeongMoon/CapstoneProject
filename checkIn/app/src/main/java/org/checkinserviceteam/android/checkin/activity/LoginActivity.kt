package org.checkinserviceteam.android.checkin.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.ads.identifier.AdvertisingIdClient
import androidx.ads.identifier.AdvertisingIdInfo
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures.addCallback
import kotlinx.android.synthetic.main.activity_login.*
import org.checkinserviceteam.android.checkin.MyApplication
import org.checkinserviceteam.android.checkin.R
import org.checkinserviceteam.android.checkin.adater.LoadingDialog
import org.checkinserviceteam.android.checkin.retrofit.service.DTO.M_LoginDTO
import org.checkinserviceteam.android.checkin.retrofit.service.LoginService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/*      * 로그인 실행 흐름
        *
        * 1. 지문으로 로그인 off
        *   1-1 아이디와 비밀번호 입력 후 서버 request
        *       1-1-1 if success pref(loginIdPref, loginPwPref)
        *           1-1-1-1 기존 계정과 다르다면 useFingerLoginPref = false
        *       1-1-2 else if fail  pref(loginIdPref, loginPwPref) reset
        *
        * 2. 지문으로 로그인 on
        *   2-1 if success
        *       서버 request
        *   2-2 if negative button
        *
        * */

class LoginActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        executor = ContextCompat.getMainExecutor(applicationContext)
        preferences = MyApplication.getPreference()
        editor = MyApplication.getEditor()

        var task = object : AsyncTask<Void, Void, String>(){
            override fun onPostExecute(result: String?) {
                Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
                super.onPostExecute(result)
            }

            override fun doInBackground(vararg params: Void?): String {
                var idInfo = AdvertisingIdClient.getAdvertisingIdInfo(applicationContext).get().id
                Log.d("idInfo", idInfo)
                return idInfo
            }
        }

        task.execute()






        if (preferences.getBoolean("useFingerLoginPref", false))
            showBiometricPrompt()

        //실제 구현부
        activity_login_bt_login.setOnClickListener {
            if (preferences.getString("deviceNamePref", "").toString() == "") {
                showInputDeviceName()
            } else
                requestLogin(
                    activity_login_et_id.text.toString(),
                    activity_login_et_pw.text.toString()
                )
        }
    }

    fun showInputDeviceName() {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.dialog_device_name, null)
        val deviceNameDialog = AlertDialog.Builder(this)
            .create()

        var deviceNameCompleteBtn: Button = view.findViewById(R.id.dialog_device_name_bt_complete)
        var deviceName: EditText = view.findViewById(R.id.dialog_device_name_et_name)

        deviceNameCompleteBtn.setOnClickListener {
            var deviceName = deviceName.text.toString()

            deviceNameDialog.dismiss()
            requestLogin(activity_login_et_id.text.toString(), activity_login_et_pw.text.toString())
            onResume()
        }

        deviceNameDialog.setView(view)
        deviceNameDialog.show()
        //onPause()
    }

    private fun showBiometricPrompt() {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("지문으로 로그인 설정")
            .setNegativeButtonText("취소")
            .build()

        val biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        editor.clear()
                        editor.commit()
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    val id: String = preferences.getString("idPref", "error").toString()
                    val pw: String = preferences.getString("pwPref", "error").toString()
                    requestLogin(id, pw)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }
            })

        // Displays the "log in" prompt.
        biometricPrompt.authenticate(promptInfo)
    }

    private fun requestLogin(id: String, pw: String) {

        var loadingDialog = LoadingDialog(this)
        loadingDialog.startLoadingDialog()

        var deviceId : String = preferences.getString("deviceIdPref", "").toString()
        var deviceName = preferences.getString("deviceNamePref", "").toString()

        if (deviceId == "") {
            deviceId = determineAdvertisingInfo()
            Log.d("deviceIdret", deviceId)
            editor.putString("deviceIdPref", deviceId)
            editor.commit()
        }

        val retrofit = MyApplication.getRetrofit()
        var sendData =
            M_LoginDTO(
                id,
                pw,
                deviceId,
                deviceName
            )
        val loginService = retrofit.create(LoginService::class.java)

        loginService.signIn(sendData).enqueue(object : Callback<M_LoginDTO> {
            override fun onFailure(call: Call<M_LoginDTO>, t: Throwable) {
                Log.d("onFailure", t.toString())
                Toast.makeText(applicationContext, "인터넷 연결을 확인해주세요.", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<M_LoginDTO>, response: Response<M_LoginDTO>) {
                //login result true
                var result = response.body()!!.result

                editor.putString("jwtPref", response.body()?.jwt)
                editor.commit()

                when (result) {
                    1 -> {
                        //기존 계정과 다르다면
                        if (preferences.getString("idPref", "error") != id) {
                            editor.putBoolean("useFingerLoginPref", false)
                            editor.putString("deviceNamePref", deviceName)
                            editor.putString("idPref", id)
                            editor.putString("pwPref", pw)
                            editor.commit()
                        }
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                    2 -> {
                        //Waiting for authentication
                        Toast.makeText(
                            applicationContext,
                            "PC에서 모바일 기기 인증을 확인해주세요",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    //login result false
                    0 -> {
                        editor.clear()
                        editor.commit()
                        Toast.makeText(applicationContext, "아이디와 비밀번호를 확인해주세요", Toast.LENGTH_LONG)
                            .show()
                    }
                }
                loadingDialog.dismissDialog()
            }
        })
    }

    private fun determineAdvertisingInfo(): String {
        lateinit var ret: String

        if (AdvertisingIdClient.isAdvertisingIdProviderAvailable(applicationContext)) {
            Log.d("if statement", "ok")
            val advertisingIdInfoListenableFuture =
                AdvertisingIdClient.getAdvertisingIdInfo(applicationContext)

            addCallback(
                advertisingIdInfoListenableFuture,
                object : FutureCallback<AdvertisingIdInfo> {
                    override fun onSuccess(adInfo: AdvertisingIdInfo?) {
                        ret = adInfo?.id ?: UUID.randomUUID().toString()
                        Log.d("deviceidmake",ret)
                    }

                    override fun onFailure(t: Throwable) {
                        Log.e(
                            "MY_APP_TAG",
                            "Failed to connect to Advertising ID provider."
                        )
                        // Try to connect to the Advertising ID provider again, or fall
                        // back to an ads solution that doesn't require using the
                        // Advertising ID library.
                    }
                }, Executors.newSingleThreadExecutor()
            )
        } else {
            ret = UUID.randomUUID().toString()
        }
        return ret
    }
}