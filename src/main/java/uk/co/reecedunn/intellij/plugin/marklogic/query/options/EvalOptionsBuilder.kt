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
package uk.co.reecedunn.intellij.plugin.marklogic.query.options

private val FILESYSTEM_MODULES_DB = "0"

object EvalOptionsBuilder : OptionsBuilder {
    override var contentDatabase: String? = null
    override var modulesDatabase: String? = null
    override var modulesRoot: String? = null

    override fun reset() {
        contentDatabase = null
        modulesDatabase = null
        modulesRoot = null
    }

    override fun build(): String {
        val builder = StringBuilder()
        builder.append("<options xmlns=\"xdmp:eval\">")
        buildDatabaseOption(builder, "database", contentDatabase, null)
        buildDatabaseOption(builder, "modules", modulesDatabase, FILESYSTEM_MODULES_DB)
        buildOption(builder, "root", modulesRoot)
        builder.append("</options>")
        return builder.toString()
    }

    private fun buildDatabaseOption(options: StringBuilder, option: String, database: String?, defaultDatabaseId: String?) {
        if (database == null || database.isEmpty()) {
            if (defaultDatabaseId != null) {
                buildOption(options, option, defaultDatabaseId)
            }
            return
        }

        val selector = "{xdmp:database(\"$database\")}"
        buildOption(options, option, selector)
    }

    private fun buildOption(options: StringBuilder, option: String, value: String?) {
        if (value == null || value.isEmpty()) {
            return
        }

        options.append("<$option>$value</$option>")
    }
}
