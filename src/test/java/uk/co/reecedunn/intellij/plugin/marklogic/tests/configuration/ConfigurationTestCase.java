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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.configuration;

import com.intellij.core.CoreFileTypeRegistry;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.lang.Language;
import com.intellij.mock.MockProjectEx;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.testFramework.PlatformLiteFixture;
import org.jetbrains.annotations.NonNls;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.configuration.MarkLogicConfigurationFactory;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.configuration.MarkLogicConfigurationType;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.configuration.MarkLogicRunConfiguration;

public abstract class ConfigurationTestCase extends PlatformLiteFixture {
    private MarkLogicConfigurationFactory mFactory;

    protected void setUp() throws Exception {
        super.setUp();
        Extensions.registerAreaClass("IDEA_PROJECT", null);
        myProject = new MockProjectEx(getTestRootDisposable());
        FileTypeRegistry.ourInstanceGetter = CoreFileTypeRegistry::new;

        ConfigurationType configurationType = new MarkLogicConfigurationType();
        mFactory = (MarkLogicConfigurationFactory)configurationType.getConfigurationFactories()[0];
    }

    public MarkLogicRunConfiguration createConfiguration() {
        return (MarkLogicRunConfiguration)mFactory.createTemplateConfiguration(myProject);
    }

    public VirtualFile createVirtualFile(@NonNls String name, String text) {
        VirtualFile file = new LightVirtualFile(name, Language.ANY, text);
        file.setCharset(CharsetToolkit.UTF8_CHARSET);
        return file;
    }
}
