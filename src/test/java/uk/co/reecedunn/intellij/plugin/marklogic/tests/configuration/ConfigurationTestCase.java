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

import com.intellij.configurationStore.XmlSerializer;
import com.intellij.core.CoreFileTypeRegistry;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.lang.Language;
import com.intellij.mock.MockProjectEx;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFileSystem;
import com.intellij.openapi.vfs.impl.VirtualFileManagerImpl;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.testFramework.PlatformLiteFixture;
import com.intellij.util.messages.MessageBus;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import uk.co.reecedunn.intellij.plugin.marklogic.tests.TestResource;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.configuration.MarkLogicConfigurationFactory;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.configuration.MarkLogicConfigurationType;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.configuration.MarkLogicRunConfiguration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class ConfigurationTestCase extends PlatformLiteFixture {
    private MarkLogicConfigurationFactory mFactory;

    protected void setUp() throws Exception {
        super.setUp();
        initApplication();

        Extensions.registerAreaClass("IDEA_PROJECT", null);
        myProject = new MockProjectEx(getTestRootDisposable());
        FileTypeRegistry.ourInstanceGetter = CoreFileTypeRegistry::new;

        ConfigurationType configurationType = new MarkLogicConfigurationType();
        mFactory = (MarkLogicConfigurationFactory)configurationType.getConfigurationFactories()[0];

        final VirtualFileSystem[] fileSystems = new VirtualFileSystem[] {};
        final MessageBus messageBus = myProject.getMessageBus();
        registerApplicationService(VirtualFileManager.class, new VirtualFileManagerImpl(fileSystems, messageBus) {
            @Override
            public VirtualFile findFileByUrl(@NotNull String url) {
                String path = url.replace("file://", "");
                if (path.startsWith("/")) {
                    path = path.substring(1);
                }
                String contents = new TestResource(path).getContents();
                return (contents == null) ? null : createVirtualFile(path, contents);
            }
        });
    }

    // IntelliJ 2017.1+ UsefulTestCase provides an assertThrows that returns void, but JUnit 5 M3+ returns the exception.
    @SuppressWarnings("unchecked")
    protected static <T extends Throwable> T assertThrows(Class<T> expectedType, Executable executable) {
        return Assertions.assertThrows(expectedType, executable);
    }

    public VirtualFile createVirtualFile(@NonNls String name, String text) {
        VirtualFile file = new LightVirtualFile(name, Language.ANY, text);
        file.setCharset(CharsetToolkit.UTF8_CHARSET);
        return file;
    }

    protected <T> void registerApplicationService(final Class<T> aClass, T object) {
        getApplication().registerService(aClass, object);
        Disposer.register(myProject, () -> getApplication().getPicoContainer().unregisterComponent(aClass.getName()));
    }

    // region Run Configuration Helpers

    public MarkLogicRunConfiguration createConfiguration() {
        return (MarkLogicRunConfiguration)mFactory.createTemplateConfiguration(myProject);
    }

    public void serializeStateInto(RunConfiguration configuration, Element element) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (configuration instanceof PersistentStateComponent) {
            try {
                // IntelliJ 2017.3 and later implements serializeStateInto, and deprecates serializeInto.
                Method method = XmlSerializer.class.getMethod("serializeStateInto", PersistentStateComponent.class, Element.class);
                method.invoke(null, (PersistentStateComponent)configuration, element);
            } catch (NoSuchMethodException e) {
                // IntelliJ 2017.2 and earlier don't implement serializeStateInto.
                Method method = XmlSerializer.class.getMethod("serializeInto", Object.class, Element.class);
                method.invoke(null, ((PersistentStateComponent)configuration).getState(), element);
            }
        } else {
            configuration.writeExternal(element);
        }
    }

    public void deserializeAndLoadState(RunConfiguration configuration, Element element) {
        if (configuration instanceof PersistentStateComponent) {
            XmlSerializer.deserializeAndLoadState((PersistentStateComponent)configuration, element);
        } else {
            configuration.readExternal(element);
        }
    }

    // endregion
}
