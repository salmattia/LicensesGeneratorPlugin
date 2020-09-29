package com.salmattia.android.licensesgeneratorplugin.data

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = "project")
class LibraryPomData {

    @get:ElementList(name = "licenses", required = false)
    var licenses: ArrayList<License> = ArrayList()

    @get:ElementList(name = "developers", required = false)
    var developers: ArrayList<Developer> = ArrayList()

    @get:ElementList(name = "organization", required = false)
    var organization: Organization? = null

    @get:Element(name = "name", required = false)
    var name: String = ""

    @get:Element(name = "version", required = false)
    var version: String = ""

    @get:Element(name = "url", required = false)
    var url: String = ""

    @get:Element(name = "inceptionYear", required = false)
    var year: String = ""

    @Root(strict = false)
    class License {
        var name: String? = ""
        var url: String? = ""
    }

    @Root(strict = false)
    class Organization {
        var name: String? = ""
        var url: String? = ""
    }

    @Root(strict = false)
    class Developer {
        var name: String? = ""
    }
}
