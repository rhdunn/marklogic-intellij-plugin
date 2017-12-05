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
package uk.co.reecedunn.intellij.plugin.marklogic.ui.runner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.filters.RegexpFilter;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAware;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Connection;
import uk.co.reecedunn.intellij.plugin.marklogic.api.EvalRequestBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.configuration.MarkLogicRunConfiguration;
import uk.co.reecedunn.intellij.plugin.marklogic.query.Function;
import uk.co.reecedunn.intellij.plugin.marklogic.query.QueryBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.QueryBuilderFactory;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.resources.MarkLogicBundle;

public class MarkLogicRunProfileState extends CommandLineState {
    public MarkLogicRunProfileState(@Nullable ExecutionEnvironment environment) {
        super(environment);
        if (environment != null) {
            addConsoleFilters(new RegexpFilter(environment.getProject(), "$FILE_PATH$:$LINE$"));
        }
    }

    @NotNull
    protected AnAction[] createActions(final ConsoleView console, final ProcessHandler processHandler, Executor executor) {
        if (console == null || !console.canPause() || (executor != null && !DefaultRunExecutor.EXECUTOR_ID.equals(executor.getId()))) {
            return new AnAction[] {
                new ViewErrorAction()
            };
        }
        return new AnAction[] {
            new PauseOutputAction(console, processHandler),
            new ViewErrorAction()
        };
    }

    @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {
        MarkLogicRunConfiguration configuration = (MarkLogicRunConfiguration)getEnvironment().getRunProfile();

        QueryBuilder queryBuilder = QueryBuilderFactory.createQueryBuilderForFile(configuration.getMainModulePath());
        if (queryBuilder == null) {
            throw new ExecutionException("Unsupported query file: " + configuration.getMainModulePath());
        }

        Function function = queryBuilder.createEvalBuilder(getEnvironment().getExecutor().getId(), configuration.getMarkLogicVersion());
        if (function == null) {
            throw new ExecutionException("Cannot run the query file with MarkLogic " + configuration.getMarkLogicVersion());
        }

        Connection connection = createConnection(configuration);
        EvalRequestBuilder builder = connection.createEvalRequestBuilder();
        builder.setContentDatabase(configuration.getContentDatabase());
        builder.setXQuery(function.buildQuery(configuration));
        return new MarkLogicRequestHandler(builder.build(), configuration.getMainModulePath());
    }

    public boolean run(String query, MarkLogicResultsHandler handler, MarkLogicRunConfiguration configuration) {
        Connection connection = createConnection(configuration);
        EvalRequestBuilder builder = connection.createEvalRequestBuilder();
        builder.setContentDatabase(configuration.getContentDatabase());
        builder.setXQuery(query);
        MarkLogicRequestHandler restHandler = new MarkLogicRequestHandler(builder.build(), "/eval");
        return restHandler.run(handler);
    }

    private Connection createConnection(MarkLogicRunConfiguration configuration) {
        return Connection.newConnection(
            configuration.getServer().getHostname(),
            configuration.getServer().getAppServerPort(),
            configuration.getServer().getUsername(),
            configuration.getServer().getPassword(),
            configuration.getMarkLogicVersion());
    }

    protected static class ViewErrorAction extends AnAction implements DumbAware {
        public ViewErrorAction() {
            final String message = MarkLogicBundle.message("action.refresh");
            getTemplatePresentation().setDescription(message);
            getTemplatePresentation().setText(message);
            getTemplatePresentation().setIcon(AllIcons.Actions.ShowViewer);
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
        }
    }
}
