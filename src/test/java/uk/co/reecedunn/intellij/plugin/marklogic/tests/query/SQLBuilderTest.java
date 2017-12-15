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
import uk.co.reecedunn.intellij.plugin.marklogic.query.SQLBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.QueryBuilder;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("ConstantConditions")
public class SQLBuilderTest extends TestCase {
    public void testEvalRun() {
        QueryBuilder sql = SQLBuilder.INSTANCE;

        assertThat(sql.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_5()), is(nullValue()));
        assertThat(sql.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_6()), is(nullValue()));
        assertThat(sql.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_7()), is(nullValue()));
        assertThat(sql.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_8()), is(Function.XDMP_SQL_80));
        assertThat(sql.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_9()), is(Function.XDMP_SQL_90));
    }

    public void testEvalProfile() {
        QueryBuilder sql = SQLBuilder.INSTANCE;

        assertThat(sql.createEvalBuilder(ProfileExecutor.Companion.getEXECUTOR_ID(), MarkLogicVersionKt.getMARKLOGIC_5()), is(nullValue()));
        assertThat(sql.createEvalBuilder(ProfileExecutor.Companion.getEXECUTOR_ID(), MarkLogicVersionKt.getMARKLOGIC_6()), is(nullValue()));
        assertThat(sql.createEvalBuilder(ProfileExecutor.Companion.getEXECUTOR_ID(), MarkLogicVersionKt.getMARKLOGIC_7()), is(nullValue()));
        assertThat(sql.createEvalBuilder(ProfileExecutor.Companion.getEXECUTOR_ID(), MarkLogicVersionKt.getMARKLOGIC_8()), is(nullValue()));
        assertThat(sql.createEvalBuilder(ProfileExecutor.Companion.getEXECUTOR_ID(), MarkLogicVersionKt.getMARKLOGIC_9()), is(nullValue()));
    }

    public void testEvalDebug() {
        QueryBuilder sql = SQLBuilder.INSTANCE;

        assertThat(sql.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_5()), is(nullValue()));
        assertThat(sql.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_6()), is(nullValue()));
        assertThat(sql.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_7()), is(nullValue()));
        assertThat(sql.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_8()), is(nullValue()));
        assertThat(sql.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_9()), is(nullValue()));
    }

    public void testInvokeRun() {
        QueryBuilder sql = SQLBuilder.INSTANCE;

        assertThat(sql.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_5()), is(nullValue()));
        assertThat(sql.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_6()), is(nullValue()));
        assertThat(sql.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_7()), is(nullValue()));
        assertThat(sql.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_8()), is(nullValue()));
        assertThat(sql.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_9()), is(nullValue()));
    }

    public void testInvokeProfile() {
        QueryBuilder sql = SQLBuilder.INSTANCE;

        assertThat(sql.createInvokeBuilder(ProfileExecutor.Companion.getEXECUTOR_ID(), MarkLogicVersionKt.getMARKLOGIC_5()), is(nullValue()));
        assertThat(sql.createInvokeBuilder(ProfileExecutor.Companion.getEXECUTOR_ID(), MarkLogicVersionKt.getMARKLOGIC_6()), is(nullValue()));
        assertThat(sql.createInvokeBuilder(ProfileExecutor.Companion.getEXECUTOR_ID(), MarkLogicVersionKt.getMARKLOGIC_7()), is(nullValue()));
        assertThat(sql.createInvokeBuilder(ProfileExecutor.Companion.getEXECUTOR_ID(), MarkLogicVersionKt.getMARKLOGIC_8()), is(nullValue()));
        assertThat(sql.createInvokeBuilder(ProfileExecutor.Companion.getEXECUTOR_ID(), MarkLogicVersionKt.getMARKLOGIC_9()), is(nullValue()));
    }

    public void testInvokeDebug() {
        QueryBuilder sql = SQLBuilder.INSTANCE;

        assertThat(sql.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_5()), is(nullValue()));
        assertThat(sql.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_6()), is(nullValue()));
        assertThat(sql.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_7()), is(nullValue()));
        assertThat(sql.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_8()), is(nullValue()));
        assertThat(sql.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MarkLogicVersionKt.getMARKLOGIC_9()), is(nullValue()));
    }
}
