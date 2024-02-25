package com.google.android.fhir.demoIPS

import android.content.Context
import android.widget.CheckBox
import androidx.lifecycle.ViewModel
import ca.uhn.fhir.context.FhirContext
import ca.uhn.fhir.context.FhirVersionEnum
import com.google.android.fhir.document.IPSDocument
import com.google.android.fhir.document.Title
import com.google.android.fhir.document.generate.DocumentGeneratorUtils
import com.google.android.fhir.document.generate.DocumentUtils
import com.google.android.fhir.document.generate.SelectResourcesImpl
import com.google.android.fhir.document.generate.hasCode
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.ResourceType

class SelectIndividualResourcesViewModel : ViewModel() {
  private var selectedTitles = listOf<Title>()
  private val parser = FhirContext.forCached(FhirVersionEnum.R4).newJsonParser()
  private val documentGenerator = SelectResourcesImpl(DocumentGeneratorUtils, DocumentUtils)
  private lateinit var patient: Resource
  private val checkBoxes = mutableListOf<CheckBox>()
  private val checkboxTitleMap = mutableMapOf<String, String>()

  /* Get the FHIR resources and display them as checkboxes for the patient to select */
  fun initializeData(context: Context, file: String) {
    val docUtils = DocumentUtils
    val doc = docUtils.readFileFromAssets(context, "immunizationBundle.json")
    val ipsDoc = IPSDocument.create(parser.parseResource(doc) as Bundle)
    selectedTitles = documentGenerator.displayOptions(context, ipsDoc, checkBoxes, checkboxTitleMap)
    patient =
      ipsDoc.document.entry.firstOrNull { it.resource.resourceType == ResourceType.Patient }?.resource
        ?: Patient()
  }

  /* Filter through the selected checkboxes and generate an IPS document
     using the patient-selected resources */
  fun generateIPSDocument(): IPSDocument {
    val selectedValues = checkBoxes.filter { it.isChecked }.map { checkBox ->
      val text = checkBox.text.toString()
      val name = checkboxTitleMap[text] ?: ""
      Pair(Title(name, arrayListOf()), text)
    }

    val outputArray = selectedValues.flatMap { (title, value) ->
      title.let { selectedTitle ->
        selectedTitles.find { it.name == selectedTitle.name }?.dataEntries?.filter { obj ->
          obj.hasCode().first?.coding?.firstOrNull { it.hasDisplay() && it.display == value } != null
        } ?: emptyList()
      }
    } + patient

    return documentGenerator.generateIPS(outputArray)
  }

}