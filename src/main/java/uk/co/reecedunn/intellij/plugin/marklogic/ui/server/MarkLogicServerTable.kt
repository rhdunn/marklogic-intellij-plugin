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
package uk.co.reecedunn.intellij.plugin.marklogic.ui.server

import com.intellij.openapi.ui.DialogBuilder
import com.intellij.ui.components.JBPasswordField
import com.intellij.ui.components.JBTextField
import com.intellij.util.Function
import com.intellij.util.ui.ColumnInfo
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.table.TableModelEditor
import com.intellij.util.ui.table.TableModelEditor.EditableColumnInfo
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicServer
import uk.co.reecedunn.intellij.plugin.marklogic.ui.resources.MarkLogicBundle

private object DISPLAY_NAME_COLUMN_INFO: EditableColumnInfo<MarkLogicServer, String>(
        MarkLogicBundle.message("marklogic.settings.server.name")) {
    override fun valueOf(item: MarkLogicServer?): String? =
        item?.displayName

    override fun setValue(item: MarkLogicServer?, value: String?) {
        item?.displayName = if (value?.isEmpty() != false) null else value
    }
}

private object HOSTNAME_COLUMN_INFO: EditableColumnInfo<MarkLogicServer, String>(
        MarkLogicBundle.message("marklogic.settings.server.hostname")) {
    override fun valueOf(item: MarkLogicServer?): String? =
        item?.hostname

    override fun setValue(item: MarkLogicServer?, value: String?) {
        value?.let { item?.hostname = it }
    }
}

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
private object APPSERVER_PORT_COLUMN_INFO: EditableColumnInfo<MarkLogicServer, Integer>(
        MarkLogicBundle.message("marklogic.settings.server.appserver-port")) {
    override fun getColumnClass(): Class<*> =
        Integer::class.java

    override fun valueOf(item: MarkLogicServer?): Integer? =
        item?.let { Integer(it.appServerPort) }

    override fun setValue(item: MarkLogicServer?, value: Integer?) {
        value?.let { item?.appServerPort = it.toInt() }
    }
}

@Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
private object ADMIN_PORT_COLUMN_INFO: EditableColumnInfo<MarkLogicServer, Integer>(
        MarkLogicBundle.message("marklogic.settings.server.admin-port")) {
    override fun getColumnClass(): Class<*> =
        Integer::class.java

    override fun valueOf(item: MarkLogicServer?): Integer? =
        item?.let { Integer(it.adminPort) }

    override fun setValue(item: MarkLogicServer?, value: Integer?) {
        value?.let { item?.adminPort = it.toInt() }
    }
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

        item.username?.let { username.text = it }
        item.password?.let { password.text = it }
        if (dialog.showAndGet()) {
            mutator.`fun`(item).username = if (username.text.isEmpty()) null else username.text
            mutator.`fun`(item).password = if (password.password.isEmpty()) null else String(password.password)
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
        oldItem.username = newItem.username
        oldItem.password = newItem.password
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
