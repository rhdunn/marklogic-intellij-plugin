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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.api

import junit.framework.TestCase
import uk.co.reecedunn.intellij.plugin.marklogic.api.Item
import uk.co.reecedunn.intellij.plugin.marklogic.tests.Query

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat

class ItemTest : TestCase() {
    // region Lexical Types

    @Query("()")
    fun testEmptySequence() {
        val xcc = Item.create("()", "empty-sequence()")
        assertThat(xcc.content, `is`("()"))
        assertThat<String>(xcc.contentType, `is`("text/plain"))
        assertThat(xcc.itemType, `is`("empty-sequence()"))
        assertThat(xcc.toString(), `is`("()"))

        val rest = Item.create("()", "text/plain", "empty-sequence()")
        assertThat(rest.content, `is`("()"))
        assertThat<String>(rest.contentType, `is`("text/plain"))
        assertThat(rest.itemType, `is`("empty-sequence()"))
        assertThat(rest.toString(), `is`("()"))
    }

    @Query("1.5")
    fun testXsDecimal() {
        val xcc = Item.create("1.5", "xs:decimal")
        assertThat(xcc.content, `is`("1.5"))
        assertThat<String>(xcc.contentType, `is`("text/plain"))
        assertThat(xcc.itemType, `is`("xs:decimal"))
        assertThat(xcc.toString(), `is`("1.5"))

        val rest = Item.create("1.5", "text/plain", "decimal")
        assertThat(rest.content, `is`("1.5"))
        assertThat<String>(rest.contentType, `is`("text/plain"))
        assertThat(rest.itemType, `is`("decimal"))
        assertThat(rest.toString(), `is`("1.5"))
    }

    @Query("1e5")
    fun testXsDouble() {
        val xcc = Item.create("1e5", "xs:double")
        assertThat(xcc.content, `is`("1e5"))
        assertThat<String>(xcc.contentType, `is`("text/plain"))
        assertThat(xcc.itemType, `is`("xs:double"))
        assertThat(xcc.toString(), `is`("1e5"))

        val rest = Item.create("1e5", "text/plain", "double")
        assertThat(rest.content, `is`("1e5"))
        assertThat<String>(rest.contentType, `is`("text/plain"))
        assertThat(rest.itemType, `is`("double"))
        assertThat(rest.toString(), `is`("1e5"))
    }

    @Query("15")
    fun testXsInteger() {
        val xcc = Item.create("15", "xs:integer")
        assertThat(xcc.content, `is`("15"))
        assertThat<String>(xcc.contentType, `is`("text/plain"))
        assertThat(xcc.itemType, `is`("xs:integer"))
        assertThat(xcc.toString(), `is`("15"))

        val rest = Item.create("15", "text/plain", "integer")
        assertThat(rest.content, `is`("15"))
        assertThat<String>(rest.contentType, `is`("text/plain"))
        assertThat(rest.itemType, `is`("integer"))
        assertThat(rest.toString(), `is`("15"))
    }

    @Query("'abc'")
    fun testXsString() {
        val xcc = Item.create("abc", "xs:string")
        assertThat(xcc.content, `is`("abc"))
        assertThat<String>(xcc.contentType, `is`("text/plain"))
        assertThat(xcc.itemType, `is`("xs:string"))
        assertThat(xcc.toString(), `is`("abc"))

        val rest = Item.create("abc", "text/plain", "string")
        assertThat(rest.content, `is`("abc"))
        assertThat<String>(rest.contentType, `is`("text/plain"))
        assertThat(rest.itemType, `is`("string"))
        assertThat(rest.toString(), `is`("abc"))
    }

    // endregion
    // region Node Construction

    @Query("array-node { 1, 2 }")
    fun testArrayNode() {
        val xcc = Item.create("[1, 2]", "array-node()")
        assertThat(xcc.content, `is`("[1, 2]"))
        assertThat<String>(xcc.contentType, `is`("application/json"))
        assertThat(xcc.itemType, `is`("array-node()"))
        assertThat(xcc.toString(), `is`("[1, 2]"))

        val rest = Item.create("[1, 2]", "application/json", "array-node()")
        assertThat(rest.content, `is`("[1, 2]"))
        assertThat<String>(rest.contentType, `is`("application/json"))
        assertThat(rest.itemType, `is`("array-node()"))
        assertThat(rest.toString(), `is`("[1, 2]"))
    }

    @Query("<a b='c'/>/@*")
    fun testAttribute() {
        val xcc = Item.create("c", "attribute()")
        assertThat(xcc.content, `is`("c"))
        assertThat<String>(xcc.contentType, `is`("text/plain"))
        assertThat(xcc.itemType, `is`("attribute()"))
        assertThat(xcc.toString(), `is`("c"))

        val rest = Item.create("c", "text/plain", "attribute()")
        assertThat(rest.content, `is`("c"))
        assertThat<String>(rest.contentType, `is`("text/plain"))
        assertThat(rest.itemType, `is`("attribute()"))
        assertThat(rest.toString(), `is`("c"))
    }

