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
package uk.co.reecedunn.intellij.plugin.marklogic.api

import java.math.BigDecimal
import java.math.BigInteger

fun primitiveToItemType(primitive: String): String {
    return if (primitive.endsWith("-order") ||
               primitive.endsWith("-query") ||
               primitive.endsWith("-reference"))
        "cts:$primitive"
    else when (primitive) {
        // CTS types ============================
        "box",
        "circle",
        "complex-polygon",
        "linestring",
        "period",
        "point",
        "polygon",
        "region",
        "unordered" ->
            "cts:$primitive"
        // JSON types ===========================
        // - `json:null()` is equivalent to `()`, so is not returned as a primitive name.
        "map" ->
            "map:map"
        "array",
        "object" ->
            "json:$primitive"
        // XMLSchema types ======================
        // - `xs:integer` includes other integer-like types, such as `xs:byte`.
        // - `xs:string` includes other string-like types, such as `xs:language` and `cts:token`.
        "anyURI",
        "base64Binary", "boolean",
        "date", "dateTime", "dayTimeDuration", "decimal", "double", "duration",
        "float",
        "gDay", "gMonth", "gMonthDay", "gYear", "gYearMonth",
        "hexBinary",
        "integer",
        "QName",
        "string",
        "time",
        "untypedAtomic",
        "yearMonthDuration" ->
            "xs:$primitive"
        // other types ==========================
        // - Don't throw an error here, so unknown types will still work
        //   (unlike with the XCC API).
        // - This also handles node types that have the same name as the
        //   primitive.
        else -> primitive
    }
}

class Item private constructor(val content: String, val contentType: String, val itemType: String) {
    override fun toString(): String {
        return content
    }

    companion object {
        fun create(content: String, itemType: String): Item {
            return Item(content, "text/plain", itemType)
        }

        fun fromType(value: Any): Item {
            return when (value) {
                is BigDecimal -> create(value.toString(), "xs:decimal")
                is BigInteger -> create(value.toString(), "xs:integer")
                is Boolean -> create(value.toString(), "xs:boolean")
                is Byte -> create(value.toString(), "xs:byte")
                is Double -> create(value.toString(), "xs:double")
                is Float -> create(value.toString(), "xs:float")
                is Int -> create(value.toString(), "xs:int")
                is Long -> create(value.toString(), "xs:long")
                is Short -> create(value.toString(), "xs:short")
                is String -> create(value.toString(), "xs:string")
                else -> throw RuntimeException("Unsupported type: ${value.javaClass.name}")
            }
        }

        fun withMimeType(content: String, contentType: String): Item {
            return Item(content, contentType, "xs:string")
        }
    }
}
