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
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersion;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.profile.ProfileExecutor;
import uk.co.reecedunn.intellij.plugin.marklogic.query.Function;
import uk.co.reecedunn.intellij.plugin.marklogic.query.XQueryBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.QueryBuilder;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("ConstantConditions")
public class XQueryBuilderTest extends TestCase {
    private static final MarkLogicVersion MARKLOGIC_5 = new MarkLogicVersion(5, 0, null, null);
    private static final MarkLogicVersion MARKLOGIC_6 = new MarkLogicVersion(6, 0, null, null);
    private static final MarkLogicVersion MARKLOGIC_7 = new MarkLogicVersion(7, 0, null, null);
    private static final MarkLogicVersion MARKLOGIC_8 = new MarkLogicVersion(8, 0, null, null);
    private static final MarkLogicVersion MARKLOGIC_9 = new MarkLogicVersion(9, 0, null, null);

    public void testEvalRun() {
        QueryBuilder xquery = XQueryBuilder.INSTANCE;

        assertThat(xquery.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_5), is(Function.XDMP_EVAL_50));
        assertThat(xquery.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_6), is(Function.XDMP_EVAL_50));
        assertThat(xquery.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_7), is(Function.XDMP_EVAL_70));
        assertThat(xquery.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_8), is(Function.XDMP_EVAL_70));
        assertThat(xquery.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_9), is(Function.XDMP_EVAL_70));
    }

    public void testEvalProfile() {
        QueryBuilder xquery = XQueryBuilder.INSTANCE;

        assertThat(xquery.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_5), is(Function.PROF_EVAL_50));
        assertThat(xquery.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_6), is(Function.PROF_EVAL_50));
        assertThat(xquery.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_7), is(Function.PROF_EVAL_50));
        assertThat(xquery.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_8), is(Function.PROF_EVAL_80));
        assertThat(xquery.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_9), is(Function.PROF_EVAL_80));
    }

    public void testEvalDebug() {
        QueryBuilder xquery = XQueryBuilder.INSTANCE;

        assertThat(xquery.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_5), is(Function.DBG_EVAL_50));
        assertThat(xquery.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_6), is(Function.DBG_EVAL_50));
        assertThat(xquery.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_7), is(Function.DBG_EVAL_50));
        assertThat(xquery.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_8), is(Function.DBG_EVAL_80));
        assertThat(xquery.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_9), is(Function.DBG_EVAL_80));
    }

    public void testInvokeRun() {
        QueryBuilder xquery = XQueryBuilder.INSTANCE;

        assertThat(xquery.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_5), is(Function.XDMP_INVOKE_50));
        assertThat(xquery.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_6), is(Function.XDMP_INVOKE_50));
        assertThat(xquery.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_7), is(Function.XDMP_INVOKE_70));
        assertThat(xquery.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_8), is(Function.XDMP_INVOKE_70));
        assertThat(xquery.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_9), is(Function.XDMP_INVOKE_70));
    }

    public void testInvokeProfile() {
        QueryBuilder xquery = XQueryBuilder.INSTANCE;

        assertThat(xquery.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_5), is(Function.PROF_INVOKE_50));
        assertThat(xquery.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_6), is(Function.PROF_INVOKE_50));
        assertThat(xquery.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_7), is(Function.PROF_INVOKE_50));
        assertThat(xquery.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_8), is(Function.PROF_INVOKE_80));
        assertThat(xquery.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_9), is(Function.PROF_INVOKE_80));
    }

    public void testInvokeDebug() {
        QueryBuilder xquery = XQueryBuilder.INSTANCE;

        assertThat(xquery.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_5), is(Function.DBG_INVOKE_50));
        assertThat(xquery.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_6), is(Function.DBG_INVOKE_50));
        assertThat(xquery.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_7), is(Function.DBG_INVOKE_50));
        assertThat(xquery.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_8), is(Function.DBG_INVOKE_80));
        assertThat(xquery.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_9), is(Function.DBG_INVOKE_80));
    }
}
