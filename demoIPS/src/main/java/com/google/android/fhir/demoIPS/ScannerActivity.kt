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
import android.os.Bundle
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.fhir.document.scan.SHLinkScannerImpl
import java.io.Serializable

class ScannerActivity : AppCompatActivity() {

  private lateinit var scanner: SHLinkScannerImpl

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.scanner)

    val surfaceView = findViewById<SurfaceView>(R.id.cameraSurfaceView)

    /* Initialize the scanner and call scan */
    scanner = SHLinkScannerImpl(this, surfaceView.holder)
    scanner.scanSHLQRCode(
      successCallback = { shlData ->
        val i = Intent()
        i.component = ComponentName(this@ScannerActivity, SuccessfulScan::class.java)
        i.putExtra("shlData", shlData as Serializable)
        startActivity(i)
      },
      failCallback = {}
    )
  }

  // Release scanner resources when the activity is destroyed
  override fun onDestroy() {
    super.onDestroy()
    scanner.release()
  }
}
