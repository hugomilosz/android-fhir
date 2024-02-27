package com.google.android.fhir.demoIPS

import android.content.Context
import androidx.work.WorkerParameters
import com.google.android.fhir.sync.AcceptLocalConflictResolver
import com.google.android.fhir.sync.FhirSyncWorker
import com.google.android.fhir.sync.upload.UploadStrategy

class AppFhirSyncWorker(appContext: Context, workerParams: WorkerParameters) :
  FhirSyncWorker(appContext, workerParams) {

  override fun getDownloadWorkManager() = DownloadWorkManagerImpl()

  override fun getConflictResolver() = AcceptLocalConflictResolver
  override fun getUploadStrategy(): UploadStrategy {
    TODO("Not yet implemented")
  }

  override fun getFhirEngine() = FhirApplication.fhirEngine(applicationContext)
}