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

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MarkLogicRunConfigurationEditor extends SettingsEditor<MarkLogicRunConfiguration> {
    private final MarkLogicConfigurationFactory mFactory;
    private final Project mProject;

    private MarkLogicRunConfigurationEditorUI mSettings;

    public MarkLogicRunConfigurationEditor(@NotNull MarkLogicConfigurationFactory factory, @NotNull Project project) {
        mFactory = factory;
        mProject = project;
    }

    @Override
    protected void resetEditorFrom(@NotNull MarkLogicRunConfiguration configuration) {
        mSettings.reset(configuration);
    }

    @Override
    protected void applyEditorTo(@NotNull MarkLogicRunConfiguration configuration) throws ConfigurationException {
        mSettings.apply(configuration);
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        mSettings = new MarkLogicRunConfigurationEditorUI(mFactory, mProject);
        return mSettings.getPanel();
    }
}
