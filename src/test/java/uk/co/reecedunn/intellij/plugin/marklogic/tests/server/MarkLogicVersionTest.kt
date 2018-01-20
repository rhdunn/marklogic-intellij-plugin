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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.server

import junit.framework.TestCase
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersion
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersionFormatException

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Assertions.assertThrows

@Suppress("JoinDeclarationAndAssignment")
class MarkLogicVersionTest : TestCase() {
    fun testApiWithPatch() {
        val version = MarkLogicVersion.parse("8.0-6.3")
        assertThat(version.major, `is`(8))
        assertThat(version.minor, `is`(0))
        assertThat<Int>(version.api, `is`(6))
        assertThat<Int>(version.patch, `is`(3))

        assertThat(version.toString(), `is`("8.0-6.3"))
    }

    fun testApiWithoutPatch() {
        val version = MarkLogicVersion.parse("9.0-2")
        assertThat(version.major, `is`(9))
        assertThat(version.minor, `is`(0))
        assertThat<Int>(version.api, `is`(2))
        assertThat<Int>(version.patch, `is`(nullValue()))

        assertThat(version.toString(), `is`("9.0-2"))
    }

    fun testSimpleVersion() {
        val version = MarkLogicVersion.parse("4.2")
        assertThat(version.major, `is`(4))
        assertThat(version.minor, `is`(2))
        assertThat<Int>(version.api, `is`(nullValue()))
        assertThat<Int>(version.patch, `is`(nullValue()))

        assertThat(version.toString(), `is`("4.2"))
    }

    fun testPartIsNotANumber() {
        val e = assertThrows(MarkLogicVersionFormatException::class.java) { MarkLogicVersion.parse("a.b-c.d") }
        assertThat<String>(e.message, `is`("Invalid MarkLogic version: a.b-c.d"))
    }

    fun testWrongNumberOfParts() {
        var e: MarkLogicVersionFormatException

        e = assertThrows(MarkLogicVersionFormatException::class.java) { MarkLogicVersion.parse("6") }
        assertThat<String>(e.message, `is`("Invalid MarkLogic version: 6"))

        e = assertThrows(MarkLogicVersionFormatException::class.java) { MarkLogicVersion.parse("7.0-5.6.7") }
        assertThat<String>(e.message, `is`("Invalid MarkLogic version: 7.0-5.6.7"))
    }
}
