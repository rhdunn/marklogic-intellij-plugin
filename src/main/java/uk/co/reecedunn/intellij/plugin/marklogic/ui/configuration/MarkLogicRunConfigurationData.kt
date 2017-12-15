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

import com.intellij.util.xmlb.Converter
import com.intellij.util.xmlb.annotations.OptionTag
import uk.co.reecedunn.compat.intellij.execution.configurations.RunConfigurationOptions
import uk.co.reecedunn.intellij.plugin.marklogic.query.*
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersion
import uk.co.reecedunn.intellij.plugin.marklogic.server.*

class MarkLogicRunConfigurationData : RunConfigurationOptions() {
    var serverHost: String? = "localhost"

    @OptionTag(converter = MarkLogicVersionConverter::class)
    var markLogicVersion = MARKLOGIC_7

    var contentDatabase: String? = null

    var moduleDatabase: String? = null

    var moduleRoot = "/"

    var mainModulePath = ""

    var tripleFormat = SEM_TRIPLE.markLogicName

    class MarkLogicVersionConverter : Converter<MarkLogicVersion>() {
        override fun fromString(version: String): MarkLogicVersion? {
            return MarkLogicVersion.parse(version)
        }

        override fun toString(version: MarkLogicVersion): String {
            return version.toString()
        }
    }
}
