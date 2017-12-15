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
package uk.co.reecedunn.intellij.plugin.marklogic.query

import com.intellij.execution.ExecutionException
import org.apache.xmlbeans.impl.common.IOUtil
import uk.co.reecedunn.intellij.plugin.marklogic.ui.configuration.MarkLogicRunConfiguration
import uk.co.reecedunn.intellij.plugin.marklogic.query.options.EvalOptionsBuilder
import uk.co.reecedunn.intellij.plugin.marklogic.query.options.OptionsBuilder
import uk.co.reecedunn.intellij.plugin.marklogic.query.vars.KeyValueVarsBuilder
import uk.co.reecedunn.intellij.plugin.marklogic.query.vars.MapVarsBuilder
import uk.co.reecedunn.intellij.plugin.marklogic.query.vars.VarsBuilder

import java.io.*

enum class Function private constructor(
        private val function: String,
        private val varsBuilder: VarsBuilder?,
        private val optionsBuilder: OptionsBuilder?,
        private val parameters: Parameters) {

    DBG_EVAL_50(
            "dbg:eval(\$query, \$vars, \$options)",
            KeyValueVarsBuilder,
            EvalOptionsBuilder,
            Parameters.Eval),
    DBG_EVAL_80(
            "dbg:eval(\$query, \$vars, \$options)",
            MapVarsBuilder,
            EvalOptionsBuilder,
            Parameters.Eval),

    DBG_INVOKE_50(
            "dbg:invoke(\$path, \$vars, \$options)",
            KeyValueVarsBuilder,
            EvalOptionsBuilder,
            Parameters.Invoke),
    DBG_INVOKE_80(
            "dbg:invoke(\$path, \$vars, \$options)",
            MapVarsBuilder,
            EvalOptionsBuilder,
            Parameters.Invoke),

    PROF_EVAL_50(
            "prof:eval(\$query, \$vars, \$options)",
            KeyValueVarsBuilder,
            EvalOptionsBuilder,
            Parameters.Eval),
    PROF_EVAL_80(
            "prof:eval(\$query, \$vars, \$options)",
            MapVarsBuilder,
            EvalOptionsBuilder,
            Parameters.Eval),

    PROF_INVOKE_50(
            "prof:invoke(\$path, \$vars, \$options)",
            KeyValueVarsBuilder,
            EvalOptionsBuilder,
            Parameters.Invoke),
    PROF_INVOKE_80(
            "prof:invoke(\$path, \$vars, \$options)",
            MapVarsBuilder,
            EvalOptionsBuilder,
            Parameters.Invoke),

    PROF_XSLT_EVAL_50(
            "prof:xslt-eval(\$query, \$input, \$vars, \$options)",
            MapVarsBuilder,
            EvalOptionsBuilder,
            Parameters.EvalStylesheet),
    PROF_XSLT_INVOKE_50(
            "prof:xslt-invoke(\$path, \$input, \$vars, \$options)",
            MapVarsBuilder,
            EvalOptionsBuilder,
            Parameters.InvokeStylesheet),

    SEM_SPARQL_70(
            "sem:sparql(\$query, \$vars)",
            MapVarsBuilder, null,
            Parameters.Eval),

    SEM_SPARQL_UPDATE_80(
            "sem:sparql-update(\$query, \$vars)",
            MapVarsBuilder, null,
            Parameters.Eval),

    XDMP_EVAL_50(
            "xdmp:eval(\$query, \$vars, \$options)",
            KeyValueVarsBuilder,
            EvalOptionsBuilder,
            Parameters.Eval),
    XDMP_EVAL_70(
            "xdmp:eval(\$query, \$vars, \$options)",
            MapVarsBuilder,
            EvalOptionsBuilder,
            Parameters.Eval),

    XDMP_INVOKE_50(
            "xdmp:invoke(\$path, \$vars, \$options)",
            KeyValueVarsBuilder,
            EvalOptionsBuilder,
            Parameters.Invoke),
    XDMP_INVOKE_70(
            "xdmp:invoke(\$path, \$vars, \$options)",
            MapVarsBuilder,
            EvalOptionsBuilder,
            Parameters.Invoke),

    XDMP_JAVASCRIPT_EVAL_80(
            "xdmp:javascript-eval(\$query, \$vars, \$options)",
            MapVarsBuilder,
            EvalOptionsBuilder,
            Parameters.Eval),

    XDMP_SQL_80(
            "xdmp:sql(\$query)", null, null,
            Parameters.Eval),
    XDMP_SQL_90(
            "xdmp:sql(\$query, (), \$vars)",
            MapVarsBuilder, null,
            Parameters.Eval),

    XDMP_XSLT_EVAL_50(
            "xdmp:xslt-eval(\$query, \$input, \$vars, \$options)",
            MapVarsBuilder,
            EvalOptionsBuilder,
            Parameters.EvalStylesheet),
    XDMP_XSLT_INVOKE_50(
            "xdmp:xslt-invoke(\$path, \$input, \$vars, \$options)",
            MapVarsBuilder,
            EvalOptionsBuilder,
            Parameters.InvokeStylesheet);

    enum class Parameters {
        //                // $query    | $path     | $input | $vars   | $options |
        //----------------//-----------|-----------|--------|---------|----------|
        Eval,             // xs:string | -         | -      | item()* | item()*  |
        Invoke,           // -         | xs:string | -      | item()* | item()*  |
        EvalStylesheet,   // node()    | -         | node() | item()* | item()*  |
        InvokeStylesheet  // -         | xs:string | node() | item()* | item()*  |
    }

    @Throws(ExecutionException::class)
    fun buildQuery(configuration: MarkLogicRunConfiguration): String {
        val query = readFileContent(configuration)

        val options: String
        if (optionsBuilder != null) {
            optionsBuilder.reset()
            optionsBuilder.contentDatabase = configuration.contentDatabase
            optionsBuilder.modulesDatabase = configuration.moduleDatabase
            optionsBuilder.modulesRoot = configuration.moduleRoot
            options = optionsBuilder.build()
        } else {
            options = "()"
        }

        val tripleFormat = configuration.tripleFormat
        val markLogicVersion = configuration.markLogicVersion

        return if (tripleFormat == SEM_TRIPLE || markLogicVersion.major < 7) {
            RUN_QUERY.query
                .replace("\$QUERY_STRING", asXQueryStringContent(query))
                .replace("\$OPTIONS", options)
                .replace("\$FUNCTION", function)
        } else {
            RUN_QUERY_AS_RDF.query
                .replace("\$QUERY_STRING", asXQueryStringContent(query))
                .replace("\$OPTIONS", options)
                .replace("\$FUNCTION", function)
                .replace("\$TRIPLE_FORMAT", tripleFormat.markLogicName)
                .replace("\$CONTENT_TYPE", tripleFormat.contentType)
        }
    }

    @Throws(ExecutionException::class)
    private fun readFileContent(configuration: MarkLogicRunConfiguration): String {
        val file = configuration.mainModuleFile ?: throw ExecutionException("Missing query file: " + configuration.mainModulePath)
        try {
            return streamToString(file.inputStream)
        } catch (e: IOException) {
            throw ExecutionException(e)
        }

    }

    @Throws(IOException::class)
    private fun streamToString(stream: InputStream): String {
        val writer = StringWriter()
        IOUtil.copyCompletely(InputStreamReader(stream), writer)
        return writer.toString()
    }

    private fun asXQueryStringContent(query: String): String {
        return query.replace("\"".toRegex(), "\"\"").replace("&".toRegex(), "&amp;")
    }
}
