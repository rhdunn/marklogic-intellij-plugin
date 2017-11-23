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
import uk.co.reecedunn.intellij.plugin.marklogic.query.XQueryBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.QueryBuilder;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("ConstantConditions")
public class XQueryBuilderTest extends TestCase {
    public void testEvalRun() {
        QueryBuilder xquery = XQueryBuilder.INSTANCE;

        assertThat(xquery.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, 5.0), is(Function.XDMP_EVAL_50));
        assertThat(xquery.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, 6.0), is(Function.XDMP_EVAL_50));
        assertThat(xquery.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, 7.0), is(Function.XDMP_EVAL_70));
        assertThat(xquery.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, 8.0), is(Function.XDMP_EVAL_70));
        assertThat(xquery.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, 9.0), is(Function.XDMP_EVAL_70));
    }

    public void testEvalProfile() {
        QueryBuilder xquery = XQueryBuilder.INSTANCE;

        assertThat(xquery.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, 5.0), is(Function.PROF_EVAL_50));
        assertThat(xquery.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, 6.0), is(Function.PROF_EVAL_50));
        assertThat(xquery.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, 7.0), is(Function.PROF_EVAL_50));
        assertThat(xquery.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, 8.0), is(Function.PROF_EVAL_80));
        assertThat(xquery.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, 9.0), is(Function.PROF_EVAL_80));
    }

    public void testEvalDebug() {
        QueryBuilder xquery = XQueryBuilder.INSTANCE;

        assertThat(xquery.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, 5.0), is(Function.DBG_EVAL_50));
        assertThat(xquery.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, 6.0), is(Function.DBG_EVAL_50));
        assertThat(xquery.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, 7.0), is(Function.DBG_EVAL_50));
        assertThat(xquery.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, 8.0), is(Function.DBG_EVAL_80));
        assertThat(xquery.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, 9.0), is(Function.DBG_EVAL_80));
    }

    public void testInvokeRun() {
        QueryBuilder xquery = XQueryBuilder.INSTANCE;

        assertThat(xquery.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, 5.0), is(Function.XDMP_INVOKE_50));
        assertThat(xquery.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, 6.0), is(Function.XDMP_INVOKE_50));
        assertThat(xquery.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, 7.0), is(Function.XDMP_INVOKE_70));
        assertThat(xquery.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, 8.0), is(Function.XDMP_INVOKE_70));
        assertThat(xquery.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, 9.0), is(Function.XDMP_INVOKE_70));
    }

    public void testInvokeProfile() {
        QueryBuilder xquery = XQueryBuilder.INSTANCE;

        assertThat(xquery.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, 5.0), is(Function.PROF_INVOKE_50));
        assertThat(xquery.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, 6.0), is(Function.PROF_INVOKE_50));
        assertThat(xquery.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, 7.0), is(Function.PROF_INVOKE_50));
        assertThat(xquery.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, 8.0), is(Function.PROF_INVOKE_80));
        assertThat(xquery.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, 9.0), is(Function.PROF_INVOKE_80));
    }

    public void testInvokeDebug() {
        QueryBuilder xquery = XQueryBuilder.INSTANCE;

        assertThat(xquery.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, 5.0), is(Function.DBG_INVOKE_50));
        assertThat(xquery.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, 6.0), is(Function.DBG_INVOKE_50));
        assertThat(xquery.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, 7.0), is(Function.DBG_INVOKE_50));
        assertThat(xquery.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, 8.0), is(Function.DBG_INVOKE_80));
        assertThat(xquery.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, 9.0), is(Function.DBG_INVOKE_80));
    }
}
