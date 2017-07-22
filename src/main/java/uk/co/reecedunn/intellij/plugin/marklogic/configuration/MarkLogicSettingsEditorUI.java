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

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import uk.co.reecedunn.intellij.plugin.marklogic.resources.MarkLogicBundle;
import uk.co.reecedunn.intellij.plugin.marklogic.runner.MarkLogicResultsHandler;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.DocumentChangedListener;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.MarkLogicQueryComboBox;

import javax.swing.*;
import javax.swing.event.DocumentListener;

public class MarkLogicSettingsEditorUI {
    private final MarkLogicConfigurationFactory mFactory;
    private final Project mProject;

    private JPanel mPanel;
    private JTextField mServerHost;
    private JTextField mServerPort;
    private JTextField mUserName;
    private JPasswordField mPassword;
    private JComboBox<String> mContentDatabase;
    private JComboBox<String> mModuleDatabase;

    public MarkLogicSettingsEditorUI(@NotNull MarkLogicConfigurationFactory factory, @NotNull Project project) {
        mFactory = factory;
        mProject = project;
    }

    private void createUIComponents() {
        mPanel = new JPanel();
        mServerHost = new JTextField();
        mServerPort = new JTextField();
        mUserName = new JTextField();
        mPassword = new JPasswordField();
        mContentDatabase = new MarkLogicQueryComboBox(MarkLogicBundle.message("database.none"));
        mModuleDatabase = new MarkLogicQueryComboBox(MarkLogicBundle.message("database.file.system"));

        DocumentListener listener = new DocumentChangedListener() {
            @Override
            public void changed() {
                String query = "for $name in xdmp:databases() ! xdmp:database-name(.) order by $name return $name";
                run(query, (MarkLogicQueryComboBox)mContentDatabase);
                run(query, (MarkLogicQueryComboBox)mModuleDatabase);
            }
        };
        mServerHost.getDocument().addDocumentListener(listener);
        mServerPort.getDocument().addDocumentListener(listener);
        mUserName.getDocument().addDocumentListener(listener);
        mPassword.getDocument().addDocumentListener(listener);
    }

    public void reset(@NotNull MarkLogicRunConfiguration configuration) {
        mServerHost.setText(configuration.getServerHost());
        mServerPort.setText(Integer.toString(configuration.getServerPort()));
        mUserName.setText(configuration.getUserName());
        mPassword.setText(configuration.getPassword());
        ((MarkLogicQueryComboBox)mContentDatabase).setItem(configuration.getContentDatabase());
        ((MarkLogicQueryComboBox)mModuleDatabase).setItem(configuration.getModuleDatabase());
    }

    public void apply(@NotNull MarkLogicRunConfiguration configuration) {
        configuration.setServerHost(mServerHost.getText());
        configuration.setServerPort(toInteger(mServerPort.getText(), configuration.getServerPort()));
        configuration.setUserName(mUserName.getText());
        configuration.setPassword(String.valueOf(mPassword.getPassword()));
        configuration.setContentDatabase(((MarkLogicQueryComboBox)mContentDatabase).getItem());
        configuration.setModuleDatabase(((MarkLogicQueryComboBox)mModuleDatabase).getItem());
    }

    @NotNull
    public JPanel getPanel() {
        return mPanel;
    }

    private int toInteger(String value, int defaultValue) {
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private boolean run(String query, MarkLogicResultsHandler handler) {
        // NOTE: Using SettingsEditor.getFactory or getSnapshot don't work, as
        // they throw a NullPointerException when processing the events.
        MarkLogicRunConfiguration configuration = (MarkLogicRunConfiguration)mFactory.createTemplateConfiguration(mProject);
        apply(configuration);
        return configuration.run(query, handler);
    }
}
