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
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersion
import java.awt.Color

private val BLACK = Color(0x000000)
private val BLUE = Color(0x0000FF)
private val DARK_ORANGE = Color(0xFF8C00)
private val MAROON = Color(0x880000)

// Reference: https://docs.marklogic.com/guide/admin/logfiles#id_37841
enum class LogLevel(val displayName: String, val rank: Int, val defaultColor: Color) {
    FINEST("Finest", 0, BLACK), // (lowest priority)
    FINER("Finer", 1, BLACK),
    FINE("Fine", 2, BLACK),
    DEBUG("Debug", 3, BLACK),
    CONFIG("Config", 4, BLACK),
    INFO("Info", 5, BLACK), // (default)
    NOTICE("Notice", 6, BLUE),
    WARNING("Warning", 7, DARK_ORANGE),
    ERROR("Error", 8, MAROON),
    CRITICAL("Critical", 9, MAROON),
    ALERT("Alert", 10, MAROON),
    EMERGENCY("Emergency", 11, MAROON); // (highest priority)

    companion object {
        fun parse(name: String): LogLevel =
            LogLevel.values().find { level -> level.displayName == name }!!
    }
}

data class MarkLogicLogEntry(
    val date: String,
    val time: String,
    val level: LogLevel,
    val appserver: String?,
    val continuation: Boolean,
    val message: Item)

class ParseException(message: String) : RuntimeException(message)

object MarkLogicLogFile {
    private val logline: Regex = """^
        ([0-9\-]+)                               # 1: date
        [\ ]                                     #
        ([0-9:.]+)                               # 2: time
        [\ ]                                     #
        ([A-Za-z]+):                             # 3: log level
        (\ ([a-zA-Z0-9\-_]+):)?                  # 5: application server name
        ([\ +])                                  # 6: MarkLogic 9 continuation
        (.*)                                     # 7: message
    $""".trimMargin().toRegex(RegexOption.COMMENTS)

    fun parse(logfile: String, version: MarkLogicVersion): Sequence<MarkLogicLogEntry> {
        return logfile.lineSequence().map { line ->
            logline.matchEntire(line)?.let {
                val groups = it.groupValues

                // MarkLogic >= 9.0 places appserver logs in their own separate
                // files, so don't treat appserver-like entries as such when
                // parsing those log files.
                val message =
                    if (version.major <= 8 || groups[5].isEmpty())
                        groups[7]
                    else
                        "${groups[5]}: ${groups[7]}"
                val appserver =
                    if (version.major >= 9 || groups[5].isEmpty())
                        null
                    else
                        groups[5]

                MarkLogicLogEntry(
                    groups[1],
                    groups[2],
                    LogLevel.parse(groups[3]),
                    appserver,
                    groups[6] == "+",
                    Item.fromType(message))
            } ?: return@map if (line.isEmpty())
                null
            else
                throw ParseException("Cannot parse line: $line")
        }.filterNotNull()
    }
}
