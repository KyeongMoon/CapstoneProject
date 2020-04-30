package org.checkinserviceteam.android.checkin.activity

import android.content.SharedPreferences
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.checkinserviceteam.android.checkin.MyApplication
import org.checkinserviceteam.android.checkin.data.LoginJSONData
import org.checkinserviceteam.android.checkin.service.LoginService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory


class LoginActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(org.checkinserviceteam.android.checkin.R.layout.activity_login)

        /*preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext())
        editor = preferences.edit()

        autoLogin()*/

        //preferences = MyApplication.getPreference()

        //var T = preferences.getString("testT", "No")
        //Toast.makeText(MyApplication.getAppContext(), T, Toast.LENGTH_LONG)

        //Log.d("preferences", T);

        //로그인 데이터 생성
        var data : LoginJSONData = LoginJSONData("abcd@naver.com", "1234", "1234")

        var retrofit = MyApplication.getRetrofit()

        var loginService : LoginService = retrofit.create(LoginService::class.java)


        login_btn_login.setOnClickListener {
            loginService.signUp(data)?.enqueue(object: Callback<LoginJSONData> {

                override fun onFailure(call: Call<LoginJSONData>, t: Throwable) {
                    Toast.makeText(applicationContext, "인터넷 연결에 실패하였습니다.", Toast.LENGTH_LONG).show()
                    Log.d("fuck", t.toString())
                }

                override fun onResponse(call: Call<LoginJSONData>, response: Response<LoginJSONData>) {
                    Log.d("error", response.errorBody().toString())

                    Log.d("connection", response.message().byteInputStream().toString())
                    Log.d("connectionCode", response.code().toString())
                    Log.d("connectionHeaders", response.headers().toString())
                    Log.d("connectionBody", response.body().toString())
                    Log.d("connectionBodyID", "response  : ${response.body()!!.agentID}")

                    //val login = response.body()
                    //val rdata = Gson().fromJson(login.toString(), LoginJSONData::class.java)

                    //if(login?.agentID == null)
                    //    Toast.makeText(baseContext,rdata.agentID, Toast.LENGTH_SHORT).show()
                }
            })
        }

        //실제 구현부
        /*login_btn_login.setOnClickListener {
            login(login_input_email.toString(), login_input_password.toString())
        }*/
    }
    private fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addInterceptor(interceptor)
        return builder.build()
    }

