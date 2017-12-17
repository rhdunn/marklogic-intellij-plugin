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
import java.math.BigDecimal
import java.math.BigInteger

class ItemTest : TestCase() {
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
        assertThat(item.itemType, `is`("string"))
        assertThat(item.toString(), `is`("<>"))
    }

    // endregion
}
