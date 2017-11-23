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
import uk.co.reecedunn.intellij.plugin.marklogic.log.MarkLogicLogEntry;
import uk.co.reecedunn.intellij.plugin.marklogic.log.MarkLogicLogFile;

import java.util.Iterator;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class MarkLogicLogFileTest extends TestCase {
    public void testEmptyLogFile() {
        final String logfile =
            "";
        Iterator<MarkLogicLogEntry> entries = MarkLogicLogFile.INSTANCE.parse(logfile).iterator();

        assertThat(entries.hasNext(), is(false));
    }

    public void testSimpleMessages() {
        MarkLogicLogEntry entry;
        final String logfile =
            "2001-01-10 12:34:56.789 Info: Lorem ipsum dolor\n" +
            "2001-01-10 12:34:56.800 Notice: Alpha beta gamma\n";
        Iterator<MarkLogicLogEntry> entries = MarkLogicLogFile.INSTANCE.parse(logfile).iterator();

        assertThat(entries.hasNext(), is(true));
        entry = entries.next();
        assertThat(entry.getDate(), is("2001-01-10"));
        assertThat(entry.getTime(), is("12:34:56.789"));
        assertThat(entry.getLevel(), is("Info"));
        assertThat(entry.getContinuation(), is(false));
        assertThat(entry.getMessage().getItemType(), is("xs:string"));
        assertThat(entry.getMessage().getContentType(), is("text/plain"));
        assertThat(entry.getMessage().getContent(), is("Lorem ipsum dolor"));

        assertThat(entries.hasNext(), is(true));
        entry = entries.next();
        assertThat(entry.getDate(), is("2001-01-10"));
        assertThat(entry.getTime(), is("12:34:56.800"));
        assertThat(entry.getLevel(), is("Notice"));
        assertThat(entry.getContinuation(), is(false));
        assertThat(entry.getMessage().getItemType(), is("xs:string"));
        assertThat(entry.getMessage().getContentType(), is("text/plain"));
        assertThat(entry.getMessage().getContent(), is("Alpha beta gamma"));

        assertThat(entries.hasNext(), is(false));
    }

    public void testMarkLogic9_MessageContinuation() {
        MarkLogicLogEntry entry;
        final String logfile =
            "2001-01-10 12:34:56.789 Info: Alpha\n" +
            "2001-01-10 12:34:56.789 Info:+Beta\n";
        Iterator<MarkLogicLogEntry> entries = MarkLogicLogFile.INSTANCE.parse(logfile).iterator();

        assertThat(entries.hasNext(), is(true));
        entry = entries.next();
        assertThat(entry.getDate(), is("2001-01-10"));
        assertThat(entry.getTime(), is("12:34:56.789"));
        assertThat(entry.getLevel(), is("Info"));
        assertThat(entry.getContinuation(), is(false));
        assertThat(entry.getMessage().getItemType(), is("xs:string"));
        assertThat(entry.getMessage().getContentType(), is("text/plain"));
        assertThat(entry.getMessage().getContent(), is("Alpha"));

        assertThat(entries.hasNext(), is(true));
        entry = entries.next();
        assertThat(entry.getDate(), is("2001-01-10"));
        assertThat(entry.getTime(), is("12:34:56.789"));
        assertThat(entry.getLevel(), is("Info"));
        assertThat(entry.getContinuation(), is(true));
        assertThat(entry.getMessage().getItemType(), is("xs:string"));
        assertThat(entry.getMessage().getContentType(), is("text/plain"));
        assertThat(entry.getMessage().getContent(), is("Beta"));

        assertThat(entries.hasNext(), is(false));
    }
}
