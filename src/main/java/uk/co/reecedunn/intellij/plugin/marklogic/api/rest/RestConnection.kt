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

import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import uk.co.reecedunn.intellij.plugin.marklogic.api.Connection
import uk.co.reecedunn.intellij.plugin.marklogic.api.EvalRequestBuilder
import uk.co.reecedunn.intellij.plugin.marklogic.api.LogRequestBuilder

class RestConnection private constructor(internal val baseUri: String, val client: CloseableHttpClient) : Connection() {
    override fun close() {
        client.close()
    }

    override fun createEvalRequestBuilder(): EvalRequestBuilder {
        return RestEvalRequestBuilder(this)
    }

    override fun createLogRequestBuilder(): LogRequestBuilder {
        return RestLogRequestBuilder(this)
    }

    companion object {
        fun newConnection(hostname: String, port: Int, username: String?, password: String?): Connection {
            val baseUri = "http://$hostname:$port"
            if (username == null || password == null) {
                return RestConnection(baseUri, HttpClients.createDefault())
            }

            val credentials = BasicCredentialsProvider()
            credentials.setCredentials(
                AuthScope(hostname, port),
                UsernamePasswordCredentials(username, password))
            return RestConnection(baseUri, HttpClients.custom().setDefaultCredentialsProvider(credentials).build())
        }
    }
}
