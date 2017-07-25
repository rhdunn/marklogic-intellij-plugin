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

import junit.framework.TestCase;
import uk.co.reecedunn.intellij.plugin.marklogic.query.Function;
import uk.co.reecedunn.intellij.plugin.marklogic.query.SQLBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.QueryBuilder;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("ConstantConditions")
public class SQLBuilderTest extends TestCase {
    public void testEvalRun() {
        QueryBuilder sql = SQLBuilder.INSTANCE;

        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Run, 5.0), is(nullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Run, 6.0), is(nullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Run, 7.0), is(nullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Run, 8.0), is(Function.XDMP_SQL_80));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Run, 9.0), is(Function.XDMP_SQL_90));
    }

    public void testEvalProfile() {
        QueryBuilder sql = SQLBuilder.INSTANCE;

        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Profile, 5.0), is(nullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Profile, 6.0), is(nullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Profile, 7.0), is(nullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Profile, 8.0), is(nullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Profile, 9.0), is(nullValue()));
    }

    public void testEvalDebug() {
        QueryBuilder sql = SQLBuilder.INSTANCE;

        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Debug, 5.0), is(nullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Debug, 6.0), is(nullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Debug, 7.0), is(nullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Debug, 8.0), is(nullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Debug, 9.0), is(nullValue()));
    }

    public void testInvokeRun() {
        QueryBuilder sql = SQLBuilder.INSTANCE;

        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Run, 5.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Run, 6.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Run, 7.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Run, 8.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Run, 9.0), is(nullValue()));
    }

    public void testInvokeProfile() {
        QueryBuilder sql = SQLBuilder.INSTANCE;

        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Profile, 5.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Profile, 6.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Profile, 7.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Profile, 8.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Profile, 9.0), is(nullValue()));
    }

    public void testInvokeDebug() {
        QueryBuilder sql = SQLBuilder.INSTANCE;

        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Debug, 5.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Debug, 6.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Debug, 7.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Debug, 8.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Debug, 9.0), is(nullValue()));
    }
}