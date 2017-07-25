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
package uk.co.reecedunn.intellij.plugin.marklogic.runner;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.filters.RegexpFilter;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.reecedunn.intellij.plugin.marklogic.configuration.MarkLogicRunConfiguration;
import uk.co.reecedunn.intellij.plugin.marklogic.configuration.script.ScriptFactory;
import uk.co.reecedunn.intellij.plugin.marklogic.api.rest.Connection;
import uk.co.reecedunn.intellij.plugin.marklogic.api.rest.EvalRequestBuilder;

public class MarkLogicRunProfileState extends CommandLineState {
    public MarkLogicRunProfileState(@Nullable ExecutionEnvironment environment) {
        super(environment);
        if (environment != null) {
            addConsoleFilters(new RegexpFilter(environment.getProject(), "$FILE_PATH$:$LINE$"));
        }
    }

    @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {
        MarkLogicRunConfiguration configuration = (MarkLogicRunConfiguration)getEnvironment().getRunProfile();
        ScriptFactory scriptFactory = configuration.getScriptFactory();
        Connection connection = createConnection(configuration);
        EvalRequestBuilder builder = new EvalRequestBuilder();
        builder.setContentDatabase(configuration.getContentDatabase());
        builder.setXQuery(scriptFactory.createScript(configuration));
        return new MarkLogicRestHandler(builder.build(connection), configuration.getMainModulePath());
    }

    public boolean run(String query, MarkLogicResultsHandler handler, MarkLogicRunConfiguration configuration) {
        Connection connection = createConnection(configuration);
        EvalRequestBuilder builder = new EvalRequestBuilder();
        builder.setContentDatabase(configuration.getContentDatabase());
        builder.setXQuery(query);
        MarkLogicRestHandler restHandler = new MarkLogicRestHandler(builder.build(connection), "/eval");
        return restHandler.run(handler);
    }

    private Connection createConnection(MarkLogicRunConfiguration configuration) {
        return Connection.newConnection(
            configuration.getServerHost(),
            configuration.getServerPort(),
            nullableValueOf(configuration.getUserName()),
            nullableValueOf(configuration.getPassword()));
    }

    private String nullableValueOf(String value) {
        if (value == null || value.isEmpty()) return null;
        return value;
    }
}
