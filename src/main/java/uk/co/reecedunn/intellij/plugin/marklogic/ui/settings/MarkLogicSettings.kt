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

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.*
import com.intellij.util.EventDispatcher
import com.intellij.util.xmlb.XmlSerializerUtil
import uk.co.reecedunn.intellij.plugin.marklogic.log.LogLevel
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicServer
import java.awt.Color
import java.util.*

@State(name = "MarkLogicSettings", storages = arrayOf(Storage("marklogic_config.xml")))
class MarkLogicSettings : PersistentStateComponent<MarkLogicSettings> {
    // region Event Handlers

    interface Listener : EventListener {
        fun serversChanged()
    }

    @Transient
    private val eventDispatcher: EventDispatcher<Listener> = EventDispatcher.create(Listener::class.java)

    fun addListener(listener: Listener, disposable: Disposable) {
        eventDispatcher.addListener(listener, disposable)
    }

    fun removeListener(listener: Listener) {
        eventDispatcher.removeListener(listener)
    }

    // endregion
    // region Settings Data

    var servers: List<MarkLogicServer> = ArrayList()
        set(value) {
            field = value
            eventDispatcher.multicaster.serversChanged()
        }

    fun logColor(level: LogLevel): Color? =
        level.defaultColor

    // endregion
    // region PersistentStateComponent

    override fun getState(): MarkLogicSettings? = this

    override fun loadState(state: MarkLogicSettings) = XmlSerializerUtil.copyBean(state, this)

    // endregion
    // region Instance

    companion object {
        fun getInstance(): MarkLogicSettings {
            return ServiceManager.getService(MarkLogicSettings::class.java)
        }
    }

    // endregion
}
