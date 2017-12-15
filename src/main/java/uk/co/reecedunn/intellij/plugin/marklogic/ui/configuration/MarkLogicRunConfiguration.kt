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

import com.intellij.execution.ExecutionException
import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.util.xmlb.XmlSerializerUtil
import uk.co.reecedunn.intellij.plugin.marklogic.query.RDFFormat
import uk.co.reecedunn.intellij.plugin.marklogic.query.SEM_TRIPLE
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicServer
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersion
import uk.co.reecedunn.intellij.plugin.marklogic.ui.runner.MarkLogicResultsHandler
import uk.co.reecedunn.intellij.plugin.marklogic.ui.runner.MarkLogicRunProfileState
import uk.co.reecedunn.intellij.plugin.marklogic.ui.settings.MarkLogicSettings

import java.io.File

class MarkLogicRunConfiguration internal constructor(project: Project, factory: ConfigurationFactory, name: String) : RunConfigurationBase(project, factory, name), PersistentStateComponent<MarkLogicRunConfigurationData> {
    private val data = MarkLogicRunConfigurationData()

    // region Settings

    var server: MarkLogicServer? = null
        set(server) {
            field = server
            data.serverHost = if (this.server == null) null else this.server!!.hostname
        }

    var markLogicVersion: MarkLogicVersion
        get() = data.markLogicVersion
        set(version) {
            data.markLogicVersion = version
        }

    var contentDatabase: String?
        get() = data.contentDatabase
        set(contentDatabase) {
            data.contentDatabase = contentDatabase
        }

    var moduleDatabase: String?
        get() = data.moduleDatabase
        set(moduleDatabase) {
            data.moduleDatabase = moduleDatabase
        }

    var moduleRoot: String
        get() = data.moduleRoot
        set(moduleRoot) {
            data.moduleRoot = moduleRoot
        }

    var mainModuleFile: VirtualFile? = null
        set(value) {
            field = value
            data.mainModulePath = value?.canonicalPath ?: ""
        }

    var mainModulePath: String
        get() = data.mainModulePath
        set(mainModulePath) {
            val url = VfsUtil.pathToUrl(mainModulePath.replace(File.separatorChar, '/'))
            mainModuleFile = VirtualFileManager.getInstance().findFileByUrl(url)
            data.mainModulePath = mainModulePath
        }

    var tripleFormat: RDFFormat
        get() = RDFFormat.parse(data.tripleFormat) ?: SEM_TRIPLE
        set(tripleFormat) {
            data.tripleFormat = tripleFormat.markLogicName
        }

    // endregion
    // region RunConfigurationBase

    @Throws(RuntimeConfigurationException::class)
    override fun checkConfiguration() {}

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return MarkLogicRunConfigurationEditor(factory as MarkLogicConfigurationFactory, project)
    }

    @Throws(ExecutionException::class)
    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
        return MarkLogicRunProfileState(environment)
    }

    fun run(query: String, handler: MarkLogicResultsHandler): Boolean {
        val state = MarkLogicRunProfileState(null)
        return state.run(query, handler, this)
    }

    // endregion
    // region PersistentStateComponent

    override fun getState(): MarkLogicRunConfigurationData? =
        data

    override fun loadState(state: MarkLogicRunConfigurationData) {
        XmlSerializerUtil.copyBean(state, data)
        mainModulePath = data.mainModulePath // Update the associated VirtualFile.
        MarkLogicSettings.getInstance().servers.forEach { server ->
            if (server.hostname == data.serverHost) {
                this.server = server
            }
        }
    }

    companion object {
        val EXTENSIONS = arrayOf("xq", "xqy", "xquery", "xql", "xqu", "js", "sjs", "sql", "sparql", "rq", "ru")
    }

    // endregion
}
