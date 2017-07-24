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
package uk.co.reecedunn.intellij.plugin.marklogic.configuration.script;

import uk.co.reecedunn.intellij.plugin.marklogic.configuration.MarkLogicRunConfiguration;
import uk.co.reecedunn.intellij.plugin.marklogic.rest.RDFFormat;

public class QueryScript extends ScriptFactory {
    private final String mQueryFunction;

    public QueryScript(String queryFunction) {
        mQueryFunction = queryFunction;
    }

    @Override
    public void createEvalScript(StringBuilder query, String script, MarkLogicRunConfiguration configuration) {
        if (mQueryFunction.equals("xdmp:sql")) {
            query.append(mQueryFunction);
            query.append("(\"");
            query.append(asXQueryStringContent(script));
            query.append("\")");
        } else {
            RDFFormat tripleFormat = configuration.getTripleFormat();

            query.append("import module namespace sem = \"http://marklogic.com/semantics\" at \"/MarkLogic/semantics.xqy\";\n");
            query.append("let $ret := sem:sparql(\"").append(asXQueryStringContent(script)).append("\")\n");
            if (tripleFormat == RDFFormat.SEM_TRIPLE) {
                query.append("return $ret");
            } else {
                query.append("return if (count($ret) > 0 and $ret[1] instance of sem:triple) then\n");
                // NOTE: Using xdmp:set-response-content-type overrides the multipart Content-Type for the response, but
                // still writes out the multipart data.
                query.append("    let $_ := xdmp:add-response-header(\"X-Content-Type\", \"").append(tripleFormat.getContentType()).append("\")");
                query.append("    return sem:rdf-serialize($ret, \"").append(tripleFormat.getMarkLogicName()).append("\")\n");
                query.append("else\n");
                query.append("    $ret\n");
            }
        }
    }
}
