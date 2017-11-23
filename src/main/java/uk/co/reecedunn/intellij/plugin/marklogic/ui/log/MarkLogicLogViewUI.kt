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
package uk.co.reecedunn.intellij.plugin.marklogic.ui.log

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import uk.co.reecedunn.intellij.plugin.marklogic.api.Connection
import uk.co.reecedunn.intellij.plugin.marklogic.api.LogRequestBuilder
import uk.co.reecedunn.intellij.plugin.marklogic.log.MarkLogicLogFile
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicAppServer
import uk.co.reecedunn.intellij.plugin.marklogic.ui.settings.MarkLogicSettings
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicServer
import uk.co.reecedunn.intellij.plugin.marklogic.ui.server.MarkLogicServerCellRenderer

import javax.swing.*
import java.io.IOException

class MarkLogicLogViewUI(private val mProject: Project) : LogViewActions {
    private inner class SettingsListener : MarkLogicSettings.Listener, Disposable {
        override fun serversChanged() {
            val settings = MarkLogicSettings.getInstance()

            mServer!!.removeAllItems()
            for (server in settings.servers) {
                mServer!!.addItem(server)
            }
        }

        override fun dispose() {
        }
    }

    private var mConnection: Connection? = null
    private var mLogBuilder: LogRequestBuilder? = null

    private var mActions: MarkLogicLogViewToolbar? = null
    private var mActionToolbar: JComponent? = null

    private var mPanel: JPanel? = null
    private var mLogText: JTextArea? = null
    private var mServer: JComboBox<MarkLogicServer>? = null
    private var mAppServer: JComboBox<MarkLogicAppServer>? = null

    val panel get(): JComponent? =
        mPanel

    private fun createUIComponents() {
        mActions = MarkLogicLogViewToolbar(this)
        mActionToolbar = mActions!!.component

        mLogText = JTextArea()
        mLogText!!.isEditable = false

        mServer = ComboBox()
        mServer!!.renderer = MarkLogicServerCellRenderer()
        mServer!!.addActionListener { e -> serverSelectionChanged() }

        mAppServer = ComboBox()
        mAppServer!!.addActionListener { e -> appserverSelectionChanged() }

        val settings = MarkLogicSettings.getInstance()
        val listener = SettingsListener()
        settings.addListener(listener, listener)
        listener.serversChanged()
    }

    private fun serverSelectionChanged() {
        val server = mServer?.selectedItem as? MarkLogicServer
        if (server == null) {
            mLogText!!.text = ""
            return
        }

        ApplicationManager.getApplication().executeOnPooledThread {
            mAppServer!!.removeAllItems()
            for (appserver in server.appservers) {
                mAppServer!!.addItem(appserver)
            }
        }

        mConnection = Connection.newConnection(
            server.hostname,
            server.adminPort,
            server.username,
            server.password,
            Connection.REST)

        mLogBuilder = mConnection!!.createLogRequestBuilder()
        appserverSelectionChanged()
    }

    private fun appserverSelectionChanged() {
        if (mLogBuilder == null) return

        val port = (mAppServer?.selectedItem as? MarkLogicAppServer)?.port ?: 0
        mLogBuilder!!.logFile = if (port == 0) "ErrorLog.txt" else "${port}_ErrorLog.txt"

        ApplicationManager.getApplication().executeOnPooledThread(refreshAction())
    }

    override fun refreshAction(): Runnable {
        return Runnable {
            try {
                val items = mLogBuilder!!.build().run().items
                mLogText!!.text = ""
                MarkLogicLogFile.parse(items[0].content).forEach { entry ->
                    val separator = if (entry.continuation) '+' else ' '
                    mLogText!!.append("${entry.date} ${entry.time} ${entry.level}:${separator}${entry.message.content}\n")
                }
                mLogText!!.caretPosition = mLogText!!.document.length
            } catch (e: IOException) {
                mLogText!!.text = e.message
            }
        }
    }
}
