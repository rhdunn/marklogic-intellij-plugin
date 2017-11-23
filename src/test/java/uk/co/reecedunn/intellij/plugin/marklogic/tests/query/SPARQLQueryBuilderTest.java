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

import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.executors.DefaultRunExecutor;
import junit.framework.TestCase;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.executors.ProfileExecutor;
import uk.co.reecedunn.intellij.plugin.marklogic.query.Function;
import uk.co.reecedunn.intellij.plugin.marklogic.query.QueryBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.SPARQLQueryBuilder;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("ConstantConditions")
public class SPARQLQueryBuilderTest extends TestCase {
    public void testEvalRun() {
        QueryBuilder sparql = SPARQLQueryBuilder.INSTANCE;

        assertThat(sparql.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, 5.0), is(nullValue()));
        assertThat(sparql.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, 6.0), is(nullValue()));
        assertThat(sparql.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, 7.0), is(Function.SEM_SPARQL_70));
        assertThat(sparql.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, 8.0), is(Function.SEM_SPARQL_70));
        assertThat(sparql.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, 9.0), is(Function.SEM_SPARQL_70));
    }

    public void testEvalProfile() {
        QueryBuilder sparql = SPARQLQueryBuilder.INSTANCE;

        assertThat(sparql.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, 5.0), is(nullValue()));
        assertThat(sparql.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, 6.0), is(nullValue()));
        assertThat(sparql.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, 7.0), is(nullValue()));
        assertThat(sparql.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, 8.0), is(nullValue()));
        assertThat(sparql.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, 9.0), is(nullValue()));
    }

    public void testEvalDebug() {
        QueryBuilder sparql = SPARQLQueryBuilder.INSTANCE;

        assertThat(sparql.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, 5.0), is(nullValue()));
        assertThat(sparql.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, 6.0), is(nullValue()));
        assertThat(sparql.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, 7.0), is(nullValue()));
        assertThat(sparql.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, 8.0), is(nullValue()));
        assertThat(sparql.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, 9.0), is(nullValue()));
    }

    public void testInvokeRun() {
        QueryBuilder sparql = SPARQLQueryBuilder.INSTANCE;

        assertThat(sparql.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, 5.0), is(nullValue()));
        assertThat(sparql.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, 6.0), is(nullValue()));
        assertThat(sparql.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, 7.0), is(nullValue()));
        assertThat(sparql.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, 8.0), is(nullValue()));
        assertThat(sparql.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, 9.0), is(nullValue()));
    }

    public void testInvokeProfile() {
        QueryBuilder sparql = SPARQLQueryBuilder.INSTANCE;

        assertThat(sparql.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, 5.0), is(nullValue()));
        assertThat(sparql.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, 6.0), is(nullValue()));
        assertThat(sparql.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, 7.0), is(nullValue()));
        assertThat(sparql.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, 8.0), is(nullValue()));
        assertThat(sparql.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, 9.0), is(nullValue()));
    }

    public void testInvokeDebug() {
        QueryBuilder sparql = SPARQLQueryBuilder.INSTANCE;

        assertThat(sparql.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, 5.0), is(nullValue()));
        assertThat(sparql.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, 6.0), is(nullValue()));
        assertThat(sparql.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, 7.0), is(nullValue()));
        assertThat(sparql.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, 8.0), is(nullValue()));
        assertThat(sparql.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, 9.0), is(nullValue()));
    }
}
