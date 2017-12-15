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
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersionKt;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.profile.ProfileExecutor;
import uk.co.reecedunn.intellij.plugin.marklogic.query.Function;
import uk.co.reecedunn.intellij.plugin.marklogic.query.QueryBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.XSLTBuilder;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("ConstantConditions")
public class XSLTBuilderTest extends TestCase {
    public void testEvalRun() {
        QueryBuilder xslt = XSLTBuilder.INSTANCE;

        assertThat(xslt.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_5()), is(Function.XDMP_XSLT_EVAL_50));
        assertThat(xslt.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_6()), is(Function.XDMP_XSLT_EVAL_50));
        assertThat(xslt.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_7()), is(Function.XDMP_XSLT_EVAL_50));
        assertThat(xslt.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_8()), is(Function.XDMP_XSLT_EVAL_50));
        assertThat(xslt.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_9()), is(Function.XDMP_XSLT_EVAL_50));
    }

    public void testEvalProfile() {
        QueryBuilder xslt = XSLTBuilder.INSTANCE;

        assertThat(xslt.createEvalBuilder(ProfileExecutor.Companion.getEXECUTOR_ID(), MarkLogicVersionKt.getMARKLOGIC_5()), is(Function.PROF_XSLT_EVAL_50));
        assertThat(xslt.createEvalBuilder(ProfileExecutor.Companion.getEXECUTOR_ID(), MarkLogicVersionKt.getMARKLOGIC_6()), is(Function.PROF_XSLT_EVAL_50));
        assertThat(xslt.createEvalBuilder(ProfileExecutor.Companion.getEXECUTOR_ID(), MarkLogicVersionKt.getMARKLOGIC_7()), is(Function.PROF_XSLT_EVAL_50));
        assertThat(xslt.createEvalBuilder(ProfileExecutor.Companion.getEXECUTOR_ID(), MarkLogicVersionKt.getMARKLOGIC_8()), is(Function.PROF_XSLT_EVAL_50));
        assertThat(xslt.createEvalBuilder(ProfileExecutor.Companion.getEXECUTOR_ID(), MarkLogicVersionKt.getMARKLOGIC_9()), is(Function.PROF_XSLT_EVAL_50));
    }

    public void testEvalDebug() {
        QueryBuilder xslt = XSLTBuilder.INSTANCE;

        assertThat(xslt.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_5()), is(nullValue()));
        assertThat(xslt.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_6()), is(nullValue()));
        assertThat(xslt.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_7()), is(nullValue()));
        assertThat(xslt.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_8()), is(nullValue()));
        assertThat(xslt.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_9()), is(nullValue()));
    }

    public void testInvokeRun() {
        QueryBuilder xslt = XSLTBuilder.INSTANCE;

        assertThat(xslt.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_5()), is(Function.XDMP_XSLT_INVOKE_50));
        assertThat(xslt.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_6()), is(Function.XDMP_XSLT_INVOKE_50));
        assertThat(xslt.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_7()), is(Function.XDMP_XSLT_INVOKE_50));
        assertThat(xslt.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_8()), is(Function.XDMP_XSLT_INVOKE_50));
        assertThat(xslt.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_9()), is(Function.XDMP_XSLT_INVOKE_50));
    }

    public void testInvokeProfile() {
        QueryBuilder xslt = XSLTBuilder.INSTANCE;

        assertThat(xslt.createInvokeBuilder(ProfileExecutor.Companion.getEXECUTOR_ID(), MarkLogicVersionKt.getMARKLOGIC_5()), is(Function.PROF_XSLT_INVOKE_50));
        assertThat(xslt.createInvokeBuilder(ProfileExecutor.Companion.getEXECUTOR_ID(), MarkLogicVersionKt.getMARKLOGIC_6()), is(Function.PROF_XSLT_INVOKE_50));
        assertThat(xslt.createInvokeBuilder(ProfileExecutor.Companion.getEXECUTOR_ID(), MarkLogicVersionKt.getMARKLOGIC_7()), is(Function.PROF_XSLT_INVOKE_50));
        assertThat(xslt.createInvokeBuilder(ProfileExecutor.Companion.getEXECUTOR_ID(), MarkLogicVersionKt.getMARKLOGIC_8()), is(Function.PROF_XSLT_INVOKE_50));
        assertThat(xslt.createInvokeBuilder(ProfileExecutor.Companion.getEXECUTOR_ID(), MarkLogicVersionKt.getMARKLOGIC_9()), is(Function.PROF_XSLT_INVOKE_50));
    }

    public void testInvokeDebug() {
        QueryBuilder xslt = XSLTBuilder.INSTANCE;

        assertThat(xslt.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_5()), is(nullValue()));
        assertThat(xslt.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_6()), is(nullValue()));
        assertThat(xslt.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_7()), is(nullValue()));
        assertThat(xslt.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_8()), is(nullValue()));
        assertThat(xslt.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_9()), is(nullValue()));
    }
}
