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
package uk.co.reecedunn.intellij.plugin.marklogic.debugger.error

import com.intellij.xdebugger.frame.XExecutionStack
import com.intellij.xdebugger.frame.XStackFrame
import org.w3c.dom.Element
import uk.co.reecedunn.intellij.plugin.core.xml.*
import uk.co.reecedunn.intellij.plugin.marklogic.ui.resources.MarkLogicBundle

private val ERROR_CODE = XmlElementName("code", "http://marklogic.com/xdmp/error")
private val ERROR_DATA = XmlElementName("data", "http://marklogic.com/xdmp/error")
private val ERROR_DATUM = XmlElementName("datum", "http://marklogic.com/xdmp/error")
private val ERROR_ERROR = XmlElementName("error", "http://marklogic.com/xdmp/error")
private val ERROR_EXPR = XmlElementName("expr", "http://marklogic.com/xdmp/error")
private val ERROR_FRAME = XmlElementName("frame", "http://marklogic.com/xdmp/error")
private val ERROR_MESSAGE = XmlElementName("message", "http://marklogic.com/xdmp/error")
private val ERROR_NAME = XmlElementName("name", "http://marklogic.com/xdmp/error")
private val ERROR_RETRYABLE = XmlElementName("retryable", "http://marklogic.com/xdmp/error")
private val ERROR_STACK = XmlElementName("stack", "http://marklogic.com/xdmp/error")
private val ERROR_XQUERY_VERSION = XmlElementName("xquery-version", "http://marklogic.com/xdmp/error")

class MarkLogicErrorXml internal constructor(private val doc: XmlDocument):
        XExecutionStack(MarkLogicBundle.message("debugger.thread.error")) {

    constructor(errorXml: String): this(XmlDocument.parse(errorXml)) {
        if (doc.root.name != ERROR_ERROR)
            throw RuntimeException("${doc.root.name} is not a MarkLogic error XML document.")
    }

    // region MarkLogic Error

    val code get(): String = doc.child(ERROR_CODE).text().first()

    val name get(): String = doc.child(ERROR_NAME).text().first()

    val XQueryVersion get(): String = doc.child(ERROR_XQUERY_VERSION).text().first()

    val message get(): String = doc.child(ERROR_MESSAGE).text().first()

    val retryable get(): Boolean = doc.child(ERROR_RETRYABLE).text().first().toBoolean()

    val expr get(): String = doc.child(ERROR_EXPR).text().first()

    val data get(): Sequence<String> =
        doc.child(ERROR_DATA).child(ERROR_DATUM).text()

    private val frames get(): Sequence<Element> =
        doc.child(ERROR_STACK).child(ERROR_FRAME)

    // endregion
    // region XExecutionStack

    override fun getTopFrame(): XStackFrame? {
        return frames.firstOrNull()?.let { MarkLogicErrorXmlFrame(it) }
    }

    override fun computeStackFrames(firstFrameIndex: Int, container: XStackFrameContainer?) {
    }

    // endregion
}
