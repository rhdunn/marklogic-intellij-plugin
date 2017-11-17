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

import com.intellij.util.ui.table.TableModelEditor.EditableColumnInfo
import uk.co.reecedunn.intellij.plugin.marklogic.resources.MarkLogicBundle

object DISPLAY_NAME_COLUMN_INFO: EditableColumnInfo<MarkLogicServer, String>(MarkLogicBundle.message("marklogic.settings.server.name")) {
    override fun valueOf(item: MarkLogicServer?): String? =
        item?.displayName
}

object HOSTNAME_COLUMN_INFO: EditableColumnInfo<MarkLogicServer, String>(MarkLogicBundle.message("marklogic.settings.server.hostname")) {
    override fun valueOf(item: MarkLogicServer?): String? =
        item?.displayName
}

object APPSERVER_PORT_COLUMN_INFO: EditableColumnInfo<MarkLogicServer, Int>(MarkLogicBundle.message("marklogic.settings.server.appserver-port")) {
    override fun valueOf(item: MarkLogicServer?): Int? =
        item?.appServerPort
}

object ADMIN_PORT_COLUMN_INFO: EditableColumnInfo<MarkLogicServer, Int>(MarkLogicBundle.message("marklogic.settings.server.admin-port")) {
    override fun valueOf(item: MarkLogicServer?): Int? =
        item?.adminPort
}
