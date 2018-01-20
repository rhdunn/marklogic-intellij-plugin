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
package uk.co.reecedunn.intellij.plugin.marklogic.ui.profile

import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.icons.AllIcons
import uk.co.reecedunn.intellij.plugin.marklogic.ui.resources.MarkLogicBundle

import javax.swing.*

class ProfileExecutor : DefaultRunExecutor() {
    override fun getToolWindowId(): String =
        EXECUTOR_ID

    override fun getToolWindowIcon(): Icon =
        AllIcons.Actions.Profile

    override fun getIcon(): Icon =
        AllIcons.Actions.Profile

    override fun getDisabledIcon(): Icon =
        AllIcons.Actions.Profile

    override fun getDescription(): String? =
        null

    override fun getActionName(): String =
        MarkLogicBundle.message("profile.action.name")

    override fun getId(): String =
        EXECUTOR_ID

    override fun getStartActionText(): String =
        MarkLogicBundle.message("profile.start.action.text")

    override fun getContextActionId(): String =
        "MarkLogicProfileClass"

    override fun getHelpId(): String? =
        null

    companion object {
        const val EXECUTOR_ID = "MarkLogicProfile"
    }
}
