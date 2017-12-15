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
package uk.co.reecedunn.intellij.plugin.marklogic.ui.configuration

import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project

import javax.swing.*

class MarkLogicRunConfigurationEditor(private val mFactory: MarkLogicConfigurationFactory, private val mProject: Project) : SettingsEditor<MarkLogicRunConfiguration>() {
    private var mSettings: MarkLogicRunConfigurationEditorUI? = null

    override fun resetEditorFrom(configuration: MarkLogicRunConfiguration) {
        mSettings!!.reset(configuration)
    }

    @Throws(ConfigurationException::class)
    override fun applyEditorTo(configuration: MarkLogicRunConfiguration) {
        mSettings!!.apply(configuration)
    }

    override fun createEditor(): JComponent {
        mSettings = MarkLogicRunConfigurationEditorUI(mFactory, mProject)
        return mSettings?.panel!!
    }
}
