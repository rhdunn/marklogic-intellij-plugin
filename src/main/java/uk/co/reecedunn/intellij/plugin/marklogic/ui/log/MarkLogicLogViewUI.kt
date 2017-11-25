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
import uk.co.reecedunn.intellij.plugin.marklogic.log.MarkLogicLogEntry
import uk.co.reecedunn.intellij.plugin.marklogic.log.MarkLogicLogFile
import uk.co.reecedunn.intellij.plugin.marklogic.server.LogType
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicAppServer
import uk.co.reecedunn.intellij.plugin.marklogic.ui.settings.MarkLogicSettings
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicServer
import uk.co.reecedunn.intellij.plugin.marklogic.ui.server.MarkLogicServerCellRenderer

import javax.swing.*
import java.io.IOException
import javax.swing.text.StyleConstants
import java.awt.Color
import java.awt.Font
import javax.swing.text.AttributeSet
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleContext

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
    private var mLogText: JTextPane? = null
    private var mServer: JComboBox<MarkLogicServer>? = null
    private var mAppServer: JComboBox<MarkLogicAppServer>? = null

    val panel get(): JComponent? =
        mPanel

    private fun createUIComponents() {
        mActions = MarkLogicLogViewToolbar(this)
        mActionToolbar = mActions!!.component

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
        ApplicationManager.getApplication().executeOnPooledThread(refreshAction())
    }

    // region Log View Pane

    private fun createTextAttributes(font: Font?, color: Color?): AttributeSet {
        val context = StyleContext.getDefaultStyleContext()
        var attributes: AttributeSet =
            context.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Alignment, StyleConstants.ALIGN_LEFT)
        if (color != null) {
            attributes = context.addAttribute(attributes, StyleConstants.Foreground, color)
        }
        if (font != null) {
            attributes = context.addAttribute(attributes, StyleConstants.FontFamily, font.family)
            attributes = context.addAttribute(attributes, StyleConstants.FontSize, font.size)
        }
        return attributes
    }

    private fun appendLogEntry(entry: MarkLogicLogEntry, color: Color? = null) {
        val separator = if (entry.continuation) '+' else ' '
        mLogText!!.caretPosition = mLogText!!.document.length
        mLogText!!.setCharacterAttributes(createTextAttributes(UIManager.getFont("Label.font"), color), false)
        mLogText!!.replaceSelection(
            "${entry.date} ${entry.time} ${entry.level.displayName}:${separator}${entry.message.content}\n")
    }

    // endregion
    // region LogViewActions

    override fun refreshAction(): Runnable {
        return Runnable {
            try {
                val appserver = (mAppServer?.selectedItem as? MarkLogicAppServer) ?: MarkLogicAppServer.SYSTEM
                val appserverName = appserver.let {
                    if (it === MarkLogicAppServer.SYSTEM) null else it.appserver
                }

                val marklogicVersion =
                    (mServer?.selectedItem as? MarkLogicServer)?.version?.split('-')?.get(0)?.toDouble() ?: 6.0

                mLogBuilder!!.logFile = appserver.logfile(LogType.ERROR_LOG, 0, marklogicVersion)
                val items = mLogBuilder!!.build().run().items

                val position = mLogText!!.caretPosition
                val scrollToEnd = scrollToEnd

                mLogText!!.text = ""
                MarkLogicLogFile.parse(items[0].content, marklogicVersion).forEach { entry ->
                    if (marklogicVersion >= 9.0) {
                        appendLogEntry(entry)
                    } else if (appserverName == entry.appserver) {
                        appendLogEntry(entry)
                    }
                }

                mLogText!!.caretPosition = if (scrollToEnd) mLogText!!.document.length else position
            } catch (e: IOException) {
                mLogText!!.text = e.message
            }
        }
    }

    override var scrollToEnd: Boolean
        get() = mLogText!!.caretPosition == mLogText!!.document.length
        set(value) {
            if (value) {
                mLogText!!.caretPosition = mLogText!!.document.length
            }
        }

    // endregion
}
