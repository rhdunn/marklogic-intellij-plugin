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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.api

import junit.framework.TestCase
import uk.co.reecedunn.intellij.plugin.marklogic.api.Item
import uk.co.reecedunn.intellij.plugin.marklogic.tests.Query

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat

class ItemTest : TestCase() {
    @Query("()")
    fun testEmptySequence() {
        val xcc = Item.create("()", "empty-sequence()")
        assertThat(xcc.content, `is`("()"))
        assertThat<String>(xcc.contentType, `is`("text/plain"))
        assertThat(xcc.itemType, `is`("empty-sequence()"))
        assertThat(xcc.toString(), `is`("()"))

        val rest = Item.create("()", "text/plain", "empty-sequence()")
        assertThat(rest.content, `is`("()"))
        assertThat<String>(rest.contentType, `is`("text/plain"))
        assertThat(rest.itemType, `is`("empty-sequence()"))
        assertThat(rest.toString(), `is`("()"))
    }
}
