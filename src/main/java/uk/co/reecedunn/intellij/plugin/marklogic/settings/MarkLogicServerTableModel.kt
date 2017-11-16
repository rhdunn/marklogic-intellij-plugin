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

import uk.co.reecedunn.intellij.plugin.marklogic.resources.MarkLogicBundle
import javax.swing.table.AbstractTableModel

private val COLUMN_NAMES = arrayOf(
    MarkLogicBundle.message("marklogic.settings.server.name"),
    MarkLogicBundle.message("marklogic.settings.server.hostname"),
    MarkLogicBundle.message("marklogic.settings.server.appserver-port"),
    MarkLogicBundle.message("marklogic.settings.server.admin-port"),
    MarkLogicBundle.message("marklogic.settings.server.username"),
    MarkLogicBundle.message("marklogic.settings.server.password"))

class MarkLogicServerTableModel : AbstractTableModel() {
    var servers: Array<MarkLogicServer> = arrayOf()

    override fun getRowCount(): Int =
        servers.size

    override fun getColumnCount(): Int =
        COLUMN_NAMES.size

    override fun getColumnName(column: Int): String =
        COLUMN_NAMES[column]

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        if (rowIndex < 0 || rowIndex >= servers.size)
            return Any()
        return when (columnIndex) {
            0 -> servers[rowIndex].displayName ?: ""
            1 -> servers[rowIndex].hostname
            2 -> servers[rowIndex].appServerPort
            3 -> servers[rowIndex].adminPort
            4 -> servers[rowIndex].username ?: ""
            5 -> servers[rowIndex].password ?: ""
            else -> Any()
        }
    }
}
