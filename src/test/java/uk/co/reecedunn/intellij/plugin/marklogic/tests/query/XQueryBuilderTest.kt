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
import uk.co.reecedunn.intellij.plugin.marklogic.query.XQueryBuilder

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat

class XQueryBuilderTest : TestCase() {
    fun testEvalRun() {
        val xquery = XQueryBuilder

        assertThat<Function>(xquery.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(Function.XDMP_EVAL_50))
        assertThat<Function>(xquery.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(Function.XDMP_EVAL_50))
        assertThat<Function>(xquery.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(Function.XDMP_EVAL_70))
        assertThat<Function>(xquery.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(Function.XDMP_EVAL_70))
        assertThat<Function>(xquery.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(Function.XDMP_EVAL_70))
    }

    fun testEvalProfile() {
        val xquery = XQueryBuilder

        assertThat<Function>(xquery.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(Function.PROF_EVAL_50))
        assertThat<Function>(xquery.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(Function.PROF_EVAL_50))
        assertThat<Function>(xquery.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(Function.PROF_EVAL_50))
        assertThat<Function>(xquery.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(Function.PROF_EVAL_80))
        assertThat<Function>(xquery.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(Function.PROF_EVAL_80))
    }

    fun testEvalDebug() {
        val xquery = XQueryBuilder

        assertThat<Function>(xquery.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(Function.DBG_EVAL_50))
        assertThat<Function>(xquery.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(Function.DBG_EVAL_50))
        assertThat<Function>(xquery.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(Function.DBG_EVAL_50))
        assertThat<Function>(xquery.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(Function.DBG_EVAL_80))
        assertThat<Function>(xquery.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(Function.DBG_EVAL_80))
    }

    fun testInvokeRun() {
        val xquery = XQueryBuilder

        assertThat<Function>(xquery.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(Function.XDMP_INVOKE_50))
        assertThat<Function>(xquery.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(Function.XDMP_INVOKE_50))
        assertThat<Function>(xquery.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(Function.XDMP_INVOKE_70))
        assertThat<Function>(xquery.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(Function.XDMP_INVOKE_70))
        assertThat<Function>(xquery.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(Function.XDMP_INVOKE_70))
    }

    fun testInvokeProfile() {
        val xquery = XQueryBuilder

        assertThat<Function>(xquery.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(Function.PROF_INVOKE_50))
        assertThat<Function>(xquery.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(Function.PROF_INVOKE_50))
        assertThat<Function>(xquery.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(Function.PROF_INVOKE_50))
        assertThat<Function>(xquery.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(Function.PROF_INVOKE_80))
        assertThat<Function>(xquery.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(Function.PROF_INVOKE_80))
    }

    fun testInvokeDebug() {
        val xquery = XQueryBuilder

        assertThat<Function>(xquery.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(Function.DBG_INVOKE_50))
        assertThat<Function>(xquery.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(Function.DBG_INVOKE_50))
        assertThat<Function>(xquery.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(Function.DBG_INVOKE_50))
        assertThat<Function>(xquery.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(Function.DBG_INVOKE_80))
        assertThat<Function>(xquery.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(Function.DBG_INVOKE_80))
    }
}
