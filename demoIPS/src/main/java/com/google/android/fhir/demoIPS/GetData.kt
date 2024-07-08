package com.google.android.fhir.demoIPS

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.fhir.document.IPSDocument
import com.google.android.fhir.document.render.IPSRenderer


class GetData : AppCompatActivity() {
  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.get_data)
    val doc = intent.getSerializableExtra("doc", IPSDocument::class.java)
    IPSRenderer(doc).render(this)
  }

}