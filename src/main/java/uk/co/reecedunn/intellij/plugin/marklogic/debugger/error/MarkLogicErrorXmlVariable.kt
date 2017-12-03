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

import com.intellij.xdebugger.frame.XNamedValue
import com.intellij.xdebugger.frame.XValueNode
import com.intellij.xdebugger.frame.XValuePlace
import org.w3c.dom.Element
import uk.co.reecedunn.intellij.plugin.core.xml.XmlElementName
import uk.co.reecedunn.intellij.plugin.core.xml.child
import uk.co.reecedunn.intellij.plugin.core.xml.text

private val ERROR_NAME = XmlElementName("name", "http://marklogic.com/xdmp/error")
private val ERROR_VALUE = XmlElementName("value", "http://marklogic.com/xdmp/error")

class MarkLogicErrorXmlVariable internal constructor(private val variable: Element):
        XNamedValue(variable.child(ERROR_NAME).text().first()) {

    override fun getEvaluationExpression(): String? =
        variable.child(ERROR_VALUE).text().first()

    override fun computePresentation(node: XValueNode, place: XValuePlace) {
    }
}
