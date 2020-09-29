package com.salmattia.android.licensesgeneratorplugin.extension

import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.internal.impldep.com.google.common.annotations.VisibleForTesting

fun ResolvedArtifact.toDependencyFormattedText(): String {
    return "${moduleVersion.id.group}:${moduleVersion.id.name}:${moduleVersion.id.version}"
}
