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
package uk.co.reecedunn.intellij.plugin.marklogic.query.vars

class KeyValueVarsBuilder private constructor() : VarsBuilder {
    private var isFirstItem = true

    override fun start(builder: StringBuilder) {
        builder.append('(')
        isFirstItem = true
    }

    override fun add(builder: StringBuilder, key: String, value: String) {
        if (isFirstItem) {
            isFirstItem = false
        } else {
            builder.append(", ")
        }

        builder.append("xs:QName(\"")
        builder.append(key)
        builder.append("\"), ")
        builder.append(value)
    }

    override fun end(builder: StringBuilder) {
        builder.append(')')
    }

    companion object {
        var INSTANCE: VarsBuilder = KeyValueVarsBuilder()
    }
}
