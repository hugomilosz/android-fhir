package com.google.android.fhir.demoIPS

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.fhir.document.generate.SHLinkGenerationData
import java.io.Serializable

class SelectIndividualResources : AppCompatActivity() {

  private fun initialiseViewModel(jsonString: String) {
    val viewModel = ViewModelProvider(this)[SelectIndividualResourcesViewModel::class.java]
    viewModel.initializeData(this, jsonString)


    val submitButton = findViewById<Button>(R.id.goToCreatePasscode)
    submitButton.setOnClickListener {
      val ipsDoc = viewModel.generateIPSDocument()
      val shlData = SHLinkGenerationData("", null, ipsDoc)
      val i = Intent()
      i.component = ComponentName(this@SelectIndividualResources, CreatePasscode::class.java)
      i.putExtra("shlData", shlData as Serializable)
      startActivity(i)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.select_individual_resources)
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    intent.type = "application/json"
    initialiseViewModel("")
  }
}