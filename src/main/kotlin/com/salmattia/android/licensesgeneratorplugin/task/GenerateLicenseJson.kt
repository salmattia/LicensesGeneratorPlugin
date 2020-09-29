//package com.salmattia.android.licensesgeneratorplugin.task
//
//import com.salmattia.android.licensesgeneratorplugin.LicensesGeneratorPluginExtension
//import com.salmattia.android.licensesgeneratorplugin.data.LibraryLicense
//import com.salmattia.android.licensesgeneratorplugin.extension.writeLicensesJsonFile
//import com.salmat.android.licensesgeneratorplugin.util.YamlUtils
//import kotlinx.serialization.Serializable
//import org.gradle.api.Project
//import org.gradle.api.Task
//
//object GenerateLicenseJson {
//    fun register(project: Project): Task {
//        return project.task("generateLicenseJson").doLast {
//            val ext = project.extensions.getByType(LicensesGeneratorPluginExtension::class.java)
//            val yamlInfoList = YamlUtils.loadToLibraryInfo(project.file(ext.licensesYaml))
//            project.writeLicensesJsonFile(yamlInfoList.toJson())
//        }
//    }
//
//    fun List<LibraryLicense>.toJson(): String {
//        return this
//            .filterNot { it.skip ?: false }
//            .map {
//                LibraryLicenseData.LibraryInfoJson(
//                    artifact = LibraryLicenseData.LibraryInfoJson.Artifact(
//                        name = it.artifact.name,
//                        group = it.artifact.group,
//                        version = it.artifact.version
//                    ),
//                    notice = it.notice,
//                    copyrightHolder = it.copyrightHolder,
//                    copyrightStatement = it.getCopyright(),
//                    license = it.license,
//                    licenseUrl = it.licenseUrl,
//                    normalizedLicense = it.normalizedLicense(),
//                    year = it.year,
//                    url = it.url,
//                    libraryName = it.libraryName
//                )
//            }
//            .toList()
//            .let { adapter.toJson(
//                LibraryLicenseData(
//                    libraries = it
//                )
//            ) }
//    }
//
//    @Serializable
//    data class LibraryLicenseData(
//        @Serializable
//        val libraries: List<LibraryInfoJson>
//    ) {
//        @Serializable
//        data class LibraryInfoJson(
//
//            val artifact: Artifact,
//            val notice: String?,
//            val copyrightHolder: String?,
//            val copyrightStatement: String?,
//            val license: String?,
//            val licenseUrl: String?,
//            val normalizedLicense: String?,
//            val year: String?,
//            val url: String?,
//            val libraryName: String?
//        ) {
//            @Serializable
//            data class Artifact(
//                val name: String,
//                val group: String,
//                val version: String
//            )
//        }
//    }
//
//}
