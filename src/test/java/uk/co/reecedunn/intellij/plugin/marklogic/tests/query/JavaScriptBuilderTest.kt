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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.query

import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.executors.DefaultRunExecutor
import junit.framework.TestCase
import uk.co.reecedunn.intellij.plugin.marklogic.server.*
import uk.co.reecedunn.intellij.plugin.marklogic.ui.profile.ProfileExecutor
import uk.co.reecedunn.intellij.plugin.marklogic.query.Function
import uk.co.reecedunn.intellij.plugin.marklogic.query.JavaScriptBuilder

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat

class JavaScriptBuilderTest : TestCase() {
    fun testEvalRun() {
        val js = JavaScriptBuilder

        assertThat<Function>(js.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(nullValue()))
        assertThat<Function>(js.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(nullValue()))
        assertThat<Function>(js.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(nullValue()))
        assertThat<Function>(js.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(Function.XDMP_JAVASCRIPT_EVAL_80))
        assertThat<Function>(js.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(Function.XDMP_JAVASCRIPT_EVAL_80))
    }

    fun testEvalProfile() {
        val js = JavaScriptBuilder

        assertThat<Function>(js.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(nullValue()))
        assertThat<Function>(js.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(nullValue()))
        assertThat<Function>(js.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(nullValue()))
        assertThat<Function>(js.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(nullValue()))
        assertThat<Function>(js.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(nullValue()))
    }

    fun testEvalDebug() {
        val js = JavaScriptBuilder

        assertThat<Function>(js.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(nullValue()))
        assertThat<Function>(js.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(nullValue()))
        assertThat<Function>(js.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(nullValue()))
        assertThat<Function>(js.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(nullValue()))
        assertThat<Function>(js.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(nullValue()))
    }

    fun testInvokeRun() {
        val js = JavaScriptBuilder

        assertThat<Function>(js.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(nullValue()))
        assertThat<Function>(js.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(nullValue()))
        assertThat<Function>(js.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(nullValue()))
        assertThat<Function>(js.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(Function.XDMP_INVOKE_70))
        assertThat<Function>(js.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(Function.XDMP_INVOKE_70))
    }

    fun testInvokeProfile() {
        val js = JavaScriptBuilder

        assertThat<Function>(js.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(nullValue()))
        assertThat<Function>(js.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(nullValue()))
        assertThat<Function>(js.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(nullValue()))
        assertThat<Function>(js.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(nullValue()))
        assertThat<Function>(js.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(nullValue()))
    }

    fun testInvokeDebug() {
        val js = JavaScriptBuilder

        assertThat<Function>(js.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(nullValue()))
        assertThat<Function>(js.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(nullValue()))
        assertThat<Function>(js.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(nullValue()))
        assertThat<Function>(js.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(nullValue()))
        assertThat<Function>(js.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(nullValue()))
    }
}
