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
import uk.co.reecedunn.intellij.plugin.marklogic.api.Connection;
import uk.co.reecedunn.intellij.plugin.marklogic.api.EvalRequestBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.configuration.MarkLogicRunConfiguration;
import uk.co.reecedunn.intellij.plugin.marklogic.query.Function;
import uk.co.reecedunn.intellij.plugin.marklogic.query.QueryBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.QueryBuilderFactory;

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

        QueryBuilder queryBuilder = QueryBuilderFactory.createQueryBuilderForFile(configuration.getMainModulePath());
        if (queryBuilder == null) {
            throw new ExecutionException("Unsupported query file: " + configuration.getMainModulePath());
        }

        Function function = queryBuilder.createEvalBuilder(getEnvironment().getExecutor().getId(), configuration.getMarkLogicVersion());
        if (function == null) {
            throw new ExecutionException("Cannot run the query file with MarkLogic " + configuration.getMarkLogicVersion());
        }

        StringBuilder xquery = new StringBuilder();
        function.buildQuery(xquery, configuration);

        Connection connection = createConnection(configuration);
        EvalRequestBuilder builder = connection.createEvalRequestBuilder();
        builder.setContentDatabase(configuration.getContentDatabase());
        builder.setXQuery(xquery.toString());
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
            configuration.getServerHost(),
            configuration.getServerPort(),
            nullableValueOf(configuration.getUserName()),
            nullableValueOf(configuration.getPassword()),
            configuration.getMarkLogicVersion());
    }

    private String nullableValueOf(String value) {
        if (value == null || value.isEmpty()) return null;
        return value;
    }
}
