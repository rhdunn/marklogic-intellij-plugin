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
package uk.co.reecedunn.intellij.plugin.marklogic.ui.executors;

import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.icons.AllIcons;
import org.jetbrains.annotations.NotNull;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.resources.MarkLogicBundle;

import javax.swing.*;

public class ProfileExecutor extends DefaultRunExecutor {
    public static final String EXECUTOR_ID = "MarkLogicProfile";

    @Override
    public String getToolWindowId() {
        return EXECUTOR_ID;
    }

    @Override
    public Icon getToolWindowIcon() {
        return AllIcons.Actions.Profile;
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return AllIcons.Actions.Profile;
    }

    @Override
    public Icon getDisabledIcon() {
        return AllIcons.Actions.Profile;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @NotNull
    @Override
    public String getActionName() {
        return MarkLogicBundle.message("profile.action.name");
    }

    @NotNull
    @Override
    public String getId() {
        return EXECUTOR_ID;
    }

    @NotNull
    @Override
    public String getStartActionText() {
        return MarkLogicBundle.message("profile.start.action.text");
    }

    @Override
    public String getContextActionId() {
        return "MarkLogicProfileClass";
    }

    @Override
    public String getHelpId() {
        return null;
    }
}
