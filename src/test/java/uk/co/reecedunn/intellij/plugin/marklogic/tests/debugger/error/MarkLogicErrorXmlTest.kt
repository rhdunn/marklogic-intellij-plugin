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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.debugger.error

import junit.framework.TestCase

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import uk.co.reecedunn.intellij.plugin.marklogic.debugger.error.MarkLogicErrorXml
import uk.co.reecedunn.intellij.plugin.marklogic.tests.TestResource

class MarkLogicErrorXmlTest : TestCase() {
    fun testErrorDetails_NoData() {
        val xml = TestResource("debugger/error/eval-divide-by-zero.xml").toString()
        val error = MarkLogicErrorXml(xml)

        assertThat(error.code, `is`("XDMP-DIVBYZERO"))
        assertThat(error.name, `is`("err:FOAR0001"))
        assertThat(error.XQueryVersion, `is`("1.0-ml"))
        assertThat(error.message, `is`("Division by zero"))
        assertThat(error.retryable, `is`(false))
        assertThat(error.expr, `is`("2 div 0"))
    }
}
