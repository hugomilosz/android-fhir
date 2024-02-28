package com.google.android.fhir.demoIPS

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.fhir.search.Search
import com.google.android.fhir.sync.Sync
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.ResourceType

class SelectDocument : AppCompatActivity() {
  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // setContentView(R.layout.select_document)
    val fhirEngine = FhirApplication.fhirEngine(this)
    lifecycleScope.launch(Dispatchers.IO) {
      val syncJob = launch {
        Sync.oneTimeSync<AppFhirSyncWorker>(application)
          .shareIn(this, SharingStarted.Eagerly, 10)
          .collect {
            println("Sync completed: $it")
          }
      }

      syncJob.join()

      val patients = fhirEngine.search<Patient>(
        Search(ResourceType.Patient)
      )

      println("Number of Patients: ${patients.size}")
      patients.forEach { patient ->
        println("Patient Name: ${patient.resource.name.first().given.joinToString(" ")}")
      }
    }

  }
}