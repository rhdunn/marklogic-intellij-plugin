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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.query.options

import junit.framework.TestCase
import uk.co.reecedunn.intellij.plugin.marklogic.query.options.EvalOptionsBuilder

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat

class EvalOptionsBuilderTest : TestCase() {
    fun testNoOptions() {
        val builder = EvalOptionsBuilder

        builder.reset()

        val expected =
            "<options xmlns=\"xdmp:eval\">" +
            "<modules>0</modules>" +
            "</options>"
        assertThat(builder.build(), `is`(expected))
    }

    fun testContentDatabaseOption() {
        val builder = EvalOptionsBuilder

        builder.reset()
        builder.contentDatabase = "lorem"

        val expected =
            "<options xmlns=\"xdmp:eval\">" +
            "<database>{xdmp:database(\"lorem\")}</database>" +
            "<modules>0</modules>" +
            "</options>"
        assertThat(builder.build(), `is`(expected))
    }

    fun testModulesDatabaseOption() {
        val builder = EvalOptionsBuilder

        builder.reset()
        builder.modulesDatabase = "lorem"

        val expected =
            "<options xmlns=\"xdmp:eval\">" +
            "<modules>{xdmp:database(\"lorem\")}</modules>" +
            "</options>"
        assertThat(builder.build(), `is`(expected))
    }

    fun testModulesRootOption() {
        val builder = EvalOptionsBuilder

        builder.reset()
        builder.modulesRoot = "lorem"

        val expected =
            "<options xmlns=\"xdmp:eval\">" +
            "<modules>0</modules>" +
            "<root>lorem</root>" +
            "</options>"
        assertThat(builder.build(), `is`(expected))
    }

    fun testResetOptions() {
        val builder = EvalOptionsBuilder

        builder.reset()
        builder.contentDatabase = "lorem"
        builder.modulesDatabase = "ipsum"
        builder.modulesRoot = "/dolor"
        builder.reset()

        val expected =
            "<options xmlns=\"xdmp:eval\">" +
            "<modules>0</modules>" +
            "</options>"
        assertThat(builder.build(), `is`(expected))
    }
}
