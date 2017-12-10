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

import com.intellij.configurationStore.deserializeAndLoadState
import com.intellij.configurationStore.serializeStateInto
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.vfs.VirtualFile
import uk.co.reecedunn.intellij.plugin.marklogic.api.RDFFormat
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicServer
import uk.co.reecedunn.intellij.plugin.marklogic.tests.configuration.ConfigurationTestCase

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.jdom.Element
import org.jdom.input.SAXBuilder
import org.jdom.output.XMLOutputter
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
        val serialized = """<test>
            <option name="serverHost" value="localhost" />
            <option name="markLogicVersion" value="7.0" />
            <option name="contentDatabase" />
            <option name="moduleDatabase" />
            <option name="moduleRoot" value="/" />
            <option name="mainModulePath" value="" />
            <option name="tripleFormat" value="sem:triple" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        val configuration = createConfiguration()
        assertThat(configuration.server, `is`(nullValue()))

        val server = MarkLogicServer(null, "localhost", 8000, 8001, "testuser", "test")
        configuration.server = server
        assertThat(configuration.server, `is`(server))

        assertThat(serialize(configuration), `is`(serialized))

        val other = deserialize(serialized)
        assertThat(other.server.hostname, `is`("localhost"))
    }

    fun testMarkLogicVersion() {
        val serialized = """<test>
            <option name="serverHost" value="localhost" />
            <option name="markLogicVersion" value="9.0" />
            <option name="contentDatabase" />
            <option name="moduleDatabase" />
            <option name="moduleRoot" value="/" />
            <option name="mainModulePath" value="" />
            <option name="tripleFormat" value="sem:triple" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        val configuration = createConfiguration()
        assertThat(configuration.markLogicVersion, `is`(7.0))

        configuration.markLogicVersion = 9.0
        assertThat(configuration.markLogicVersion, `is`(9.0))

        assertThat(serialize(configuration), `is`(serialized))

        val other = deserialize(serialized)
        assertThat(other.markLogicVersion, `is`(9.0))
    }

    fun testContentDatabase() {
        val serialized = """<test>
            <option name="serverHost" value="localhost" />
            <option name="markLogicVersion" value="7.0" />
            <option name="contentDatabase" value="test-content" />
            <option name="moduleDatabase" />
            <option name="moduleRoot" value="/" />
            <option name="mainModulePath" value="" />
            <option name="tripleFormat" value="sem:triple" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        val configuration = createConfiguration()
        assertThat(configuration.contentDatabase, `is`(nullValue()))

        configuration.contentDatabase = "test-content"
        assertThat(configuration.contentDatabase, `is`("test-content"))

        assertThat(serialize(configuration), `is`(serialized))

        val other = deserialize(serialized)
        assertThat(other.contentDatabase, `is`("test-content"))
    }

    fun testModuleDatabase() {
        val serialized = """<test>
            <option name="serverHost" value="localhost" />
            <option name="markLogicVersion" value="7.0" />
            <option name="contentDatabase" />
            <option name="moduleDatabase" value="test-modules" />
            <option name="moduleRoot" value="/" />
            <option name="mainModulePath" value="" />
            <option name="tripleFormat" value="sem:triple" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        val configuration = createConfiguration()
        assertThat(configuration.moduleDatabase, `is`(nullValue()))

        configuration.moduleDatabase = "test-modules"
        assertThat(configuration.moduleDatabase, `is`("test-modules"))

        assertThat(serialize(configuration), `is`(serialized))

        val other = deserialize(serialized)
        assertThat(other.moduleDatabase, `is`("test-modules"))
    }

    fun testModuleRoot() {
        val serialized = """<test>
            <option name="serverHost" value="localhost" />
            <option name="markLogicVersion" value="7.0" />
            <option name="contentDatabase" />
            <option name="moduleDatabase" />
            <option name="moduleRoot" value="/lorem/ipsum" />
            <option name="mainModulePath" value="" />
            <option name="tripleFormat" value="sem:triple" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        val configuration = createConfiguration()
        assertThat(configuration.moduleRoot, `is`("/"))

        configuration.moduleRoot = "/lorem/ipsum"
        assertThat(configuration.moduleRoot, `is`("/lorem/ipsum"))

        assertThat(serialize(configuration), `is`(serialized))

        val other = deserialize(serialized)
        assertThat(other.moduleRoot, `is`("/lorem/ipsum"))
    }

    fun testMainModulePath() {
        val serialized = """<test>
            <option name="serverHost" value="localhost" />
            <option name="markLogicVersion" value="7.0" />
            <option name="contentDatabase" />
            <option name="moduleDatabase" />
            <option name="moduleRoot" value="/" />
            <option name="mainModulePath" value="module/test.xqy" />
            <option name="tripleFormat" value="sem:triple" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        val configuration = createConfiguration()
        assertThat(configuration.mainModulePath, `is`(""))
        assertThat<VirtualFile>(configuration.mainModuleFile, `is`(nullValue()))

        configuration.mainModulePath = "module/test.xqy"
        assertThat(configuration.mainModulePath, `is`("module/test.xqy"))
        assertThat<VirtualFile>(configuration.mainModuleFile, `is`(notNullValue()))
        assertThat<String>(configuration.mainModuleFile.canonicalPath, `is`("/module/test.xqy"))

        assertThat(serialize(configuration), `is`(serialized))

        val other = deserialize(serialized)
        assertThat(other.mainModulePath, `is`("module/test.xqy"))
        assertThat<VirtualFile>(other.mainModuleFile, `is`(notNullValue()))
        assertThat<String>(other.mainModuleFile.canonicalPath, `is`("/module/test.xqy"))
    }

    fun testMainModuleFile() {
        val serialized = """<test>
            <option name="serverHost" value="localhost" />
            <option name="markLogicVersion" value="7.0" />
            <option name="contentDatabase" />
            <option name="moduleDatabase" />
            <option name="moduleRoot" value="/" />
            <option name="mainModulePath" value="/module/test.xqy" />
            <option name="tripleFormat" value="sem:triple" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        val configuration = createConfiguration()
        assertThat(configuration.mainModulePath, `is`(""))
        assertThat<VirtualFile>(configuration.mainModuleFile, `is`(nullValue()))

        configuration.mainModuleFile = createVirtualFile("module/test.xqy", "\"Lorem ipsum\"")
        assertThat(configuration.mainModulePath, `is`("/module/test.xqy"))
        assertThat<VirtualFile>(configuration.mainModuleFile, `is`(notNullValue()))
        assertThat<String>(configuration.mainModuleFile.canonicalPath, `is`("/module/test.xqy"))

        assertThat(serialize(configuration), `is`(serialized))

        val other = deserialize(serialized)
        assertThat(other.mainModulePath, `is`("/module/test.xqy"))
        assertThat<VirtualFile>(other.mainModuleFile, `is`(notNullValue()))
        assertThat<String>(other.mainModuleFile.canonicalPath, `is`("/module/test.xqy"))
    }

    fun testTripleFormat() {
        val serialized = """<test>
            <option name="serverHost" value="localhost" />
            <option name="markLogicVersion" value="7.0" />
            <option name="contentDatabase" />
            <option name="moduleDatabase" />
            <option name="moduleRoot" value="/" />
            <option name="mainModulePath" value="" />
            <option name="tripleFormat" value="turtle" />
        </test>""".replace("\n[ ]*".toRegex(), "")

        val configuration = createConfiguration()
        assertThat(configuration.tripleFormat, `is`(RDFFormat.SEM_TRIPLE))

        configuration.tripleFormat = RDFFormat.TURTLE
        assertThat(configuration.tripleFormat, `is`(RDFFormat.TURTLE))

        assertThat(serialize(configuration), `is`(serialized))

        val other = deserialize(serialized)
        assertThat(other.tripleFormat, `is`(RDFFormat.TURTLE))
    }

    // region Serialization Helpers

    private fun serialize(configuration: RunConfiguration): String {
        val element = Element("test")
        if (configuration is PersistentStateComponent<*>) {
            configuration.serializeStateInto(element)
        } else {
            configuration.writeExternal(element)
        }
        return XMLOutputter().outputString(element)
    }

    private fun deserialize(xml: String): MarkLogicRunConfiguration {
        val document = SAXBuilder().build(StringReader(xml))

        val ret = createConfiguration()
        if (ret is PersistentStateComponent<*>) {
            ret.deserializeAndLoadState(document.rootElement)
        } else {
            ret.readExternal(document.rootElement)
        }
        return ret
    }

    // endregion
}
