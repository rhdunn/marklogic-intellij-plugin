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
import uk.co.reecedunn.intellij.plugin.marklogic.query.XSLTBuilder

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat

class XSLTBuilderTest : TestCase() {
    fun testEvalRun() {
        val xslt = XSLTBuilder

        assertThat<Function>(xslt.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(Function.XDMP_XSLT_EVAL_50))
        assertThat<Function>(xslt.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(Function.XDMP_XSLT_EVAL_50))
        assertThat<Function>(xslt.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(Function.XDMP_XSLT_EVAL_50))
        assertThat<Function>(xslt.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(Function.XDMP_XSLT_EVAL_50))
        assertThat<Function>(xslt.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(Function.XDMP_XSLT_EVAL_50))
    }

    fun testEvalProfile() {
        val xslt = XSLTBuilder

        assertThat<Function>(xslt.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(Function.PROF_XSLT_EVAL_50))
        assertThat<Function>(xslt.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(Function.PROF_XSLT_EVAL_50))
        assertThat<Function>(xslt.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(Function.PROF_XSLT_EVAL_50))
        assertThat<Function>(xslt.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(Function.PROF_XSLT_EVAL_50))
        assertThat<Function>(xslt.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(Function.PROF_XSLT_EVAL_50))
    }

    fun testEvalDebug() {
        val xslt = XSLTBuilder

        assertThat<Function>(xslt.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(nullValue()))
        assertThat<Function>(xslt.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(nullValue()))
        assertThat<Function>(xslt.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(nullValue()))
        assertThat<Function>(xslt.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(nullValue()))
        assertThat<Function>(xslt.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(nullValue()))
    }

    fun testInvokeRun() {
        val xslt = XSLTBuilder

        assertThat<Function>(xslt.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(Function.XDMP_XSLT_INVOKE_50))
        assertThat<Function>(xslt.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(Function.XDMP_XSLT_INVOKE_50))
        assertThat<Function>(xslt.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(Function.XDMP_XSLT_INVOKE_50))
        assertThat<Function>(xslt.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(Function.XDMP_XSLT_INVOKE_50))
        assertThat<Function>(xslt.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(Function.XDMP_XSLT_INVOKE_50))
    }

    fun testInvokeProfile() {
        val xslt = XSLTBuilder

        assertThat<Function>(xslt.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(Function.PROF_XSLT_INVOKE_50))
        assertThat<Function>(xslt.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(Function.PROF_XSLT_INVOKE_50))
        assertThat<Function>(xslt.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(Function.PROF_XSLT_INVOKE_50))
        assertThat<Function>(xslt.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(Function.PROF_XSLT_INVOKE_50))
        assertThat<Function>(xslt.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(Function.PROF_XSLT_INVOKE_50))
    }

    fun testInvokeDebug() {
        val xslt = XSLTBuilder

        assertThat<Function>(xslt.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(nullValue()))
        assertThat<Function>(xslt.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(nullValue()))
        assertThat<Function>(xslt.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(nullValue()))
        assertThat<Function>(xslt.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(nullValue()))
        assertThat<Function>(xslt.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(nullValue()))
    }
}
