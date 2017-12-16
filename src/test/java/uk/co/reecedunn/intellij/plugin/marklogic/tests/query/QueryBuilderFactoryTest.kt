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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.query

import junit.framework.TestCase
import uk.co.reecedunn.intellij.plugin.marklogic.query.*

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat

class QueryBuilderFactoryTest : TestCase() {
    fun testUnsupported() {
        assertThat<QueryBuilder>(QueryBuilderFactory.createQueryBuilderForFile("test.txt"),
                `is`(nullValue()))
    }

    fun testXQuery() {
        assertThat<QueryBuilder>(QueryBuilderFactory.createQueryBuilderForFile("test.xq"),
                `is`(instanceOf(XQueryBuilder::class.java)))
        assertThat<QueryBuilder>(QueryBuilderFactory.createQueryBuilderForFile("test.xqy"),
                `is`(instanceOf(XQueryBuilder::class.java)))
        assertThat<QueryBuilder>(QueryBuilderFactory.createQueryBuilderForFile("test.xquery"),
                `is`(instanceOf(XQueryBuilder::class.java)))
        assertThat<QueryBuilder>(QueryBuilderFactory.createQueryBuilderForFile("test.xqm"), // XQuery Module File
                `is`(nullValue()))
        assertThat<QueryBuilder>(QueryBuilderFactory.createQueryBuilderForFile("test.xql"), // XQuery Language File
                `is`(instanceOf(XQueryBuilder::class.java)))
        assertThat<QueryBuilder>(QueryBuilderFactory.createQueryBuilderForFile("test.xqu"),
                `is`(instanceOf(XQueryBuilder::class.java)))
        assertThat<QueryBuilder>(QueryBuilderFactory.createQueryBuilderForFile("test.xqws"), // XQuery Web Service
                `is`(nullValue()))
    }

    fun testJavaScript() {
        assertThat<QueryBuilder>(QueryBuilderFactory.createQueryBuilderForFile("test.js"),
                `is`(instanceOf(JavaScriptBuilder::class.java)))
        assertThat<QueryBuilder>(QueryBuilderFactory.createQueryBuilderForFile("test.sjs"), // Server-side JavaScript
                `is`(instanceOf(JavaScriptBuilder::class.java)))
    }

    fun testSQL() {
        assertThat<QueryBuilder>(QueryBuilderFactory.createQueryBuilderForFile("test.sql"),
                `is`(instanceOf(SQLBuilder::class.java)))
    }

    fun testSPARQLQuery() {
        assertThat<QueryBuilder>(QueryBuilderFactory.createQueryBuilderForFile("test.sparql"),
                `is`(instanceOf(SPARQLQueryBuilder::class.java)))
        assertThat<QueryBuilder>(QueryBuilderFactory.createQueryBuilderForFile("test.rq"),
                `is`(instanceOf(SPARQLQueryBuilder::class.java)))
    }

    fun testSPARQLUpdate() {
        assertThat<QueryBuilder>(QueryBuilderFactory.createQueryBuilderForFile("test.ru"),
                `is`(instanceOf(SPARQLUpdateBuilder::class.java)))
    }

    fun testXSLT() {
        assertThat<QueryBuilder>(QueryBuilderFactory.createQueryBuilderForFile("test.xsl"),
                `is`(instanceOf(XSLTBuilder::class.java)))
        assertThat<QueryBuilder>(QueryBuilderFactory.createQueryBuilderForFile("test.xslt"),
                `is`(instanceOf(XSLTBuilder::class.java)))
    }
}
