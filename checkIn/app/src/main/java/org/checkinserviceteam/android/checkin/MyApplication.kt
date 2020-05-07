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
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

class MyApplication : Application() {

    companion object {

        private lateinit var context: Context
        private lateinit var retrofit: Retrofit

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

    }

    override fun onCreate() {
        super.onCreate()

        context = applicationContext

        retrofit = Retrofit.Builder()
            .baseUrl("https://54.180.153.254/")
            .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()))
            .client(createOkHttpClient())
            .build()
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
}