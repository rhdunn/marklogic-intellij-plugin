/*
 * Copyright (C) 2017-2018 Reece H. Dunn
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
package uk.co.reecedunn.intellij.plugin.marklogic.api

import java.util.HashMap

abstract class EvalRequestBuilder protected constructor() {
    var contentDatabase: String? = null
    var transactionID: String? = null

    private var xquery: String? = null
    @Suppress("PropertyName")
    var XQuery: String?
        get() = xquery
        set(xquery) {
            this.xquery = xquery
            this.javascript = null // There can only be one!
        }

    private var javascript: String? = null
    var javaScript: String?
        get() = javascript
        set(javascript) {
            this.xquery = null // There can only be one!
            this.javascript = javascript
        }

    private val vars = HashMap<QName, Item>()
    val variableNames get(): Set<QName> =
        vars.keys

    fun addVariables(variables: Map<QName, Item>) {
        for ((key, value) in variables) {
            vars.put(key, value)
        }
    }

    fun addVariable(name: QName, value: Item) {
        vars[name] = value
    }

    fun getVariable(name: QName): Item {
        return vars[name]!!
    }

    abstract fun build(): Request
}
