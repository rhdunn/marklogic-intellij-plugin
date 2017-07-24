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
        OptionsBuilder builder = EvalOptionsBuilder.INSTANCE;
        StringBuilder vars = new StringBuilder();

        builder.reset();
        builder.build(vars);

        final String expected =
            "<options xmlns=\"xdmp:eval\">" +
            "</options>";
        assertThat(vars.toString(), is(expected));
    }

    public void testContentDatabaseOption() {
        OptionsBuilder builder = EvalOptionsBuilder.INSTANCE;
        StringBuilder vars = new StringBuilder();

        builder.reset();
        builder.setContentDatabase("lorem");
        builder.build(vars);

        final String expected =
            "<options xmlns=\"xdmp:eval\">" +
            "<database>{xdmp:database(\"lorem\")}</database>" +
            "</options>";
        assertThat(vars.toString(), is(expected));
    }

    public void testModulesDatabaseOption() {
        OptionsBuilder builder = EvalOptionsBuilder.INSTANCE;
        StringBuilder vars = new StringBuilder();

        builder.reset();
        builder.setModulesDatabase("lorem");
        builder.build(vars);

        final String expected =
            "<options xmlns=\"xdmp:eval\">" +
            "<modules>{xdmp:database(\"lorem\")}</modules>" +
            "</options>";
        assertThat(vars.toString(), is(expected));
    }

    public void testModulesRootOption() {
        OptionsBuilder builder = EvalOptionsBuilder.INSTANCE;
        StringBuilder vars = new StringBuilder();

        builder.reset();
        builder.setModulesRoot("lorem");
        builder.build(vars);

        final String expected =
            "<options xmlns=\"xdmp:eval\">" +
            "<root>lorem</root>" +
            "</options>";
        assertThat(vars.toString(), is(expected));
    }

    public void testResetOptions() {
        OptionsBuilder builder = EvalOptionsBuilder.INSTANCE;
        StringBuilder vars = new StringBuilder();

        builder.reset();
        builder.setContentDatabase("lorem");
        builder.setModulesDatabase("ipsum");
        builder.setModulesRoot("/dolor");
        builder.reset();

        builder.build(vars);

        final String expected =
            "<options xmlns=\"xdmp:eval\">" +
            "</options>";
        assertThat(vars.toString(), is(expected));
    }
}
