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
import uk.co.reecedunn.intellij.plugin.core.xml.*
import uk.co.reecedunn.intellij.plugin.marklogic.ui.resources.MarkLogicBundle

private val ERROR_CODE = XmlElementName("code", "http://marklogic.com/xdmp/error")
private val ERROR_ERROR = XmlElementName("error", "http://marklogic.com/xdmp/error")

class MarkLogicErrorXml internal constructor(val doc: XmlDocument):
        XExecutionStack(MarkLogicBundle.message("debugger.thread.error")) {

    constructor(errorXml: String): this(XmlDocument.parse(errorXml)) {
        if (doc.root.name != ERROR_ERROR)
            throw RuntimeException("${doc.root.name} is not a MarkLogic error XML document.")
    }

    // region MarkLogic Error

    val code get(): String =
        doc.child(ERROR_CODE).text().first()

    // endregion
    // region XExecutionStack

    override fun getTopFrame(): XStackFrame? {
        return null
    }

    override fun computeStackFrames(firstFrameIndex: Int, container: XStackFrameContainer?) {
    }

    // endregion
}
