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
package uk.co.reecedunn.intellij.plugin.marklogic.settings;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import uk.co.reecedunn.intellij.plugin.core.ui.ConfigurableImpl;
import uk.co.reecedunn.intellij.plugin.core.ui.SettingsUI;

public class MarkLogicProjectSettingsConfigurable extends ConfigurableImpl<MarkLogicProjectSettings> {
    public MarkLogicProjectSettingsConfigurable(Project project) {
        super(MarkLogicProjectSettings.Companion.getInstance(project));
    }

    @Override
    @Nls
    public String getDisplayName() {
        return "MarkLogic";
    }

    @Override
    @Nullable
    public String getHelpTopic() {
        return null;
    }

    @Override
    public SettingsUI<MarkLogicProjectSettings> createSettingsUI() {
        return new MarkLogicProjectSettingsUI();
    }
}
