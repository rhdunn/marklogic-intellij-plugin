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
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.marklogic.xcc.*;
import org.jetbrains.annotations.NotNull;
import uk.co.reecedunn.intellij.plugin.marklogic.configuration.MarkLogicRunConfiguration;

public class MarkLogicRunProfileState extends CommandLineState {
    public MarkLogicRunProfileState(ExecutionEnvironment environment) {
        super(environment);
    }

    @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {
        MarkLogicRunConfiguration configuration = (MarkLogicRunConfiguration)getEnvironment().getRunProfile();
        ContentSource source = createContentSource(configuration);
        Session session = source.newSession();
        RequestOptions options = new RequestOptions();
        options.setQueryLanguage("xquery");
        Request request = session.newAdhocQuery("\"done\"", options);
        return new MarkLogicRequestHandler(session, request);
    }

    public boolean run(String query, MarkLogicResultsHandler handler, MarkLogicRunConfiguration configuration) {
        ContentSource source = createContentSource(configuration);
        Session session = source.newSession();
        RequestOptions options = new RequestOptions();
        options.setQueryLanguage("xquery");
        Request request = session.newAdhocQuery(query, options);
        MarkLogicRequestHandler requestHandler = new MarkLogicRequestHandler(session, request);
        return requestHandler.run(handler);
    }

    private ContentSource createContentSource(MarkLogicRunConfiguration configuration) {
        return ContentSourceFactory.newContentSource(
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
