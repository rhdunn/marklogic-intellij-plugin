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
package uk.co.reecedunn.intellij.plugin.marklogic.logview;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import org.jetbrains.annotations.NotNull;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Connection;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Item;
import uk.co.reecedunn.intellij.plugin.marklogic.api.LogRequestBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.settings.MarkLogicAppServer;
import uk.co.reecedunn.intellij.plugin.marklogic.settings.MarkLogicProjectSettings;
import uk.co.reecedunn.intellij.plugin.marklogic.settings.MarkLogicServer;
import uk.co.reecedunn.intellij.plugin.marklogic.settings.MarkLogicServerCellRenderer;

import javax.swing.*;
import java.io.IOException;

public class MarkLogicLogViewUI implements LogViewActions {
    private class SettingsListener implements MarkLogicProjectSettings.Listener, Disposable {
        @Override
        public void serversChanged() {
            MarkLogicProjectSettings settings = MarkLogicProjectSettings.Companion.getInstance(mProject);

            mServer.removeAllItems();
            for (MarkLogicServer server : settings.getServers()) {
                mServer.addItem(server);
            }
        }

        @Override
        public void dispose() {
        }
    }

    private Project mProject;
    private Connection mConnection;
    private LogRequestBuilder mLogBuilder;

    private MarkLogicLogViewToolbar mActions;
    private JComponent mActionToolbar;

    private JPanel mPanel;
    private JTextArea mLogText;
    private JComboBox<MarkLogicServer> mServer;
    private JComboBox<MarkLogicAppServer> mAppServer;

    public MarkLogicLogViewUI(@NotNull Project project) {
        mProject = project;
    }

    public JComponent getPanel() {
        return mPanel;
    }

    private void createUIComponents() {
        mActions = new MarkLogicLogViewToolbar(this);
        mActionToolbar = mActions.getComponent();

        mLogText = new JTextArea();
        mLogText.setEditable(false);

        mServer = new ComboBox<>();
        mServer.setRenderer(new MarkLogicServerCellRenderer());
        mServer.addActionListener(e -> serverSelectionChanged());

        mAppServer = new ComboBox<>();

        MarkLogicProjectSettings settings = MarkLogicProjectSettings.Companion.getInstance(mProject);
        SettingsListener listener = new SettingsListener();
        settings.addListener(listener, listener);
        listener.serversChanged();
    }

    private void serverSelectionChanged() {
        MarkLogicServer server = (MarkLogicServer)mServer.getSelectedItem();
        if (server == null) {
            mLogText.setText("");
            return;
        }

        ApplicationManager.getApplication().executeOnPooledThread(() -> {
            mAppServer.removeAllItems();
            for (MarkLogicAppServer appserver : server.getAppservers()) {
                mAppServer.addItem(appserver);
            }
        });

        mConnection = Connection.newConnection(
            server.getHostname(),
            server.getAdminPort(),
            server.getUsername(),
            server.getPassword(),
            Connection.REST);

        mLogBuilder = mConnection.createLogRequestBuilder();
        mLogBuilder.setAppServerPort(0);
        mLogBuilder.setLogFile("ErrorLog.txt");

        ApplicationManager.getApplication().executeOnPooledThread(refreshAction());
    }

    @NotNull
    @Override
    public Runnable refreshAction() {
        return () -> {
            try {
                Item[] items = mLogBuilder.build().run().getItems();
                mLogText.setText(items[0].getContent());
                mLogText.setCaretPosition(mLogText.getDocument().getLength());
            } catch (IOException e) {
                mLogText.setText(e.getMessage());
            }
        };
    }
}
