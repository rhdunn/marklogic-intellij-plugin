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
package uk.co.reecedunn.intellij.plugin.marklogic.query;

import com.intellij.execution.ExecutionException;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.xmlbeans.impl.common.IOUtil;
import org.jetbrains.annotations.NotNull;
import uk.co.reecedunn.intellij.plugin.marklogic.api.RDFFormat;
import uk.co.reecedunn.intellij.plugin.marklogic.ui.configuration.MarkLogicRunConfiguration;
import uk.co.reecedunn.intellij.plugin.marklogic.query.options.EvalOptionsBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.options.OptionsBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.vars.KeyValueVarsBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.vars.MapVarsBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.vars.VarsBuilder;

import java.io.*;

public enum Function {
    DBG_EVAL_50(
        "dbg:eval($query, $vars, $options)",
        KeyValueVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE,
        Parameters.Eval),
    DBG_EVAL_80(
        "dbg:eval($query, $vars, $options)",
        MapVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE,
        Parameters.Eval),

    DBG_INVOKE_50(
        "dbg:invoke($path, $vars, $options)",
        KeyValueVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE,
        Parameters.Invoke),
    DBG_INVOKE_80(
        "dbg:invoke($path, $vars, $options)",
        MapVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE,
        Parameters.Invoke),

    PROF_EVAL_50(
        "prof:eval($query, $vars, $options)",
        KeyValueVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE,
        Parameters.Eval),
    PROF_EVAL_80(
        "prof:eval($query, $vars, $options)",
        MapVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE,
        Parameters.Eval),

    PROF_INVOKE_50(
        "prof:invoke($path, $vars, $options)",
        KeyValueVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE,
        Parameters.Invoke),
    PROF_INVOKE_80(
        "prof:invoke($path, $vars, $options)",
        MapVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE,
        Parameters.Invoke),

    PROF_XSLT_EVAL_50(
        "prof:xslt-eval($query, $input, $vars, $options)",
        MapVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE,
        Parameters.EvalStylesheet),
    PROF_XSLT_INVOKE_50(
        "prof:xslt-invoke($path, $input, $vars, $options)",
        MapVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE,
        Parameters.InvokeStylesheet),

    SEM_SPARQL_70(
        "sem:sparql($query, $vars)",
        MapVarsBuilder.INSTANCE,
        null,
        Parameters.Eval),

    SEM_SPARQL_UPDATE_80(
        "sem:sparql-update($query, $vars)",
        MapVarsBuilder.INSTANCE,
        null,
        Parameters.Eval),

    XDMP_EVAL_50(
        "xdmp:eval($query, $vars, $options)",
        KeyValueVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE,
        Parameters.Eval),
    XDMP_EVAL_70(
        "xdmp:eval($query, $vars, $options)",
        MapVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE,
        Parameters.Eval),

    XDMP_INVOKE_50(
        "xdmp:invoke($path, $vars, $options)",
        KeyValueVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE,
        Parameters.Invoke),
    XDMP_INVOKE_70(
        "xdmp:invoke($path, $vars, $options)",
        MapVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE,
        Parameters.Invoke),

    XDMP_JAVASCRIPT_EVAL_80(
        "xdmp:javascript-eval($query, $vars, $options)",
        MapVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE,
        Parameters.Eval),

    XDMP_SQL_80(
        "xdmp:sql($query)",
        null,
        null,
        Parameters.Eval),
    XDMP_SQL_90(
        "xdmp:sql($query, (), $vars)",
        MapVarsBuilder.INSTANCE,
        null,
        Parameters.Eval),

    XDMP_XSLT_EVAL_50(
        "xdmp:xslt-eval($query, $input, $vars, $options)",
        MapVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE,
        Parameters.EvalStylesheet),
    XDMP_XSLT_INVOKE_50(
        "xdmp:xslt-invoke($path, $input, $vars, $options)",
        MapVarsBuilder.INSTANCE,
        EvalOptionsBuilder.INSTANCE,
        Parameters.InvokeStylesheet);

    public enum Parameters {
        //                // $query    | $path     | $input | $vars   | $options |
        //----------------//-----------|-----------|--------|---------|----------|
        Eval,             // xs:string | -         | -      | item()* | item()*  |
        Invoke,           // -         | xs:string | -      | item()* | item()*  |
        EvalStylesheet,   // node()    | -         | node() | item()* | item()*  |
        InvokeStylesheet, // -         | xs:string | node() | item()* | item()*  |
    }

    private final String function;
    private final VarsBuilder varsBuilder;
    private final OptionsBuilder optionsBuilder;
    private final Parameters parameters;

    Function(String function, VarsBuilder varsBuilder, OptionsBuilder optionsBuilder, Parameters parameters) {
        this.function = function;
        this.varsBuilder = varsBuilder;
        this.optionsBuilder = optionsBuilder;
        this.parameters = parameters;
    }

    public String buildQuery(MarkLogicRunConfiguration configuration) throws ExecutionException {
        final String query = readFileContent(configuration);

        final String options;
        if (optionsBuilder != null) {
            optionsBuilder.reset();
            optionsBuilder.setContentDatabase(configuration.getContentDatabase());
            optionsBuilder.setModulesDatabase(configuration.getModuleDatabase());
            optionsBuilder.setModulesRoot(configuration.getModuleRoot());
            options = optionsBuilder.build();
        } else {
            options = "()";
        }

        RDFFormat tripleFormat = configuration.getTripleFormat();
        double markLogicVersion = configuration.getMarkLogicVersion();

        if (tripleFormat == RDFFormat.SEM_TRIPLE || markLogicVersion < 7.0) {
            return MarkLogicQueryKt.getRUN_QUERY().getQuery()
                .replace("$QUERY_STRING", asXQueryStringContent(query))
                .replace("$OPTIONS",      options)
                .replace("$FUNCTION",     function);
        } else {
            return MarkLogicQueryKt.getRUN_QUERY_AS_RDF().getQuery()
                .replace("$QUERY_STRING",  asXQueryStringContent(query))
                .replace("$OPTIONS",       options)
                .replace("$FUNCTION",      function)
                .replace("$TRIPLE_FORMAT", tripleFormat.getMarkLogicName())
                .replace("$CONTENT_TYPE",  tripleFormat.getContentType());
        }
    }

    private String readFileContent(MarkLogicRunConfiguration configuration) throws ExecutionException {
        final VirtualFile file = configuration.getMainModuleFile();
        if (file == null)
            throw new ExecutionException("Missing query file: " + configuration.getMainModulePath());
        try {
            return streamToString(file.getInputStream());
        } catch (IOException e) {
            throw new ExecutionException(e);
        }
    }

    private String streamToString(InputStream stream) throws IOException {
        StringWriter writer = new StringWriter();
        IOUtil.copyCompletely(new InputStreamReader(stream), writer);
        return writer.toString();
    }

    private String asXQueryStringContent(@NotNull String query) {
        return query.replaceAll("\"", "\"\"").replaceAll("&", "&amp;");
    }
}
