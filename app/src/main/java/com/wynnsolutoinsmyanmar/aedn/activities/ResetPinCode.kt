package com.wynnsolutoinsmyanmar.aedn.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.hanks.passcodeview.PasscodeView
import com.wynnsolutoinsmyanmar.aedn.R
import com.wynnsolutoinsmyanmar.aedn.util.SharedPreference

class ResetPinCode : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_pin_code)
        val sharedPreference: SharedPreference = SharedPreference(this)
        val passcodeView = findViewById(R.id.passcodeView) as PasscodeView

        passcodeView.setPasscodeLength(4).setListener(object : PasscodeView.PasscodeViewListener {
            override fun onFail() {
                Toast.makeText(application, "Wrong!!", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(number: String) {
                sharedPreference.save("code", passcodeView.getLocalPasscode().toString())
                Toast.makeText(application, "Passcode Updated", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ResetPinCode, MainActivity::class.java)
                this@ResetPinCode.startActivity(intent)
                finish()
            }
        })

    }
}
