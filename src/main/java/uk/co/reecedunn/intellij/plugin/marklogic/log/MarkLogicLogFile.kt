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
package uk.co.reecedunn.intellij.plugin.marklogic.log

import uk.co.reecedunn.intellij.plugin.marklogic.api.Item

data class MarkLogicLogEntry(
    val date: String,
    val time: String,
    val level: String,
    val appserver: String?,
    val continuation: Boolean,
    val message: Item)

class ParseException(message: String) : RuntimeException(message)

object MarkLogicLogFile {
    val logline: Regex = """^
        ([0-9\-]+)                     # 1: date
        [\ ]                           #
        ([0-9:.]+)                     # 2: time
        [\ ]                           #
        ([A-Za-z]+):                   # 3: log level
        (\ (Task\ Server):)?           # 5: application server name
        ([\ +])                        # 6: MarkLogic 9 continuation
        (.*)                           # 7: message
    $""".trimMargin().toRegex(RegexOption.COMMENTS)

    fun parse(logfile: String): Sequence<MarkLogicLogEntry> {
        return logfile.lineSequence().map { line ->
            logline.matchEntire(line)?.let {
                val groups = it.groupValues
                MarkLogicLogEntry(
                    groups[1],
                    groups[2],
                    groups[3],
                    if (groups[5].isEmpty()) null else groups[5],
                    groups[6] == "+",
                    Item.create(groups[7], "text/plain", "xs:string"))
            } ?: return@map if (line.isEmpty())
                null
            else
                throw ParseException("Cannot parse line: $line")
        }.filterNotNull()
    }
}
