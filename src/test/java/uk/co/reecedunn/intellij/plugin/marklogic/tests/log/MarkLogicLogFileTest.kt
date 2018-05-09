/*
 * Copyright (C) 2017-2018 Reece H. Dunn
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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.log

import junit.framework.TestCase
import uk.co.reecedunn.intellij.plugin.marklogic.log.LogLevel
import uk.co.reecedunn.intellij.plugin.marklogic.log.MarkLogicLogEntry
import uk.co.reecedunn.intellij.plugin.marklogic.log.MarkLogicLogFile
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicAppServer
import uk.co.reecedunn.intellij.plugin.marklogic.server.*

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat

class MarkLogicLogFileTest : TestCase() {
    fun testEmptyLogFile() {
        val logfile = ""
        val entries = MarkLogicLogFile.parse(logfile, MARKLOGIC_5).iterator()

        assertThat(entries.hasNext(), `is`(false))
    }

    fun testSimpleMessages() {
        var entry: MarkLogicLogEntry
        val logfile =
                "2001-01-10 12:34:56.789 Info: Lorem ipsum dolor\n" +
                "2001-01-10 12:34:56.800 Notice: Alpha beta gamma\n"
        val entries = MarkLogicLogFile.parse(logfile, MARKLOGIC_5).iterator()

        assertThat(entries.hasNext(), `is`(true))
        entry = entries.next()
        assertThat(entry.date, `is`("2001-01-10"))
        assertThat(entry.time, `is`("12:34:56.789"))
        assertThat(entry.level, `is`(LogLevel.INFO))
        assertThat<String>(entry.appserver, `is`(nullValue()))
        assertThat(entry.continuation, `is`(false))
        assertThat(entry.message.itemType, `is`("xs:string"))
        assertThat(entry.message.contentType, `is`("text/plain"))
        assertThat(entry.message.content, `is`("Lorem ipsum dolor"))
        assertThat(entry.toString(), `is`("2001-01-10 12:34:56.789 Info: Lorem ipsum dolor"))

        assertThat(entries.hasNext(), `is`(true))
        entry = entries.next()
        assertThat(entry.date, `is`("2001-01-10"))
        assertThat(entry.time, `is`("12:34:56.800"))
        assertThat(entry.level, `is`(LogLevel.NOTICE))
        assertThat<String>(entry.appserver, `is`(nullValue()))
        assertThat(entry.continuation, `is`(false))
        assertThat(entry.message.itemType, `is`("xs:string"))
        assertThat(entry.message.contentType, `is`("text/plain"))
        assertThat(entry.message.content, `is`("Alpha beta gamma"))
        assertThat(entry.toString(), `is`("2001-01-10 12:34:56.800 Notice: Alpha beta gamma"))

        assertThat(entries.hasNext(), `is`(false))
    }

    fun testTaskServer() {
        val entry: MarkLogicLogEntry
        val logfile = "2001-01-10 12:34:56.789 Debug: TaskServer: Lorem ipsum dolor\n"
        val entries = MarkLogicLogFile.parse(logfile, MARKLOGIC_5).iterator()

        assertThat(entries.hasNext(), `is`(true))
        entry = entries.next()
        assertThat(entry.date, `is`("2001-01-10"))
        assertThat(entry.time, `is`("12:34:56.789"))
        assertThat(entry.level, `is`(LogLevel.DEBUG))
        assertThat<String>(entry.appserver, `is`(MarkLogicAppServer.TASKSERVER.appserver))
        assertThat(entry.continuation, `is`(false))
        assertThat(entry.message.itemType, `is`("xs:string"))
        assertThat(entry.message.contentType, `is`("text/plain"))
        assertThat(entry.message.content, `is`("Lorem ipsum dolor"))
        assertThat(entry.toString(), `is`("2001-01-10 12:34:56.789 Debug: Lorem ipsum dolor"))

        assertThat(entries.hasNext(), `is`(false))
    }

    fun testAppServer() {
        val entry: MarkLogicLogEntry
        val logfile = "2001-01-10 12:34:56.789 Debug: abc-2d_3e: Lorem ipsum dolor\n"
        val entries = MarkLogicLogFile.parse(logfile, MARKLOGIC_5).iterator()

        assertThat(entries.hasNext(), `is`(true))
        entry = entries.next()
        assertThat(entry.date, `is`("2001-01-10"))
        assertThat(entry.time, `is`("12:34:56.789"))
        assertThat(entry.level, `is`(LogLevel.DEBUG))
        assertThat<String>(entry.appserver, `is`("abc-2d_3e"))
        assertThat(entry.continuation, `is`(false))
        assertThat(entry.message.itemType, `is`("xs:string"))
        assertThat(entry.message.contentType, `is`("text/plain"))
        assertThat(entry.message.content, `is`("Lorem ipsum dolor"))
        assertThat(entry.toString(), `is`("2001-01-10 12:34:56.789 Debug: Lorem ipsum dolor"))

        assertThat(entries.hasNext(), `is`(false))
    }

    fun testMarkLogic9_MessageContinuation() {
        var entry: MarkLogicLogEntry
        val logfile =
                "2001-01-10 12:34:56.789 Info: Alpha\n" +
                "2001-01-10 12:34:56.789 Info:+Beta\n"
        val entries = MarkLogicLogFile.parse(logfile, MARKLOGIC_9).iterator()

        assertThat(entries.hasNext(), `is`(true))
        entry = entries.next()
        assertThat(entry.date, `is`("2001-01-10"))
        assertThat(entry.time, `is`("12:34:56.789"))
        assertThat(entry.level, `is`(LogLevel.INFO))
        assertThat<String>(entry.appserver, `is`(nullValue()))
        assertThat(entry.continuation, `is`(false))
        assertThat(entry.message.itemType, `is`("xs:string"))
        assertThat(entry.message.contentType, `is`("text/plain"))
        assertThat(entry.message.content, `is`("Alpha"))
        assertThat(entry.toString(), `is`("2001-01-10 12:34:56.789 Info: Alpha"))

        assertThat(entries.hasNext(), `is`(true))
        entry = entries.next()
        assertThat(entry.date, `is`("2001-01-10"))
        assertThat(entry.time, `is`("12:34:56.789"))
        assertThat(entry.level, `is`(LogLevel.INFO))
        assertThat<String>(entry.appserver, `is`(nullValue()))
        assertThat(entry.continuation, `is`(true))
        assertThat(entry.message.itemType, `is`("xs:string"))
        assertThat(entry.message.contentType, `is`("text/plain"))
        assertThat(entry.message.content, `is`("Beta"))
        assertThat(entry.toString(), `is`("2001-01-10 12:34:56.789 Info:+Beta"))

        assertThat(entries.hasNext(), `is`(false))
    }

    fun testMarkLogic_JavaException() {
        var entry: MarkLogicLogEntry
        val logfile =
                "WARNING: JNI local refs: zu, exceeds capacity: zu\n" +
                "\tat java.lang.System.initProperties(Native Method)\n" +
                "\tat java.lang.System.initializeSystemClass(System.java:1166)\n"
        val entries = MarkLogicLogFile.parse(logfile, MARKLOGIC_9).iterator()

        assertThat(entries.hasNext(), `is`(true))
        entry = entries.next()
        assertThat(entry.date, `is`(nullValue()))
        assertThat(entry.time, `is`(nullValue()))
        assertThat(entry.level, `is`(LogLevel.WARNING))
        assertThat<String>(entry.appserver, `is`(nullValue()))
        assertThat(entry.continuation, `is`(false))
        assertThat(entry.message.itemType, `is`("xs:string"))
        assertThat(entry.message.contentType, `is`("text/plain"))
        assertThat(entry.message.content, `is`("JNI local refs: zu, exceeds capacity: zu"))
        assertThat(entry.toString(), `is`("Warning: JNI local refs: zu, exceeds capacity: zu"))

        assertThat(entries.hasNext(), `is`(true))
        entry = entries.next()
        assertThat(entry.date, `is`(nullValue()))
        assertThat(entry.time, `is`(nullValue()))
        assertThat(entry.level, `is`(LogLevel.WARNING))
        assertThat<String>(entry.appserver, `is`(nullValue()))
        assertThat(entry.continuation, `is`(true))
        assertThat(entry.message.itemType, `is`("xs:string"))
        assertThat(entry.message.contentType, `is`("text/plain"))
        assertThat(entry.message.content, `is`("\tat java.lang.System.initProperties(Native Method)"))
        assertThat(entry.toString(), `is`("Warning:+\tat java.lang.System.initProperties(Native Method)"))

        assertThat(entries.hasNext(), `is`(true))
        entry = entries.next()
        assertThat(entry.date, `is`(nullValue()))
        assertThat(entry.time, `is`(nullValue()))
        assertThat(entry.level, `is`(LogLevel.WARNING))
        assertThat<String>(entry.appserver, `is`(nullValue()))
        assertThat(entry.continuation, `is`(true))
        assertThat(entry.message.itemType, `is`("xs:string"))
        assertThat(entry.message.contentType, `is`("text/plain"))
        assertThat(entry.message.content, `is`("\tat java.lang.System.initializeSystemClass(System.java:1166)"))
        assertThat(entry.toString(), `is`("Warning:+\tat java.lang.System.initializeSystemClass(System.java:1166)"))

        assertThat(entries.hasNext(), `is`(false))
    }
}
