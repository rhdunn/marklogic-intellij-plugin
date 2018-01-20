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

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.application.ApplicationManager
import uk.co.reecedunn.intellij.plugin.marklogic.ui.resources.MarkLogicBundle
import javax.swing.JComponent

interface LogViewActions {
    fun refreshLog(): Runnable

    var scrollToEnd: Boolean
}

private class RefreshAction(val actions: LogViewActions) : AnAction() {
    init {
        val message = MarkLogicBundle.message("action.refresh")
        templatePresentation.description = message
        templatePresentation.text = message
        templatePresentation.icon = AllIcons.Actions.Refresh
    }

    override fun actionPerformed(e: AnActionEvent?) {
        ApplicationManager.getApplication().executeOnPooledThread(actions.refreshLog())
    }
}

private class ScrollToEndAction(val actions: LogViewActions) : ToggleAction() {
    init {
        val message = MarkLogicBundle.message("action.scroll-to-end")
        templatePresentation.description = message
        templatePresentation.text = message
        templatePresentation.icon = AllIcons.RunConfigurations.Scroll_down
    }

    override fun isSelected(e: AnActionEvent?): Boolean =
        actions.scrollToEnd

    override fun setSelected(e: AnActionEvent?, state: Boolean) {
        actions.scrollToEnd = state
    }
}

class MarkLogicLogViewToolbar(actions: LogViewActions) {
    private val group: ActionGroup = DefaultActionGroup(
        RefreshAction(actions),
        ScrollToEndAction(actions))

    private val toolbar: ActionToolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.TOOLBAR, group, false)

    val component get(): JComponent = toolbar.component
}