    @Query("binary { 'ab' }")
    fun testBinary() {
        val xcc = Item.create("«", "binary()")
        assertThat(xcc.content, `is`("«"))
        assertThat<String>(xcc.contentType, `is`("application/octet-stream"))
        assertThat(xcc.itemType, `is`("binary()"))
        assertThat(xcc.toString(), `is`("«"))

        val rest = Item.create("«", "application/x-unknown-content-type", "binary()")
        assertThat(rest.content, `is`("«"))
        assertThat<String>(rest.contentType, `is`("application/octet-stream"))
        assertThat(rest.itemType, `is`("binary()"))
        assertThat(rest.toString(), `is`("«"))
    }

    @Query("boolean-node { true() }")
    fun testBooleanNode() {
        val xcc = Item.create("true", "boolean-node()")
        assertThat(xcc.content, `is`("true"))
        assertThat<String>(xcc.contentType, `is`("application/json"))
        assertThat(xcc.itemType, `is`("boolean-node()"))
        assertThat(xcc.toString(), `is`("true"))

        val rest = Item.create("true", "application/json", "boolean-node()")
        assertThat(rest.content, `is`("true"))
        assertThat<String>(rest.contentType, `is`("application/json"))
        assertThat(rest.itemType, `is`("boolean-node()"))
        assertThat(rest.toString(), `is`("true"))
    }

    @Query("<!-- a -->")
    fun testComment() {
        val xcc = Item.create("<!-- a -->", "comment()")
        assertThat(xcc.content, `is`("<!-- a -->"))
        assertThat<String>(xcc.contentType, `is`("text/plain"))
        assertThat(xcc.itemType, `is`("comment()"))
        assertThat(xcc.toString(), `is`("<!-- a -->"))

        val rest = Item.create("<!-- a -->", "text/plain", "comment()")
        assertThat(rest.content, `is`("<!-- a -->"))
        assertThat<String>(rest.contentType, `is`("text/plain"))
        assertThat(rest.itemType, `is`("comment()"))
        assertThat(rest.toString(), `is`("<!-- a -->"))
    }

    @Query("document { <a/> }")
    fun testDocument() {
        val xcc = Item.create("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a/>", "document-node()")
        assertThat(xcc.content, `is`("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a/>"))
        assertThat<String>(xcc.contentType, `is`("application/xml"))
        assertThat(xcc.itemType, `is`("document-node()"))
        assertThat(xcc.toString(), `is`("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a/>"))

        val rest = Item.create("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a/>", "application/xml", "document-node()")
        assertThat(rest.content, `is`("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a/>"))
        assertThat<String>(rest.contentType, `is`("application/xml"))
        assertThat(rest.itemType, `is`("document-node()"))
        assertThat(rest.toString(), `is`("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a/>"))
    }

    @Query("<a/>")
    fun testElement() {
        val xcc = Item.create("<a/>", "element()")
        assertThat(xcc.content, `is`("<a/>"))
        assertThat<String>(xcc.contentType, `is`("application/xml"))
        assertThat(xcc.itemType, `is`("element()"))
        assertThat(xcc.toString(), `is`("<a/>"))

        val rest = Item.create("<a/>", "application/xml", "element()")
        assertThat(rest.content, `is`("<a/>"))
        assertThat<String>(rest.contentType, `is`("application/xml"))
        assertThat(rest.itemType, `is`("element()"))
        assertThat(rest.toString(), `is`("<a/>"))
    }

    @Query("null-node {}")
    fun testNullNode() {
        val xcc = Item.create("null", "null-node()")
        assertThat(xcc.content, `is`("null"))
        assertThat<String>(xcc.contentType, `is`("application/json"))
        assertThat(xcc.itemType, `is`("null-node()"))
        assertThat(xcc.toString(), `is`("null"))

        val rest = Item.create("null", "application/json", "null-node()")
        assertThat(rest.content, `is`("null"))
        assertThat<String>(rest.contentType, `is`("application/json"))
        assertThat(rest.itemType, `is`("null-node()"))
        assertThat(rest.toString(), `is`("null"))
    }

    @Query("number-node { 2 }")
    fun testNumberNode() {
        val xcc = Item.create("2", "number-node()")
        assertThat(xcc.content, `is`("2"))
        assertThat<String>(xcc.contentType, `is`("application/json"))
        assertThat(xcc.itemType, `is`("number-node()"))
        assertThat(xcc.toString(), `is`("2"))

        val rest = Item.create("2", "application/json", "number-node()")
        assertThat(rest.content, `is`("2"))
        assertThat<String>(rest.contentType, `is`("application/json"))
        assertThat(rest.itemType, `is`("number-node()"))
        assertThat(rest.toString(), `is`("2"))
    }

