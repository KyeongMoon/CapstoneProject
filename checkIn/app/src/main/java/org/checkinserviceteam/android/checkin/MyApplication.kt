package org.checkinserviceteam.android.checkin

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.checkinserviceteam.android.checkin.service.LoginService
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

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
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
        }

        fun getAppContext(): Context {
            return context
        }

        fun getRetrofit(): Retrofit {
            return retrofit
        }

        fun getPreference() : SharedPreferences{
            return preferences
        }

        fun getEditor() : SharedPreferences.Editor{
            return preferences.edit()
        }

    }

    override fun onCreate() {
        super.onCreate()

        context = applicationContext

        retrofit = Retrofit.Builder()
            .baseUrl("http://54.180.153.254:8080/")
            .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper()))
            .client(createOkHttpClient())
            .build()
    }

    private fun createOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        builder.addInterceptor(interceptor)
        return builder.build()
    }
}