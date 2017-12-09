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
import com.intellij.ui.ColoredListCellRenderer
import uk.co.reecedunn.intellij.plugin.core.ui.ComboBox
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicServer
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersion
import uk.co.reecedunn.intellij.plugin.marklogic.ui.settings.MarkLogicSettings
import javax.swing.JList

private class MarkLogicServerCellRenderer(val cache: HashMap<MarkLogicServer, Any?>): ColoredListCellRenderer<MarkLogicServer>() {
    private fun format(server: MarkLogicServer, version: Any?) {
        clear()
        when (version) {
            is MarkLogicVersion -> append("$server (MarkLogic $version)")
            else -> append(server.toString())
        }
    }

    override fun customizeCellRenderer(list: JList<out MarkLogicServer>, value: MarkLogicServer?, index: Int, selected: Boolean, hasFocus: Boolean) {
        value?.let {
            val cachedVersion = cache[value]
            cachedVersion?.let { format(value, cachedVersion) } ?: ApplicationManager.getApplication().executeOnPooledThread({
                val version: Any? = try { value.version } catch (e: Exception) { e }
                cache.put(value, version)
                format(value, version)
            })
        }
    }

}

class MarkLogicServerComboBox : ComboBox<MarkLogicServer>(), MarkLogicSettings.Listener, Disposable {
    val cache: HashMap<MarkLogicServer, Any?> = HashMap()

    init {
        setRenderer(MarkLogicServerCellRenderer(cache))
        MarkLogicSettings.getInstance().addListener(this, this)
    }

    val server get(): MarkLogicServer? =
        selectedItem as? MarkLogicServer

    var hostname: String?
        get() = server?.hostname
        set(value) {
            items.find { item -> item.hostname == value }.let { item -> selectedItem = item }
        }

    val version get(): MarkLogicVersion? =
        cache[server] as? MarkLogicVersion

    val exception get(): Exception? =
        cache[server] as? Exception

    override fun serversChanged() {
        val settings = MarkLogicSettings.getInstance()
        cache.clear()
        items = settings.servers.asSequence()
    }

    override fun dispose() {
        MarkLogicSettings.getInstance().removeListener(this)
    }
}
