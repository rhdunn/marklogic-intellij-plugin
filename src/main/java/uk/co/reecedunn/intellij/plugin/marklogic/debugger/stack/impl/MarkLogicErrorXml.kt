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
package uk.co.reecedunn.intellij.plugin.marklogic.debugger.stack.impl

import com.intellij.xdebugger.frame.*
import org.w3c.dom.Element
import uk.co.reecedunn.intellij.plugin.core.xml.*
import uk.co.reecedunn.intellij.plugin.marklogic.debugger.stack.MarkLogicError
import uk.co.reecedunn.intellij.plugin.marklogic.debugger.stack.MarkLogicFrame
import uk.co.reecedunn.intellij.plugin.marklogic.debugger.stack.MarkLogicVariable
import uk.co.reecedunn.intellij.plugin.marklogic.ui.resources.MarkLogicBundle

private val ERROR_CODE = XmlElementName("code", "http://marklogic.com/xdmp/error")
private val ERROR_COLUMN = XmlElementName("column", "http://marklogic.com/xdmp/error")
private val ERROR_DATA = XmlElementName("data", "http://marklogic.com/xdmp/error")
private val ERROR_DATUM = XmlElementName("datum", "http://marklogic.com/xdmp/error")
private val ERROR_ERROR = XmlElementName("error", "http://marklogic.com/xdmp/error")
private val ERROR_EXPR = XmlElementName("expr", "http://marklogic.com/xdmp/error")
private val ERROR_FRAME = XmlElementName("frame", "http://marklogic.com/xdmp/error")
private val ERROR_LINE = XmlElementName("line", "http://marklogic.com/xdmp/error")
private val ERROR_MESSAGE = XmlElementName("message", "http://marklogic.com/xdmp/error")
private val ERROR_NAME = XmlElementName("name", "http://marklogic.com/xdmp/error")
private val ERROR_OPERATION = XmlElementName("operation", "http://marklogic.com/xdmp/error")
private val ERROR_RETRYABLE = XmlElementName("retryable", "http://marklogic.com/xdmp/error")
private val ERROR_STACK = XmlElementName("stack", "http://marklogic.com/xdmp/error")
private val ERROR_URI = XmlElementName("uri", "http://marklogic.com/xdmp/error")
private val ERROR_VALUE = XmlElementName("value", "http://marklogic.com/xdmp/error")
private val ERROR_VARIABLE = XmlElementName("variable", "http://marklogic.com/xdmp/error")
private val ERROR_VARIABLES = XmlElementName("variables", "http://marklogic.com/xdmp/error")
private val ERROR_XQUERY_VERSION = XmlElementName("xquery-version", "http://marklogic.com/xdmp/error")

class MarkLogicErrorXml internal constructor(private val doc: XmlDocument):
        XExecutionStack(MarkLogicBundle.message("debugger.thread.error")),
        MarkLogicError {

    constructor(errorXml: String): this(XmlDocument.parse(errorXml)) {
        if (doc.root.name != ERROR_ERROR)
            throw RuntimeException("${doc.root.name} is not a MarkLogic error XML document.")
    }

    // region MarkLogicError

    override val code get(): String = doc.child(ERROR_CODE).text().first()

    override val name get(): String = doc.child(ERROR_NAME).text().first()

    override val XQueryVersion get(): String = doc.child(ERROR_XQUERY_VERSION).text().first()

    override val message get(): String = doc.child(ERROR_MESSAGE).text().first()

    override val retryable get(): Boolean = doc.child(ERROR_RETRYABLE).text().first().toBoolean()

    override val expr get(): String = doc.child(ERROR_EXPR).text().first()

    override val data get(): Sequence<String> =
        doc.child(ERROR_DATA).child(ERROR_DATUM).text()

    override val frames get(): Sequence<Element> =
        doc.child(ERROR_STACK).child(ERROR_FRAME)

    // endregion
    // region XExecutionStack

    override fun getTopFrame(): XStackFrame? {
        return frames.firstOrNull()?.let { MarkLogicErrorXmlFrame(it) }
    }

    override fun computeStackFrames(firstFrameIndex: Int, container: XStackFrameContainer?) {
        container?.let {
            val frames = frames.drop(firstFrameIndex).map { frame -> MarkLogicErrorXmlFrame(frame) }
            it.addStackFrames(frames.toMutableList(), true)
        }
    }

    // endregion
}

class MarkLogicErrorXmlFrame internal constructor(val frame: Element): XStackFrame(), MarkLogicFrame {
    // region MarkLogicFrame

    override val uri get(): String? = frame.child(ERROR_URI).text().firstOrNull()

    override val line get(): Int = frame.child(ERROR_LINE).text().first().toInt()

    override val column get(): Int = frame.child(ERROR_COLUMN).text().first().toInt()

    override val operation get(): String? = frame.child(ERROR_OPERATION).text().firstOrNull()

    override val XQueryVersion get(): String = frame.child(ERROR_XQUERY_VERSION).text().first()

    override val variables get(): Sequence<Element> = frame.child(ERROR_VARIABLES).child(ERROR_VARIABLE)

    // endregion
    // reion XStackFrame

    override fun computeChildren(node: XCompositeNode) {
        val vars = XValueChildrenList()
        variables.map { variable -> MarkLogicErrorXmlVariable(variable) }.forEach { variable ->
            vars.add(variable)
        }
        node.addChildren(vars, true)
    }

    // endregion
}

class MarkLogicErrorXmlVariable internal constructor(private val variable: Element):
        XNamedValue(variable.child(ERROR_NAME).text().first()),
        MarkLogicVariable {
    // region MarkLogicVariable

    override val localName get(): String = super.getName()

    override val value: String = variable.child(ERROR_VALUE).text().first()

    // endregion
    // region XValue

    override fun getEvaluationExpression(): String? =
        value

    override fun computePresentation(node: XValueNode, place: XValuePlace) {
    }

    // endregion
}
