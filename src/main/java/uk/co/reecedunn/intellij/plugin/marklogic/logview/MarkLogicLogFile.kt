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
package uk.co.reecedunn.intellij.plugin.marklogic.logview

import uk.co.reecedunn.intellij.plugin.marklogic.api.Item

data class MarkLogicLogEntry(
    val timestamp: String,
    val level: String,
    val message: Item)

class ParseException(message: String) : RuntimeException(message)

object MarkLogicLogFile {
    val logline: Regex = "^([0-9\\-]+ [0-9:.]+) ([A-Za-z]+): (.*)$".toRegex()

    fun parse(logfile: String): Sequence<MarkLogicLogEntry> {
        return logfile.lineSequence().map { line ->
            logline.matchEntire(line)?.let {
                val groups = it.groupValues
                MarkLogicLogEntry(groups[1], groups[2], Item.create(groups[3], "text/plain", "xs:string"))
            } ?: return@map if (line.isEmpty())
                null
            else
                throw ParseException("Cannot parse line: $line")
        }.filterNotNull()
    }
}
