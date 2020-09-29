package com.salmattia.android.licensesgeneratorplugin.data

data class LibraryLicense(
    val artifactDescriptor: ArtifactDescriptor,
    val licenseName: String? = null,
    val libraryName: String? = null,
    val url: String? = null,
    val fileName: String? = null,
    val license: String? = null,
    val year: String? = null,
    val copyright: String? = null,
    val notice: String? = null,
    val licenseUrl: String? = null
)
