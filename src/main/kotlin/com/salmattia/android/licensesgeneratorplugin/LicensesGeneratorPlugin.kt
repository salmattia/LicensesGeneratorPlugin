package com.salmattia.android.licensesgeneratorplugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.api.ApplicationVariant
import com.salmattia.android.licensesgeneratorplugin.task.GenerateLicensesJson
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project

class LicensesGeneratorPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create(
            LicensesGeneratorPluginExtension.NAME,
            LicensesGeneratorPluginExtension::class.java
        )

        var variants: DomainObjectSet<ApplicationVariant>? = null
        if (project.plugins.hasPlugin("com.android.application")) {
            val androidAppPlugin = project.extensions.findByType(AppExtension::class.java)
            variants = androidAppPlugin?.applicationVariants
        }

        variants?.forEach {
            GenerateLicensesJson.createTask(project, it)
        }
    }
}
