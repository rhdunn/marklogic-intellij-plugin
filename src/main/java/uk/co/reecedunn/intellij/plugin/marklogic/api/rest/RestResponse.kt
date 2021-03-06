/*
 * Copyright (C) 2017-2018 Reece H. Dunn
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
package uk.co.reecedunn.intellij.plugin.marklogic.api.rest

import uk.co.reecedunn.intellij.plugin.marklogic.api.Item
import uk.co.reecedunn.intellij.plugin.marklogic.api.Response
import uk.co.reecedunn.intellij.plugin.marklogic.api.ResponseException
import uk.co.reecedunn.intellij.plugin.marklogic.api.mime.MimeResponse
import uk.co.reecedunn.intellij.plugin.marklogic.api.primitiveToItemType

import java.util.ArrayList

class RestResponse(private val response: MimeResponse) : Response {
    override val items get(): Array<Item> {
        val statusCode = response.status.statusCode
        if (statusCode != 200) {
            val part = response.parts[0]
            val message = Item.withMimeType(part.body, part.getHeader("Content-Type")!!)
            throw ResponseException(statusCode, response.status.reasonPhrase, message)
        }

        val items = ArrayList<Item>()
        val internalContentType = response.getHeader("X-Content-Type")
        for (part in response.parts) {
            if (part.getHeader("Content-Length")?.toInt() == 0)
                continue

            if (internalContentType == null) {
                val primitive = part.getHeader("X-Primitive")
                items.add(Item.create(part.body, primitive?.let { primitiveToItemType(it) } ?: "xs:string"))
            } else {
                items.add(Item.withMimeType(part.body, internalContentType))
            }
        }
        if (items.isEmpty()) {
            items.add(Item.create("()", "empty-sequence()"))
        }
        return items.toTypedArray()
    }

    override fun close() {}
}
