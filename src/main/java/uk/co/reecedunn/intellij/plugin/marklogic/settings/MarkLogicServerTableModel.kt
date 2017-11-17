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

import com.intellij.util.ui.ColumnInfo
import com.intellij.util.ui.SortableColumnModel
import javax.swing.RowSorter
import javax.swing.table.AbstractTableModel

private val COLUMN_INFOS: Array<ColumnInfo<*, *>> = arrayOf(
    DISPLAY_NAME_COLUMN_INFO,
    HOSTNAME_COLUMN_INFO,
    APPSERVER_PORT_COLUMN_INFO,
    ADMIN_PORT_COLUMN_INFO)

class MarkLogicServerTableModel : AbstractTableModel(), SortableColumnModel {
    override fun isSortable(): Boolean =
        false

    @Suppress("UNCHECKED_CAST")
    override fun getColumnInfos(): Array<ColumnInfo<Any, Any>> =
        COLUMN_INFOS as Array<ColumnInfo<Any, Any>>

    override fun setSortable(aBoolean: Boolean) {
    }

    override fun getDefaultSortKey(): RowSorter.SortKey? =
        null

    override fun getRowValue(row: Int): Any =
        servers[row]

    var servers: Array<MarkLogicServer> = arrayOf()

    // AbstractTableModel

    override fun getRowCount(): Int =
        servers.size

    override fun getColumnCount(): Int =
        columnInfos.size

    override fun getColumnName(column: Int): String =
        columnInfos[column].name

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any =
        columnInfos[columnIndex].valueOf(getRowValue(rowIndex))!!
}
