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

import com.intellij.execution.ExecutionException;
import com.intellij.execution.executors.DefaultRunExecutor;
import uk.co.reecedunn.intellij.plugin.marklogic.api.RDFFormat;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.configuration.MarkLogicRunConfiguration;
import uk.co.reecedunn.intellij.plugin.marklogic.query.Function;
import uk.co.reecedunn.intellij.plugin.marklogic.query.QueryBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.QueryBuilderFactory;
import uk.co.reecedunn.intellij.plugin.marklogic.tests.configuration.ConfigurationTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("ConstantConditions")
public class FunctionTest extends ConfigurationTestCase {
    public void testMainModuleFilePathNotFound() {
        final MarkLogicRunConfiguration configuration = createConfiguration();
        configuration.setMainModulePath("test-main-module-file-file-path-not-found.xqy");

        QueryBuilder queryBuilder = QueryBuilderFactory.createQueryBuilderForFile(configuration.getMainModulePath());
        Function function = queryBuilder.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, 5.0);

        ExecutionException e = assertThrows(ExecutionException.class, () -> function.buildQuery(configuration));
        assertThat(e.getMessage(), is("Missing query file: test-main-module-file-file-path-not-found.xqy"));
    }

    public void testEval() throws ExecutionException {
        final String query = "(1, 2, 3)";
        final MarkLogicRunConfiguration configuration = createConfiguration();
        configuration.setMainModuleFile(createVirtualFile("test.xqy", query));

        QueryBuilder queryBuilder = QueryBuilderFactory.createQueryBuilderForFile(configuration.getMainModulePath());
        Function function = queryBuilder.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, 5.0);

        final String actual = function.buildQuery(configuration).replaceAll("\r\n", "\n");
        final String expected =
            "let $query := \"(1, 2, 3)\"\n" +
            "let $vars := ()\n" +
            "let $options := <options xmlns=\"xdmp:eval\"><modules>0</modules><root>/</root></options>\n" +
            "return try { xdmp:eval($query, $vars, $options) } catch ($e) { $e }\n";
        assertThat(actual, is(expected));
    }

    public void testQueryWithDoubleQuotes() throws ExecutionException {
        final String query = "1 || \"st\"";
        final MarkLogicRunConfiguration configuration = createConfiguration();
        configuration.setMainModuleFile(createVirtualFile("test.xqy", query));

        QueryBuilder queryBuilder = QueryBuilderFactory.createQueryBuilderForFile(configuration.getMainModulePath());
        Function function = queryBuilder.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, 5.0);

        final String actual = function.buildQuery(configuration).replaceAll("\r\n", "\n");
        final String expected =
            "let $query := \"1 || \"\"st\"\"\"\n" +
            "let $vars := ()\n" +
            "let $options := <options xmlns=\"xdmp:eval\"><modules>0</modules><root>/</root></options>\n" +
            "return try { xdmp:eval($query, $vars, $options) } catch ($e) { $e }\n";
        assertThat(actual, is(expected));
    }

    public void testQueryWithXmlEntities() throws ExecutionException {
        final String query = "<a>&amp;</a>";
        final MarkLogicRunConfiguration configuration = createConfiguration();
        configuration.setMainModuleFile(createVirtualFile("test.xqy", query));

        QueryBuilder queryBuilder = QueryBuilderFactory.createQueryBuilderForFile(configuration.getMainModulePath());
        Function function = queryBuilder.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, 5.0);

        final String actual = function.buildQuery(configuration).replaceAll("\r\n", "\n");
        final String expected =
            "let $query := \"<a>&amp;amp;</a>\"\n" +
            "let $vars := ()\n" +
            "let $options := <options xmlns=\"xdmp:eval\"><modules>0</modules><root>/</root></options>\n" +
            "return try { xdmp:eval($query, $vars, $options) } catch ($e) { $e }\n";
        assertThat(actual, is(expected));
    }

    public void testQueryWithOptions() throws ExecutionException {
        final String query = "(1, 2)";
        final MarkLogicRunConfiguration configuration = createConfiguration();
        configuration.setMainModuleFile(createVirtualFile("test.xqy", query));
        configuration.setContentDatabase("lorem");
        configuration.setModuleDatabase("ipsum");
        configuration.setModuleRoot("dolor");

        QueryBuilder queryBuilder = QueryBuilderFactory.createQueryBuilderForFile(configuration.getMainModulePath());
        Function function = queryBuilder.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, 5.0);

        final String actual = function.buildQuery(configuration).replaceAll("\r\n", "\n");
        final String expected =
            "let $query := \"(1, 2)\"\n" +
            "let $vars := ()\n" +
            "let $options := " +
                "<options xmlns=\"xdmp:eval\">" +
                    "<database>{xdmp:database(\"lorem\")}</database>" +
                    "<modules>{xdmp:database(\"ipsum\")}</modules>" +
                    "<root>dolor</root>" +
                "</options>\n" +
            "return try { xdmp:eval($query, $vars, $options) } catch ($e) { $e }\n";
        assertThat(actual, is(expected));
    }

    public void testNoVarsAndOptionsBuilder() throws ExecutionException {
        final String query = "select * from authors";
        final MarkLogicRunConfiguration configuration = createConfiguration();
        configuration.setMainModuleFile(createVirtualFile("test.sql", query));

        QueryBuilder queryBuilder = QueryBuilderFactory.createQueryBuilderForFile(configuration.getMainModulePath());
        Function function = queryBuilder.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, 8.0);

        final String actual = function.buildQuery(configuration).replaceAll("\r\n", "\n");
        final String expected =
            "let $query := \"select * from authors\"\n" +
            "let $vars := ()\n" +
            "let $options := ()\n" +
            "return try { xdmp:sql($query) } catch ($e) { $e }\n";
        assertThat(actual, is(expected));
    }

    public void testRdfTripleFormat_MarkLogic6() throws ExecutionException {
        final String query = "34";
        final MarkLogicRunConfiguration configuration = createConfiguration();
        configuration.setMainModuleFile(createVirtualFile("test.xqy", query));
        configuration.setMarkLogicMajorMinor(6.0);
        configuration.setTripleFormat(RDFFormat.TURTLE);

        QueryBuilder queryBuilder = QueryBuilderFactory.createQueryBuilderForFile(configuration.getMainModulePath());
        Function function = queryBuilder.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, 8.0);

        final String actual = function.buildQuery(configuration).replaceAll("\r\n", "\n");
        final String expected =
            "let $query := \"34\"\n" +
            "let $vars := ()\n" +
            "let $options := <options xmlns=\"xdmp:eval\"><modules>0</modules><root>/</root></options>\n" +
            "return try { xdmp:eval($query, $vars, $options) } catch ($e) { $e }\n";
        assertThat(actual, is(expected));
    }

    public void testRdfTripleFormat_MarkLogic7() throws ExecutionException {
        final String query = "34";
        final MarkLogicRunConfiguration configuration = createConfiguration();
        configuration.setMainModuleFile(createVirtualFile("test.xqy", query));
        configuration.setMarkLogicMajorMinor(7.0);
        configuration.setTripleFormat(RDFFormat.TURTLE);

        QueryBuilder queryBuilder = QueryBuilderFactory.createQueryBuilderForFile(configuration.getMainModulePath());
        Function function = queryBuilder.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, 8.0);

        final String actual = function.buildQuery(configuration).replaceAll("\r\n", "\n");
        final String expected =
            "import module namespace sem = \"http://marklogic.com/semantics\" at \"/MarkLogic/semantics.xqy\";\n" +
            "let $query := \"34\"\n" +
            "let $vars := ()\n" +
            "let $options := <options xmlns=\"xdmp:eval\"><modules>0</modules><root>/</root></options>\n" +
            "return try {\n" +
            "  let $ret     := xdmp:eval($query, $vars, $options)\n" +
            "  let $triples := for $item in $ret where $item instance of sem:triple return $item\n" +
            "  let $other   := for $item in $ret where not($item instance of sem:triple) return $item\n" +
            "  return if (count($triples) > 0) then\n" +
            "    let $fmt := sem:rdf-serialize($triples, \"turtle\")\n" +
            "    let $_ := xdmp:add-response-header(\"X-Content-Type\", \"text/turtle\")\n" +
            "    return ($fmt, $other)\n" +
            "  else\n" +
            "    $ret\n" +
            "} catch ($e) { $e }\n";
        assertThat(actual, is(expected));
    }
}
