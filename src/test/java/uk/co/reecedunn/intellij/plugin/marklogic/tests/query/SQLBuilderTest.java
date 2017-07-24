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
import uk.co.reecedunn.intellij.plugin.marklogic.query.SQLBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.QueryBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.vars.MapVarsBuilder;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("ConstantConditions")
public class SQLBuilderTest extends TestCase {
    public void testEvalRun() {
        SQLBuilder sql = new SQLBuilder();

        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Run, 5.0), is(nullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Run, 6.0), is(nullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Run, 7.0), is(nullValue()));

        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Run, 8.0), is(notNullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Run, 8.0).getFunction(),
                is("xdmp:sql($query)"));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Run, 8.0).getVarsBuilder(),
                is(nullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Run, 8.0).getOptionsBuilder(),
                is(nullValue()));

        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Run, 9.0), is(notNullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Run, 9.0).getFunction(),
                is("xdmp:sql($query, (), $vars)"));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Run, 9.0).getVarsBuilder(),
                is(instanceOf(MapVarsBuilder.class)));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Run, 9.0).getOptionsBuilder(),
                is(nullValue()));
    }

    public void testEvalProfile() {
        SQLBuilder sql = new SQLBuilder();

        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Profile, 5.0), is(nullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Profile, 6.0), is(nullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Profile, 7.0), is(nullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Profile, 8.0), is(nullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Profile, 9.0), is(nullValue()));
    }

    public void testEvalDebug() {
        SQLBuilder sql = new SQLBuilder();

        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Debug, 5.0), is(nullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Debug, 6.0), is(nullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Debug, 7.0), is(nullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Debug, 8.0), is(nullValue()));
        assertThat(sql.createEvalBuilder(QueryBuilder.ExecMode.Debug, 9.0), is(nullValue()));
    }

    public void testInvokeRun() {
        SQLBuilder sql = new SQLBuilder();

        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Run, 5.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Run, 6.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Run, 7.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Run, 8.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Run, 9.0), is(nullValue()));
    }

    public void testInvokeProfile() {
        SQLBuilder sql = new SQLBuilder();

        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Profile, 5.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Profile, 6.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Profile, 7.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Profile, 8.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Profile, 9.0), is(nullValue()));
    }

    public void testInvokeDebug() {
        SQLBuilder sql = new SQLBuilder();

        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Debug, 5.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Debug, 6.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Debug, 7.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Debug, 8.0), is(nullValue()));
        assertThat(sql.createInvokeBuilder(QueryBuilder.ExecMode.Debug, 9.0), is(nullValue()));
    }
}
