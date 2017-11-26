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
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersion;
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersionFormatException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MarkLogicVersionTest extends TestCase {
    public void testFullVersion() {
        final MarkLogicVersion version = MarkLogicVersion.Companion.parse("8.0-6.3");
        assertThat(version.getMajor(), is(8));
        assertThat(version.getMinor(), is(0));
        assertThat(version.getApi(), is(6));
        assertThat(version.getPatch(), is(3));

        assertThat(version.toString(), is("8.0-6.3"));
    }

    public void testSimpleVersion() {
        final MarkLogicVersion version = MarkLogicVersion.Companion.parse("4.2");
        assertThat(version.getMajor(), is(4));
        assertThat(version.getMinor(), is(2));
        assertThat(version.getApi(), is(nullValue()));
        assertThat(version.getPatch(), is(nullValue()));

        assertThat(version.toString(), is("4.2"));
    }

    public void testPartIsNotANumber() {
        MarkLogicVersionFormatException e =
            assertThrows(MarkLogicVersionFormatException.class, () -> MarkLogicVersion.Companion.parse("a.b-c.d"));
        assertThat(e.getMessage(), is("Invalid MarkLogic version: a.b-c.d"));
    }

    public void testWrongNumberOfParts() {
        MarkLogicVersionFormatException e =
                assertThrows(MarkLogicVersionFormatException.class, () -> MarkLogicVersion.Companion.parse("7.0-5"));
        assertThat(e.getMessage(), is("Invalid MarkLogic version: 7.0-5"));
    }
}
