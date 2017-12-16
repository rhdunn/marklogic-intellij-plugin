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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.query

import com.intellij.execution.ExecutionException
import com.intellij.execution.executors.DefaultRunExecutor
import uk.co.reecedunn.intellij.plugin.marklogic.query.*
import uk.co.reecedunn.intellij.plugin.marklogic.server.*
import uk.co.reecedunn.intellij.plugin.marklogic.tests.configuration.ConfigurationTestCase

import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.function.Executable

class FunctionTest : ConfigurationTestCase() {
    fun testMainModuleFilePathNotFound() {
        val configuration = createConfiguration()
        configuration.mainModulePath = "test-main-module-file-file-path-not-found.xqy"

        val queryBuilder = QueryBuilderFactory.createQueryBuilderForFile(configuration.mainModulePath)
        val function = queryBuilder!!.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_5)

        val e = assertThrows(ExecutionException::class.java, Executable { function!!.buildQuery(configuration) })
        assertThat(e.message, `is`("Missing query file: test-main-module-file-file-path-not-found.xqy"))
    }

    @Throws(ExecutionException::class)
    fun testEval() {
        val query = "(1, 2, 3)"
        val configuration = createConfiguration()
        configuration.mainModuleFile = createVirtualFile("test.xqy", query)

        val queryBuilder = QueryBuilderFactory.createQueryBuilderForFile(configuration.mainModulePath)
        val function = queryBuilder!!.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_5)

        val actual = function!!.buildQuery(configuration).replace("\r\n".toRegex(), "\n")
        val expected =
            "let \$query := \"(1, 2, 3)\"\n" +
            "let \$vars := ()\n" +
            "let \$options := <options xmlns=\"xdmp:eval\"><modules>0</modules><root>/</root></options>\n" +
            "return try { xdmp:eval(\$query, \$vars, \$options) } catch (\$e) { \$e }\n"
        assertThat(actual, `is`(expected))
    }

    @Throws(ExecutionException::class)
    fun testQueryWithDoubleQuotes() {
        val query = "1 || \"st\""
        val configuration = createConfiguration()
        configuration.mainModuleFile = createVirtualFile("test.xqy", query)

        val queryBuilder = QueryBuilderFactory.createQueryBuilderForFile(configuration.mainModulePath)
        val function = queryBuilder!!.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_5)

        val actual = function!!.buildQuery(configuration).replace("\r\n".toRegex(), "\n")
        val expected =
            "let \$query := \"1 || \"\"st\"\"\"\n" +
            "let \$vars := ()\n" +
            "let \$options := <options xmlns=\"xdmp:eval\"><modules>0</modules><root>/</root></options>\n" +
            "return try { xdmp:eval(\$query, \$vars, \$options) } catch (\$e) { \$e }\n"
        assertThat(actual, `is`(expected))
    }

    @Throws(ExecutionException::class)
    fun testQueryWithXmlEntities() {
        val query = "<a>&amp;</a>"
        val configuration = createConfiguration()
        configuration.mainModuleFile = createVirtualFile("test.xqy", query)

        val queryBuilder = QueryBuilderFactory.createQueryBuilderForFile(configuration.mainModulePath)
        val function = queryBuilder!!.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_5)

        val actual = function!!.buildQuery(configuration).replace("\r\n".toRegex(), "\n")
        val expected =
            "let \$query := \"<a>&amp;amp;</a>\"\n" +
            "let \$vars := ()\n" +
            "let \$options := <options xmlns=\"xdmp:eval\"><modules>0</modules><root>/</root></options>\n" +
            "return try { xdmp:eval(\$query, \$vars, \$options) } catch (\$e) { \$e }\n"
        assertThat(actual, `is`(expected))
    }

    @Throws(ExecutionException::class)
    fun testQueryWithOptions() {
        val query = "(1, 2)"
        val configuration = createConfiguration()
        configuration.mainModuleFile = createVirtualFile("test.xqy", query)
        configuration.contentDatabase = "lorem"
        configuration.moduleDatabase = "ipsum"
        configuration.moduleRoot = "dolor"

        val queryBuilder = QueryBuilderFactory.createQueryBuilderForFile(configuration.mainModulePath)
        val function = queryBuilder!!.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_5)

        val actual = function!!.buildQuery(configuration).replace("\r\n".toRegex(), "\n")
        val expected =
            "let \$query := \"(1, 2)\"\n" +
            "let \$vars := ()\n" +
            "let \$options := " +
                "<options xmlns=\"xdmp:eval\">" +
                "<database>{xdmp:database(\"lorem\")}</database>" +
                "<modules>{xdmp:database(\"ipsum\")}</modules>" +
                "<root>dolor</root>" +
                "</options>\n" +
            "return try { xdmp:eval(\$query, \$vars, \$options) } catch (\$e) { \$e }\n"
        assertThat(actual, `is`(expected))
    }

    @Throws(ExecutionException::class)
    fun testNoVarsAndOptionsBuilder() {
        val query = "select * from authors"
        val configuration = createConfiguration()
        configuration.mainModuleFile = createVirtualFile("test.sql", query)

        val queryBuilder = QueryBuilderFactory.createQueryBuilderForFile(configuration.mainModulePath)
        val function = queryBuilder!!.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_8)

        val actual = function!!.buildQuery(configuration).replace("\r\n".toRegex(), "\n")
        val expected =
            "let \$query := \"select * from authors\"\n" +
            "let \$vars := ()\n" +
            "let \$options := ()\n" +
            "return try { xdmp:sql(\$query) } catch (\$e) { \$e }\n"
        assertThat(actual, `is`(expected))
    }

    @Throws(ExecutionException::class)
    fun testRdfTripleFormat_MarkLogic6() {
        val query = "34"
        val configuration = createConfiguration()
        configuration.mainModuleFile = createVirtualFile("test.xqy", query)
        configuration.markLogicVersion = MARKLOGIC_6
        configuration.tripleFormat = TURTLE

        val queryBuilder = QueryBuilderFactory.createQueryBuilderForFile(configuration.mainModulePath)
        val function = queryBuilder!!.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_8)

        val actual = function!!.buildQuery(configuration).replace("\r\n".toRegex(), "\n")
        val expected =
            "let \$query := \"34\"\n" +
            "let \$vars := ()\n" +
            "let \$options := <options xmlns=\"xdmp:eval\"><modules>0</modules><root>/</root></options>\n" +
            "return try { xdmp:eval(\$query, \$vars, \$options) } catch (\$e) { \$e }\n"
        assertThat(actual, `is`(expected))
    }

    @Throws(ExecutionException::class)
    fun testRdfTripleFormat_MarkLogic7() {
        val query = "34"
        val configuration = createConfiguration()
        configuration.mainModuleFile = createVirtualFile("test.xqy", query)
        configuration.markLogicVersion = MARKLOGIC_7
        configuration.tripleFormat = TURTLE

        val queryBuilder = QueryBuilderFactory.createQueryBuilderForFile(configuration.mainModulePath)
        val function = queryBuilder!!.createEvalBuilder(DefaultRunExecutor.EXECUTOR_ID, MARKLOGIC_8)

        val actual = function!!.buildQuery(configuration).replace("\r\n".toRegex(), "\n")
        val expected =
            "import module namespace sem = \"http://marklogic.com/semantics\" at \"/MarkLogic/semantics.xqy\";\n" +
            "let \$query := \"34\"\n" +
            "let \$vars := ()\n" +
            "let \$options := <options xmlns=\"xdmp:eval\"><modules>0</modules><root>/</root></options>\n" +
            "return try {\n" +
            "  let \$ret     := xdmp:eval(\$query, \$vars, \$options)\n" +
            "  let \$triples := for \$item in \$ret where \$item instance of sem:triple return \$item\n" +
            "  let \$other   := for \$item in \$ret where not(\$item instance of sem:triple) return \$item\n" +
            "  return if (count(\$triples) > 0) then\n" +
            "    let \$fmt := sem:rdf-serialize(\$triples, \"turtle\")\n" +
            "    let \$_ := xdmp:add-response-header(\"X-Content-Type\", \"text/turtle\")\n" +
            "    return (\$fmt, \$other)\n" +
            "  else\n" +
            "    \$ret\n" +
            "} catch (\$e) { \$e }\n"
        assertThat(actual, `is`(expected))
    }
}
