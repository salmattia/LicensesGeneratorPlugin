//package com.salmattia.android.licensesgeneratorplugin.task
//
//import com.salmattia.android.licensesgeneratorplugin.LicensesGeneratorPluginExtension
//import com.salmattia.android.licensesgeneratorplugin.data.Artifact
//import com.salmattia.android.licensesgeneratorplugin.data.LibraryLicense
//import com.salmattia.android.licensesgeneratorplugin.data.LibraryProjectPom
//import com.salmat.android.licensesgeneratorplugin.extension.*
//import com.salmat.android.licensesgeneratorplugin.util.YamlUtils
//import com.salmattia.android.licensesgeneratorplugin.extension.licensesUnMatched
//import com.salmattia.android.licensesgeneratorplugin.extension.notListedIn
//import com.salmattia.android.licensesgeneratorplugin.extension.toDependencyFormattedText
//import kotlinx.serialization.Decoder
//import org.gradle.api.GradleException
//import org.gradle.api.Project
//import org.gradle.api.Task
//import org.gradle.api.artifacts.ResolvedArtifact
//import org.gradle.internal.impldep.com.google.common.annotations.VisibleForTesting
//import java.io.File
//
//object CheckLicenses {
//    fun register(project: Project): Task {
//
//        return project.task("checkLicenses").doLast {
//            val ext = project.extensions.getByType(LicensesGeneratorPluginExtension::class.java)
//            // based on license plugin's dependency-license.xml
//            val resolvedArtifacts =
//                resolveProjectDependencies(
//                    project,
//                    ext.ignoredProjects
//                )
//            val dependencyLicenses =
//                loadDependencyLicenses(
//                    project,
//                    resolvedArtifacts,
//                    ext.ignoredGroups
//                )
//            // based on libraries.yml
//            val librariesYaml = YamlUtils.loadToLibraryInfo(project.file(ext.licensesYaml))
//
//            val notDocumented = dependencyLicenses.notListedIn(librariesYaml)
//            val notInDependencies = librariesYaml.notListedIn(dependencyLicenses)
//            val licensesUnMatched = dependencyLicenses.licensesUnMatched(librariesYaml)
//            val duplicatedArtifactIds = librariesYaml.duplicatedArtifacts()
//
//            if (
//                notDocumented.isEmpty() &&
//                notInDependencies.isEmpty() &&
//                licensesUnMatched.isEmpty() &&
//                duplicatedArtifactIds.isEmpty()
//            ) {
//                project.logger.info("checkLicenses: ok")
//                return@doLast
//            }
//
//            if (notDocumented.isNotEmpty()) {
//                project.logger.warn("# Libraries not listed in ${ext.licensesYaml}:")
//                notDocumented
//                    .distinctBy { it.artifact.toWildcardString() }
//                    .sortedBy { it.artifact.toWildcardString() }
//                    .forEach { libraryInfo ->
//                        val text =
//                            generateLibraryInfoText(
//                                libraryInfo
//                            )
//                        project.logger.warn(text)
//                    }
//            }
//
//            if (notInDependencies.isNotEmpty()) {
//                project.logger.warn("# Libraries listed in ${ext.licensesYaml} but not in dependencies:")
//                notInDependencies
//                    .sortedBy { it.artifact.toWildcardString() }
//                    .forEach { libraryInfo ->
//                        project.logger.warn("- artifact: ${libraryInfo.artifact}\n")
//                    }
//            }
//            if (licensesUnMatched.isNotEmpty()) {
//                project.logger.warn("# Licenses not matched with pom.xml in dependencies:")
//                licensesUnMatched
//                    .sortedBy { it.artifact.toWildcardString() }
//                    .forEach { libraryInfo ->
//                        project.logger.warn("- artifact: ${libraryInfo.artifact}\n  license: ${libraryInfo.license}")
//                    }
//            }
//            if (duplicatedArtifactIds.isNotEmpty()) {
//                project.logger.warn("# Libraries is duplicated listed in ${ext.licensesYaml}:")
//                duplicatedArtifactIds
//                    .sorted()
//                    .forEach { artifactId ->
//                        project.logger.warn("- artifact: $artifactId\n")
//                    }
//            }
//            throw GradleException("checkLicenses: missing libraries in ${ext.licensesYaml}")
//        }.also {
//            it.group = "Verification"
//            it.description = "Check whether dependency licenses are listed in licenses.yml"
//        }
//    }
//
//    @VisibleForTesting
//    fun generateLibraryInfoText(libraryLicense: LibraryLicense): String {
//        val text = StringBuffer()
//        text.append("- artifact: ${libraryLicense.artifact.toWildcardString()}\n")
//        text.append("  name: ${libraryLicense.licenseName ?: "#NAME#"}\n")
//        if (libraryLicense.libraryName?.isNotBlank() == true)
//            text.append("  libraryName: ${libraryLicense.libraryName}\n")
////        if (libraryInfo.copyrightHolder?.isNotBlank() == true)
//        text.append("  copyrightHolder: ${libraryLicense.copyrightHolder ?: "#COPYRIGHT HOLDER#"}\n")
//        text.append("  license: ${libraryLicense.license ?: "#LICENSE#"}\n")
//        if (libraryLicense.licenseUrl?.isNotBlank() == true) {
//            text.append("  licenseUrl: ${libraryLicense.licenseUrl}\n")
//        }
//        if (libraryLicense.url?.isNotBlank() == true) {
//            text.append("  url: ${libraryLicense.url}\n")
//        }
//        return text.toString().trim()
//    }
//
//    @VisibleForTesting
//    fun loadDependencyLicenses(
//        project: Project,
//        resolvedArtifacts: Set<ResolvedArtifact>,
//        ignoredGroups: Set<String>
//    ): List<LibraryLicense> {
//        return resolvedArtifacts
//            .filterNot { it.moduleVersion.id.version == "unspecified" }
//            .filterNot { ignoredGroups.contains(it.moduleVersion.id.group) }
//            .mapNotNull {
//                resolvedArtifactToLibraryInfo(
//                    it,
//                    project
//                )
//            }
//    }
//
//    @VisibleForTesting
//
//
//    @VisibleForTesting
//    fun isConfigForDependencies(name: String): Boolean {
//        return name.matches(dependencyKeywordPattern)
//    }
//
//    @VisibleForTesting
//    fun resolvedArtifactToLibraryInfo(artifact: ResolvedArtifact, project: Project): LibraryLicense? {
//        val dependencyDesc =
//            "${artifact.moduleVersion.id.group}:${artifact.moduleVersion.id.name}:${artifact.moduleVersion.id.version}"
//
//        val artifactItem = Artifact.createArtifact(dependencyDesc)
//
//        val pomDependency = project.dependencies.create("$dependencyDesc@pom")
//        val pomConfiguration = project.configurations.detachedConfiguration(pomDependency)
//        val pomFile: File
//        try {
//            pomConfiguration.resolve().forEach { file ->
//                project.logger.info("POM: $file")
//            }
//            pomFile = pomConfiguration.resolve().toList().first()
//        } catch (e: Exception) {
//            project.logger.warn("Unable to retrieve license for $dependencyDesc")
//            return null
//        }
//
//        LibraryProjectPom.serializer().deserialize(Decoder().)
//
//        val result = pomFile.des.read(LibraryProjectPom::class.java, pomStream)
//        val licenseName = result.licenses.firstOrNull()?.name
//        val licenseUrl = result.licenses.firstOrNull()?.url
//        val libraryName = result.name
//        val libraryUrl = result.url
//        val copyrightHolder = result.organization?.name ?: result.developers.firstOrNull()?.name
//        val year = result.year
//
//
//        return LibraryLicense(
//            artifact = artifactItem,
//            licenseName = artifact.name,
//            libraryName = libraryName,
//            url = libraryUrl,
//            fileName = artifact.file.name,
//            license = licenseName,
//            licenseUrl = licenseUrl,
//            copyrightHolder = copyrightHolder,
//            year = year
//        )
//    }
//
//    private fun resolveProjectDependencies(
//        project: Project,
//        ignoredProjects: Set<String>
//    ): List<ResolvedArtifact> {
//        val projectNamesToSkip = ignoredProjects.toMutableSet()
//        return getProjectDependencies(
//            project,
//            projectNamesToSkip
//        )
//            .distinctBy { it.toDependencyFormattedText() }
//    }
//
//    private fun getProjectDependencies(
//        project: Project?,
//        projectNamesToSkip: MutableSet<String>
//    ): List<ResolvedArtifact> {
//        project ?: return emptyList()
//        if (project.name in projectNamesToSkip)
//            return emptyList()
//        projectNamesToSkip.add(project.name)
//        val subProjects = targetSubProjects(
//            project,
//            projectNamesToSkip
//        ).associateBy {
//            it.toDependecyFormattedText()
//        }
//        return (listOf(project) + subProjects.values)
//            .asSequence()
//            .map { it.configurations }
//            .flatten()
//            .filter {
//                isConfigForDependencies(
//                    it.name
//                )
//            }
//            .map { it.resolvedArtifacts() }
//            .flatten()
//            .distinctBy { it.toDependecyFormattedText() }
//            .toList()
//            .flatMap {
//                val subProject = subProjects[it.toDependecyFormattedText()]
//                listOf(it) + getProjectDependencies(
//                    subProject,
//                    projectNamesToSkip
//                )
//            }
//    }
//
//    private val dependencyKeywordPattern =
//        """^(?!releaseUnitTest)(?:release\w*)?([cC]ompile|[cC]ompileOnly|[iI]mplementation|[aA]pi)$""".toRegex()
//}
