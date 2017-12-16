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

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import uk.co.reecedunn.intellij.plugin.marklogic.api.QName

class QNameTest : TestCase() {
    fun testNCName() {
        val ncname = QName(null, "test")
        assertThat(ncname.namespace, `is`(nullValue()))
        assertThat(ncname.localname, `is`("test"))
        assertThat(ncname.toString(), `is`("xs:QName(\"test\")"))
    }

    fun testQName() {
        val qname = QName("xs", "string")
        assertThat(qname.namespace, `is`("xs"))
        assertThat(qname.localname, `is`("string"))
        assertThat(qname.toString(), `is`("xs:QName(\"xs\", \"string\")"))
    }
}
