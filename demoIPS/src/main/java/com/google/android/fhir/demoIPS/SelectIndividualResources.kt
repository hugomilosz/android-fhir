/*
 * Copyright 2024 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.fhir.demoIPS

import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.fhir.document.generate.SHLinkGenerationData
import com.google.android.fhir.search.Search
import java.io.Serializable
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.AllergyIntolerance
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.ResourceType

class SelectIndividualResources : AppCompatActivity() {

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  private fun initialiseViewModel() {
    val viewModel = ViewModelProvider(this)[SelectIndividualResourcesViewModel::class.java]
    val fhirEngine = FhirApplication.fhirEngine(this)
    val resources = ArrayList<Resource>()

    lifecycleScope.launch {
      val allergyIntoleranceResults =
        fhirEngine.search<AllergyIntolerance>(
          Search(
            ResourceType.AllergyIntolerance,
          ),
        )

      val conditionResults =
        fhirEngine.search<Resource>(
          Search(
            ResourceType.Condition,
          ),
        )

      resources.addAll(allergyIntoleranceResults.map { it.resource })
      resources.addAll(conditionResults.map { it.resource })
      viewModel.initializeData(this@SelectIndividualResources, resources)
    }

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

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.select_individual_resources)
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    intent.type = "application/json"
    initialiseViewModel()
  }
}
