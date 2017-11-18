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

import com.intellij.openapi.ui.DialogBuilder
import com.intellij.ui.components.JBPasswordField
import com.intellij.ui.components.JBTextField
import com.intellij.util.Function
import com.intellij.util.ui.ColumnInfo
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.table.TableModelEditor
import com.intellij.util.ui.table.TableModelEditor.EditableColumnInfo
import uk.co.reecedunn.intellij.plugin.marklogic.resources.MarkLogicBundle

private object DISPLAY_NAME_COLUMN_INFO: EditableColumnInfo<MarkLogicServer, String>(
        MarkLogicBundle.message("marklogic.settings.server.name")) {
    override fun valueOf(item: MarkLogicServer?): String? =
        item?.displayName
}

private object HOSTNAME_COLUMN_INFO: EditableColumnInfo<MarkLogicServer, String>(
        MarkLogicBundle.message("marklogic.settings.server.hostname")) {
    override fun valueOf(item: MarkLogicServer?): String? =
        item?.hostname
}

private object APPSERVER_PORT_COLUMN_INFO: EditableColumnInfo<MarkLogicServer, Int>(
        MarkLogicBundle.message("marklogic.settings.server.appserver-port")) {
    override fun valueOf(item: MarkLogicServer?): Int? =
        item?.appServerPort
}

private object ADMIN_PORT_COLUMN_INFO: EditableColumnInfo<MarkLogicServer, Int>(
        MarkLogicBundle.message("marklogic.settings.server.admin-port")) {
    override fun valueOf(item: MarkLogicServer?): Int? =
        item?.adminPort
}

private object ServerEditor: TableModelEditor.DialogItemEditor<MarkLogicServer> {
    override fun edit(item: MarkLogicServer, mutator: Function<MarkLogicServer, MarkLogicServer>, isAdd: Boolean) {
        val username = JBTextField(20)
        val password = JBPasswordField()
        val dialog = DialogBuilder()
        dialog.title(MarkLogicBundle.message("marklogic.settings.server.dialog.title"))
            .resizable(false)
            .centerPanel(FormBuilder
                .createFormBuilder()
                .addLabeledComponent(MarkLogicBundle.message("marklogic.settings.server.dialog.username"), username)
                .addLabeledComponent(MarkLogicBundle.message("marklogic.settings.server.dialog.password"), password)
                .panel)
            .setPreferredFocusComponent(username)
        if (dialog.showAndGet()) {
            mutator.`fun`(item).username = username.text
            mutator.`fun`(item).password = String(password.password)
        }
    }

    override fun getItemClass(): Class<out MarkLogicServer> =
        MarkLogicServer::class.java

    override fun clone(item: MarkLogicServer, forInPlaceEditing: Boolean): MarkLogicServer {
        return MarkLogicServer(
            item.displayName,
            item.hostname,
            item.appServerPort,
            item.adminPort,
            item.username,
            item.password)
    }

    override fun applyEdited(oldItem: MarkLogicServer, newItem: MarkLogicServer) {
        newItem.username = oldItem.username
        newItem.password = oldItem.password
    }
}

private val COLUMNS: Array<ColumnInfo<*, *>> = arrayOf(
    DISPLAY_NAME_COLUMN_INFO,
    HOSTNAME_COLUMN_INFO,
    APPSERVER_PORT_COLUMN_INFO,
    ADMIN_PORT_COLUMN_INFO)

class MarkLogicServerTable: TableModelEditor<MarkLogicServer>(
    COLUMNS,
    ServerEditor,
    MarkLogicBundle.message("marklogic.settings.server.no-servers"))
