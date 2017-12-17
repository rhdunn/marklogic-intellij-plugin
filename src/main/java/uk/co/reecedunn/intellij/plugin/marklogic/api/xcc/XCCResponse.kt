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
package uk.co.reecedunn.intellij.plugin.marklogic.api.xcc

import com.marklogic.xcc.ResultSequence
import com.marklogic.xcc.Session
import uk.co.reecedunn.intellij.plugin.marklogic.api.Item
import uk.co.reecedunn.intellij.plugin.marklogic.api.Response

import java.util.ArrayList

class XCCResponse internal constructor(private val resultSequence: ResultSequence, private val session: Session) : Response {
    override val items get(): Array<Item> {
        val items = ArrayList<Item>()
        while (resultSequence.hasNext()) {
            val result = resultSequence.next()
            items.add(Item.create(result.asString(), result.itemType.toString()))
        }
        if (items.isEmpty()) {
            items.add(Item.create("()", "empty-sequence()"))
        }
        return items.toTypedArray()
    }

    override fun close() {
        session.close()
        resultSequence.close()
    }
}
