package org.checkinserviceteam.android.checkin

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.checkinserviceteam.android.checkin.retrofit.service.DTO.M_LoginDTO
import org.checkinserviceteam.android.checkin.retrofit.service.LoginService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*
import kotlin.system.exitProcess

class MyApplication : Application() {

    companion object {

        private lateinit var context: Context
        private lateinit var retrofit: Retrofit
        private lateinit var androidDefaultUEH: Thread.UncaughtExceptionHandler

        private val preferences: SharedPreferences by lazy {
            val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
            val masterKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)

            EncryptedSharedPreferences.create(
                "security_test",
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }

        fun getAppContext(): Context {
            return context
        }

        fun getRetrofit(): Retrofit {
            return retrofit
        }

        fun getPreference(): SharedPreferences {
            return preferences
        }

        fun getEditor(): SharedPreferences.Editor {
            return preferences.edit()
        }

        fun getAndroidDefault(): Thread.UncaughtExceptionHandler{
            return androidDefaultUEH
        }

    }

    override fun onCreate() {
        super.onCreate()

        context = applicationContext

        retrofit = Retrofit.Builder()
            .baseUrl("https://54.180.153.254/")
            .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()))
            .client(createOkHttpClient())
            .build()

        androidDefaultUEH = Thread.getDefaultUncaughtExceptionHandler()

        Thread.setDefaultUncaughtExceptionHandler(UncaughtExceptionHandler())
    }

    private fun createOkHttpClient(): OkHttpClient {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts =
                arrayOf<TrustManager>(
                    object : X509TrustManager {
                        @Throws(CertificateException::class)
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        @Throws(CertificateException::class)
                        override fun checkServerTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf<X509Certificate>()
                        }
                    }
                )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory
            val builder = OkHttpClient.Builder()

            //val builder = OkHttpClient.Builder()
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
            //return builder.build()



            builder.sslSocketFactory(sslSocketFactory)
            builder.hostnameVerifier(object : HostnameVerifier {
                override fun verify(hostname: String?, session: SSLSession?): Boolean {
                    return true
                }
            })
            builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private class UncaughtExceptionHandler : Thread.UncaughtExceptionHandler {
        override fun uncaughtException(t: Thread, e: Throwable) {
            Log.d("Myapplication", "onDestroyStart")

            var currId = preferences.getString("idPref", "error").toString()
            val currJwt = preferences.getString("jwtPref", "").toString()

            var sendData = M_LoginDTO(currId, currJwt)
            var retrofit  = MyApplication. getRetrofit()
            var logoutService = retrofit.create(LoginService::class.java)


            //do nothing
            logoutService.signOut(sendData)?.enqueue(object : Callback<M_LoginDTO> {
                override fun onFailure(call: Call<M_LoginDTO>, t: Throwable) {
                }

                override fun onResponse(call: Call<M_LoginDTO>, response: Response<M_LoginDTO>) {            }
            })
            Log.d("Myapplication", "onDestroyEnd")


            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(10);
        }

    }
}