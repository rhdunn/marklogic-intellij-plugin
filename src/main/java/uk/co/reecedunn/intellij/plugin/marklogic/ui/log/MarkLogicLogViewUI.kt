/*
 * Copyright (C) 2017-2018 Reece H. Dunn
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

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import org.jetbrains.annotations.CalledInBackground
import uk.co.reecedunn.intellij.plugin.core.ui.ComboBox
import uk.co.reecedunn.intellij.plugin.marklogic.api.Connection
import uk.co.reecedunn.intellij.plugin.marklogic.api.LogRequestBuilder
import uk.co.reecedunn.intellij.plugin.marklogic.log.MarkLogicLogFile
import uk.co.reecedunn.intellij.plugin.marklogic.server.LogType
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicAppServer
import uk.co.reecedunn.intellij.plugin.marklogic.ui.settings.MarkLogicSettings
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicServer
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersion
import uk.co.reecedunn.intellij.plugin.marklogic.ui.server.MarkLogicServerComboBox

import javax.swing.*
import java.io.IOException
import javax.swing.text.StyledDocument

class MarkLogicLogViewUI(private val mProject: Project) : LogViewActions {
    private var mSettings: MarkLogicSettings? = null
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
        mSettings = MarkLogicSettings.getInstance()

        mActions = MarkLogicLogViewToolbar(this)
        mActionToolbar = mActions!!.component

        mLogText = MarkLogicLogView()

        mServer = MarkLogicServerComboBox()
        mServer!!.addActionListener { _ -> serverSelectionChanged() }

        mAppServer = ComboBox()
        mAppServer!!.addActionListener { _ -> appserverSelectionChanged() }

        (mServer as? MarkLogicSettings.Listener)!!.serversChanged()
    }

    private fun serverSelectionChanged() {
        val server = mServer?.selectedItem as? MarkLogicServer
        if (server == null) {
            mLogText!!.text = ""
            return
        }

        ApplicationManager.getApplication().executeOnPooledThread {
            val appservers = server.appservers.toList()
            SwingUtilities.invokeLater {
                (mAppServer as? ComboBox<MarkLogicAppServer>)!!.items = appservers
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
        ApplicationManager.getApplication().executeOnPooledThread(refreshLog())
    }

    // region LogViewActions

    private fun updateLog(): StyledDocument {
        try {
            val appserver = (mAppServer?.selectedItem as? MarkLogicAppServer) ?: MarkLogicAppServer.SYSTEM
            val appserverName = appserver.let {
                if (it === MarkLogicAppServer.SYSTEM) null else it.appserver
            }

            val marklogicVersion =
                    (mServer?.selectedItem as? MarkLogicServer)?.version ?: MarkLogicVersion(6, 0)

            mLogBuilder!!.logFile = appserver.logfile(LogType.ERROR_LOG, 0, marklogicVersion)
            val items = mLogBuilder!!.build().run().items

            return (mLogText as? MarkLogicLogView)!!.update(
                    MarkLogicLogFile.parse(items[0].content, marklogicVersion),
                    appserverName,
                    marklogicVersion,
                    mSettings)
        } catch (e: IOException) {
            return (mLogText as? MarkLogicLogView)!!.logException(e, mSettings)
        }
    }

    @CalledInBackground
    override fun refreshLog(): Runnable {
        return Runnable {
            val doc = updateLog()
            SwingUtilities.invokeLater {
                val position = mLogText!!.caretPosition
                val scrollToEnd = scrollToEnd

                mLogText!!.styledDocument = doc

                val length = mLogText!!.document.length
                mLogText!!.caretPosition = if (scrollToEnd) length else Math.min(position, length)
                if (mLogText!!.hasFocus()) {
                    mLogText!!.caret.isVisible = true
                }
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
