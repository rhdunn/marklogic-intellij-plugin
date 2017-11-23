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
package uk.co.reecedunn.intellij.plugin.marklogic.server

import uk.co.reecedunn.intellij.plugin.marklogic.api.Connection
import uk.co.reecedunn.intellij.plugin.marklogic.api.Item
import uk.co.reecedunn.intellij.plugin.marklogic.ui.resources.MarkLogicBundle
import java.io.IOException

private val MARKLOGIC_VERSION_XQUERY =
    "xdmp:version()"

private val LIST_APPSERVERS_XQUERY =
    "import module namespace admin = \"http://marklogic.com/xdmp/admin\" at \"/MarkLogic/admin.xqy\";\n" +
    "let \$config := admin:get-configuration()\n" +
    "for \$groupId in admin:get-group-ids(\$config)\n" +
    "let \$groupName := admin:group-get-name(\$config, \$groupId)\n" +
    "for \$appServerId in admin:group-get-appserver-ids(\$config, \$groupId)" +
    "let \$appServerName := admin:appserver-get-name(\$config, \$appServerId)" +
    "let \$port := admin:appserver-get-port(\$config, \$appServerId)" +
    "let \$type := admin:appserver-get-type(\$config, \$appServerId)" +
    "return (\$groupName, \$appServerName, \$type, \$port)"

/**
 * Connection details for a MarkLogic server.
 */
class MarkLogicServer {
    /**
      * A user-friendly server name; will use `hostname` if this is `null`.
      */
    var displayName: String? = null

    /**
     * The server name.
     */
    var hostname: String = "localhost"

    /**
     * `App-Server` port, used for running `eval` and `invoke` queries.
     */
    var appServerPort: Int = 8000

    /**
     * `Admin` port, used for displaying error log files.
     */
    var adminPort: Int = 8001

    /**
     * The username to connect to the MarkLogic server.
     */
    var username: String? = null

    /**
     * The password to connect to the MarkLogic server.
     */
    var password: String? = null

    constructor()

    constructor(displayName: String?, hostname: String, appServerPort: Int, adminPort: Int, username: String?, password: String?) {
        this.displayName = displayName
        this.hostname = hostname
        this.appServerPort = appServerPort
        this.adminPort = adminPort
        this.username = username
        this.password = password
    }

    override fun toString(): String =
        displayName ?: hostname

    fun xquery(query: String): Array<Item> {
        val connection = Connection.newConnection(hostname, appServerPort, username, password, Connection.XCC)
        val queryBuilder = connection.createEvalRequestBuilder()
        queryBuilder.xQuery = query
        return queryBuilder.build().run().items
    }

    val version get(): String =
        xquery(MARKLOGIC_VERSION_XQUERY)[0].content

    val appservers get(): List<MarkLogicAppServer> {
        val servers = ArrayList<MarkLogicAppServer>()
        try {
            val items = xquery(LIST_APPSERVERS_XQUERY)
            servers.ensureCapacity((items.size / 4) + 1)
            servers.add(MarkLogicAppServer(null, MarkLogicBundle.message("logviewer.app-server.none"), null, 0))
            servers.add(MarkLogicAppServer(null, "Task Server", null, 0))
            for (i in 0..(items.size - 1) step 4) {
                val server = MarkLogicAppServer(
                    items[i].content,
                    items[i + 1].content,
                    items[i + 2].content.toUpperCase(),
                    items[i + 3].content.toInt())
                servers.add(server)
            }
        } catch (e: IOException) {
            if (servers.isEmpty()) {
                servers.add(MarkLogicAppServer(null, MarkLogicBundle.message("logviewer.app-server.none"), null, 0))
                servers.add(MarkLogicAppServer(null, "Task Server", null, 0))
            }
        }
        return servers
    }

}
