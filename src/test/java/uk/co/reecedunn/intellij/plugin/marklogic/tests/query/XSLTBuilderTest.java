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
import uk.co.reecedunn.intellij.plugin.marklogic.query.Function;
import uk.co.reecedunn.intellij.plugin.marklogic.query.QueryBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.XSLTBuilder;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("ConstantConditions")
public class XSLTBuilderTest extends TestCase {
    public void testEvalRun() {
        QueryBuilder xslt = XSLTBuilder.INSTANCE;

        assertThat(xslt.createEvalBuilder(QueryBuilder.ExecMode.Run, 5.0), is(Function.XDMP_XSLT_EVAL_50));
        assertThat(xslt.createEvalBuilder(QueryBuilder.ExecMode.Run, 6.0), is(Function.XDMP_XSLT_EVAL_50));
        assertThat(xslt.createEvalBuilder(QueryBuilder.ExecMode.Run, 7.0), is(Function.XDMP_XSLT_EVAL_50));
        assertThat(xslt.createEvalBuilder(QueryBuilder.ExecMode.Run, 8.0), is(Function.XDMP_XSLT_EVAL_50));
        assertThat(xslt.createEvalBuilder(QueryBuilder.ExecMode.Run, 9.0), is(Function.XDMP_XSLT_EVAL_50));
    }

    public void testEvalProfile() {
        QueryBuilder xslt = XSLTBuilder.INSTANCE;

        assertThat(xslt.createEvalBuilder(QueryBuilder.ExecMode.Profile, 5.0), is(Function.PROF_XSLT_EVAL_50));
        assertThat(xslt.createEvalBuilder(QueryBuilder.ExecMode.Profile, 6.0), is(Function.PROF_XSLT_EVAL_50));
        assertThat(xslt.createEvalBuilder(QueryBuilder.ExecMode.Profile, 7.0), is(Function.PROF_XSLT_EVAL_50));
        assertThat(xslt.createEvalBuilder(QueryBuilder.ExecMode.Profile, 8.0), is(Function.PROF_XSLT_EVAL_50));
        assertThat(xslt.createEvalBuilder(QueryBuilder.ExecMode.Profile, 9.0), is(Function.PROF_XSLT_EVAL_50));
    }

    public void testEvalDebug() {
        QueryBuilder xslt = XSLTBuilder.INSTANCE;

        assertThat(xslt.createEvalBuilder(QueryBuilder.ExecMode.Debug, 5.0), is(nullValue()));
        assertThat(xslt.createEvalBuilder(QueryBuilder.ExecMode.Debug, 6.0), is(nullValue()));
        assertThat(xslt.createEvalBuilder(QueryBuilder.ExecMode.Debug, 7.0), is(nullValue()));
        assertThat(xslt.createEvalBuilder(QueryBuilder.ExecMode.Debug, 8.0), is(nullValue()));
        assertThat(xslt.createEvalBuilder(QueryBuilder.ExecMode.Debug, 9.0), is(nullValue()));
    }

    public void testInvokeRun() {
        QueryBuilder xslt = XSLTBuilder.INSTANCE;

        assertThat(xslt.createInvokeBuilder(QueryBuilder.ExecMode.Run, 5.0), is(Function.XDMP_XSLT_INVOKE_50));
        assertThat(xslt.createInvokeBuilder(QueryBuilder.ExecMode.Run, 6.0), is(Function.XDMP_XSLT_INVOKE_50));
        assertThat(xslt.createInvokeBuilder(QueryBuilder.ExecMode.Run, 7.0), is(Function.XDMP_XSLT_INVOKE_50));
        assertThat(xslt.createInvokeBuilder(QueryBuilder.ExecMode.Run, 8.0), is(Function.XDMP_XSLT_INVOKE_50));
        assertThat(xslt.createInvokeBuilder(QueryBuilder.ExecMode.Run, 9.0), is(Function.XDMP_XSLT_INVOKE_50));
    }

    public void testInvokeProfile() {
        QueryBuilder xslt = XSLTBuilder.INSTANCE;

        assertThat(xslt.createInvokeBuilder(QueryBuilder.ExecMode.Profile, 5.0), is(Function.PROF_XSLT_INVOKE_50));
        assertThat(xslt.createInvokeBuilder(QueryBuilder.ExecMode.Profile, 6.0), is(Function.PROF_XSLT_INVOKE_50));
        assertThat(xslt.createInvokeBuilder(QueryBuilder.ExecMode.Profile, 7.0), is(Function.PROF_XSLT_INVOKE_50));
        assertThat(xslt.createInvokeBuilder(QueryBuilder.ExecMode.Profile, 8.0), is(Function.PROF_XSLT_INVOKE_50));
        assertThat(xslt.createInvokeBuilder(QueryBuilder.ExecMode.Profile, 9.0), is(Function.PROF_XSLT_INVOKE_50));
    }

    public void testInvokeDebug() {
        QueryBuilder xslt = XSLTBuilder.INSTANCE;

        assertThat(xslt.createInvokeBuilder(QueryBuilder.ExecMode.Debug, 5.0), is(nullValue()));
        assertThat(xslt.createInvokeBuilder(QueryBuilder.ExecMode.Debug, 6.0), is(nullValue()));
        assertThat(xslt.createInvokeBuilder(QueryBuilder.ExecMode.Debug, 7.0), is(nullValue()));
        assertThat(xslt.createInvokeBuilder(QueryBuilder.ExecMode.Debug, 8.0), is(nullValue()));
        assertThat(xslt.createInvokeBuilder(QueryBuilder.ExecMode.Debug, 9.0), is(nullValue()));
    }
}