    @Query("object-node { 1: 2 }")
    fun testObjectNode() {
        val xcc = Item.create("{\"1\": 2}", "object-node()")
        assertThat(xcc.content, `is`("{\"1\": 2}"))
        assertThat<String>(xcc.contentType, `is`("application/json"))
        assertThat(xcc.itemType, `is`("object-node()"))
        assertThat(xcc.toString(), `is`("{\"1\": 2}"))

        val rest = Item.create("{\"1\": 2}", "application/json", "object-node()")
        assertThat(rest.content, `is`("{\"1\": 2}"))
        assertThat<String>(rest.contentType, `is`("application/json"))
        assertThat(rest.itemType, `is`("object-node()"))
        assertThat(rest.toString(), `is`("{\"1\": 2}"))
    }

    @Query("<?a?>")
    fun testProcessingInstruction() {
        val xcc = Item.create("<?a?>", "processing-instruction()")
        assertThat(xcc.content, `is`("<?a?>"))
        assertThat<String>(xcc.contentType, `is`("text/plain"))
        assertThat(xcc.itemType, `is`("processing-instruction()"))
        assertThat(xcc.toString(), `is`("<?a?>"))

        val rest = Item.create("<?a?>", "text/plain", "processing-instruction()")
        assertThat(rest.content, `is`("<?a?>"))
        assertThat<String>(rest.contentType, `is`("text/plain"))
        assertThat(rest.itemType, `is`("processing-instruction()"))
        assertThat(rest.toString(), `is`("<?a?>"))
    }

    @Query("<a>abc</a>/text()")
    fun testText() {
        val xcc = Item.create("abc", "text()")
        assertThat(xcc.content, `is`("abc"))
        assertThat<String>(xcc.contentType, `is`("text/plain"))
        assertThat(xcc.itemType, `is`("text()"))
        assertThat(xcc.toString(), `is`("abc"))

        val rest = Item.create("abc", "text/plain", "text()")
        assertThat(rest.content, `is`("abc"))
        assertThat<String>(rest.contentType, `is`("text/plain"))
        assertThat(rest.itemType, `is`("text()"))
        assertThat(rest.toString(), `is`("abc"))
    }

    // endregion
    // region Function Return Types

    @Query("true()")
    fun testXsBoolean() {
        val xcc = Item.create("true", "xs:boolean")
        assertThat(xcc.content, `is`("true"))
        assertThat<String>(xcc.contentType, `is`("text/plain"))
        assertThat(xcc.itemType, `is`("xs:boolean"))
        assertThat(xcc.toString(), `is`("true"))

        val rest = Item.create("true", "text/plain", "boolean")
        assertThat(rest.content, `is`("true"))
        assertThat<String>(rest.contentType, `is`("text/plain"))
        assertThat(rest.itemType, `is`("boolean"))
        assertThat(rest.toString(), `is`("true"))
    }

    @Query("map:map()")
    fun testMapMap() {
        val xcc = Item.create("{}", "json:object")
        assertThat(xcc.content, `is`("{}"))
        assertThat<String>(xcc.contentType, `is`("application/json"))
        assertThat(xcc.itemType, `is`("map:map"))
        assertThat(xcc.toString(), `is`("{}"))

        val rest = Item.create("{}", "text/plain", "map")
        assertThat(rest.content, `is`("{}"))
        assertThat<String>(rest.contentType, `is`("application/json"))
        assertThat(rest.itemType, `is`("map:map"))
        assertThat(rest.toString(), `is`("{}"))
    }

    // endregion
    // region Type Constructors

    @Query("xs:float(1.5)")
    fun testXsFloat() {
        val xcc = Item.create("1.5", "xs:float")
        assertThat(xcc.content, `is`("1.5"))
        assertThat<String>(xcc.contentType, `is`("text/plain"))
        assertThat(xcc.itemType, `is`("xs:float"))
        assertThat(xcc.toString(), `is`("1.5"))

        val rest = Item.create("1.5", "text/plain", "float")
        assertThat(rest.content, `is`("1.5"))
        assertThat<String>(rest.contentType, `is`("text/plain"))
        assertThat(rest.itemType, `is`("float"))
        assertThat(xcc.toString(), `is`("1.5"))
    }

    @Query("xs:yearMonthDuration('P2Y')")
    fun testXsYearMonthDuration() {
        val xcc = Item.create("P2Y", "xs:yearMonthDuration")
        assertThat(xcc.content, `is`("P2Y"))
        assertThat<String>(xcc.contentType, `is`("text/plain"))
        assertThat(xcc.itemType, `is`("xs:yearMonthDuration"))
        assertThat(xcc.toString(), `is`("P2Y"))

        val rest = Item.create("P2Y", "text/plain", "yearMonthDuration")
        assertThat(rest.content, `is`("P2Y"))
        assertThat<String>(rest.contentType, `is`("text/plain"))
        assertThat(rest.itemType, `is`("yearMonthDuration"))
        assertThat(xcc.toString(), `is`("P2Y"))
    }

    // endregion
}
