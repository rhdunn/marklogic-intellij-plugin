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
package uk.co.reecedunn.intellij.plugin.marklogic.api.rest

import com.google.gson.JsonObject
import org.apache.http.client.methods.RequestBuilder
import uk.co.reecedunn.intellij.plugin.marklogic.api.EvalRequestBuilder
import uk.co.reecedunn.intellij.plugin.marklogic.api.Request

class RestEvalRequestBuilder internal constructor(private val connection: RestConnection) : EvalRequestBuilder() {
    override fun build(): Request {
        var variables: JsonObject? = null
        for (name in variableNames) {
            if (variables == null) variables = JsonObject()

            val value = getVariable(name).content // itemType is not supported via the REST API
            if (name.namespace != null) {
                variables.addProperty("{${name.namespace}}${name.localname}", value)
            } else {
                variables.addProperty(name.localname, value)
            }
        }

        val builder = RequestBuilder.post("${connection.baseUri}/v1/eval")
        addParameter(builder, "xquery", XQuery)
        addParameter(builder, "javascript", javaScript)
        addParameter(builder, "database", contentDatabase)
        addParameter(builder, "txid", transactionID)
        variables?.let { addParameter(builder, "vars", it.toString()) }
        return RestRequest(builder.build(), connection)
    }

    private fun addParameter(builder: RequestBuilder, key: String, value: String?) {
        if (value != null && !value.isEmpty()) {
            builder.addParameter(key, value)
        }
    }
}
