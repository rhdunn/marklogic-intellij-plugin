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
package uk.co.reecedunn.intellij.plugin.marklogic.configuration;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.DefaultJDOMExternalizer;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.util.ArrayUtil;
import com.intellij.util.PathUtil;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.reecedunn.intellij.plugin.marklogic.configuration.script.QueryScript;
import uk.co.reecedunn.intellij.plugin.marklogic.configuration.script.ScriptFactory;
import uk.co.reecedunn.intellij.plugin.marklogic.configuration.script.EvalScript;
import uk.co.reecedunn.intellij.plugin.marklogic.runner.MarkLogicResultsHandler;
import uk.co.reecedunn.intellij.plugin.marklogic.runner.MarkLogicRunProfileState;

public class MarkLogicRunConfiguration extends RunConfigurationBase {
    private static ScriptFactory JAVASCRIPT = new EvalScript("xdmp:javascript-eval");
    private static ScriptFactory SQL = new QueryScript("xdmp:sql");
    private static ScriptFactory XQUERY = new EvalScript("xdmp:eval");

    public static final String[] EXTENSIONS = new String[]{
        "xq", "xqy", "xquery", "xql", "xqu",
        "js", "sjs",
        "sql",
    };

    public static final ScriptFactory[] SCRIPT_FACTORIES = new ScriptFactory[] {
        XQUERY, XQUERY, XQUERY, XQUERY, XQUERY,
        JAVASCRIPT, JAVASCRIPT,
        SQL,
    };

    @SuppressWarnings("WeakerAccess") // DefaultJDOMExternalizer requires public access to the fields.
    static class ConfigData {
        public String serverHost = "localhost";
        public int serverPort = 8000;
        public String userName = "";
        public String password = "";
        public String contentDatabase = null;
        public String moduleDatabase = null;
        public String moduleRoot = "/";
        public String mainModulePath = "";
    }

    private ConfigData data = new ConfigData();

    MarkLogicRunConfiguration(@NotNull Project project, @NotNull ConfigurationFactory factory, String name) {
        super(project, factory, name);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new MarkLogicSettingsEditor((MarkLogicConfigurationFactory)getFactory(), getProject());
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) throws ExecutionException {
        return new MarkLogicRunProfileState(environment);
    }

    public boolean run(String query, MarkLogicResultsHandler handler) {
        MarkLogicRunProfileState state = new MarkLogicRunProfileState(null);
        return state.run(query, handler, this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void readExternal(Element element) throws InvalidDataException {
        super.readExternal(element);
        DefaultJDOMExternalizer.readExternal(data, element);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        super.writeExternal(element);
        DefaultJDOMExternalizer.writeExternal(data, element);
    }

    public String getServerHost() {
        return data.serverHost;
    }

    public void setServerHost(String host) {
        data.serverHost = host;
    }

    public int getServerPort() {
        return data.serverPort;
    }

    public void setServerPort(int port) {
        data.serverPort = port;
    }

    public String getUserName() {
        return data.userName;
    }

    public void setUserName(String userName) {
        this.data.userName = userName;
    }

    public String getPassword() {
        return data.password;
    }

    public void setPassword(String password) {
        this.data.password = password;
    }

    public String getContentDatabase() {
        return data.contentDatabase;
    }

    public void setContentDatabase(String contentDatabase) {
        data.contentDatabase = contentDatabase;
    }

    public String getModuleDatabase() {
        return data.moduleDatabase;
    }

    public void setModuleDatabase(String moduleDatabase) {
        data.moduleDatabase = moduleDatabase;
    }

    public String getModuleRoot() {
        return data.moduleRoot;
    }

    public void setModuleRoot(String moduleRoot) {
        data.moduleRoot = moduleRoot;
    }

    public String getMainModulePath() {
        return data.mainModulePath;
    }

    public void setMainModulePath(String mainModulePath) {
        data.mainModulePath = mainModulePath;
    }

    public String getAdhocQuery() {
        final String ext = PathUtil.getFileExtension(getMainModulePath());
        final int index = ArrayUtil.indexOf(EXTENSIONS, ext);
        if (index < 0) {
            return null;
        }

        return SCRIPT_FACTORIES[index].createScript(this);
    }
}
