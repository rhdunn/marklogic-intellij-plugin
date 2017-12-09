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
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.util.xmlb.Converter;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.OptionTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.reecedunn.intellij.plugin.marklogic.api.RDFFormat;
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicServer;
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersion;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.runner.MarkLogicResultsHandler;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.runner.MarkLogicRunProfileState;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.settings.MarkLogicSettings;

import java.io.File;

public class MarkLogicRunConfiguration extends RunConfigurationBase implements PersistentStateComponent<MarkLogicRunConfiguration.ConfigData> {
    public static class MarkLogicVersionConverter extends Converter<MarkLogicVersion> {
        @Nullable
        @Override
        public MarkLogicVersion fromString(@NotNull String version) {
            return MarkLogicVersion.Companion.parse(version);
        }

        @NotNull
        @Override
        public String toString(@NotNull MarkLogicVersion version) {
            return version.toString();
        }
    }

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
        @OptionTag(converter = MarkLogicVersionConverter.class)
        public MarkLogicVersion markLogicVersion = new MarkLogicVersion(7, 0, null, null);
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

    // region RunConfigurationBase

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

    // endregion
    // region PersistentStateComponent

    @Nullable
    @Override
    public ConfigData getState() {
        return data;
    }

    @Override
    public void loadState(ConfigData state) {
        XmlSerializerUtil.copyBean(state, data);
        setMainModulePath(data.mainModulePath); // Update the associated VirtualFile.
        MarkLogicSettings.Companion.getInstance().getServers().forEach((server) -> {
            if (server.getHostname().equals(data.serverHost)) {
                setServer(server);
            }
        });
    }

    // endregion
    // region Settings

    public MarkLogicServer getServer() {
        return mServer;
    }

    public void setServer(MarkLogicServer server) {
        mServer = server;
        data.serverHost = (mServer == null) ? null : mServer.getHostname();
    }

    public double getMarkLogicMajorMinor() {
        return data.markLogicVersion.getMajor();
    }

    public void setMarkLogicMajorMinor(double markLogicVersion) {
        data.markLogicVersion = new MarkLogicVersion((int)markLogicVersion, 0, null, null);
    }

    public MarkLogicVersion getMarkLogicVersion() {
        return data.markLogicVersion;
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

    // endregion
}
