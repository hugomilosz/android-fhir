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

package com.google.android.fhir.document.generate

import android.content.Context
import org.hl7.fhir.r4.model.AllergyIntolerance
import org.hl7.fhir.r4.model.CodeableConcept
import org.hl7.fhir.r4.model.Coding
import org.hl7.fhir.r4.model.Condition
import org.hl7.fhir.r4.model.Immunization
import org.hl7.fhir.r4.model.Medication
import org.hl7.fhir.r4.model.Observation
import org.hl7.fhir.r4.model.Resource

object DocumentUtils {

  fun shouldExcludeResource(title: String, resource: Resource): Boolean {
    val code = resource.hasCode().second
    return (title == "History of Past Illness" && code == "active") ||
      ((title == "Active Problems" || title == "Allergies and Intolerances") && code != "active")
  }

  fun getSearchingCondition(resource: String, resourceType: String): Boolean {
    return when (resource) {
      "Allergies and Intolerances" -> resourceType == "AllergyIntolerance"
      "Medication" -> resourceType == "Medication"
      "Active Problems",
      "History of Past Illness", -> resourceType == "Condition"
      "Immunizations" -> resourceType == "Immunization"
      "Results" -> resourceType == "Observation"
      "Plan of Treatment" -> false // inside div
      // titles have to change
      "procedure history" -> false
      "medical devices" -> false
      else -> false
    }
  }

  fun readFileFromAssets(context: Context, filename: String): String {
    return context.assets.open(filename).bufferedReader().use { it.readText() }
  }

  fun getCodings(res: Resource): MutableList<Coding>? {
    return res.hasCode().first?.coding
  }
}

fun Resource.hasCode(): Pair<CodeableConcept?, String?> {
  return when (this) {
    is AllergyIntolerance -> Pair(code, clinicalStatus.coding.firstOrNull()?.code)
    is Condition -> Pair(code, clinicalStatus.coding.firstOrNull()?.code)
    is Medication -> Pair(code, null)
    is Observation -> Pair(code, null)
    is Immunization -> Pair(vaccineCode, "")
    else -> Pair(null, null)
  }
}
