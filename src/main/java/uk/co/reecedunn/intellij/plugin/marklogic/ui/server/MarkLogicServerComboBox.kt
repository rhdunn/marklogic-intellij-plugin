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

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.ColoredListCellRenderer
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicServer
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersion
import uk.co.reecedunn.intellij.plugin.marklogic.ui.settings.MarkLogicSettings
import javax.swing.JList

private class MarkLogicServerCellRenderer(val cache: HashMap<MarkLogicServer, MarkLogicVersion?>): ColoredListCellRenderer<MarkLogicServer>() {
    private fun format(server: MarkLogicServer, version: MarkLogicVersion?) {
        clear()
        append(version?.let { "$server (MarkLogic $version)" } ?: server.toString())
    }

    override fun customizeCellRenderer(list: JList<out MarkLogicServer>, value: MarkLogicServer?, index: Int, selected: Boolean, hasFocus: Boolean) {
        value?.let {
            cache[value]?.let { format(value, it) } ?: ApplicationManager.getApplication().executeOnPooledThread({
                val version = try { value.version } catch (e: Exception) { null }
                cache.put(value, version)
                format(value, version)
            })
        }
    }

}

class MarkLogicServerComboBox : ComboBox<MarkLogicServer>(), MarkLogicSettings.Listener, Disposable {
    val cache: HashMap<MarkLogicServer, MarkLogicVersion?> = HashMap()

    init {
        renderer = MarkLogicServerCellRenderer(cache)
        MarkLogicSettings.getInstance().addListener(this, this)
    }

    val server get(): MarkLogicServer? =
        selectedItem as? MarkLogicServer

    var hostname: String?
        get() = server?.hostname
        set(value) {
            for (i in 0 until itemCount) {
                if (getItemAt(i).hostname == value) {
                    selectedIndex = i
                }
            }
        }

    val version get(): MarkLogicVersion? =
        cache[server]

    override fun serversChanged() {
        val settings = MarkLogicSettings.getInstance()

        removeAllItems()
        for (server in settings.servers) {
            addItem(server)
        }
    }

    override fun dispose() {
    }
}
