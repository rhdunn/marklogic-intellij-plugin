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

import com.intellij.util.ArrayUtil

private val BINARY_ITEM_TYPES = arrayOf("binary()")
private val JSON_ITEM_TYPES = arrayOf("array-node()", "boolean-node()", "null-node()", "number-node()", "object-node()")
private val XML_ITEM_TYPES = arrayOf("document-node()", "element()")

private fun getContentTypeForItemType(itemType: String): String {
    if (ArrayUtil.contains(itemType, *BINARY_ITEM_TYPES)) {
        return "application/x-unknown-content-type"
    }
    if (ArrayUtil.contains(itemType, *JSON_ITEM_TYPES)) {
        return "application/json"
    }
    return if (ArrayUtil.contains(itemType, *XML_ITEM_TYPES)) "application/xml" else "text/plain"
}

class Item private constructor(val content: String, val contentType: String, val itemType: String) {
    override fun toString(): String {
        return content
    }

    companion object {
        fun create(content: String, contentType: String, itemType: String): Item {
            val type =
                if (contentType == "application/x-unknown-content-type")
                    "application/octet-stream"
                else if (itemType == "map" || itemType == "json:object")
                    "application/json"
                else
                    contentType
            val item =
                if (itemType == "map" || itemType == "json:object")
                    "map:map"
                else
                    itemType
            return Item(content, type, item)
        }

        fun create(content: String, itemType: String): Item {
            return create(content, getContentTypeForItemType(itemType), itemType)
        }
    }
}
