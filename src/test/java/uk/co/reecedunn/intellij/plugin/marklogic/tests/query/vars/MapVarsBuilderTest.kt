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
import uk.co.reecedunn.intellij.plugin.marklogic.query.vars.MapVarsBuilder

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat

class MapVarsBuilderTest : TestCase() {
    fun testNoVars() {
        val builder = MapVarsBuilder
        val vars = StringBuilder()

        builder.start(vars)
        builder.end(vars)

        val expected = "let \$vars := map:map()\n" + "return \$vars\n"
        assertThat(vars.toString(), `is`(expected))
    }

    fun testVarsSingle() {
        val builder = MapVarsBuilder
        val vars = StringBuilder()

        builder.start(vars)
        builder.add(vars, "x", "2")
        builder.end(vars)

        val expected =
            "let \$vars := map:map()\n" +
            "let \$_ := map:put(\$vars, xdmp:key-from-QName(xs:QName(\"x\")), 2)\n" +
            "return \$vars\n"
        assertThat(vars.toString(), `is`(expected))
    }

    fun testVarsMultiple() {
        val builder = MapVarsBuilder
        val vars = StringBuilder()

        builder.start(vars)
        builder.add(vars, "r", "5.7")
        builder.add(vars, "theta", "0.5265")
        builder.end(vars)

        val expected =
            "let \$vars := map:map()\n" +
            "let \$_ := map:put(\$vars, xdmp:key-from-QName(xs:QName(\"r\")), 5.7)\n" +
            "let \$_ := map:put(\$vars, xdmp:key-from-QName(xs:QName(\"theta\")), 0.5265)\n" +
            "return \$vars\n"
        assertThat(vars.toString(), `is`(expected))
    }
}