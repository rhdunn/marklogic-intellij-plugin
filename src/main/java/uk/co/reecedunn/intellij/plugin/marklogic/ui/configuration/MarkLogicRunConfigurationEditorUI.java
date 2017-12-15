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

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileTypeDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.ComponentWithBrowseButton;
import com.intellij.openapi.ui.TextComponentAccessor;
import org.jetbrains.annotations.NotNull;
import uk.co.reecedunn.intellij.plugin.marklogic.query.MarkLogicQuery;
import uk.co.reecedunn.intellij.plugin.marklogic.query.MarkLogicQueryKt;
import uk.co.reecedunn.intellij.plugin.marklogic.query.RDFFormatKt;
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicServer;
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersion;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.resources.MarkLogicBundle;
import uk.co.reecedunn.intellij.plugin.marklogic.query.RDFFormat;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.runner.MarkLogicResultsHandler;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.runner.MarkLogicQueryComboBox;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.server.MarkLogicServerComboBox;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MarkLogicRunConfigurationEditorUI {
    private class MarkLogicServerChangedListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            run(MarkLogicQueryKt.getLIST_DATABASES_XQUERY(), (MarkLogicQueryComboBox)mContentDatabase);
            run(MarkLogicQueryKt.getLIST_DATABASES_XQUERY(), (MarkLogicQueryComboBox)mModuleDatabase);
        }
    }

    private final MarkLogicConfigurationFactory mFactory;
    private final Project mProject;

    private JPanel mPanel;
    private JComboBox<MarkLogicServer> mServerHost;
    private JComboBox<String> mContentDatabase;
    private JComboBox<String> mModuleDatabase;
    private ComponentWithBrowseButton<JTextField> mModuleRoot;
    private ComponentWithBrowseButton<JTextField> mMainModulePath;
    private JComboBox<RDFFormat> mTripleFormat;

    public MarkLogicRunConfigurationEditorUI(@NotNull MarkLogicConfigurationFactory factory, @NotNull Project project) {
        mFactory = factory;
        mProject = project;
    }

    private void createUIComponents() {
        mPanel = new JPanel();
        mServerHost = new MarkLogicServerComboBox();
        mContentDatabase = new MarkLogicQueryComboBox(MarkLogicBundle.message("database.none"));
        mModuleDatabase = new MarkLogicQueryComboBox(MarkLogicBundle.message("database.file.system"));
        mModuleRoot = new ComponentWithBrowseButton<>(new JTextField(), null);
        mMainModulePath = new ComponentWithBrowseButton<>(new JTextField(), null);
        mTripleFormat = new ComboBox<>(RDFFormatKt.getRDF_FORMATS());

        MarkLogicServerChangedListener listener = new MarkLogicServerChangedListener();
        mServerHost.addActionListener(listener);

        mModuleRoot.addBrowseFolderListener(
            MarkLogicBundle.message("browser.choose.module.root"),
            null,
            null,
            new FileChooserDescriptor(false, true, false, false, false, false),
            TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);

        mMainModulePath.addBrowseFolderListener(
            MarkLogicBundle.message("browser.choose.main.module"),
            null,
            mProject,
            new FileTypeDescriptor(MarkLogicBundle.message("browser.choose.main.module"), MarkLogicRunConfiguration.Companion.getEXTENSIONS()),
            TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);

        ((MarkLogicServerComboBox)mServerHost).serversChanged();
    }

    public void reset(@NotNull MarkLogicRunConfiguration configuration) {
        mServerHost.setSelectedItem(configuration.getServer());
        ((MarkLogicQueryComboBox)mContentDatabase).setItem(configuration.getContentDatabase());
        ((MarkLogicQueryComboBox)mModuleDatabase).setItem(configuration.getModuleDatabase());
        mModuleRoot.getChildComponent().setText(configuration.getModuleRoot());
        mMainModulePath.getChildComponent().setText(configuration.getMainModulePath());
        mTripleFormat.setSelectedItem(configuration.getTripleFormat());
    }

    public void apply(@NotNull MarkLogicRunConfiguration configuration) {
        configuration.setServer((MarkLogicServer)mServerHost.getSelectedItem());
        configuration.setContentDatabase(((MarkLogicQueryComboBox)mContentDatabase).getItem());
        configuration.setModuleDatabase(((MarkLogicQueryComboBox)mModuleDatabase).getItem());
        configuration.setModuleRoot(mModuleRoot.getChildComponent().getText());
        configuration.setMainModulePath(mMainModulePath.getChildComponent().getText());
        configuration.setTripleFormat((RDFFormat)mTripleFormat.getSelectedItem());

        MarkLogicVersion version = ((MarkLogicServerComboBox)mServerHost).getVersion();
        if (version != null) {
            configuration.setMarkLogicVersion(version);
        }
    }

    @NotNull
    public JPanel getPanel() {
        return mPanel;
    }

    private boolean run(MarkLogicQuery query, MarkLogicResultsHandler handler) {
        // NOTE: Using SettingsEditor.getFactory or getSnapshot don't work, as
        // they throw a NullPointerException when processing the events.
        MarkLogicRunConfiguration configuration = (MarkLogicRunConfiguration) mFactory.createTemplateConfiguration(mProject);
        apply(configuration);
        return configuration.getServer() != null && configuration.run(query.getQuery(), handler);
    }
}
