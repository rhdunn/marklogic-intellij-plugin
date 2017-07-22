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

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MarkLogicSettingsEditorUI {
    private JPanel mPanel;
    private JTextField mServerHost;
    private JTextField mServerPort;
    private JTextField mUserName;
    private JPasswordField mPassword;

    private void createUIComponents() {
        mPanel = new JPanel();
        mServerHost = new JTextField();
        mServerPort = new JTextField();
        mUserName = new JTextField();
        mPassword = new JPasswordField();
    }

    public void reset(@NotNull MarkLogicRunConfiguration configuration) {
        mServerHost.setText(configuration.getServerHost());
        mServerPort.setText(Integer.toString(configuration.getServerPort()));
        mUserName.setText(configuration.getUserName());
        mPassword.setText(configuration.getPassword());
    }

    @SuppressWarnings("ImplicitArrayToString")
    public void apply(@NotNull MarkLogicRunConfiguration configuration) {
        configuration.setServerHost(mServerHost.getText());
        configuration.setServerPort(toInteger(mServerPort.getText(), configuration.getServerPort()));
        configuration.setUserName(mUserName.getText());
        configuration.setPassword(mPassword.getPassword().toString());
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
}
