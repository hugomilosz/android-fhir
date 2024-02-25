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
import android.widget.CheckBox
import com.google.android.fhir.document.IPSDocument
import com.google.android.fhir.document.Title
import org.hl7.fhir.r4.model.Resource

/**
 * An International Patient Summary (IPS) document is an electronic health record extract containing
 * essential healthcare information about a subject of care. For detailed specifications, see
 * [Official IPS Implementation Guide](https://build.fhir.org/ig/HL7/fhir-ips/index.html).
 *
 * The [SelectResources] interface defines a contract for generating IPS documents based on a list
 * of patient-selected resources and rendering them for an Android device.
 */
interface SelectResources {

  /**
   * Generates a new IPS document given a list of patient-selected resources.
   *
   * @param selectedResources List of selected FHIR resources.
   * @return IPSDocument representing the generated document.
   */
  fun generateIPS(selectedResources: List<Resource>): IPSDocument

  /**
   * Renders an IPS document for an Android device, providing options for selected resources.
   *
   * @param context The Android application context.
   * @param bundle IPSDocument to be displayed.
   * @param checkboxes List of CheckBox widgets to be displayed.
   * @param checkboxTitleMap Mapping of CheckBox titles to resource identifiers.
   * @return List of Title objects representing the displayed options.
   */
  fun displayOptions(
    context: Context,
    bundle: IPSDocument,
    checkboxes: MutableList<CheckBox>,
    checkboxTitleMap: MutableMap<String, String>,
  ): List<Title>
}
