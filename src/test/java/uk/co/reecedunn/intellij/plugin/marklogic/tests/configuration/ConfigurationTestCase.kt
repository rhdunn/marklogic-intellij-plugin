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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.configuration

import com.intellij.configurationStore.deserializeAndLoadState
import com.intellij.core.CoreFileTypeRegistry
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.lang.Language
import com.intellij.mock.MockProjectEx
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.extensions.Extensions
import com.intellij.openapi.fileTypes.FileTypeRegistry
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.Getter
import com.intellij.openapi.vfs.CharsetToolkit
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.VirtualFileSystem
import com.intellij.openapi.vfs.impl.VirtualFileManagerImpl
import com.intellij.testFramework.LightVirtualFile
import com.intellij.testFramework.PlatformLiteFixture
import com.intellij.util.xmlb.XmlSerializer
import org.jdom.Element
import org.jetbrains.annotations.NonNls
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.function.Executable
import uk.co.reecedunn.intellij.plugin.marklogic.tests.TestResource
import uk.co.reecedunn.intellij.plugin.marklogic.ui.configuration.MarkLogicConfigurationFactory
import uk.co.reecedunn.intellij.plugin.marklogic.ui.configuration.MarkLogicConfigurationType
import uk.co.reecedunn.intellij.plugin.marklogic.ui.configuration.MarkLogicRunConfiguration

import java.lang.reflect.InvocationTargetException

abstract class ConfigurationTestCase : PlatformLiteFixture() {
    private var mFactory: MarkLogicConfigurationFactory? = null

    @Throws(Exception::class)
    override fun setUp() {
        super.setUp()
        initApplication()

        Extensions.registerAreaClass("IDEA_PROJECT", null)
        myProject = MockProjectEx(testRootDisposable)
        FileTypeRegistry.ourInstanceGetter = Getter<FileTypeRegistry> { CoreFileTypeRegistry() }

        val configurationType = MarkLogicConfigurationType()
        mFactory = configurationType.configurationFactories[0] as MarkLogicConfigurationFactory

        val fileSystems = arrayOf<VirtualFileSystem>()
        val messageBus = myProject.messageBus
        registerApplicationService(VirtualFileManager::class.java, object : VirtualFileManagerImpl(fileSystems, messageBus) {
            override fun findFileByUrl(url: String): VirtualFile? {
                var path = url.replace("file://", "")
                if (path.startsWith("/")) {
                    path = path.substring(1)
                }
                val contents = TestResource(path).contents
                return if (contents == null) null else createVirtualFile(path, contents)
            }
        })
    }

    fun createVirtualFile(@NonNls name: String, text: String?): VirtualFile {
        val file = LightVirtualFile(name, Language.ANY, text!!)
        file.charset = CharsetToolkit.UTF8_CHARSET
        return file
    }

    protected fun <T> registerApplicationService(aClass: Class<T>, `object`: T) {
        PlatformLiteFixture.getApplication().registerService(aClass, `object`)
        Disposer.register(myProject, Disposable { PlatformLiteFixture.getApplication().picoContainer.unregisterComponent(aClass.name) })
    }

    // IntelliJ 2017.1+ UsefulTestCase provides an assertThrows that returns void, but JUnit 5 M3+ returns the exception.
    protected fun <T : Throwable> assertThrows(expectedType: Class<T>, executable: Executable): T {
        return Assertions.assertThrows(expectedType, executable)
    }

    // region Run Configuration Helpers

    fun createConfiguration(): MarkLogicRunConfiguration {
        return mFactory!!.createTemplateConfiguration(myProject) as MarkLogicRunConfiguration
    }

    @Throws(NoSuchMethodException::class, InvocationTargetException::class, IllegalAccessException::class)
    fun serializeStateInto(configuration: RunConfiguration, element: Element) {
        if (configuration is PersistentStateComponent<*>) {
            try {
                // IntelliJ 2017.3 and later implements serializeStateInto, and deprecates serializeInto.
                val method = XmlSerializer::class.java!!.getMethod("serializeStateInto", PersistentStateComponent::class.java, Element::class.java)
                method.invoke(null, configuration as PersistentStateComponent<*>, element)
            } catch (e: NoSuchMethodException) {
                // IntelliJ 2017.2 and earlier don't implement serializeStateInto.
                val method = XmlSerializer::class.java!!.getMethod("serializeInto", Any::class.java, Element::class.java)
                method.invoke(null, (configuration as PersistentStateComponent<*>).state, element)
            }

        } else {
            configuration.writeExternal(element)
        }
    }

    fun deserializeAndLoadState(configuration: RunConfiguration, element: Element) {
        if (configuration is PersistentStateComponent<*>) {
            (configuration as PersistentStateComponent<*>).deserializeAndLoadState(element)
        } else {
            configuration.readExternal(element)
        }
    }

    // endregion
}
