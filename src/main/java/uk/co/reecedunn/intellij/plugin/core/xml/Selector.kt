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
package uk.co.reecedunn.intellij.plugin.core.xml

import org.w3c.dom.Element
import org.w3c.dom.NodeList
import org.w3c.dom.Text

// region child:: axis

fun Element.child(ref: XmlElementName?): Sequence<Element> {
    val nodes: NodeList =
        if (ref == null)                childNodes
        else if (ref.namespace == null) getElementsByTagName(ref.localName)
        else                            getElementsByTagNameNS(ref.namespace, ref.localName)
    return nodes.asSequence().filterIsInstance<Element>()
}

fun XmlDocument.child(ref: XmlElementName?): Sequence<Element> =
    root.child(ref)

fun Sequence<Element>.child(ref: XmlElementName?): Sequence<Element> =
    flatMap { element -> element.child(ref) }

// endregion
// region text()

fun Element.text(): Sequence<String> =
    childNodes.asSequence().filterIsInstance<Text>().map { node -> node.nodeValue }

fun Sequence<Element>.text(): Sequence<String> =
    flatMap { element -> element.text() }

// endregion
