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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.runner;

import com.intellij.coverage.CoverageExecutor;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.reecedunn.intellij.plugin.marklogic.configuration.MarkLogicRunConfiguration;
import uk.co.reecedunn.intellij.plugin.marklogic.executors.ProfileExecutor;
import uk.co.reecedunn.intellij.plugin.marklogic.runner.MarkLogicProgramRunner;
import uk.co.reecedunn.intellij.plugin.marklogic.tests.configuration.ConfigurationTestCase;

import javax.swing.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class MarkLogicProgramRunnerTest extends ConfigurationTestCase {
    public void testUnknownRunConfiguration() {
        ProgramRunner runner = new MarkLogicProgramRunner();
        RunProfile profile = new RunProfile() {
            @Nullable
            @Override
            public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) throws ExecutionException {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Nullable
            @Override
            public Icon getIcon() {
                return null;
            }
        };

        assertThat(runner.canRun(DefaultRunExecutor.EXECUTOR_ID, profile), is(false));
        assertThat(runner.canRun(DefaultDebugExecutor.EXECUTOR_ID, profile), is(false));
        assertThat(runner.canRun(CoverageExecutor.EXECUTOR_ID, profile), is(false));
        assertThat(runner.canRun(ProfileExecutor.EXECUTOR_ID, profile), is(false));
    }

    public void testDefaultRunConfiguration() {
        RunProfile profile = createConfiguration();

        ProgramRunner runner = new MarkLogicProgramRunner();
        assertThat(runner.canRun(DefaultRunExecutor.EXECUTOR_ID, profile), is(false));
        assertThat(runner.canRun(DefaultDebugExecutor.EXECUTOR_ID, profile), is(false));
        assertThat(runner.canRun(CoverageExecutor.EXECUTOR_ID, profile), is(false));
        assertThat(runner.canRun(ProfileExecutor.EXECUTOR_ID, profile), is(false));
    }

    public void testNoActionsAvailable() {
        MarkLogicRunConfiguration profile = createConfiguration();
        profile.setMainModuleFile(createVirtualFile("test.sjs", "2"));
        profile.setMarkLogicVersion(7.0);

        ProgramRunner runner = new MarkLogicProgramRunner();
        assertThat(runner.canRun(DefaultRunExecutor.EXECUTOR_ID, profile), is(false));
        assertThat(runner.canRun(DefaultDebugExecutor.EXECUTOR_ID, profile), is(false));
        assertThat(runner.canRun(CoverageExecutor.EXECUTOR_ID, profile), is(false));
        assertThat(runner.canRun(ProfileExecutor.EXECUTOR_ID, profile), is(false));
    }

    public void testRunOnly() {
        MarkLogicRunConfiguration profile = createConfiguration();
        profile.setMainModuleFile(createVirtualFile("test.sjs", "2"));
        profile.setMarkLogicVersion(8.0);

        ProgramRunner runner = new MarkLogicProgramRunner();
        assertThat(runner.canRun(DefaultRunExecutor.EXECUTOR_ID, profile), is(true));
        assertThat(runner.canRun(DefaultDebugExecutor.EXECUTOR_ID, profile), is(false));
        assertThat(runner.canRun(CoverageExecutor.EXECUTOR_ID, profile), is(false));
        assertThat(runner.canRun(ProfileExecutor.EXECUTOR_ID, profile), is(false));
    }

    public void testRunAndProfile() {
        MarkLogicRunConfiguration profile = createConfiguration();
        profile.setMainModuleFile(createVirtualFile("test.xsl", ""));
        profile.setMarkLogicVersion(8.0);

        ProgramRunner runner = new MarkLogicProgramRunner();
        assertThat(runner.canRun(DefaultRunExecutor.EXECUTOR_ID, profile), is(true));
        assertThat(runner.canRun(DefaultDebugExecutor.EXECUTOR_ID, profile), is(false));
        assertThat(runner.canRun(CoverageExecutor.EXECUTOR_ID, profile), is(false));
        assertThat(runner.canRun(ProfileExecutor.EXECUTOR_ID, profile), is(true));
    }

    public void testRunProfileAndDebug() {
        // NOTE: Debugging support is not currently enabled.
        MarkLogicRunConfiguration profile = createConfiguration();
        profile.setMainModuleFile(createVirtualFile("test.xqy", "2"));
        profile.setMarkLogicVersion(8.0);

        ProgramRunner runner = new MarkLogicProgramRunner();
        assertThat(runner.canRun(DefaultRunExecutor.EXECUTOR_ID, profile), is(true));
        assertThat(runner.canRun(DefaultDebugExecutor.EXECUTOR_ID, profile), is(false));
        assertThat(runner.canRun(CoverageExecutor.EXECUTOR_ID, profile), is(false));
        assertThat(runner.canRun(ProfileExecutor.EXECUTOR_ID, profile), is(true));
    }
}
