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
import uk.co.reecedunn.intellij.plugin.marklogic.debugger.stack.impl.MarkLogicErrorXmlFrame
import uk.co.reecedunn.intellij.plugin.marklogic.debugger.stack.impl.MarkLogicErrorXmlVariable
import uk.co.reecedunn.intellij.plugin.marklogic.tests.TestResource
import uk.co.reecedunn.intellij.plugin.marklogic.tests.debugger.CompositeNode
import uk.co.reecedunn.intellij.plugin.marklogic.tests.debugger.StackFrameContainer

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

    fun testComputeStackFrames() {
        val xml = TestResource("debugger/error/eval-divide-by-zero.xml").toString()
        val frames = StackFrameContainer()
        MarkLogicErrorXml(xml).computeStackFrames(0, frames)
        assertThat(frames.frames.size, `is`(2))

        val frame1 = frames.frames[0] as MarkLogicErrorXmlFrame
        assertThat(frame1.uri, `is`(nullValue()))
        assertThat(frame1.line, `is`(1))
        assertThat(frame1.column, `is`(2))
        assertThat(frame1.operation, `is`("xdmp:eval(\"2 div 0\", (), <options xmlns=\"xdmp:eval\"><database>1598436954797797328</database>...</options>)"))
        assertThat(frame1.XQueryVersion, `is`("1.0-ml"))

        val frame2 = frames.frames[1] as MarkLogicErrorXmlFrame
        assertThat(frame2.uri, `is`("/eval"))
        assertThat(frame2.line, `is`(6))
        assertThat(frame2.column, `is`(18))
        assertThat(frame2.operation, `is`(nullValue()))
        assertThat(frame2.XQueryVersion, `is`("1.0-ml"))
    }

    fun testTopFrame() {
        val xml = TestResource("debugger/error/eval-divide-by-zero.xml").toString()
        val frame = (MarkLogicErrorXml(xml).topFrame as? MarkLogicErrorXmlFrame)!!

        assertThat(frame.uri, `is`(nullValue()))
        assertThat(frame.line, `is`(1))
        assertThat(frame.column, `is`(2))
        assertThat(frame.operation, `is`("xdmp:eval(\"2 div 0\", (), <options xmlns=\"xdmp:eval\"><database>1598436954797797328</database>...</options>)"))
        assertThat(frame.XQueryVersion, `is`("1.0-ml"))
    }

    fun testVariables_NCName() {
        val xml = TestResource("debugger/error/eval-divide-by-zero.xml").toString()
        val frames = StackFrameContainer()
        MarkLogicErrorXml(xml).computeStackFrames(1, frames)
        assertThat(frames.frames.size, `is`(1))

        val node = CompositeNode()
        frames.frames[0].computeChildren(node)
        assertThat(node.children.size(), `is`(3))

        val child1 = node.children.getValue(0) as MarkLogicErrorXmlVariable
        assertThat(node.children.getName(0), `is`("query"))
        assertThat(child1.name, `is`("query"))
        assertThat(child1.evaluationExpression, `is`("\"2 div 0\""))

        assertThat(child1.namespace, `is`(nullValue()))
        assertThat(child1.localName, `is`("query"))
        assertThat(child1.value, `is`("\"2 div 0\""))

        val child2 = node.children.getValue(1) as MarkLogicErrorXmlVariable
        assertThat(node.children.getName(1), `is`("vars"))
        assertThat(child2.name, `is`("vars"))
        assertThat(child2.evaluationExpression, `is`("()"))

        assertThat(child2.namespace, `is`(nullValue()))
        assertThat(child2.localName, `is`("vars"))
        assertThat(child2.value, `is`("()"))

        val child3 = node.children.getValue(2) as MarkLogicErrorXmlVariable
        assertThat(node.children.getName(2), `is`("options"))
        assertThat(child3.name, `is`("options"))
        assertThat(child3.evaluationExpression, `is`("<options xmlns=\"xdmp:eval\"><database>1598436954797797328</database>...</options>"))

        assertThat(child3.namespace, `is`(nullValue()))
        assertThat(child3.localName, `is`("options"))
        assertThat(child3.value, `is`("<options xmlns=\"xdmp:eval\"><database>1598436954797797328</database>...</options>"))
    }

    fun testVariables_QName() {
        val xml = TestResource("debugger/error/cast-as-var-qname.xml").toString()
        val frame = MarkLogicErrorXml(xml).topFrame!!

        val node = CompositeNode()
        frame.computeChildren(node)
        assertThat(node.children.size(), `is`(1))

        val child1 = node.children.getValue(0) as MarkLogicErrorXmlVariable
        assertThat(node.children.getName(0), `is`("x"))
        assertThat(child1.name, `is`("x"))
        assertThat(child1.evaluationExpression, `is`("()"))

        assertThat(child1.namespace, `is`("http://www.w3.org/2005/xquery-local-functions"))
        assertThat(child1.localName, `is`("x"))
        assertThat(child1.value, `is`("()"))
    }
}
