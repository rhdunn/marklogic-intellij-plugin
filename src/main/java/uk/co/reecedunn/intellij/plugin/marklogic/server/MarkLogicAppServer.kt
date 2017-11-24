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
package uk.co.reecedunn.intellij.plugin.marklogic.server

import uk.co.reecedunn.intellij.plugin.marklogic.ui.resources.MarkLogicBundle

enum class LogType(val type: String) {
    ACCESS_LOG("AccessLog"), // appserver
    AUDIT_LOG("AuditLog"),   // system
    CRASH_LOG("CrashLog"),   // system
    ERROR_LOG("ErrorLog")    // appserver, system, taskserver
}

data class MarkLogicAppServer(
    val group: String?,
    val appserver: String,
    val type: String?,
    val port: Int) {

    override fun toString(): String =
        group?.let { "$group :: $appserver : $port [$type]" } ?: appserver

    fun logfile(logtype: LogType, generation: Int, marklogicVersion: Double): String {
        val suffix = if (generation == 0) "" else "_${generation}"
        return if (marklogicVersion <= 8.0 && logtype === LogType.ERROR_LOG)
            "${logtype.type}${suffix}.txt"
        else if (this === TASKSERVER)
            "TaskServer_${logtype.type}${suffix}.txt"
        else if (port == 0)
            "${logtype.type}${suffix}.txt"
        else
            "${port}_${logtype.type}${suffix}.txt"
    }

    companion object {
        val SYSTEM     = MarkLogicAppServer(null, MarkLogicBundle.message("logviewer.app-server.none"), null, 0)
        val TASKSERVER = MarkLogicAppServer(null, "TaskServer", null, 0)
    }

}
