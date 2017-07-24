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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.query;

import junit.framework.TestCase;
import uk.co.reecedunn.intellij.plugin.marklogic.query.JavaScriptBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.QueryBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.vars.MapVarsBuilder;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("ConstantConditions")
public class JavaScriptBuilderTest extends TestCase {
    public void testEvalRun() {
        JavaScriptBuilder js = new JavaScriptBuilder();

        assertThat(js.createEvalBuilder(QueryBuilder.ExecMode.Run, 5.0), is(nullValue()));
        assertThat(js.createEvalBuilder(QueryBuilder.ExecMode.Run, 6.0), is(nullValue()));
        assertThat(js.createEvalBuilder(QueryBuilder.ExecMode.Run, 7.0), is(nullValue()));

        assertThat(js.createEvalBuilder(QueryBuilder.ExecMode.Run, 8.0), is(notNullValue()));
        assertThat(js.createEvalBuilder(QueryBuilder.ExecMode.Run, 8.0).getFunction(),
                   is("xdmp:javascript-eval($query, $vars, $options)"));
        assertThat(js.createEvalBuilder(QueryBuilder.ExecMode.Run, 8.0).getVarsBuilder(),
                   is(instanceOf(MapVarsBuilder.class)));

        assertThat(js.createEvalBuilder(QueryBuilder.ExecMode.Run, 9.0), is(notNullValue()));
        assertThat(js.createEvalBuilder(QueryBuilder.ExecMode.Run, 9.0).getFunction(),
                is("xdmp:javascript-eval($query, $vars, $options)"));
        assertThat(js.createEvalBuilder(QueryBuilder.ExecMode.Run, 9.0).getVarsBuilder(),
                is(instanceOf(MapVarsBuilder.class)));
    }

    public void testEvalProfile() {
        JavaScriptBuilder js = new JavaScriptBuilder();

        assertThat(js.createEvalBuilder(QueryBuilder.ExecMode.Profile, 5.0), is(nullValue()));
        assertThat(js.createEvalBuilder(QueryBuilder.ExecMode.Profile, 6.0), is(nullValue()));
        assertThat(js.createEvalBuilder(QueryBuilder.ExecMode.Profile, 7.0), is(nullValue()));
        assertThat(js.createEvalBuilder(QueryBuilder.ExecMode.Profile, 8.0), is(nullValue()));
        assertThat(js.createEvalBuilder(QueryBuilder.ExecMode.Profile, 9.0), is(nullValue()));
    }

    public void testEvalDebug() {
        JavaScriptBuilder js = new JavaScriptBuilder();

        assertThat(js.createEvalBuilder(QueryBuilder.ExecMode.Debug, 5.0), is(nullValue()));
        assertThat(js.createEvalBuilder(QueryBuilder.ExecMode.Debug, 6.0), is(nullValue()));
        assertThat(js.createEvalBuilder(QueryBuilder.ExecMode.Debug, 7.0), is(nullValue()));
        assertThat(js.createEvalBuilder(QueryBuilder.ExecMode.Debug, 8.0), is(nullValue()));
        assertThat(js.createEvalBuilder(QueryBuilder.ExecMode.Debug, 9.0), is(nullValue()));
    }

    public void testInvokeRun() {
        JavaScriptBuilder js = new JavaScriptBuilder();

        assertThat(js.createInvokeBuilder(QueryBuilder.ExecMode.Run, 5.0), is(nullValue()));
        assertThat(js.createInvokeBuilder(QueryBuilder.ExecMode.Run, 6.0), is(nullValue()));
        assertThat(js.createInvokeBuilder(QueryBuilder.ExecMode.Run, 7.0), is(nullValue()));

        assertThat(js.createInvokeBuilder(QueryBuilder.ExecMode.Run, 8.0), is(notNullValue()));
        assertThat(js.createInvokeBuilder(QueryBuilder.ExecMode.Run, 8.0).getFunction(),
                is("xdmp:invoke($path, $vars, $options)"));
        assertThat(js.createInvokeBuilder(QueryBuilder.ExecMode.Run, 8.0).getVarsBuilder(),
                is(instanceOf(MapVarsBuilder.class)));

        assertThat(js.createInvokeBuilder(QueryBuilder.ExecMode.Run, 9.0), is(notNullValue()));
        assertThat(js.createInvokeBuilder(QueryBuilder.ExecMode.Run, 9.0).getFunction(),
                is("xdmp:invoke($path, $vars, $options)"));
        assertThat(js.createInvokeBuilder(QueryBuilder.ExecMode.Run, 9.0).getVarsBuilder(),
                is(instanceOf(MapVarsBuilder.class)));
    }

    public void testInvokeProfile() {
        JavaScriptBuilder js = new JavaScriptBuilder();

        assertThat(js.createInvokeBuilder(QueryBuilder.ExecMode.Profile, 5.0), is(nullValue()));
        assertThat(js.createInvokeBuilder(QueryBuilder.ExecMode.Profile, 6.0), is(nullValue()));
        assertThat(js.createInvokeBuilder(QueryBuilder.ExecMode.Profile, 7.0), is(nullValue()));
        assertThat(js.createInvokeBuilder(QueryBuilder.ExecMode.Profile, 8.0), is(nullValue()));
        assertThat(js.createInvokeBuilder(QueryBuilder.ExecMode.Profile, 9.0), is(nullValue()));
    }

    public void testInvokeDebug() {
        JavaScriptBuilder js = new JavaScriptBuilder();

        assertThat(js.createInvokeBuilder(QueryBuilder.ExecMode.Debug, 5.0), is(nullValue()));
        assertThat(js.createInvokeBuilder(QueryBuilder.ExecMode.Debug, 6.0), is(nullValue()));
        assertThat(js.createInvokeBuilder(QueryBuilder.ExecMode.Debug, 7.0), is(nullValue()));
        assertThat(js.createInvokeBuilder(QueryBuilder.ExecMode.Debug, 8.0), is(nullValue()));
        assertThat(js.createInvokeBuilder(QueryBuilder.ExecMode.Debug, 9.0), is(nullValue()));
    }
}
