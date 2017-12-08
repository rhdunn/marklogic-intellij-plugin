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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.ui.configuration;

import uk.co.reecedunn.intellij.plugin.marklogic.api.RDFFormat;
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicServer;
import uk.co.reecedunn.intellij.plugin.marklogic.tests.configuration.ConfigurationTestCase;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.configuration.MarkLogicRunConfiguration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class MarkLogicRunConfigurationTest extends ConfigurationTestCase {
    public void testServer() {
        MarkLogicRunConfiguration configuration = createConfiguration();
        assertThat(configuration.getServer(), is(nullValue()));

        MarkLogicServer server = new MarkLogicServer(null, "localhost", 8000, 8001, "testuser", "test");
        configuration.setServer(server);
        assertThat(configuration.getServer(), is(server));
    }

    public void testMarkLogicVersion() {
        MarkLogicRunConfiguration configuration = createConfiguration();
        assertThat(configuration.getMarkLogicVersion(), is(7.0));

        configuration.setMarkLogicVersion(9.0);
        assertThat(configuration.getMarkLogicVersion(), is(9.0));
    }

    public void testContentDatabase() {
        MarkLogicRunConfiguration configuration = createConfiguration();
        assertThat(configuration.getContentDatabase(), is(nullValue()));

        configuration.setContentDatabase("test-content");
        assertThat(configuration.getContentDatabase(), is("test-content"));
    }

    public void testModuleDatabase() {
        MarkLogicRunConfiguration configuration = createConfiguration();
        assertThat(configuration.getModuleDatabase(), is(nullValue()));

        configuration.setModuleDatabase("test-modules");
        assertThat(configuration.getModuleDatabase(), is("test-modules"));
    }

    public void testModuleRoot() {
        MarkLogicRunConfiguration configuration = createConfiguration();
        assertThat(configuration.getModuleRoot(), is("/"));

        configuration.setModuleRoot("/lorem/ipsum");
        assertThat(configuration.getModuleRoot(), is("/lorem/ipsum"));
    }

    public void testMainModulePath() {
        MarkLogicRunConfiguration configuration = createConfiguration();
        assertThat(configuration.getMainModulePath(), is(""));
        assertThat(configuration.getMainModuleFile(), is(nullValue()));

        configuration.setMainModulePath("module/test.xqy");
        assertThat(configuration.getMainModulePath(), is("module/test.xqy"));
        assertThat(configuration.getMainModuleFile(), is(nullValue()));
    }

    public void testMainModuleFile() {
        MarkLogicRunConfiguration configuration = createConfiguration();
        assertThat(configuration.getMainModulePath(), is(""));
        assertThat(configuration.getMainModuleFile(), is(nullValue()));

        configuration.setMainModuleFile(createVirtualFile("module/test.xqy", "\"Lorem ipsum\""));
        assertThat(configuration.getMainModulePath(), is("/module/test.xqy"));
        assertThat(configuration.getMainModuleFile(), is(notNullValue()));
        assertThat(configuration.getMainModuleFile().getCanonicalPath(), is("/module/test.xqy"));
    }

    public void testTripleFormat() {
        MarkLogicRunConfiguration configuration = createConfiguration();
        assertThat(configuration.getTripleFormat(), is(RDFFormat.SEM_TRIPLE));

        configuration.setTripleFormat(RDFFormat.TURTLE);
        assertThat(configuration.getTripleFormat(), is(RDFFormat.TURTLE));
    }
}
