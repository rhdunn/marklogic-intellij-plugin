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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.server;

import junit.framework.TestCase;
import uk.co.reecedunn.intellij.plugin.marklogic.server.LogType;
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicAppServer;
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersion;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MarkLogicAppServerTest extends TestCase {
    private static final MarkLogicVersion MARKLOGIC_8 = new MarkLogicVersion(8, 0, null, null);
    private static final MarkLogicVersion MARKLOGIC_9 = new MarkLogicVersion(9, 0, null, null);

    public void testSystem() {
        final MarkLogicAppServer appserver = MarkLogicAppServer.Companion.getSYSTEM();
        assertThat(appserver.toString(), is("System (MarkLogic)"));

        assertThat(appserver.logfile(LogType.ACCESS_LOG, 0, MARKLOGIC_8), is("AccessLog.txt"));
        assertThat(appserver.logfile(LogType.ACCESS_LOG, 1, MARKLOGIC_8), is("AccessLog_1.txt"));
        assertThat(appserver.logfile(LogType.ACCESS_LOG, 0, MARKLOGIC_9), is("AccessLog.txt"));
        assertThat(appserver.logfile(LogType.ACCESS_LOG, 1, MARKLOGIC_9), is("AccessLog_1.txt"));

        assertThat(appserver.logfile(LogType.AUDIT_LOG, 0, MARKLOGIC_8), is("AuditLog.txt"));
        assertThat(appserver.logfile(LogType.AUDIT_LOG, 1, MARKLOGIC_8), is("AuditLog_1.txt"));
        assertThat(appserver.logfile(LogType.AUDIT_LOG, 0, MARKLOGIC_9), is("AuditLog.txt"));
        assertThat(appserver.logfile(LogType.AUDIT_LOG, 1, MARKLOGIC_9), is("AuditLog_1.txt"));

        assertThat(appserver.logfile(LogType.CRASH_LOG, 0, MARKLOGIC_8), is("CrashLog.txt"));
        assertThat(appserver.logfile(LogType.CRASH_LOG, 1, MARKLOGIC_8), is("CrashLog_1.txt"));
        assertThat(appserver.logfile(LogType.CRASH_LOG, 0, MARKLOGIC_9), is("CrashLog.txt"));
        assertThat(appserver.logfile(LogType.CRASH_LOG, 1, MARKLOGIC_9), is("CrashLog_1.txt"));

        assertThat(appserver.logfile(LogType.ERROR_LOG, 0, MARKLOGIC_8), is("ErrorLog.txt"));
        assertThat(appserver.logfile(LogType.ERROR_LOG, 1, MARKLOGIC_8), is("ErrorLog_1.txt"));
        assertThat(appserver.logfile(LogType.ERROR_LOG, 0, MARKLOGIC_9), is("ErrorLog.txt"));
        assertThat(appserver.logfile(LogType.ERROR_LOG, 1, MARKLOGIC_9), is("ErrorLog_1.txt"));
    }

    public void testAppServer() {
        final MarkLogicAppServer appserver = new MarkLogicAppServer("Default", "test", "HTTP", 8020);
        assertThat(appserver.toString(), is("Default :: test : 8020 [HTTP]"));

        assertThat(appserver.logfile(LogType.ACCESS_LOG, 0, MARKLOGIC_8), is("8020_AccessLog.txt"));
        assertThat(appserver.logfile(LogType.ACCESS_LOG, 1, MARKLOGIC_8), is("8020_AccessLog_1.txt"));
        assertThat(appserver.logfile(LogType.ACCESS_LOG, 0, MARKLOGIC_9), is("8020_AccessLog.txt"));
        assertThat(appserver.logfile(LogType.ACCESS_LOG, 1, MARKLOGIC_9), is("8020_AccessLog_1.txt"));

        assertThat(appserver.logfile(LogType.AUDIT_LOG, 0, MARKLOGIC_8), is("8020_AuditLog.txt"));
        assertThat(appserver.logfile(LogType.AUDIT_LOG, 1, MARKLOGIC_8), is("8020_AuditLog_1.txt"));
        assertThat(appserver.logfile(LogType.AUDIT_LOG, 0, MARKLOGIC_9), is("8020_AuditLog.txt"));
        assertThat(appserver.logfile(LogType.AUDIT_LOG, 1, MARKLOGIC_9), is("8020_AuditLog_1.txt"));

        assertThat(appserver.logfile(LogType.CRASH_LOG, 0, MARKLOGIC_8), is("8020_CrashLog.txt"));
        assertThat(appserver.logfile(LogType.CRASH_LOG, 1, MARKLOGIC_8), is("8020_CrashLog_1.txt"));
        assertThat(appserver.logfile(LogType.CRASH_LOG, 0, MARKLOGIC_9), is("8020_CrashLog.txt"));
        assertThat(appserver.logfile(LogType.CRASH_LOG, 1, MARKLOGIC_9), is("8020_CrashLog_1.txt"));

        assertThat(appserver.logfile(LogType.ERROR_LOG, 0, MARKLOGIC_8), is("ErrorLog.txt"));
        assertThat(appserver.logfile(LogType.ERROR_LOG, 1, MARKLOGIC_8), is("ErrorLog_1.txt"));
        assertThat(appserver.logfile(LogType.ERROR_LOG, 0, MARKLOGIC_9), is("8020_ErrorLog.txt"));
        assertThat(appserver.logfile(LogType.ERROR_LOG, 1, MARKLOGIC_9), is("8020_ErrorLog_1.txt"));
    }

    public void testTaskServer() {
        final MarkLogicAppServer appserver = MarkLogicAppServer.Companion.getTASKSERVER();
        assertThat(appserver.toString(), is("TaskServer"));

        assertThat(appserver.logfile(LogType.ACCESS_LOG, 0, MARKLOGIC_8), is("TaskServer_AccessLog.txt"));
        assertThat(appserver.logfile(LogType.ACCESS_LOG, 1, MARKLOGIC_8), is("TaskServer_AccessLog_1.txt"));
        assertThat(appserver.logfile(LogType.ACCESS_LOG, 0, MARKLOGIC_9), is("TaskServer_AccessLog.txt"));
        assertThat(appserver.logfile(LogType.ACCESS_LOG, 1, MARKLOGIC_9), is("TaskServer_AccessLog_1.txt"));

        assertThat(appserver.logfile(LogType.AUDIT_LOG, 0, MARKLOGIC_8), is("TaskServer_AuditLog.txt"));
        assertThat(appserver.logfile(LogType.AUDIT_LOG, 1, MARKLOGIC_8), is("TaskServer_AuditLog_1.txt"));
        assertThat(appserver.logfile(LogType.AUDIT_LOG, 0, MARKLOGIC_9), is("TaskServer_AuditLog.txt"));
        assertThat(appserver.logfile(LogType.AUDIT_LOG, 1, MARKLOGIC_9), is("TaskServer_AuditLog_1.txt"));

        assertThat(appserver.logfile(LogType.CRASH_LOG, 0, MARKLOGIC_8), is("TaskServer_CrashLog.txt"));
        assertThat(appserver.logfile(LogType.CRASH_LOG, 1, MARKLOGIC_8), is("TaskServer_CrashLog_1.txt"));
        assertThat(appserver.logfile(LogType.CRASH_LOG, 0, MARKLOGIC_9), is("TaskServer_CrashLog.txt"));
        assertThat(appserver.logfile(LogType.CRASH_LOG, 1, MARKLOGIC_9), is("TaskServer_CrashLog_1.txt"));

        assertThat(appserver.logfile(LogType.ERROR_LOG, 0, MARKLOGIC_8), is("ErrorLog.txt"));
        assertThat(appserver.logfile(LogType.ERROR_LOG, 1, MARKLOGIC_8), is("ErrorLog_1.txt"));
        assertThat(appserver.logfile(LogType.ERROR_LOG, 0, MARKLOGIC_9), is("TaskServer_ErrorLog.txt"));
        assertThat(appserver.logfile(LogType.ERROR_LOG, 1, MARKLOGIC_9), is("TaskServer_ErrorLog_1.txt"));
    }
}
