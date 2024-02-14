package com.google.android.fhir.document.generate

import android.content.Context
import android.widget.CheckBox
import com.google.android.fhir.document.IPSDocument
import com.google.android.fhir.document.Title
import org.hl7.fhir.r4.model.Resource

interface SelectResourcesAndGenerateDocument {

  /* Returns a map of all the sections in the document to the list of resources listed under that section */
  fun getDataFromDoc(doc: IPSDocument): List<Title>

  /* Generates a new IPS document given a list of patient-selected resources */
  fun generateIPS(selectedResources: List<Resource>): IPSDocument

  /* Renders an IPS document for an android device */
  fun displayOptions(
    context: Context,
    bundle: IPSDocument,
    checkboxes: MutableList<CheckBox>,
    checkboxTitleMap: MutableMap<String, String>
  ): List<Title>

}