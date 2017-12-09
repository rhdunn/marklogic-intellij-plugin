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
import uk.co.reecedunn.intellij.plugin.marklogic.query.JavaScriptBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.QueryBuilder;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("ConstantConditions")
public class JavaScriptBuilderTest extends TestCase {
    private static final MarkLogicVersion MARKLOGIC_5 = new MarkLogicVersion(5, 0, null, null);
    private static final MarkLogicVersion MARKLOGIC_6 = new MarkLogicVersion(6, 0, null, null);
    private static final MarkLogicVersion MARKLOGIC_7 = new MarkLogicVersion(7, 0, null, null);
    private static final MarkLogicVersion MARKLOGIC_8 = new MarkLogicVersion(8, 0, null, null);
    private static final MarkLogicVersion MARKLOGIC_9 = new MarkLogicVersion(9, 0, null, null);

    public void testEvalRun() {
        QueryBuilder js = JavaScriptBuilder.INSTANCE;

        assertThat(js.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_5), is(nullValue()));
        assertThat(js.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_6), is(nullValue()));
        assertThat(js.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_7), is(nullValue()));
        assertThat(js.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_8), is(Function.XDMP_JAVASCRIPT_EVAL_80));
        assertThat(js.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_9), is(Function.XDMP_JAVASCRIPT_EVAL_80));
    }

    public void testEvalProfile() {
        QueryBuilder js = JavaScriptBuilder.INSTANCE;

        assertThat(js.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_5), is(nullValue()));
        assertThat(js.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_6), is(nullValue()));
        assertThat(js.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_7), is(nullValue()));
        assertThat(js.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_8), is(nullValue()));
        assertThat(js.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_9), is(nullValue()));
    }

    public void testEvalDebug() {
        QueryBuilder js = JavaScriptBuilder.INSTANCE;

        assertThat(js.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_5), is(nullValue()));
        assertThat(js.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_6), is(nullValue()));
        assertThat(js.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_7), is(nullValue()));
        assertThat(js.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_8), is(nullValue()));
        assertThat(js.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_9), is(nullValue()));
    }

    public void testInvokeRun() {
        QueryBuilder js = JavaScriptBuilder.INSTANCE;

        assertThat(js.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_5), is(nullValue()));
        assertThat(js.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_6), is(nullValue()));
        assertThat(js.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_7), is(nullValue()));
        assertThat(js.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_8), is(Function.XDMP_INVOKE_70));
        assertThat(js.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_9), is(Function.XDMP_INVOKE_70));
    }

    public void testInvokeProfile() {
        QueryBuilder js = JavaScriptBuilder.INSTANCE;

        assertThat(js.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_5), is(nullValue()));
        assertThat(js.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_6), is(nullValue()));
        assertThat(js.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_7), is(nullValue()));
        assertThat(js.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_8), is(nullValue()));
        assertThat(js.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_9), is(nullValue()));
    }

    public void testInvokeDebug() {
        QueryBuilder js = JavaScriptBuilder.INSTANCE;

        assertThat(js.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_5), is(nullValue()));
        assertThat(js.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_6), is(nullValue()));
        assertThat(js.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_7), is(nullValue()));
        assertThat(js.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_8), is(nullValue()));
        assertThat(js.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_9), is(nullValue()));
    }
}
