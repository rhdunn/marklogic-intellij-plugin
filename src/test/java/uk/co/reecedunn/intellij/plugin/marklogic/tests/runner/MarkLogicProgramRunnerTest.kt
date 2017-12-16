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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.runner

import com.intellij.coverage.CoverageExecutor
import com.intellij.execution.ExecutionException
import com.intellij.execution.Executor
import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionEnvironment
import uk.co.reecedunn.intellij.plugin.marklogic.server.*
import uk.co.reecedunn.intellij.plugin.marklogic.ui.profile.ProfileExecutor
import uk.co.reecedunn.intellij.plugin.marklogic.ui.runner.MarkLogicProgramRunner
import uk.co.reecedunn.intellij.plugin.marklogic.tests.configuration.ConfigurationTestCase

import javax.swing.*

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat

class MarkLogicProgramRunnerTest : ConfigurationTestCase() {
    fun testUnknownRunConfiguration() {
        val runner = MarkLogicProgramRunner()
        val profile = object : RunProfile {
            @Throws(ExecutionException::class)
            override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState? {
                return null
            }

            override fun getName(): String? {
                return null
            }

            override fun getIcon(): Icon? {
                return null
            }
        }

        assertThat(runner.canRun(DefaultRunExecutor.EXECUTOR_ID, profile), `is`(false))
        assertThat(runner.canRun(DefaultDebugExecutor.EXECUTOR_ID, profile), `is`(false))
        assertThat(runner.canRun(CoverageExecutor.EXECUTOR_ID, profile), `is`(false))
        assertThat(runner.canRun(ProfileExecutor.EXECUTOR_ID, profile), `is`(false))
    }

    fun testDefaultRunConfiguration() {
        val profile = createConfiguration()

        val runner = MarkLogicProgramRunner()
        assertThat(runner.canRun(DefaultRunExecutor.EXECUTOR_ID, profile), `is`(false))
        assertThat(runner.canRun(DefaultDebugExecutor.EXECUTOR_ID, profile), `is`(false))
        assertThat(runner.canRun(CoverageExecutor.EXECUTOR_ID, profile), `is`(false))
        assertThat(runner.canRun(ProfileExecutor.EXECUTOR_ID, profile), `is`(false))
    }

    fun testNoActionsAvailable() {
        val profile = createConfiguration()
        profile.mainModuleFile = createVirtualFile("test.sjs", "2")
        profile.markLogicVersion = MARKLOGIC_7

        val runner = MarkLogicProgramRunner()
        assertThat(runner.canRun(DefaultRunExecutor.EXECUTOR_ID, profile), `is`(false))
        assertThat(runner.canRun(DefaultDebugExecutor.EXECUTOR_ID, profile), `is`(false))
        assertThat(runner.canRun(CoverageExecutor.EXECUTOR_ID, profile), `is`(false))
        assertThat(runner.canRun(ProfileExecutor.EXECUTOR_ID, profile), `is`(false))
    }

    fun testRunOnly() {
        val profile = createConfiguration()
        profile.mainModuleFile = createVirtualFile("test.sjs", "2")
        profile.markLogicVersion = MARKLOGIC_8

        val runner = MarkLogicProgramRunner()
        assertThat(runner.canRun(DefaultRunExecutor.EXECUTOR_ID, profile), `is`(true))
        assertThat(runner.canRun(DefaultDebugExecutor.EXECUTOR_ID, profile), `is`(false))
        assertThat(runner.canRun(CoverageExecutor.EXECUTOR_ID, profile), `is`(false))
        assertThat(runner.canRun(ProfileExecutor.EXECUTOR_ID, profile), `is`(false))
    }

    fun testRunAndProfile() {
        val profile = createConfiguration()
        profile.mainModuleFile = createVirtualFile("test.xsl", "")
        profile.markLogicVersion = MARKLOGIC_8

        val runner = MarkLogicProgramRunner()
        assertThat(runner.canRun(DefaultRunExecutor.EXECUTOR_ID, profile), `is`(true))
        assertThat(runner.canRun(DefaultDebugExecutor.EXECUTOR_ID, profile), `is`(false))
        assertThat(runner.canRun(CoverageExecutor.EXECUTOR_ID, profile), `is`(false))
        assertThat(runner.canRun(ProfileExecutor.EXECUTOR_ID, profile), `is`(true))
    }

    fun testRunProfileAndDebug() {
        // NOTE: Debugging support is not currently enabled.
        val profile = createConfiguration()
        profile.mainModuleFile = createVirtualFile("test.xqy", "2")
        profile.markLogicVersion = MARKLOGIC_8

        val runner = MarkLogicProgramRunner()
        assertThat(runner.canRun(DefaultRunExecutor.EXECUTOR_ID, profile), `is`(true))
        assertThat(runner.canRun(DefaultDebugExecutor.EXECUTOR_ID, profile), `is`(false))
        assertThat(runner.canRun(CoverageExecutor.EXECUTOR_ID, profile), `is`(false))
        assertThat(runner.canRun(ProfileExecutor.EXECUTOR_ID, profile), `is`(true))
    }
}
