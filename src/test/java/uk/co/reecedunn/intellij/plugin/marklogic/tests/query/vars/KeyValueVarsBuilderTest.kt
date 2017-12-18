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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.query.vars

import junit.framework.TestCase
import uk.co.reecedunn.intellij.plugin.marklogic.query.vars.KeyValueVarsBuilder

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import uk.co.reecedunn.intellij.plugin.marklogic.api.Item
import uk.co.reecedunn.intellij.plugin.marklogic.api.QName

class KeyValueVarsBuilderTest : TestCase() {
    fun testNoVars() {
        val builder = KeyValueVarsBuilder
        val vars = StringBuilder()

        builder.start(vars)
        builder.end(vars)

        assertThat(vars.toString(), `is`("()"))
    }

    fun testVarsSingle() {
        val builder = KeyValueVarsBuilder
        val vars = StringBuilder()

        builder.start(vars)
        builder.add(vars, QName(null, "x"), Item.fromType(2))
        builder.end(vars)

        assertThat(vars.toString(), `is`("(fn:QName((), \"x\"), 2)"))
    }

    fun testVarsMultiple() {
        val builder = KeyValueVarsBuilder
        val vars = StringBuilder()

        builder.start(vars)
        builder.add(vars, QName(null, "r"), Item.fromType(5.7))
        builder.add(vars, QName(null, "theta"), Item.fromType(0.5265))
        builder.end(vars)

        assertThat(vars.toString(), `is`("(fn:QName((), \"r\"), 5.7, fn:QName((), \"theta\"), 0.5265)"))
    }
}
