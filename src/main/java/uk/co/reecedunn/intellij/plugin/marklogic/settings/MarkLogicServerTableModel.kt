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
    override fun getRowCount(): Int =
        0

    override fun getColumnCount(): Int =
        COLUMN_NAMES.size

    override fun getColumnName(column: Int): String =
        COLUMN_NAMES[column]

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any =
        Any()
}