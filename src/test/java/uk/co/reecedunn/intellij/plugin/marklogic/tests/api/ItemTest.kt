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
import uk.co.reecedunn.intellij.plugin.marklogic.api.primitiveToItemType
import java.math.BigDecimal
import java.math.BigInteger

class ItemTest : TestCase() {
    // region primitiveToItemType :: node types

    fun testPrimitiveToItemType_NodeTypes() {
        assertThat(primitiveToItemType("array-node()"), `is`("array-node()"))
        assertThat(primitiveToItemType("attribute()"), `is`("attribute()"))
        assertThat(primitiveToItemType("binary()"), `is`("binary()"))
        assertThat(primitiveToItemType("boolean-node()"), `is`("boolean-node()"))
        assertThat(primitiveToItemType("comment()"), `is`("comment()"))
        assertThat(primitiveToItemType("document-node()"), `is`("document-node()"))
        assertThat(primitiveToItemType("element()"), `is`("element()"))
        assertThat(primitiveToItemType("node()"), `is`("node()"))
        assertThat(primitiveToItemType("null-node()"), `is`("null-node()"))
        assertThat(primitiveToItemType("number-node()"), `is`("number-node()"))
        assertThat(primitiveToItemType("object-node()"), `is`("object-node()"))
        assertThat(primitiveToItemType("processing-instruction()"), `is`("processing-instruction()"))
        assertThat(primitiveToItemType("text()"), `is`("text()"))
    }

    // endregion
    // region primitiveToItemType :: MarkLogic types

    fun testPrimitiveToItemType_MarkLogicTypes() {
        assertThat(primitiveToItemType("array"), `is`("json:array"))
        assertThat(primitiveToItemType("box"), `is`("cts:box"))
        assertThat(primitiveToItemType("circle"), `is`("cts:circle"))
        assertThat(primitiveToItemType("complex-polygon"), `is`("cts:complex-polygon"))
        assertThat(primitiveToItemType("linestring"), `is`("cts:linestring"))
        assertThat(primitiveToItemType("map"), `is`("map:map"))
        assertThat(primitiveToItemType("object"), `is`("json:object"))
        assertThat(primitiveToItemType("period"), `is`("cts:period"))
        assertThat(primitiveToItemType("point"), `is`("cts:point"))
        assertThat(primitiveToItemType("polygon"), `is`("cts:polygon"))
        assertThat(primitiveToItemType("region"), `is`("cts:region"))

        // CTS order types
        assertThat(primitiveToItemType("confidence-order"), `is`("cts:confidence-order"))
        assertThat(primitiveToItemType("document-order"), `is`("cts:document-order"))
        assertThat(primitiveToItemType("fitness-order"), `is`("cts:fitness-order"))
        assertThat(primitiveToItemType("index-order"), `is`("cts:index-order"))
        assertThat(primitiveToItemType("quality-order"), `is`("cts:quality-order"))
        assertThat(primitiveToItemType("score-order"), `is`("cts:score-order"))
        assertThat(primitiveToItemType("unordered"), `is`("cts:unordered"))

        // CTS query types (not a full list)
        assertThat(primitiveToItemType("collection-query"), `is`("cts:collection-query"))
        assertThat(primitiveToItemType("element-query"), `is`("cts:element-query"))
        assertThat(primitiveToItemType("field-range-query"), `is`("cts:field-range-query"))

        // CTS reference types (not a full list)
        assertThat(primitiveToItemType("collection-reference"), `is`("cts:collection-reference"))
        assertThat(primitiveToItemType("element-reference"), `is`("cts:element-reference"))
        assertThat(primitiveToItemType("field-reference"), `is`("cts:field-reference"))
    }

    // endregion
    // region primitiveToItemType :: XMLSchema

    fun testPrimitiveToItemType_XMLSchema() {
        assertThat(primitiveToItemType("anyURI"), `is`("xs:anyURI"))
        assertThat(primitiveToItemType("base64Binary"), `is`("xs:base64Binary"))
        assertThat(primitiveToItemType("boolean"), `is`("xs:boolean"))
        assertThat(primitiveToItemType("date"), `is`("xs:date"))
        assertThat(primitiveToItemType("dateTime"), `is`("xs:dateTime"))
        assertThat(primitiveToItemType("dayTimeDuration"), `is`("xs:dayTimeDuration"))
        assertThat(primitiveToItemType("decimal"), `is`("xs:decimal"))
        assertThat(primitiveToItemType("double"), `is`("xs:double"))
        assertThat(primitiveToItemType("duration"), `is`("xs:duration"))
        assertThat(primitiveToItemType("float"), `is`("xs:float"))
        assertThat(primitiveToItemType("gDay"), `is`("xs:gDay"))
        assertThat(primitiveToItemType("gMonth"), `is`("xs:gMonth"))
        assertThat(primitiveToItemType("gMonthDay"), `is`("xs:gMonthDay"))
        assertThat(primitiveToItemType("gYear"), `is`("xs:gYear"))
        assertThat(primitiveToItemType("gYearMonth"), `is`("xs:gYearMonth"))
        assertThat(primitiveToItemType("hexBinary"), `is`("xs:hexBinary"))
        assertThat(primitiveToItemType("integer"), `is`("xs:integer"))
        assertThat(primitiveToItemType("QName"), `is`("xs:QName"))
        assertThat(primitiveToItemType("string"), `is`("xs:string"))
        assertThat(primitiveToItemType("time"), `is`("xs:time"))
        assertThat(primitiveToItemType("untypedAtomic"), `is`("xs:untypedAtomic"))
        assertThat(primitiveToItemType("yearMonthDuration"), `is`("xs:yearMonthDuration"))
    }

