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
import uk.co.reecedunn.intellij.plugin.marklogic.query.SPARQLQueryBuilder

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat

class SPARQLQueryBuilderTest : TestCase() {
    fun testEvalRun() {
        val sparql = SPARQLQueryBuilder

        assertThat<Function>(sparql.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(nullValue()))
        assertThat<Function>(sparql.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(nullValue()))
        assertThat<Function>(sparql.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(Function.SEM_SPARQL_70))
        assertThat<Function>(sparql.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(Function.SEM_SPARQL_70))
        assertThat<Function>(sparql.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(Function.SEM_SPARQL_70))
    }

    fun testEvalProfile() {
        val sparql = SPARQLQueryBuilder

        assertThat<Function>(sparql.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(nullValue()))
        assertThat<Function>(sparql.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(nullValue()))
        assertThat<Function>(sparql.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(nullValue()))
        assertThat<Function>(sparql.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(nullValue()))
        assertThat<Function>(sparql.createEvalBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(nullValue()))
    }

    fun testEvalDebug() {
        val sparql = SPARQLQueryBuilder

        assertThat<Function>(sparql.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(nullValue()))
        assertThat<Function>(sparql.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(nullValue()))
        assertThat<Function>(sparql.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(nullValue()))
        assertThat<Function>(sparql.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(nullValue()))
        assertThat<Function>(sparql.createEvalBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(nullValue()))
    }

    fun testInvokeRun() {
        val sparql = SPARQLQueryBuilder

        assertThat<Function>(sparql.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(nullValue()))
        assertThat<Function>(sparql.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(nullValue()))
        assertThat<Function>(sparql.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(nullValue()))
        assertThat<Function>(sparql.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(nullValue()))
        assertThat<Function>(sparql.createInvokeBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(nullValue()))
    }

    fun testInvokeProfile() {
        val sparql = SPARQLQueryBuilder

        assertThat<Function>(sparql.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(nullValue()))
        assertThat<Function>(sparql.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(nullValue()))
        assertThat<Function>(sparql.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(nullValue()))
        assertThat<Function>(sparql.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(nullValue()))
        assertThat<Function>(sparql.createInvokeBuilder(ProfileExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(nullValue()))
    }

    fun testInvokeDebug() {
        val sparql = SPARQLQueryBuilder

        assertThat<Function>(sparql.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_5), `is`(nullValue()))
        assertThat<Function>(sparql.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_6), `is`(nullValue()))
        assertThat<Function>(sparql.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_7), `is`(nullValue()))
        assertThat<Function>(sparql.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_8), `is`(nullValue()))
        assertThat<Function>(sparql.createInvokeBuilder(DefaultDebugExecutor.EXECUTOR_ID, MARKLOGIC_9), `is`(nullValue()))
    }
}
