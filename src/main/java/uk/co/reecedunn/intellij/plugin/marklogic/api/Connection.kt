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

import com.intellij.execution.ExecutionException
import uk.co.reecedunn.intellij.plugin.marklogic.api.rest.RestConnection
import uk.co.reecedunn.intellij.plugin.marklogic.api.xcc.XCCConnection
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersion
import uk.co.reecedunn.intellij.plugin.marklogic.server.*

abstract class Connection protected constructor() {
    abstract fun close()

    abstract fun createEvalRequestBuilder(): EvalRequestBuilder

    abstract fun createLogRequestBuilder(): LogRequestBuilder

    companion object {
        /**
         * MarkLogic version to use the XCC API in newConnection.
         */
        val XCC = MARKLOGIC_5

        /**
         * MarkLogic version to use the REST API in newConnection, including fetching error logs.
         */
        val REST = MARKLOGIC_8

        @Throws(ExecutionException::class)
        fun newConnection(hostname: String, port: Int, username: String?, password: String?, markLogicVersion: MarkLogicVersion): Connection {
            return if (markLogicVersion.major >= REST.major) {
                RestConnection.newConnection(hostname, port, username, password)
            } else {
                XCCConnection.newConnection(hostname, port, username, password)
            }
        }
    }
}
