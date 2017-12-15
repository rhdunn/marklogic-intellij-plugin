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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.query;

import com.intellij.util.ArrayUtil;
import junit.framework.TestCase;
import uk.co.reecedunn.intellij.plugin.marklogic.query.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("ConstantConditions")
public class QueryBuilderFactoryTest extends TestCase {
    public void testUnsupported() {
        assertThat(QueryBuilderFactory.INSTANCE.createQueryBuilderForFile("test.txt"),
                is(nullValue()));
    }

    public void testXQuery() {
        assertThat(QueryBuilderFactory.INSTANCE.createQueryBuilderForFile("test.xq"),
                is(instanceOf(XQueryBuilder.class)));
        assertThat(QueryBuilderFactory.INSTANCE.createQueryBuilderForFile("test.xqy"),
                is(instanceOf(XQueryBuilder.class)));
        assertThat(QueryBuilderFactory.INSTANCE.createQueryBuilderForFile("test.xquery"),
                is(instanceOf(XQueryBuilder.class)));
        assertThat(QueryBuilderFactory.INSTANCE.createQueryBuilderForFile("test.xqm"), // XQuery Module File
                is(nullValue()));
        assertThat(QueryBuilderFactory.INSTANCE.createQueryBuilderForFile("test.xql"), // XQuery Language File
                is(instanceOf(XQueryBuilder.class)));
        assertThat(QueryBuilderFactory.INSTANCE.createQueryBuilderForFile("test.xqu"),
                is(instanceOf(XQueryBuilder.class)));
        assertThat(QueryBuilderFactory.INSTANCE.createQueryBuilderForFile("test.xqws"), // XQuery Web Service
                is(nullValue()));
    }

    public void testJavaScript() {
        assertThat(QueryBuilderFactory.INSTANCE.createQueryBuilderForFile("test.js"),
                is(instanceOf(JavaScriptBuilder.class)));
        assertThat(QueryBuilderFactory.INSTANCE.createQueryBuilderForFile("test.sjs"), // Server-side JavaScript
                is(instanceOf(JavaScriptBuilder.class)));
    }

    public void testSQL() {
        assertThat(QueryBuilderFactory.INSTANCE.createQueryBuilderForFile("test.sql"),
                is(instanceOf(SQLBuilder.class)));
    }

    public void testSPARQLQuery() {
        assertThat(QueryBuilderFactory.INSTANCE.createQueryBuilderForFile("test.sparql"),
                is(instanceOf(SPARQLQueryBuilder.class)));
        assertThat(QueryBuilderFactory.INSTANCE.createQueryBuilderForFile("test.rq"),
                is(instanceOf(SPARQLQueryBuilder.class)));
    }

    public void testSPARQLUpdate() {
        assertThat(QueryBuilderFactory.INSTANCE.createQueryBuilderForFile("test.ru"),
                is(instanceOf(SPARQLUpdateBuilder.class)));
    }

    public void testXSLT() {
        assertThat(QueryBuilderFactory.INSTANCE.createQueryBuilderForFile("test.xsl"),
                is(instanceOf(XSLTBuilder.class)));
        assertThat(QueryBuilderFactory.INSTANCE.createQueryBuilderForFile("test.xslt"),
                is(instanceOf(XSLTBuilder.class)));
    }
}
