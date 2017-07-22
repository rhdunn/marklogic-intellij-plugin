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

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MarkLogicConfigurationType implements ConfigurationType {
    private static final Icon FILETYPE_ICON = IconLoader.getIcon("/icons/marklogic.png");

    @Override
    public String getDisplayName() {
        return "MarkLogic";
    }

    @Override
    public String getConfigurationTypeDescription() {
        return getDisplayName();
    }

    @Override
    public Icon getIcon() {
        return FILETYPE_ICON;
    }

    @NotNull
    @Override
    public String getId() {
        return "MarkLogicConfiguration";
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[] {
            new MarkLogicConfigurationFactory(this)
        };
    }
}
