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

import com.marklogic.xcc.Session
import com.marklogic.xcc.exceptions.RequestException
import com.marklogic.xcc.exceptions.RequestPermissionException
import com.marklogic.xcc.exceptions.ServerResponseException
import uk.co.reecedunn.intellij.plugin.marklogic.api.Request
import uk.co.reecedunn.intellij.plugin.marklogic.api.Response
import uk.co.reecedunn.intellij.plugin.marklogic.api.ResponseException

import java.io.IOException

class XCCRequest internal constructor(private val session: Session, private val request: com.marklogic.xcc.Request) : Request {
    override fun run(): Response {
        try {
            return XCCResponse(session.submitRequest(request), session)
        } catch (e: ServerResponseException) {
            throw ResponseException(e.responseCode, e.responseMessage, null)
        } catch (e: RequestPermissionException) {
            throw ResponseException(401, "Unauthorized", null)
        } catch (e: RequestException) {
            throw IOException(e.message, e)
        } catch (e: IllegalStateException) {
            throw IOException(e.message, e)
        }
    }
}
