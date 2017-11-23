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
package uk.co.reecedunn.intellij.plugin.marklogic.ui.settings;

import uk.co.reecedunn.intellij.plugin.core.ui.SettingsUI;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.server.MarkLogicServerTable;

import javax.swing.*;

public class MarkLogicSettingsUI implements SettingsUI<MarkLogicSettings> {
    private JPanel mPanel;
    private MarkLogicServerTable mTableEditor;
    private JComponent mTable;

    @Override
    public JPanel getPanel() {
        return mPanel;
    }

    @Override
    public boolean isModified(MarkLogicSettings settings) {
        return mTableEditor.isModified();
    }

    @Override
    public void reset(MarkLogicSettings settings) {
        mTableEditor.reset(settings.getServers());
    }

    @Override
    public void apply(MarkLogicSettings settings) {
        settings.setServers(mTableEditor.apply());
    }

    private void createUIComponents() {
        mTableEditor = new MarkLogicServerTable();
        mTable = mTableEditor.createComponent();
    }
}
