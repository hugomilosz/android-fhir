package com.google.android.fhir.demoIPS

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.fhir.search.Search
import java.io.Serializable
import kotlinx.coroutines.launch
import org.hl7.fhir.r4.model.AllergyIntolerance
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.ResourceType

class SelectDocument : AppCompatActivity() {

  private lateinit var compositionListView: ListView
  private lateinit var adapter: ArrayAdapter<Resource>

  @RequiresApi(Build.VERSION_CODES.O)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.select_document)

    compositionListView = findViewById(R.id.composition_list_view)
    adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
    compositionListView.adapter = adapter

    compositionListView.setOnItemClickListener { _, _, position, _ ->
      val selectedComposition = adapter.getItem(position)
      Log.d("SELECTED", selectedComposition.toString())
      val intent = Intent(this, SelectIndividualResources::class.java)
      intent.putExtra("selectedCompositions", selectedComposition as Serializable)
      startActivity(intent)
    }
    val fhirEngine = FhirApplication.fhirEngine(this)

    lifecycleScope.launch {
      val allergyIntoleranceResults = fhirEngine.search<AllergyIntolerance>(
        Search(
          ResourceType.AllergyIntolerance,
        )
      )

      val conditionResults = fhirEngine.search<Resource>(
        Search(
          ResourceType.Condition,
        )
      )

      adapter.addAll(allergyIntoleranceResults.map { it.resource })
      adapter.addAll(conditionResults.map { it.resource })
      adapter.notifyDataSetChanged()
    }
  }
}