    // endregion
    // region Item.create :: empty-sequence()

    @Query("()")
    fun testEmptySequence() {
        val item = Item.create("()", "empty-sequence()")
        assertThat(item.content, `is`("()"))
        assertThat<String>(item.contentType, `is`("text/plain"))
        assertThat(item.itemType, `is`("empty-sequence()"))
        assertThat(item.toString(), `is`("()"))
    }

    // endregion
    // region Item.fromType :: BigDecimal

    fun testFromBigDecimal() {
        val item = Item.fromType(BigDecimal.valueOf(15, 1))
        assertThat(item.content, `is`("1.5"))
        assertThat<String>(item.contentType, `is`("text/plain"))
        assertThat(item.itemType, `is`("xs:decimal"))
        assertThat(item.toString(), `is`("1.5"))
    }

    // endregion
    // region Item.fromType :: BigInteger

    fun testFromBigInteger() {
        val item = Item.fromType(BigInteger.valueOf(15))
        assertThat(item.content, `is`("15"))
        assertThat<String>(item.contentType, `is`("text/plain"))
        assertThat(item.itemType, `is`("xs:integer"))
        assertThat(item.toString(), `is`("15"))
    }

    // endregion
    // region Item.fromType :: Boolean

    fun testFromBoolean() {
        val item = Item.fromType(true)
        assertThat(item.content, `is`("true"))
        assertThat<String>(item.contentType, `is`("text/plain"))
        assertThat(item.itemType, `is`("xs:boolean"))
        assertThat(item.toString(), `is`("true"))
    }

    // endregion
    // region Item.fromType :: Byte

    fun testFromByte() {
        val item = Item.fromType(20.toByte())
        assertThat(item.content, `is`("20"))
        assertThat<String>(item.contentType, `is`("text/plain"))
        assertThat(item.itemType, `is`("xs:byte"))
        assertThat(item.toString(), `is`("20"))
    }

    // endregion
    // region Item.fromType :: Double

    fun testFromDouble() {
        val item = Item.fromType(1.5)
        assertThat(item.content, `is`("1.5"))
        assertThat<String>(item.contentType, `is`("text/plain"))
        assertThat(item.itemType, `is`("xs:double"))
        assertThat(item.toString(), `is`("1.5"))
    }

    // endregion
    // region Item.fromType :: Float

    fun testFromFloat() {
        val item = Item.fromType(1.5f)
        assertThat(item.content, `is`("1.5"))
        assertThat<String>(item.contentType, `is`("text/plain"))
        assertThat(item.itemType, `is`("xs:float"))
        assertThat(item.toString(), `is`("1.5"))
    }

    // endregion
    // region Item.fromType :: Integer

    fun testFromInteger() {
        val item = Item.fromType(15)
        assertThat(item.content, `is`("15"))
        assertThat<String>(item.contentType, `is`("text/plain"))
        assertThat(item.itemType, `is`("xs:int"))
        assertThat(item.toString(), `is`("15"))
    }

    // endregion
    // region Item.fromType :: Long

    fun testFromLong() {
        val item = Item.fromType(15.toLong())
        assertThat(item.content, `is`("15"))
        assertThat<String>(item.contentType, `is`("text/plain"))
        assertThat(item.itemType, `is`("xs:long"))
        assertThat(item.toString(), `is`("15"))
    }

    // endregion
    // region Item.fromType :: Short

    fun testFromShort() {
        val item = Item.fromType(15.toShort())
        assertThat(item.content, `is`("15"))
        assertThat<String>(item.contentType, `is`("text/plain"))
        assertThat(item.itemType, `is`("xs:short"))
        assertThat(item.toString(), `is`("15"))
    }

    // endregion
    // region Item.fromType :: String

    fun testFromString() {
        val item = Item.fromType("abc")
        assertThat(item.content, `is`("abc"))
        assertThat<String>(item.contentType, `is`("text/plain"))
        assertThat(item.itemType, `is`("xs:string"))
        assertThat(item.toString(), `is`("abc"))
    }

    // endregion
    // region Item.withMimeType

    fun testWithMimeType() {
        val item = Item.withMimeType("<>", "text/turtle")
        assertThat(item.content, `is`("<>"))
        assertThat<String>(item.contentType, `is`("text/turtle"))
        assertThat(item.itemType, `is`("xs:string"))
        assertThat(item.toString(), `is`("<>"))
    }

    // endregion
}
