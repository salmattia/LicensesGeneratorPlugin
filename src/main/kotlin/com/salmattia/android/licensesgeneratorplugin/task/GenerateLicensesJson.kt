package com.salmattia.android.licensesgeneratorplugin.task

import com.android.build.gradle.api.ApplicationVariant
import com.google.gson.Gson
import com.salmattia.android.licensesgeneratorplugin.data.ArtifactDescriptor
import com.salmattia.android.licensesgeneratorplugin.data.LibraryLicense
import com.salmattia.android.licensesgeneratorplugin.data.LibraryPomData
import com.salmattia.android.licensesgeneratorplugin.extension.resolvedArtifacts
import com.salmattia.android.licensesgeneratorplugin.extension.toDependencyFormattedText
import com.salmattia.android.licensesgeneratorplugin.extension.writeLicensesJsonFile
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.ResolvedArtifact
import org.simpleframework.xml.core.Persister
import java.io.File

object GenerateLicensesJson {
    var dependencyRegex: Regex = Regex("")

    fun createTask(project: Project, applicationVariant: ApplicationVariant): Task {
        //TODO intercept build and inject this task
        return project.task("generate${applicationVariant.flavorName.capitalize()}LicensesJson").doLast {
            //TODO improve this pattern
            dependencyRegex = Regex(
                "^(${applicationVariant.flavorName})?(${applicationVariant.buildType})?(implementation|api)$",
                RegexOption.IGNORE_CASE
            )

            val licenses = resolveProjectDependencies(project)
                .map { convertToLicense(project, it) }
                .toList()

            val licensesJsonString = Gson().toJson(licenses)
            project.writeLicensesJsonFile(licensesJsonString, applicationVariant.flavorName)

        }.also {
            it.group = "Verification"
            it.description = "Generates licenses json file"
        }
    }

    private fun convertToLicense(project: Project, artifact: ResolvedArtifact): LibraryLicense? {
        val dependencyFormattedText =
            "${artifact.moduleVersion.id.group}:${artifact.moduleVersion.id.name}:${artifact.moduleVersion.id.version}"

        ArtifactDescriptor.createArtifact(dependencyFormattedText)?.let {
            val pomDependency = project.dependencies.create("$dependencyFormattedText@pom")
            val pomConfiguration = project.configurations.detachedConfiguration(pomDependency)
            val pomFile: File
            try {
                pomFile = pomConfiguration.resolve().toList().first()
            } catch (e: Exception) {
                project.logger.warn("Cannot get license from $dependencyFormattedText")
                return null
            }

            val serializer = Persister()
            val result = serializer.read(LibraryPomData::class.java, pomFile)

            val licenseName = result.licenses.firstOrNull()?.name
            val licenseUrl = result.licenses.firstOrNull()?.url
            val libraryName = result.name
            val libraryUrl = result.url
            val copyrightHolder = result.organization?.name ?: result.developers.firstOrNull()?.name
            val year = result.year

            return LibraryLicense(
                artifactDescriptor = it,
                licenseName = artifact.name,
                libraryName = libraryName,
                url = libraryUrl,
                fileName = artifact.file.name,
                license = licenseName,
                licenseUrl = licenseUrl,
                copyright = copyrightHolder,
                year = year
            )
        } ?: return null
    }

    private fun resolveProjectDependencies(project: Project?): Sequence<ResolvedArtifact> {
        return getProjectDependencies(project)
            .distinctBy { it.toDependencyFormattedText() }
    }

    private fun getProjectDependencies(project: Project?): Sequence<ResolvedArtifact> {
        project ?: return emptySequence()
        val subProjects = project.rootProject.subprojects
            .filterNot { it.subprojects.isNotEmpty() }
            .associateBy { it.toDependencyFormattedText() }
        return (sequenceOf(project) + subProjects.values)
            .map { it.configurations }
            .flatten()
            .filter { it.name.matches(dependencyRegex) }
            .map { it.resolvedArtifacts() }
            .flatten()
            .distinctBy { it.toDependencyFormattedText() }
            .flatMap {
                val subProject = subProjects[it.toDependencyFormattedText()]
                sequenceOf(it) + getProjectDependencies(subProject)
            }
    }
}