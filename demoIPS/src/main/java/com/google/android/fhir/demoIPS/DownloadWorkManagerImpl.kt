package com.google.android.fhir.demoIPS

import com.google.android.fhir.sync.DownloadWorkManager
import com.google.android.fhir.sync.download.DownloadRequest
import java.util.LinkedList
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.ResourceType

class DownloadWorkManagerImpl : DownloadWorkManager {
  private val urls = LinkedList(listOf("Patient"))

  override suspend fun getNextRequest(): DownloadRequest? {
    val url = urls.poll() ?: return null
    return DownloadRequest.of(url)
  }

  override suspend fun getSummaryRequestUrls() = mapOf<ResourceType, String>()

  override suspend fun processResponse(response: Resource): Collection<Resource> {
    var bundleCollection: Collection<Resource> = mutableListOf()
    if (response is Bundle && response.type == Bundle.BundleType.SEARCHSET) {
      bundleCollection = response.entry.map { it.resource }
    }
    return bundleCollection
  }
}