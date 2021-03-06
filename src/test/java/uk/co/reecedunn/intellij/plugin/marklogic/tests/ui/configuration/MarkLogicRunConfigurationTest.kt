/*
 * Copyright (C) 2017 Reece H. Dunn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.co.reecedunn.intellij.plugin.marklogic.tests.ui.configuration

import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.vfs.VirtualFile
import org.hamcrest.CoreMatchers.*
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicServer
import uk.co.reecedunn.intellij.plugin.marklogic.tests.configuration.ConfigurationTestCase

import org.hamcrest.MatcherAssert.assertThat
import org.jdom.Element
import org.jdom.input.SAXBuilder
import org.jdom.output.XMLOutputter
import uk.co.reecedunn.intellij.plugin.marklogic.query.SEM_TRIPLE
import uk.co.reecedunn.intellij.plugin.marklogic.query.TURTLE
import uk.co.reecedunn.intellij.plugin.marklogic.server.MARKLOGIC_9
import uk.co.reecedunn.intellij.plugin.marklogic.ui.configuration.MarkLogicRunConfiguration
import uk.co.reecedunn.intellij.plugin.marklogic.ui.settings.MarkLogicSettings
import java.io.StringReader

class MarkLogicRunConfigurationTest : ConfigurationTestCase() {
    override fun setUp() {
        super.setUp()
        registerApplicationService(MarkLogicSettings::class.java, MarkLogicSettings())
        val servers = ArrayList<MarkLogicServer>()
        servers.add(MarkLogicServer("lorem", "localhost", 9100, 9101, "testuser2", "test2"))
        servers.add(MarkLogicServer("ipsum", "localhost.test", 9000, 9001, "testuser3", "test3"))
        MarkLogicSettings.getInstance().servers = servers
    }

    fun testServer() {
        val serializedCompact = """<test>
            <option name="serverHost" value="localhost.test" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        val serializedFull = """<test>
            <option name="contentDatabase" />
            <option name="mainModulePath" value="" />
            <option name="moduleDatabase" />
            <option name="moduleRoot" value="/" />
            <option name="serverHost" value="localhost.test" />
            <option name="tripleFormat" value="sem:triple" />
            <option name="markLogicVersion" value="7.0" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        var configuration = createConfiguration()
        assertThat(configuration.server, `is`(nullValue()))

        val server = MarkLogicServer(null, "localhost.test", 8000, 8001, "testuser", "test")
        configuration.server = server
        assertThat(configuration.server, `is`(notNullValue()))
        assertThat(configuration.server!!.displayName, `is`(nullValue()))
        assertThat(configuration.server!!.hostname, `is`("localhost.test"))
        assertThat(configuration.server!!.appServerPort, `is`(8000))
        assertThat(configuration.server!!.adminPort, `is`(8001))
        assertThat(configuration.server!!.username, `is`("testuser"))
        assertThat(configuration.server!!.password, `is`("test"))

        assertThat(serialize(configuration), `is`(anyOf(equalTo(serializedCompact), equalTo(serializedFull))))

        configuration = deserialize(serializedCompact)
        assertThat(configuration.server, `is`(notNullValue()))
        assertThat(configuration.server!!.displayName, `is`("ipsum"))
        assertThat(configuration.server!!.hostname, `is`("localhost.test"))
        assertThat(configuration.server!!.appServerPort, `is`(9000))
        assertThat(configuration.server!!.adminPort, `is`(9001))
        assertThat(configuration.server!!.username, `is`("testuser3"))
        assertThat(configuration.server!!.password, `is`("test3"))
    }

    fun testMarkLogicVersion() {
        val serializedCompact = """<test>
            <option name="markLogicVersion" value="9.0" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        val serializedFull = """<test>
            <option name="contentDatabase" />
            <option name="mainModulePath" value="" />
            <option name="moduleDatabase" />
            <option name="moduleRoot" value="/" />
            <option name="serverHost" value="localhost" />
            <option name="tripleFormat" value="sem:triple" />
            <option name="markLogicVersion" value="9.0" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        var configuration = createConfiguration()
        assertThat(configuration.markLogicVersion.major, `is`(7))
        assertThat(configuration.markLogicVersion.minor, `is`(0))
        assertThat(configuration.markLogicVersion.api, `is`(nullValue()))
        assertThat(configuration.markLogicVersion.patch, `is`(nullValue()))

        configuration.markLogicVersion = MARKLOGIC_9
        assertThat(configuration.markLogicVersion.major, `is`(9))
        assertThat(configuration.markLogicVersion.minor, `is`(0))
        assertThat(configuration.markLogicVersion.api, `is`(nullValue()))
        assertThat(configuration.markLogicVersion.patch, `is`(nullValue()))

        assertThat(serialize(configuration), `is`(anyOf(equalTo(serializedCompact), equalTo(serializedFull))))

        configuration = deserialize(serializedCompact)
        assertThat(configuration.markLogicVersion.major, `is`(9))
        assertThat(configuration.markLogicVersion.minor, `is`(0))
        assertThat(configuration.markLogicVersion.api, `is`(nullValue()))
        assertThat(configuration.markLogicVersion.patch, `is`(nullValue()))
    }

    fun testContentDatabase() {
        val serializedCompact = """<test>
            <option name="contentDatabase" value="test-content" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        val serializedFull = """<test>
            <option name="contentDatabase" value="test-content" />
            <option name="mainModulePath" value="" />
            <option name="moduleDatabase" />
            <option name="moduleRoot" value="/" />
            <option name="serverHost" value="localhost" />
            <option name="tripleFormat" value="sem:triple" />
            <option name="markLogicVersion" value="7.0" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        var configuration = createConfiguration()
        assertThat(configuration.contentDatabase, `is`(nullValue()))

        configuration.contentDatabase = "test-content"
        assertThat(configuration.contentDatabase, `is`("test-content"))

        assertThat(serialize(configuration), `is`(anyOf(equalTo(serializedCompact), equalTo(serializedFull))))

        configuration = deserialize(serializedCompact)
        assertThat(configuration.contentDatabase, `is`("test-content"))
    }

    fun testModuleDatabase() {
        val serializedCompact = """<test>
            <option name="moduleDatabase" value="test-modules" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        val serializedFull = """<test>
            <option name="contentDatabase" />
            <option name="mainModulePath" value="" />
            <option name="moduleDatabase" value="test-modules" />
            <option name="moduleRoot" value="/" />
            <option name="serverHost" value="localhost" />
            <option name="tripleFormat" value="sem:triple" />
            <option name="markLogicVersion" value="7.0" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        var configuration = createConfiguration()
        assertThat(configuration.moduleDatabase, `is`(nullValue()))

        configuration.moduleDatabase = "test-modules"
        assertThat(configuration.moduleDatabase, `is`("test-modules"))

        assertThat(serialize(configuration), `is`(anyOf(equalTo(serializedCompact), equalTo(serializedFull))))

        configuration = deserialize(serializedCompact)
        assertThat(configuration.moduleDatabase, `is`("test-modules"))
    }

    fun testModuleRoot() {
        val serializedCompact = """<test>
            <option name="moduleRoot" value="/lorem/ipsum" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        val serializedFull = """<test>
            <option name="contentDatabase" />
            <option name="mainModulePath" value="" />
            <option name="moduleDatabase" />
            <option name="moduleRoot" value="/lorem/ipsum" />
            <option name="serverHost" value="localhost" />
            <option name="tripleFormat" value="sem:triple" />
            <option name="markLogicVersion" value="7.0" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        var configuration = createConfiguration()
        assertThat(configuration.moduleRoot, `is`("/"))

        configuration.moduleRoot = "/lorem/ipsum"
        assertThat(configuration.moduleRoot, `is`("/lorem/ipsum"))

        assertThat(serialize(configuration), `is`(anyOf(equalTo(serializedCompact), equalTo(serializedFull))))

        configuration = deserialize(serializedCompact)
        assertThat(configuration.moduleRoot, `is`("/lorem/ipsum"))
    }

    fun testMainModulePath() {
        val serializedCompact = """<test>
            <option name="mainModulePath" value="module/test.xqy" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        val serializedFull = """<test>
            <option name="contentDatabase" />
            <option name="mainModulePath" value="module/test.xqy" />
            <option name="moduleDatabase" />
            <option name="moduleRoot" value="/" />
            <option name="serverHost" value="localhost" />
            <option name="tripleFormat" value="sem:triple" />
            <option name="markLogicVersion" value="7.0" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        var configuration = createConfiguration()
        assertThat(configuration.mainModulePath, `is`(""))
        assertThat<VirtualFile>(configuration.mainModuleFile, `is`(nullValue()))

        configuration.mainModulePath = "module/test.xqy"
        assertThat(configuration.mainModulePath, `is`("module/test.xqy"))
        assertThat<VirtualFile>(configuration.mainModuleFile, `is`(notNullValue()))
        assertThat<String>(configuration.mainModuleFile!!.canonicalPath, `is`("/module/test.xqy"))

        assertThat(serialize(configuration), `is`(anyOf(equalTo(serializedCompact), equalTo(serializedFull))))

        configuration = deserialize(serializedCompact)
        assertThat(configuration.mainModulePath, `is`("module/test.xqy"))
        assertThat<VirtualFile>(configuration.mainModuleFile, `is`(notNullValue()))
        assertThat<String>(configuration.mainModuleFile!!.canonicalPath, `is`("/module/test.xqy"))
    }

    fun testMainModuleFile() {
        val serializedCompact = """<test>
            <option name="mainModulePath" value="/module/test.xqy" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        val serializedFull = """<test>
            <option name="contentDatabase" />
            <option name="mainModulePath" value="/module/test.xqy" />
            <option name="moduleDatabase" />
            <option name="moduleRoot" value="/" />
            <option name="serverHost" value="localhost" />
            <option name="tripleFormat" value="sem:triple" />
            <option name="markLogicVersion" value="7.0" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        var configuration = createConfiguration()
        assertThat(configuration.mainModulePath, `is`(""))
        assertThat<VirtualFile>(configuration.mainModuleFile, `is`(nullValue()))

        configuration.mainModuleFile = createVirtualFile("module/test.xqy", "\"Lorem ipsum\"")
        assertThat(configuration.mainModulePath, `is`("/module/test.xqy"))
        assertThat<VirtualFile>(configuration.mainModuleFile, `is`(notNullValue()))
        assertThat<String>(configuration.mainModuleFile!!.canonicalPath, `is`("/module/test.xqy"))

        assertThat(serialize(configuration), `is`(anyOf(equalTo(serializedCompact), equalTo(serializedFull))))

        configuration = deserialize(serializedCompact)
        assertThat(configuration.mainModulePath, `is`("/module/test.xqy"))
        assertThat<VirtualFile>(configuration.mainModuleFile, `is`(notNullValue()))
        assertThat<String>(configuration.mainModuleFile!!.canonicalPath, `is`("/module/test.xqy"))
    }

    fun testTripleFormat() {
        val serializedCompact = """<test>
            <option name="tripleFormat" value="turtle" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        val serializedFull = """<test>
            <option name="contentDatabase" />
            <option name="mainModulePath" value="" />
            <option name="moduleDatabase" />
            <option name="moduleRoot" value="/" />
            <option name="serverHost" value="localhost" />
            <option name="tripleFormat" value="turtle" />
            <option name="markLogicVersion" value="7.0" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        var configuration = createConfiguration()
        assertThat(configuration.tripleFormat, `is`(SEM_TRIPLE))

        configuration.tripleFormat = TURTLE
        assertThat(configuration.tripleFormat, `is`(TURTLE))

        assertThat(serialize(configuration), `is`(anyOf(equalTo(serializedCompact), equalTo(serializedFull))))

        configuration = deserialize(serializedCompact)
        assertThat(configuration.tripleFormat, `is`(TURTLE))
    }

    // region Serialization Helpers

    private fun serialize(configuration: RunConfiguration): String {
        val element = Element("test")
        serializeStateInto(configuration, element)
        return XMLOutputter().outputString(element)
    }

    private fun deserialize(xml: String): MarkLogicRunConfiguration {
        val document = SAXBuilder().build(StringReader(xml))
        val ret = createConfiguration()
        deserializeAndLoadState(ret, document.rootElement)
        return ret
    }

    // endregion
}
