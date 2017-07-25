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
        assertThat(QueryBuilderFactory.createQueryBuilderForFile("test.txt"),
                is(nullValue()));
    }

    public void testXQuery() {
        assertThat(ArrayUtil.indexOf(QueryBuilderFactory.EXTENSIONS, "xq"), is(not(-1)));
        assertThat(ArrayUtil.indexOf(QueryBuilderFactory.EXTENSIONS, "xqy"), is(not(-1)));
        assertThat(ArrayUtil.indexOf(QueryBuilderFactory.EXTENSIONS, "xquery"), is(not(-1)));
        assertThat(ArrayUtil.indexOf(QueryBuilderFactory.EXTENSIONS, "xql"), is(not(-1))); // XQuery Language File
        assertThat(ArrayUtil.indexOf(QueryBuilderFactory.EXTENSIONS, "xqm"), is(-1)); // XQuery Module File
        assertThat(ArrayUtil.indexOf(QueryBuilderFactory.EXTENSIONS, "xqu"), is(not(-1)));
        assertThat(ArrayUtil.indexOf(QueryBuilderFactory.EXTENSIONS, "xqws"), is(-1)); // XQuery Web Service

        assertThat(QueryBuilderFactory.createQueryBuilderForFile("test.xq"),
                is(instanceOf(XQueryBuilder.class)));
        assertThat(QueryBuilderFactory.createQueryBuilderForFile("test.xqy"),
                is(instanceOf(XQueryBuilder.class)));
        assertThat(QueryBuilderFactory.createQueryBuilderForFile("test.xquery"),
                is(instanceOf(XQueryBuilder.class)));
        assertThat(QueryBuilderFactory.createQueryBuilderForFile("test.xql"),
                is(instanceOf(XQueryBuilder.class)));
        assertThat(QueryBuilderFactory.createQueryBuilderForFile("test.xqu"),
                is(instanceOf(XQueryBuilder.class)));
    }

    public void testJavaScript() {
        assertThat(ArrayUtil.indexOf(QueryBuilderFactory.EXTENSIONS, "js"), is(not(-1)));
        assertThat(ArrayUtil.indexOf(QueryBuilderFactory.EXTENSIONS, "sjs"), is(not(-1))); // Server-side JavaScript

        assertThat(QueryBuilderFactory.createQueryBuilderForFile("test.js"),
                is(instanceOf(JavaScriptBuilder.class)));
        assertThat(QueryBuilderFactory.createQueryBuilderForFile("test.sjs"),
                is(instanceOf(JavaScriptBuilder.class)));
    }

    public void testSQL() {
        assertThat(ArrayUtil.indexOf(QueryBuilderFactory.EXTENSIONS, "sql"), is(not(-1)));

        assertThat(QueryBuilderFactory.createQueryBuilderForFile("test.sql"),
                is(instanceOf(SQLBuilder.class)));
    }

    public void testSPARQLQuery() {
        assertThat(ArrayUtil.indexOf(QueryBuilderFactory.EXTENSIONS, "sparql"), is(not(-1)));
        assertThat(ArrayUtil.indexOf(QueryBuilderFactory.EXTENSIONS, "rq"), is(not(-1)));

        assertThat(QueryBuilderFactory.createQueryBuilderForFile("test.sparql"),
                is(instanceOf(SPARQLQueryBuilder.class)));
        assertThat(QueryBuilderFactory.createQueryBuilderForFile("test.rq"),
                is(instanceOf(SPARQLQueryBuilder.class)));
    }

    public void testSPARQLUpdate() {
        assertThat(ArrayUtil.indexOf(QueryBuilderFactory.EXTENSIONS, "ru"), is(not(-1)));

        assertThat(QueryBuilderFactory.createQueryBuilderForFile("test.ru"),
                is(instanceOf(SPARQLUpdateBuilder.class)));
    }

    public void testXSLT() {
        assertThat(ArrayUtil.indexOf(QueryBuilderFactory.EXTENSIONS, "xsl"), is(not(-1)));
        assertThat(ArrayUtil.indexOf(QueryBuilderFactory.EXTENSIONS, "xslt"), is(not(-1)));

        assertThat(QueryBuilderFactory.createQueryBuilderForFile("test.xsl"),
                is(instanceOf(XSLTBuilder.class)));
        assertThat(QueryBuilderFactory.createQueryBuilderForFile("test.xslt"),
                is(instanceOf(XSLTBuilder.class)));
    }
}
