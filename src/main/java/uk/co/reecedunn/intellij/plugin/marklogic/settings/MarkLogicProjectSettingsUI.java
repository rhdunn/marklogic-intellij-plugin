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

import com.intellij.ui.table.JBTable;
import uk.co.reecedunn.intellij.plugin.core.ui.SettingsUI;
import uk.co.reecedunn.intellij.plugin.marklogic.resources.MarkLogicBundle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MarkLogicProjectSettingsUI implements SettingsUI<MarkLogicProjectSettings > {
    private static final String COLUMN_NAMES[] = new String[] {
        MarkLogicBundle.message("marklogic.settings.server.name"),
        MarkLogicBundle.message("marklogic.settings.server.hostname"),
        MarkLogicBundle.message("marklogic.settings.server.appserver-port"),
        MarkLogicBundle.message("marklogic.settings.server.admin-port"),
        MarkLogicBundle.message("marklogic.settings.server.username"),
        MarkLogicBundle.message("marklogic.settings.server.password"),
    };

    private JPanel mPanel;
    private JTable mTable;

    @Override
    public JPanel getPanel() {
        return mPanel;
    }

    @Override
    public boolean isModified(MarkLogicProjectSettings markLogicProjectSettings) {
        return false;
    }

    @Override
    public void reset(MarkLogicProjectSettings markLogicProjectSettings) {
    }

    @Override
    public void apply(MarkLogicProjectSettings markLogicProjectSettings) {
    }

    private void createUIComponents() {
        mTable = new JBTable();
        mTable.setModel(new DefaultTableModel(COLUMN_NAMES, 0));
    }
}
