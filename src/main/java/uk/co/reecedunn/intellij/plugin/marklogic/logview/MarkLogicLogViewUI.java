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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import org.jetbrains.annotations.NotNull;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Connection;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Item;
import uk.co.reecedunn.intellij.plugin.marklogic.api.LogRequestBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.settings.MarkLogicProjectSettings;
import uk.co.reecedunn.intellij.plugin.marklogic.settings.MarkLogicServer;

import javax.swing.*;
import java.io.IOException;

public class MarkLogicLogViewUI {
    private Project mProject;
    private Connection mConnection;
    private LogRequestBuilder mLogBuilder;

    private JPanel mPanel;
    private JTextArea mLogText;
    private JComboBox<MarkLogicServer> mServer;

    public MarkLogicLogViewUI(@NotNull Project project) {
        mProject = project;
    }

    public JComponent getPanel() {
        return mPanel;
    }

    private void createUIComponents() {
        mLogText = new JTextArea();

        mServer = new ComboBox<>();
        mServer.addActionListener(e -> serverSelectionChanged());

        MarkLogicProjectSettings settings = MarkLogicProjectSettings.Companion.getInstance(mProject);
        for (MarkLogicServer server : settings.getServers()) {
            mServer.addItem(server);
        }

        serverSelectionChanged();
    }

    private void serverSelectionChanged() {
        MarkLogicServer server = (MarkLogicServer)mServer.getSelectedItem();
        if (server == null) return;

        mConnection = Connection.newConnection(
            server.getHostname(),
            server.getAdminPort(),
            server.getUsername(),
            server.getPassword(),
            Connection.REST_API_MINIMUM_VERSION /* Always use the REST API for log requests. */);

        mLogBuilder = mConnection.createLogRequestBuilder();
        mLogBuilder.setAppServerPort(0);
        mLogBuilder.setLogFile("ErrorLog.txt");

        refreshLog();
    }

    private void refreshLog() {
        try {
            Item[] items = mLogBuilder.build().run().getItems();
            mLogText.setText(items[0].getContent());
        } catch (IOException e) {
            mLogText.setText(e.getMessage());
        }
    }
}
