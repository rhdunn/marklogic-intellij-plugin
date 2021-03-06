/*
 * Copyright (C) 2017-2018 Reece H. Dunn
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
import uk.co.reecedunn.intellij.plugin.marklogic.debugger.stack.impl.MarkLogicErrorXml
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

        assertThat(error.data.count(), `is`(0))
    }

    fun testErrorDetails_Data() {
        val xml = TestResource("debugger/error/cast-as.xml").toString()
        val error = MarkLogicErrorXml(xml)

        assertThat(error.code, `is`("XDMP-CAST"))
        assertThat(error.name, `is`("err:FORG0001"))
        assertThat(error.XQueryVersion, `is`("1.0-ml"))
        assertThat(error.message, `is`("Invalid cast"))
        assertThat(error.retryable, `is`(false))
        assertThat(error.expr, `is`("() cast as xs:boolean"))

        assertThat(error.data.count(), `is`(2))
        assertThat(error.data.first(), `is`("()"))
        assertThat(error.data.last(), `is`("xs:boolean"))
    }

    fun testStackFrames() {
        val xml = TestResource("debugger/error/eval-divide-by-zero.xml").toString()
        val frames = MarkLogicErrorXml(xml).frames.toList()
        assertThat(frames.size, `is`(2))

        assertThat(frames[0].uri, `is`(nullValue()))
        assertThat(frames[0].line, `is`(1))
        assertThat(frames[0].column, `is`(2))
        assertThat(frames[0].operation, `is`("xdmp:eval(\"2 div 0\", (), <options xmlns=\"xdmp:eval\"><database>1598436954797797328</database>...</options>)"))
        assertThat(frames[0].XQueryVersion, `is`("1.0-ml"))

        assertThat(frames[1].uri, `is`("/eval"))
        assertThat(frames[1].line, `is`(6))
        assertThat(frames[1].column, `is`(18))
        assertThat(frames[1].operation, `is`(nullValue()))
        assertThat(frames[1].XQueryVersion, `is`("1.0-ml"))
    }

    fun testVariables_NCName() {
        val xml = TestResource("debugger/error/eval-divide-by-zero.xml").toString()
        val frames = MarkLogicErrorXml(xml).frames.toList()
        assertThat(frames.size, `is`(2))

        val variables = frames[1].variables.toList()
        assertThat(variables.size, `is`(3))

        assertThat(variables[0].namespace, `is`(nullValue()))
        assertThat(variables[0].localName, `is`("query"))
        assertThat(variables[0].value, `is`("\"2 div 0\""))

        assertThat(variables[1].namespace, `is`(nullValue()))
        assertThat(variables[1].localName, `is`("vars"))
        assertThat(variables[1].value, `is`("()"))

        assertThat(variables[2].namespace, `is`(nullValue()))
        assertThat(variables[2].localName, `is`("options"))
        assertThat(variables[2].value, `is`("<options xmlns=\"xdmp:eval\"><database>1598436954797797328</database>...</options>"))
    }

    fun testVariables_QName() {
        val xml = TestResource("debugger/error/cast-as-var-qname.xml").toString()
        val frames = MarkLogicErrorXml(xml).frames.toList()
        assertThat(frames.size, `is`(2))

        val variables = frames[0].variables.toList()
        assertThat(variables.size, `is`(1))

        assertThat(variables[0].namespace, `is`("http://www.w3.org/2005/xquery-local-functions"))
        assertThat(variables[0].localName, `is`("x"))
        assertThat(variables[0].value, `is`("()"))
    }
}
