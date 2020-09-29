package com.salmattia.android.licensesgeneratorplugin.data

data class ArtifactDescriptor(
    val group: String,
    val name: String,
    val version: String
) {

    override fun hashCode(): Int {
        return arrayOf(group, name, version).contentHashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is ArtifactDescriptor &&
                equalsWildcard(group, other.group) &&
                equalsWildcard(name, other.name) &&
                equalsWildcard(version, other.version)

    }

    private fun equalsWildcard(artifactProp1: String, artifactProp2: String): Boolean {
        return artifactProp1 == "+" || artifactProp2 == "+" || artifactProp1 == artifactProp2
    }

    fun toWildcardString(): String {
        return "$group:$name:+"
    }

    override fun toString(): String {
        return "$group:$name:$version"
    }

    companion object {
        fun createArtifact(artifactDescriptor: String): ArtifactDescriptor? {
            val artifactProperties =
                artifactDescriptor.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toMutableList()
            if (artifactProperties.size == 3) {
                return ArtifactDescriptor(
                    artifactProperties[0],
                    artifactProperties[1],
                    artifactProperties[2]
                )
            }
            return null
        }
    }
}
