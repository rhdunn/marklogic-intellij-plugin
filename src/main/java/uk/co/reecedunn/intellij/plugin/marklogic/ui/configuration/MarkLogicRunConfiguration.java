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
package uk.co.reecedunn.intellij.plugin.marklogic.ui.configuration;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.DefaultJDOMExternalizer;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.reecedunn.intellij.plugin.marklogic.api.RDFFormat;
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicServer;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.runner.MarkLogicResultsHandler;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.runner.MarkLogicRunProfileState;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.settings.MarkLogicSettings;

import java.io.File;

public class MarkLogicRunConfiguration extends RunConfigurationBase {
    private MarkLogicServer mServer;

    public static final String[] EXTENSIONS = new String[]{
        "xq", "xqy", "xquery", "xql", "xqu",
        "js", "sjs",
        "sql",
        "sparql", "rq",
        "ru",
    };

    @SuppressWarnings("WeakerAccess") // DefaultJDOMExternalizer requires public access to the fields.
    static class ConfigData {
        public String serverHost = "localhost";
        public double markLogicVersion = 7.0;
        public String contentDatabase = null;
        public String moduleDatabase = null;
        public String moduleRoot = "/";
        public String mainModulePath = "";
        public String tripleFormat = RDFFormat.SEM_TRIPLE.getMarkLogicName();
    }

    private ConfigData data = new ConfigData();
    private VirtualFile mainModuleFile = null;

    MarkLogicRunConfiguration(@NotNull Project project, @NotNull ConfigurationFactory factory, String name) {
        super(project, factory, name);
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new MarkLogicRunConfigurationEditor((MarkLogicConfigurationFactory)getFactory(), getProject());
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
        setMainModulePath(data.mainModulePath); // Update the associated VirtualFile.
        MarkLogicSettings.Companion.getInstance().getServers().forEach((server) -> {
            if (server.getHostname().equals(data.serverHost)) {
                setServer(server);
            }
        });
    }

    @SuppressWarnings("deprecation")
    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        super.writeExternal(element);
        DefaultJDOMExternalizer.writeExternal(data, element);
    }

    public MarkLogicServer getServer() {
        return mServer;
    }

    public void setServer(MarkLogicServer server) {
        mServer = server;
        data.serverHost = mServer.getHostname();
    }

    public double getMarkLogicVersion() {
        return data.markLogicVersion;
    }

    public void setMarkLogicVersion(double markLogicVersion) {
        data.markLogicVersion = markLogicVersion;
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

        final String url = VfsUtil.pathToUrl(mainModulePath.replace(File.separatorChar, '/'));
        mainModuleFile = VirtualFileManager.getInstance().findFileByUrl(url);
    }

    public VirtualFile getMainModuleFile() {
        return mainModuleFile;
    }

    public void setMainModuleFile(VirtualFile mainModuleFile) {
        this.mainModuleFile = mainModuleFile;
        data.mainModulePath = mainModuleFile.getCanonicalPath();
    }

    public RDFFormat getTripleFormat() {
        return RDFFormat.parse(data.tripleFormat);
    }

    public void setTripleFormat(RDFFormat tripleFormat) {
        data.tripleFormat = tripleFormat.getMarkLogicName();
    }
}
