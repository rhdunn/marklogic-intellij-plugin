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

import com.intellij.openapi.vfs.VirtualFile
import uk.co.reecedunn.intellij.plugin.marklogic.api.RDFFormat
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicServer
import uk.co.reecedunn.intellij.plugin.marklogic.tests.configuration.ConfigurationTestCase

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat

class MarkLogicRunConfigurationTest : ConfigurationTestCase() {
    fun testServer() {
        val configuration = createConfiguration()
        assertThat(configuration.server, `is`(nullValue()))

        val server = MarkLogicServer(null, "localhost", 8000, 8001, "testuser", "test")
        configuration.server = server
        assertThat(configuration.server, `is`(server))
    }

    fun testMarkLogicVersion() {
        val configuration = createConfiguration()
        assertThat(configuration.markLogicVersion, `is`(7.0))

        configuration.markLogicVersion = 9.0
        assertThat(configuration.markLogicVersion, `is`(9.0))
    }

    fun testContentDatabase() {
        val configuration = createConfiguration()
        assertThat(configuration.contentDatabase, `is`(nullValue()))

        configuration.contentDatabase = "test-content"
        assertThat(configuration.contentDatabase, `is`("test-content"))
    }

    fun testModuleDatabase() {
        val configuration = createConfiguration()
        assertThat(configuration.moduleDatabase, `is`(nullValue()))

        configuration.moduleDatabase = "test-modules"
        assertThat(configuration.moduleDatabase, `is`("test-modules"))
    }

    fun testModuleRoot() {
        val configuration = createConfiguration()
        assertThat(configuration.moduleRoot, `is`("/"))

        configuration.moduleRoot = "/lorem/ipsum"
        assertThat(configuration.moduleRoot, `is`("/lorem/ipsum"))
    }

    fun testMainModulePath() {
        val configuration = createConfiguration()
        assertThat(configuration.mainModulePath, `is`(""))
        assertThat<VirtualFile>(configuration.mainModuleFile, `is`(nullValue()))

        configuration.mainModulePath = "module/test.xqy"
        assertThat(configuration.mainModulePath, `is`("module/test.xqy"))
        assertThat<VirtualFile>(configuration.mainModuleFile, `is`(notNullValue()))
        assertThat<String>(configuration.mainModuleFile.canonicalPath, `is`("/module/test.xqy"))
    }

    fun testMainModuleFile() {
        val configuration = createConfiguration()
        assertThat(configuration.mainModulePath, `is`(""))
        assertThat<VirtualFile>(configuration.mainModuleFile, `is`(nullValue()))

        configuration.mainModuleFile = createVirtualFile("module/test.xqy", "\"Lorem ipsum\"")
        assertThat(configuration.mainModulePath, `is`("/module/test.xqy"))
        assertThat<VirtualFile>(configuration.mainModuleFile, `is`(notNullValue()))
        assertThat<String>(configuration.mainModuleFile.canonicalPath, `is`("/module/test.xqy"))
    }

    fun testTripleFormat() {
        val configuration = createConfiguration()
        assertThat(configuration.tripleFormat, `is`(RDFFormat.SEM_TRIPLE))

        configuration.tripleFormat = RDFFormat.TURTLE
        assertThat(configuration.tripleFormat, `is`(RDFFormat.TURTLE))
    }
}
