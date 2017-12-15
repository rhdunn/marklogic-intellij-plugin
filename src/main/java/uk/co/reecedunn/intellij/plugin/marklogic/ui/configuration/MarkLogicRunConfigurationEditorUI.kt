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

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileChooser.FileTypeDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.ComponentWithBrowseButton
import com.intellij.openapi.ui.TextComponentAccessor
import uk.co.reecedunn.intellij.plugin.marklogic.query.*
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicServer
import uk.co.reecedunn.intellij.plugin.marklogic.ui.resources.MarkLogicBundle
import uk.co.reecedunn.intellij.plugin.marklogic.ui.runner.MarkLogicResultsHandler
import uk.co.reecedunn.intellij.plugin.marklogic.ui.runner.MarkLogicQueryComboBox
import uk.co.reecedunn.intellij.plugin.marklogic.ui.server.MarkLogicServerComboBox

import javax.swing.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class MarkLogicRunConfigurationEditorUI(private val mFactory: MarkLogicConfigurationFactory, private val mProject: Project) {
    var panel: JPanel? = null
        private set

    private var mServerHost: JComboBox<MarkLogicServer>? = null
    private var mContentDatabase: JComboBox<String>? = null
    private var mModuleDatabase: JComboBox<String>? = null
    private var mModuleRoot: ComponentWithBrowseButton<JTextField>? = null
    private var mMainModulePath: ComponentWithBrowseButton<JTextField>? = null
    private var mTripleFormat: JComboBox<RDFFormat>? = null

    private inner class MarkLogicServerChangedListener : ActionListener {
        override fun actionPerformed(e: ActionEvent) {
            run(LIST_DATABASES_XQUERY, mContentDatabase as MarkLogicQueryComboBox)
            run(LIST_DATABASES_XQUERY, mModuleDatabase as MarkLogicQueryComboBox)
        }
    }

    private fun createUIComponents() {
        panel = JPanel()
        mServerHost = MarkLogicServerComboBox()
        mContentDatabase = MarkLogicQueryComboBox(MarkLogicBundle.message("database.none"))
        mModuleDatabase = MarkLogicQueryComboBox(MarkLogicBundle.message("database.file.system"))
        mModuleRoot = ComponentWithBrowseButton(JTextField(), null)
        mMainModulePath = ComponentWithBrowseButton(JTextField(), null)
        mTripleFormat = ComboBox(RDF_FORMATS)

        val listener = MarkLogicServerChangedListener()
        mServerHost!!.addActionListener(listener)

        mModuleRoot!!.addBrowseFolderListener(
            MarkLogicBundle.message("browser.choose.module.root"), null, null,
            FileChooserDescriptor(false, true, false, false, false, false),
            TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT)

        mMainModulePath!!.addBrowseFolderListener(
            MarkLogicBundle.message("browser.choose.main.module"), null,
            mProject,
            FileTypeDescriptor(MarkLogicBundle.message("browser.choose.main.module"), *QUERY_EXTENSIONS.toTypedArray()),
            TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT)

        (mServerHost as MarkLogicServerComboBox).serversChanged()
    }

    fun reset(configuration: MarkLogicRunConfiguration) {
        mServerHost!!.selectedItem = configuration.server
        (mContentDatabase as MarkLogicQueryComboBox).item = configuration.contentDatabase
        (mModuleDatabase as MarkLogicQueryComboBox).item = configuration.moduleDatabase
        mModuleRoot!!.childComponent.text = configuration.moduleRoot
        mMainModulePath!!.childComponent.text = configuration.mainModulePath
        mTripleFormat!!.selectedItem = configuration.tripleFormat
    }

    fun apply(configuration: MarkLogicRunConfiguration) {
        configuration.server = mServerHost!!.selectedItem as MarkLogicServer
        configuration.contentDatabase = (mContentDatabase as MarkLogicQueryComboBox).item
        configuration.moduleDatabase = (mModuleDatabase as MarkLogicQueryComboBox).item
        configuration.moduleRoot = mModuleRoot!!.childComponent.text
        configuration.mainModulePath = mMainModulePath!!.childComponent.text
        configuration.tripleFormat = mTripleFormat!!.selectedItem as RDFFormat

        val version = (mServerHost as MarkLogicServerComboBox).version
        if (version != null) {
            configuration.markLogicVersion = version
        }
    }

    private fun run(query: MarkLogicQuery, handler: MarkLogicResultsHandler): Boolean {
        // NOTE: Using SettingsEditor.getFactory or getSnapshot don't work, as
        // they throw a NullPointerException when processing the events.
        val configuration = mFactory.createTemplateConfiguration(mProject) as MarkLogicRunConfiguration
        apply(configuration)
        return configuration.server != null && configuration.run(query.query, handler)
    }
}
