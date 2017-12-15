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

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.openapi.util.IconLoader

import javax.swing.*

private val FILETYPE_ICON = IconLoader.getIcon("/icons/marklogic.png")

class MarkLogicConfigurationType : ConfigurationType {
    override fun getDisplayName(): String =
        "MarkLogic"

    override fun getConfigurationTypeDescription(): String =
        displayName

    override fun getIcon(): Icon =
        FILETYPE_ICON

    override fun getId(): String =
        "MarkLogicConfiguration"

    override fun getConfigurationFactories(): Array<ConfigurationFactory> =
        arrayOf(MarkLogicConfigurationFactory(this))
}
