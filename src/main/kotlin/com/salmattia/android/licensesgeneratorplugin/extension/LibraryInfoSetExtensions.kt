//package com.salmattia.android.licensesgeneratorplugin.extension
//
//import com.salmattia.android.licensesgeneratorplugin.data.Artifact
//import com.salmattia.android.licensesgeneratorplugin.data.LibraryLicense
//
//fun List<LibraryLicense>.duplicatedArtifacts(): List<String> {
//    return this
//        .groupingBy { it.artifact.toWildcardString() }
//        .eachCount()
//        .filter { it.value > 1 }
//        .map { it.key }
//}
//
//fun List<LibraryLicense>.notListedIn(dependencySet: List<LibraryLicense>): List<LibraryLicense> {
//    return this
//        .filterNot { dependencySet.hasArtifact(it.artifact) }
//        .filterNot { it.skip ?: false }
//        .filterNot { it.forceGenerate ?: false }
//}
//
//fun List<LibraryLicense>.licensesUnMatched(librariesLicense: List<LibraryLicense>): List<LibraryLicense> {
//    return librariesLicense
//        .filterNot { it.skip ?: false }
//        .filterNot { it.forceGenerate ?: false }
//        .filter { it.license?.isNotBlank() ?: false }
//        .filter { this.checkUnMatchedLicense(it) }
//}
//
//private fun List<LibraryLicense>.checkUnMatchedLicense(libraryLicense: LibraryLicense): Boolean {
//    return this
//        .asSequence()
//        .filter { it.artifact == libraryLicense.artifact }
//        .filterNot { it.skip ?: false }
//        .filterNot { it.forceGenerate ?: false }
//        .filter { it.license?.isNotBlank() ?: false }
////        .filterNot {
////            it.normalizedLicense().equals(libraryLicense.normalizedLicense(), ignoreCase = true)
////        }
//        .toList()
//        .isNotEmpty()
//}
//
//private fun List<LibraryLicense>.hasArtifact(artifact: Artifact): Boolean {
//    this.forEach {
//        if (it.artifact == artifact) {
//            return true
//        }
//    }
//    return false
//}
