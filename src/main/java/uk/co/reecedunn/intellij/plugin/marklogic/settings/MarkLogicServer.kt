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
package uk.co.reecedunn.intellij.plugin.marklogic.settings

import uk.co.reecedunn.intellij.plugin.marklogic.api.Connection
import uk.co.reecedunn.intellij.plugin.marklogic.api.Item

/**
 * Connection details for a MarkLogic server.
 */
data class MarkLogicServer(
        /**
          * A user-friendly server name; will use `hostname` if this is `null`.
          */
        var displayName: String?,
        /**
         * The server name.
         */
        var hostname: String,
        /**
         * `App-Server` port, used for running `eval` and `invoke` queries.
         */
        var appServerPort: Int,
        /**
         * `Admin` port, used for displaying error log files.
         */
        var adminPort: Int,
        /**
         * The username to connect to the MarkLogic server.
         */
        var username: String?,
        /**
         * The password to connect to the MarkLogic server.
         */
        var password: String?) {

    constructor() : this(null, "localhost", 8000, 8001, null, null)

    override fun toString(): String {
        val version = xqueryVersion
        return if (version == null)
            displayName ?: hostname
        else
            (displayName ?: hostname) + " (MarkLogic " + version + ")"
    }

    fun xquery(query: String): Array<Item> {
        val connection = Connection.newConnection(hostname, appServerPort, username, password, Connection.XCC)
        val queryBuilder = connection.createEvalRequestBuilder()
        queryBuilder.xQuery = query
        return queryBuilder.build().run().items
    }

    val xqueryVersion get(): String? {
        return try {
            xquery("xdmp:version()")[0].content
        } catch (e: Exception) {
            null
        }
    }

}
