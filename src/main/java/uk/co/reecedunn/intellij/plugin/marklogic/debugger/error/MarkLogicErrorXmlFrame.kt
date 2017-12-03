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

import com.intellij.xdebugger.frame.XStackFrame
import org.w3c.dom.Element
import uk.co.reecedunn.intellij.plugin.core.xml.XmlElementName
import uk.co.reecedunn.intellij.plugin.core.xml.child
import uk.co.reecedunn.intellij.plugin.core.xml.text

private val ERROR_COLUMN = XmlElementName("column", "http://marklogic.com/xdmp/error")
private val ERROR_LINE = XmlElementName("line", "http://marklogic.com/xdmp/error")
private val ERROR_OPERATION = XmlElementName("operation", "http://marklogic.com/xdmp/error")
private val ERROR_XQUERY_VERSION = XmlElementName("xquery-version", "http://marklogic.com/xdmp/error")

class MarkLogicErrorXmlFrame internal constructor(val frame: Element): XStackFrame() {
    val line: Int = frame.child(ERROR_LINE).text().first().toInt()

    val column: Int = frame.child(ERROR_COLUMN).text().first().toInt()

    val operation: String = frame.child(ERROR_OPERATION).text().first()

    val XQueryVersion get(): String = frame.child(ERROR_XQUERY_VERSION).text().first()
}
