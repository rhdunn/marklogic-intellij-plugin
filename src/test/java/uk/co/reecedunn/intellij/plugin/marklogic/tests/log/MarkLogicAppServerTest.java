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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.log;

import junit.framework.TestCase;
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicAppServer;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.resources.MarkLogicBundle;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MarkLogicAppServerTest extends TestCase {
    public void testSystem() {
        final MarkLogicAppServer appserver =
            new MarkLogicAppServer(null, MarkLogicBundle.message("logviewer.app-server.none"), null, 0);

        assertThat(appserver.toString(), is("(none)"));

        assertThat(appserver.logfile(), is("ErrorLog.txt"));
    }

    public void testAppServer() {
        final MarkLogicAppServer appserver =
            new MarkLogicAppServer("Default", "test", "HTTP", 8020);

        assertThat(appserver.toString(), is("Default :: test : 8020 [HTTP]"));

        assertThat(appserver.logfile(), is("8020_ErrorLog.txt"));
    }
}
