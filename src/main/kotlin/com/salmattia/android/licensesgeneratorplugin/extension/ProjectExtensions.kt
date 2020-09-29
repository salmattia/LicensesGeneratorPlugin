package com.salmattia.android.licensesgeneratorplugin.extension

import com.salmattia.android.licensesgeneratorplugin.LicensesGeneratorPluginExtension
import org.gradle.api.Project

fun Project.toDependencyFormattedText(): String {
    return "$group:$name:$version"
}


fun Project.writeLicensesJsonFile(json: String, flavorName: String?) {
    val ext = extensions.getByType(LicensesGeneratorPluginExtension::class.java)
    val assetsDir = file("src/${flavorName ?: "main"}/assets")
    assetsDir.mkdirs()
    file("$assetsDir/${ext.output}").writeText(json)
}
