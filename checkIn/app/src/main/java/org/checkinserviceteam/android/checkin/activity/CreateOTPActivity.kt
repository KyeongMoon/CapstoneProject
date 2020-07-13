package org.checkinserviceteam.android.checkin.activity

import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_create_otp.*
import org.checkinserviceteam.android.checkin.MyApplication
import org.checkinserviceteam.android.checkin.R
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.experimental.and

class CreateOTPActivity : AppCompatActivity() {

    lateinit var mCountDownTimer: CountDownTimer
    lateinit var preferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_otp)

        preferences = MyApplication.getPreference()
        editor = MyApplication.getEditor()
    }

    override fun onStart() {

        val deviceId = preferences.getString("deviceIdPref", "").toString()
        Log.d("deviceId", deviceId)

        //verify_code(deviceId, 1594645306038)

        mCountDownTimer = object : CountDownTimer((1000 * 60 * 60).toLong(), 1000) {
            private var i = 61

            override fun onTick(millisUntilFinished: Long) {
                Log.v("Log_tag", "Tick of Progress $i $millisUntilFinished")

                if (i == 0 || i == 61) {
                    var OTP = UpdateOTP(deviceId)
                    Log.d("OTP", OTP.toString())
                    activity_create_otp_tv_otp.text = OTP.toString()
                    i = 60
                }
                activity_create_otp_pb_otp.progress = i
                activity_create_otp_tv_sec.text = "$i 초"
                i--
            }

            override fun onFinish() {
                //nothing
                mCountDownTimer.cancel()
            }
        }
        mCountDownTimer.start()
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        mCountDownTimer.cancel()
    }

    private fun UpdateOTP(UUID: String): Int {
        val time = Date().time
        Log.d("Currtime", time.toString())
        try {
            return verify_code(UUID, time)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: InvalidKeyException) {
            e.printStackTrace()
        }

        return -1
    }

    @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
    fun verify_code(key: String, t: Long): Int {
        Log.d("Verify : Key", key)
        Log.d("Verify : time", t.toString())
        var data = ByteArray(8)
        var value = t / 1000

        run {
            var i = 8
            while (i-- > 0) {
                Log.d("Verify : data" + i + " ", " " + data[i].toString())
            }
        }

        Log.d("Verify : FirstValue", value.toString())

        //run {
        var i = 8
        while (i-- > 0) {
            data[i] = value.toByte()
            value = value ushr 8
        }
        //}

        run {
            var i = 8
            while (i-- > 0) {
                Log.d("Verify : data" + i + " ", " " + data[i].toString())
            }
        }

        Log.d("Verify : afterData : ", data.toString())

        val signKey = SecretKeySpec(key.toByteArray(), "HmacSHA1")

        Log.d("Verify : signKey", signKey.toString())

        var mac = Mac.getInstance("HmacSHA1")
        mac.init(signKey)
        val hash = mac.doFinal(data)

        Log.d("Verify : hash", hash[0].toString())
        Log.d("Verify : hash", hash[1].toString())
        Log.d("Verify : hash", hash[2].toString())
        Log.d("Verify : hash", hash[3].toString())

        val offset : Int = (hash[20 - 1] and 0xF).toInt()

        Log.d("Verify : offset", offset.toString())
        Log.d("Verify : 0xFF", 0xFF.toByte().toString())

        var truncatedHash: Long = 0
        for (i in 0..3) {
            Log.d("Verify : before", truncatedHash.toString())
            truncatedHash = truncatedHash shl 8
            Log.d("Verify : mid", truncatedHash.toString())
            Log.d("Verify : mid hash", hash[offset+i].toString())
            Log.d("Verify : mid after", (hash[offset+i].toLong() and 255).toString())
            truncatedHash = truncatedHash or (hash[offset + i].toLong() and 0xFF)
            Log.d("Verify : after", truncatedHash.toString())
        }
        truncatedHash = truncatedHash and 0x7FFFFFFF
        truncatedHash %= 900000
        truncatedHash += 100000

        Log.d("Verify : return", truncatedHash.toString())

        return truncatedHash.toInt()
    }

    fun checkCode(userCode: String, otpkey: String): Boolean {
        val otpnum = Integer.parseInt(userCode).toLong()
        val wave = Date().time
        var result = false
        try {
            val window = 1000 * 60
            var i = -window
            while (i <= 2000) {
                val hash = verify_code(otpkey, wave + i).toLong()
                if (hash == otpnum)
                    result = true
                i += 1000
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }
    fun FinishActivity(view: View){
        finish()
    }
}
