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
package uk.co.reecedunn.intellij.plugin.marklogic.settings

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil
import uk.co.reecedunn.intellij.plugin.marklogic.resources.MarkLogicBundle
import java.io.File

@State(name = "MarkLogicProjectSettings", storages = arrayOf(Storage(StoragePathMacros.WORKSPACE_FILE), Storage("marklogic_config.xml")))
class MarkLogicProjectSettings : PersistentStateComponent<MarkLogicProjectSettings>, ExportableComponent {
    // region PersistentStateComponent

    override fun getState(): MarkLogicProjectSettings? = this

    override fun loadState(state: MarkLogicProjectSettings) = XmlSerializerUtil.copyBean(state, this)

    // endregion
    // region ExportableComponent

    override fun getExportFiles(): Array<File> = arrayOf(PathManager.getOptionsFile("marklogic_project_settings"))

    override fun getPresentableName(): String = MarkLogicBundle.message("marklogic.settings.project.title")

    // endregion

    companion object {
        fun getInstance(project: Project): MarkLogicProjectSettings {
            return ServiceManager.getService(project, MarkLogicProjectSettings::class.java)
        }
    }
}
