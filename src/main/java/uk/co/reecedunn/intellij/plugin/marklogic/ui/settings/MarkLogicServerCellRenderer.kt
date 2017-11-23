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
package uk.co.reecedunn.intellij.plugin.marklogic.ui.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.ui.ColoredListCellRenderer
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicServer
import javax.swing.JList

class MarkLogicServerCellRenderer: ColoredListCellRenderer<MarkLogicServer>() {
    val cache: HashMap<MarkLogicServer, String> = HashMap()

    private fun format(server: MarkLogicServer, version: String?) {
        clear()
        if (version?.isEmpty() != false) {
            append(server.toString())
        } else {
            append("$server (MarkLogic $version)")
        }
    }

    override fun customizeCellRenderer(list: JList<out MarkLogicServer>, value: MarkLogicServer?, index: Int, selected: Boolean, hasFocus: Boolean) {
        value?.let {
            cache[value]?.let { format(value, it) } ?: ApplicationManager.getApplication().executeOnPooledThread({
                val version = try { value.version } catch (e: Exception) { "" }
                cache.put(value, version)
                format(value, version)
            })
        }
    }

}