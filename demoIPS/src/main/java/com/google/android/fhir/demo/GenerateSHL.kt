package com.google.android.fhir.demoIPS

import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.fhir.NetworkConfiguration
import com.google.android.fhir.demoIPS.R
import com.google.android.fhir.document.RetrofitSHLService
import com.google.android.fhir.document.generate.EncryptionUtils
import com.google.android.fhir.document.generate.SHLinkGenerationData
import com.google.android.fhir.document.generate.SHLinkGeneratorImpl
import kotlinx.coroutines.launch


class GenerateSHL : AppCompatActivity() {

  private val linkGenerator = SHLinkGeneratorImpl(RetrofitSHLService.Builder("", NetworkConfiguration()).build(), EncryptionUtils)

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.view_shl)

    val passcode: String = intent.getStringExtra("passcode").toString()
    val shlData = intent.getSerializableExtra("shlData", SHLinkGenerationData::class.java)
    val passcodeField = findViewById<TextView>(R.id.passcode)
    val expirationDateField = findViewById<TextView>(R.id.expirationDate)
    passcodeField.text = passcode
    expirationDateField.text = shlData?.expirationTime.toString()

    if (shlData?.ipsDoc?.document != null) {
      lifecycleScope.launch {
        linkGenerator.generateSHLink(
          shlData, passcode, "", ""
        )
      }
    }
  }
}