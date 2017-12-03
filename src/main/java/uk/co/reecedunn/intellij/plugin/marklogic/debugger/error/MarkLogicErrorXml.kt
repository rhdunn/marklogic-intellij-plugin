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
import org.w3c.dom.Document
import org.xml.sax.InputSource
import uk.co.reecedunn.intellij.plugin.marklogic.api.Item
import uk.co.reecedunn.intellij.plugin.marklogic.ui.resources.MarkLogicBundle
import java.io.StringReader
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

private fun createDocumentBuilder(): DocumentBuilder {
    val factory = DocumentBuilderFactory.newInstance()
    factory.isNamespaceAware = true
    return factory.newDocumentBuilder()
}

private val builder = createDocumentBuilder()

class MarkLogicErrorXml internal constructor(val doc: Document):
        XExecutionStack(MarkLogicBundle.message("debugger.thread.error")) {

    constructor(errorXml: String): this(builder.parse(InputSource(StringReader(errorXml)))) {
        val error = doc.documentElement
        if (error.localName != "error" ||
            error.namespaceURI != "http://marklogic.com/xdmp/error") {
            throw RuntimeException("[${error.namespaceURI}]${error.localName} is not a MarkLogic error XML document.")
        }
    }

    override fun getTopFrame(): XStackFrame? {
        return null
    }

    override fun computeStackFrames(firstFrameIndex: Int, container: XStackFrameContainer?) {
    }
}
