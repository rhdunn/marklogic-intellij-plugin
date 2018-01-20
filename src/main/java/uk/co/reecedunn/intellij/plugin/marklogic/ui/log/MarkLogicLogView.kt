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

import uk.co.reecedunn.intellij.plugin.marklogic.log.LogLevel
import uk.co.reecedunn.intellij.plugin.marklogic.log.MarkLogicLogEntry
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersion
import uk.co.reecedunn.intellij.plugin.marklogic.ui.settings.MarkLogicSettings
import java.awt.Color
import java.awt.Font
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import java.awt.event.MouseEvent
import javax.swing.JTextPane
import javax.swing.UIManager
import javax.swing.text.*

private class LogViewFocusListener : FocusListener {
    override fun focusLost(e: FocusEvent?) {
        (e?.component as? JTextPane)?.caret?.isVisible = false
    }

    override fun focusGained(e: FocusEvent?) {
        (e?.component as? JTextPane)?.caret?.isVisible = true
    }
}

class MarkLogicLogDocumentBuilder {
    val doc = DefaultStyledDocument()

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

    fun append(entry: MarkLogicLogEntry, color: Color? = null) {
        val separator = if (entry.continuation) '+' else ' '
        append("${entry.date} ${entry.time} ${entry.level.displayName}:$separator${entry.message.content}", color)
    }

    fun append(message: String, color: Color? = null) {
        doc.insertString(0, "$message\n", createTextAttributes(UIManager.getFont("TextArea.font"), color))
    }
}

class MarkLogicLogView: JTextPane() {
    init {
        isEditable = false
        addFocusListener(LogViewFocusListener())
    }

    fun update(entries: Sequence<MarkLogicLogEntry>,
               appserverName: String?,
               marklogicVersion: MarkLogicVersion,
               settings: MarkLogicSettings?): StyledDocument {
        val builder = MarkLogicLogDocumentBuilder()
        entries.forEach { entry ->
            val color = settings?.logColor(entry.level)
            if (marklogicVersion.major >= 9) {
                builder.append(entry, color)
            } else if (appserverName == entry.appserver) {
                builder.append(entry, color)
            }
        }
        return builder.doc
    }

    fun logException(e: Exception, settings: MarkLogicSettings?): StyledDocument {
        val builder = MarkLogicLogDocumentBuilder()
        e.message?.let { builder.append(it, settings?.logColor(LogLevel.ERROR)) }
        return builder.doc
    }

    // JTextComponent.getToolTipText can throw an NPE in some cases.
    // Specifically:
    // 1.  Switching from the System app-server logs, to the TaskServer logs;
    // 2.  Moving the mouse between the log view text pane and the app-server
    //     combobox.
    //
    // This NPE occurs in javax.swing.text.View, line 1041. Therefore, disable
    // tooltips for this control.
    override fun getToolTipText(event: MouseEvent?): String? =
        null

    override fun updateUI() {
        super.updateUI()
        background = UIManager.getColor("TextField.background")
    }
}
