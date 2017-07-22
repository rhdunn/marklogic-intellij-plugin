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

public class XQueryScript extends ScriptFactory {
    @Override
    public String createEvalScript(String script, MarkLogicRunConfiguration configuration) {
        StringBuilder query = new StringBuilder();
        query.append("xdmp:eval(\"");
        query.append(asXQueryStringContent(script));
        query.append("\", ");
        query.append("()"); // no variables
        query.append(", <options xmlns=\"xdmp:eval\">");
        appendDatabaseOption(query, "database", configuration.getContentDatabase());
        appendDatabaseOption(query, "modules", configuration.getModuleDatabase());
        appendOption(query, "root", configuration.getModuleRoot());
        query.append("</options>)");
        return query.toString();
    }

    private void appendDatabaseOption(StringBuilder options, String option, String database) {
        final String selector = "{xdmp:database(\"" + asXQueryStringContent(database) + "\")}";
        appendOption(options, option, selector);
    }

    private void appendOption(StringBuilder options, String option, String value) {
        if (value == null || value.isEmpty()) {
            return;
        }

        options.append('<');
        options.append(option);
        options.append('>');
        options.append(value);
        options.append("</");
        options.append(option);
        options.append('>');
    }
}
