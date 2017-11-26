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

class MarkLogicVersionFormatException(message: String): RuntimeException(message)

data class MarkLogicVersion(val major: Int, val minor: Int, val api: Int?, val patch: Int?) {

    override fun toString(): String =
        api?.let { "${major}.${minor}-${api}.${patch}" } ?: "${major}.${minor}"

    companion object {
        fun parse(version: String): MarkLogicVersion {
            val parts =
                try {
                    version.split("[.-]".toRegex()).map { p -> p.toInt() }
                } catch (e: NumberFormatException) {
                    throw MarkLogicVersionFormatException("Invalid MarkLogic version: $version")
                }
            return when (parts.size) {
                2 -> MarkLogicVersion(parts[0], parts[1], null, null)
                4 -> MarkLogicVersion(parts[0], parts[1], parts[2], parts[3])
                else -> throw MarkLogicVersionFormatException("Invalid MarkLogic version: $version")
            }
        }
    }

}
