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

import com.marklogic.xcc.RequestOptions
import com.marklogic.xcc.types.ValueType
import uk.co.reecedunn.intellij.plugin.marklogic.api.EvalRequestBuilder
import uk.co.reecedunn.intellij.plugin.marklogic.api.Request

class XCCEvalRequestBuilder internal constructor(private val connection: XCCConnection) : EvalRequestBuilder() {
    override fun build(): Request {
        val options = RequestOptions()
        var query = XQuery
        if (query != null) {
            options.queryLanguage = "xquery"
        } else {
            query = javaScript
            options.queryLanguage = "javascript"
        }

        val session = connection.contentSource.newSession(contentDatabase)
        val request = session.newAdhocQuery(query, options)

        for (name in variableNames) {
            val value = getVariable(name)
            val type =
                try {
                    if (value.itemType == "empty-sequence()")
                        ValueType.SEQUENCE
                    else
                        ValueType.valueOf(value.itemType)
                } catch (e: Exception) {
                    ValueType.XS_STRING // unknown item
                }
            if (name.namespace != null) {
                request.setNewVariable(name.namespace, name.localname, type, value.content)
            } else {
                request.setNewVariable(name.localname, type, value.content)
            }
        }
        return XCCRequest(session, request)
    }
}
