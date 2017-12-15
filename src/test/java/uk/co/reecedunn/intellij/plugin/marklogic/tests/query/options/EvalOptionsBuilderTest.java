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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.query.options;

import junit.framework.TestCase;
import uk.co.reecedunn.intellij.plugin.marklogic.query.options.EvalOptionsBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.options.OptionsBuilder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("ConstantConditions")
public class EvalOptionsBuilderTest extends TestCase {
    public void testNoOptions() {
        OptionsBuilder builder = EvalOptionsBuilder.Companion.getINSTANCE();

        builder.reset();

        final String expected =
            "<options xmlns=\"xdmp:eval\">" +
            "<modules>0</modules>" +
            "</options>";
        assertThat(builder.build(), is(expected));
    }

    public void testContentDatabaseOption() {
        OptionsBuilder builder = EvalOptionsBuilder.Companion.getINSTANCE();

        builder.reset();
        builder.setContentDatabase("lorem");

        final String expected =
            "<options xmlns=\"xdmp:eval\">" +
            "<database>{xdmp:database(\"lorem\")}</database>" +
            "<modules>0</modules>" +
            "</options>";
        assertThat(builder.build(), is(expected));
    }

    public void testModulesDatabaseOption() {
        OptionsBuilder builder = EvalOptionsBuilder.Companion.getINSTANCE();

        builder.reset();
        builder.setModulesDatabase("lorem");

        final String expected =
            "<options xmlns=\"xdmp:eval\">" +
            "<modules>{xdmp:database(\"lorem\")}</modules>" +
            "</options>";
        assertThat(builder.build(), is(expected));
    }

    public void testModulesRootOption() {
        OptionsBuilder builder = EvalOptionsBuilder.Companion.getINSTANCE();

        builder.reset();
        builder.setModulesRoot("lorem");

        final String expected =
            "<options xmlns=\"xdmp:eval\">" +
            "<modules>0</modules>" +
            "<root>lorem</root>" +
            "</options>";
        assertThat(builder.build(), is(expected));
    }

    public void testResetOptions() {
        OptionsBuilder builder = EvalOptionsBuilder.Companion.getINSTANCE();

        builder.reset();
        builder.setContentDatabase("lorem");
        builder.setModulesDatabase("ipsum");
        builder.setModulesRoot("/dolor");
        builder.reset();

        final String expected =
            "<options xmlns=\"xdmp:eval\">" +
            "<modules>0</modules>" +
            "</options>";
        assertThat(builder.build(), is(expected));
    }
}
