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

import org.apache.http.client.methods.HttpUriRequest
import org.apache.http.util.EntityUtils
import uk.co.reecedunn.intellij.plugin.marklogic.api.Request
import uk.co.reecedunn.intellij.plugin.marklogic.api.Response
import uk.co.reecedunn.intellij.plugin.marklogic.api.mime.MimeResponse

class RestRequest internal constructor(private val request: HttpUriRequest, private val connection: RestConnection) : Request {
    override fun run(): Response {
        val response = connection.client.execute(request)
        val status = response.statusLine
        val headers = response.allHeaders
        val body = EntityUtils.toString(response.entity)
        response.close()

        return RestResponse(MimeResponse(status, headers, body))
    }
}
