package com.google.android.fhir.demoIPS

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.fhir.demo.SelectIndividualResourcesViewModel
import com.google.android.fhir.library.dataClasses.SHLData
import java.io.Serializable

class SelectIndividualResources : AppCompatActivity() {

  private val pickFileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
    if (result.resultCode == Activity.RESULT_OK) {
      val data: Intent? = result.data
      data?.data?.let { fileUri ->
        contentResolver.openInputStream(fileUri)?.use { inputStream ->
          val jsonString = inputStream.bufferedReader().use { it.readText() }
          initialiseViewModel(jsonString)
        }
      }
    }
  }

  private fun initialiseViewModel(jsonString: String) {
    val viewModel = ViewModelProvider(this)[SelectIndividualResourcesViewModel::class.java]
    viewModel.initializeData(this, jsonString)


    val submitButton = findViewById<Button>(com.google.android.fhir.library.R.id.goToCreatePasscode)
    submitButton.setOnClickListener {
      val ipsDoc = viewModel.generateIPSDocument()
      val shlData = SHLData(ipsDoc)
      val i = Intent()
      i.component = ComponentName(this@SelectIndividualResources, CreatePasscode::class.java)
      i.putExtra("shlData", shlData as Serializable)
      startActivity(i)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(com.google.android.fhir.library.R.layout.select_individual_resources)
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    intent.type = "application/json"
    pickFileLauncher.launch(intent)

  }
}