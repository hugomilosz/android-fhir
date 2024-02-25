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

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import androidx.annotation.RequiresApi
import com.google.android.fhir.document.generate.SHLinkGenerationData
import java.io.Serializable
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Calendar

class CreatePasscode : Activity() {

  @RequiresApi(Build.VERSION_CODES.TIRAMISU)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.create_passcode)

    val datePicker = findViewById<DatePicker>(R.id.datePicker)
    val shlData = intent.getSerializableExtra("shlData", SHLinkGenerationData::class.java)

    val today: Calendar = Calendar.getInstance()
    datePicker.minDate = today.timeInMillis

    val submitResourcesButton = findViewById<Button>(R.id.generateSHL)
    val checkboxDate = findViewById<CheckBox>(R.id.checkboxDate)
    val passcodeField = findViewById<EditText>(R.id.passcode)
    val labelField = findViewById<EditText>(R.id.label)

    /*
    When the submit button is pressed, the state of the checkbox is checked, and the passcode
    and expiration date are added to the intent to be passed into the next activity.
    They are empty strings if they haven't been inputted
    */
    submitResourcesButton.setOnClickListener {
      val i = Intent()
      i.component = ComponentName(this@CreatePasscode, GenerateSHL::class.java)
      val passcode = passcodeField.text.toString()
      val label = labelField.text.toString()
      val exp: Instant? =
        if (checkboxDate.isChecked) {
          val year = datePicker.year
          val month = datePicker.month + 1
          val dayOfMonth = datePicker.dayOfMonth

          val localDate = LocalDate.of(year, month, dayOfMonth)
          val instant = localDate.atStartOfDay().toInstant(ZoneOffset.UTC)

          instant
        } else {
          null
        }
      val newSHLData = shlData?.let { it1 -> SHLinkGenerationData(label, exp, it1.ipsDoc) }
      i.putExtra("passcode", passcode)
      i.putExtra("shlData", newSHLData as Serializable)
      startActivity(i)
    }

    /* Set the initial state of the DatePicker based on the Checkbox state */
    datePicker.isEnabled = checkboxDate.isChecked
    checkboxDate.setOnCheckedChangeListener { _, isChecked -> datePicker.isEnabled = isChecked }
  }
}