//    fun login(email: String, password: String) {
//        Log.d(TAG, "Login")
//
//        // TODO : 이메일, 비밀번호 형식을 잘못 입력한 경우 오류메세지를 출력할지.
//        //아이디 비밀번호 형식이 유효하지 않다면 Toast 를 띄우고 종료
//        //        if (!validate()) {
//        //            onLoginFailed();
//        //            return;
//        //        }
//
//        login_btn_login.isEnabled = false
//
//        //TODO : 뭔소리여 , progressbar 색상변경
//        //R.style.AppTheme_Dark_Dialog
//        val progressDialog = ProgressDialog(
//            this@LoginActivity,
//            R.style.Theme_AppCompat_DayNight_Dialog
//        )
//
//        //TODO : 로그인 시 progressbar 진행
//        progressDialog.isIndeterminate = true
//        progressDialog.setMessage("Authenticating...")
//        progressDialog.show()
//
//        // TODO: Implement your own authentication logic here.
//        var loginBit = 0
//
//        // TODO 주소 수정
//        val url = "http://18.218.11.150:8080/checkIN/signIn"
//        val loginData = LoginJSONData(email, password, uuid)
//
//        //TODO : Retrofit2 작성하기
//        //val networkTask = NetworkTask(url, loginData)
//        //val networkResult = networkTask.execute()
//
//
//        val mapper = ObjectMapper()
//        try {
//            //            Log.d("LoginResult", networkResult.get());
//            val result = mapper.readValue(networkResult.get(), LoginJSONData::class.java)
//            if (result.isResult())
//                loginBit = 1
//        } catch (e: IOException) {
//            e.printStackTrace()
//        } catch (e: ExecutionException) {
//            e.printStackTrace()
//        } catch (e: InterruptedException) {
//            e.printStackTrace()
//        }
//
//        //TODO: 3초뒤 프로세스가 마무리 되는것이기 때문에 이전에 WAS와 통신을 하고 결과 bit만 아래에 넘겨줄 것.
//        val finalLoginBit = loginBit
//        android.os.Handler().postDelayed(
//            {
//                if (finalLoginBit == 1) {
//                    onLoginSuccess()
//                    editor.putString("id", email)
//                    editor.putString("pw", password)
//                    editor.commit()
//
//                } else
//                    onLoginFailed()
//
//
//                // On complete call either onLoginSuccess or onLoginFailed
//                //onLoginSuccess();
//                // onLoginFailed();
//                progressDialog.dismiss()
//            }, 1000
//        )
//    }
//
//    //private//기기 키값 받아오는 내용
//    //이제부터 사용할 키값
//    val uuid: String
//        get() {
//            var encodedWidevineId: String? = ""
//            encodedWidevineId = preferences.getString("UUID", "null")
//
//            if (encodedWidevineId != "null")
//                return encodedWidevineId
//            val tm = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
//            val WIDEVINE_UUID = UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L)
//            val wvDrm: MediaDrm
//            try {
//
//                wvDrm = MediaDrm(WIDEVINE_UUID)
//                val widevineId = wvDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID)
//                val aa = widevineId.toString()
//
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                    encodedWidevineId =
//                        Base64.getEncoder().encodeToString(widevineId).trim { it <= ' ' }
//                }
//            } catch (e: UnsupportedSchemeException) {
//                e.printStackTrace()
//            }
//
//            editor.putString("UUID", encodedWidevineId)
//            editor.commit()
//            return encodedWidevineId
//        }
//
//    // autologin 상태확인 TRUE : 지문까지 일치하면 서버 연결 이후 토큰값 받기 2. FALSE : 서버연결이후 토큰값
//    //TODO : 첫 로그인 이후 항상 자동 로그인 구현, 로컬에 있는 정보를 받아 id와 pw를 받아 서버에 접속 요청하기
//    fun autoLogin() {
//        val id = preferences.getString("id", "null")
//        val pw = preferences.getString("pw", "null")
//        val useFingerPrint = preferences.getBoolean("useFingerPrint", false)
//
//        Log.d(TAG, id!!)
//        Log.d(TAG, pw!!)
//        Log.d(TAG, useFingerPrint.toString())
//
//        //TODO : Dialog빌더로 만들까 말까
//        //지문등록을 했고, 로그인을 했을 경우 2차인증을 실시한다.
//        val biometricManager = BiometricManager.from(this)
//
//        when (biometricManager.canAuthenticate()) {
//            BiometricManager.BIOMETRIC_SUCCESS -> Log.e(
//                "Finger",
//                "App can authenticate using biometrics."
//            )
//            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> Log.e(
//                "Finger",
//                "No biometric features available on this device."
//            )
//            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> Log.e(
//                "Finger",
//                "Biometric features are currently unavailable."
//            )
//            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> Log.e(
//                "Finger",
//                "The user hasn't associated any biometric credentials " + "with their account."
//            )
//        }
//
//
//        // 자동로그인 상태에서 지문등록한 경우, 지문으로 자동로그인
//        if (id != "null" && pw != "null" && useFingerPrint) {
//            val a = FingerBioFactory(this, object : BiometricPrompt.AuthenticationCallback() {
//                fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
//                    super.onAuthenticationError(errorCode, errString)
//                }
//
//                fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
//                    super.onAuthenticationSucceeded(result)
//                    //지문이 성공 했을때
//
//                    onLoginSuccess()
//                }
//
//                fun onAuthenticationFailed() {
//                    super.onAuthenticationFailed()
//                }
//            })
//            a.authenticate()
//        }
//
//    }
//
//    inner class FingerBioFactory(
//        private val activity: AppCompatActivity,
//        private val callback: BiometricPrompt.AuthenticationCallback
//    ) {
//        private var myBiometricPrompt: BiometricPrompt? = null
//        private var biPromptInfo: BiometricPrompt.PromptInfo? = null
//
//        init {
//            setting()
//        }
//
//        private fun setting() {
//            val newExecutor = Executors.newSingleThreadExecutor()
//
//            biPromptInfo = BiometricPrompt.PromptInfo.Builder()
//                .setTitle("지문으로 로그인하기")
//                .setDescription("지문을 입력해주세요")
//                .setNegativeButtonText("취소")
//                .build()
//
//            myBiometricPrompt = BiometricPrompt(activity, newExecutor, callback)
//        }
//
//        fun authenticate() {
//            myBiometricPrompt!!.authenticate(biPromptInfo)
//        }
//    }
//
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_SIGNUP) {
//            if (resultCode == Activity.RESULT_OK) {
//
//                // TODO: Implement successful signup logic here
//                // By default we just finish the Activity and log them in automatically
//                this.finish()
//            }
//        }
//    }
//
//    override fun onBackPressed() {
//        // disable going back to the MainActivity
//        moveTaskToBack(true)
//    }
//
//    fun onLoginSuccess() {
//        loginButton.isEnabled = true
//        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//        finish()
//    }
//
//    fun onLoginFailed() {
//        Toast.makeText(baseContext, "Login failed", Toast.LENGTH_LONG).show()
//
//        loginButton.isEnabled = true
//    }
//
//    fun validate(): Boolean {
//        var valid = true
//
//        val email = emailTextView.text.toString()
//        val password = passwordTextView.text.toString()
//
//        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            emailTextView.error = "enter a valid email address"
//            valid = false
//        } else {
//            emailTextView.error = null
//        }
//
//        if (password.isEmpty() || password.length < 4 || password.length > 10) {
//            passwordTextView.error = "between 4 and 10 alphanumeric characters"
//            valid = false
//        } else {
//            passwordTextView.error = null
//        }
//
//        return valid
//    }
//
//    companion object {
//
//        private val TAG = "LoginActivity"
//        private val REQUEST_SIGNUP = 0
//    }
}