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
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.reecedunn.intellij.plugin.marklogic.runner.MarkLogicRunProfileState;

public class MarkLogicRunConfiguration extends RunConfigurationBase {
    @SuppressWarnings("WeakerAccess") // DefaultJDOMExternalizer requires public access to the fields.
    static class ConfigData {
        public String serverHost = "localhost";
        public int serverPort = 8000;
        public String userName = "";
        public String password = "";
    }

    private ConfigData data = new ConfigData();

    MarkLogicRunConfiguration(@NotNull Project project, @NotNull ConfigurationFactory factory, String name) {
        super(project, factory, name);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new MarkLogicSettingsEditor();
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment environment) throws ExecutionException {
        return new MarkLogicRunProfileState(environment);
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
}
