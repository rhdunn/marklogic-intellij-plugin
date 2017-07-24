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
package uk.co.reecedunn.intellij.plugin.marklogic.query.options;

import org.jetbrains.annotations.NotNull;

public class EvalOptionsBuilder implements OptionsBuilder {
    public static OptionsBuilder INSTANCE = new EvalOptionsBuilder();

    private String contentDatabase = null;
    private String modulesDatabase = null;
    private String modulesRoot = null;

    private EvalOptionsBuilder() {
    }

    @Override
    public void reset() {
        contentDatabase = null;
        modulesDatabase = null;
        modulesRoot = null;
    }

    @Override
    public void setContentDatabase(String contentDatabase) {
        this.contentDatabase = contentDatabase;
    }

    @Override
    public void setModulesDatabase(String modulesDatabase) {
        this.modulesDatabase = modulesDatabase;
    }

    @Override
    public void setModulesRoot(String modulesRoot) {
        this.modulesRoot = modulesRoot;
    }

    @Override
    public void build(@NotNull StringBuilder builder) {
        builder.append("<options xmlns=\"xdmp:eval\">");
        buildDatabaseOption(builder, "database", contentDatabase);
        buildDatabaseOption(builder, "modules", modulesDatabase);
        buildOption(builder, "root", modulesRoot);
        builder.append("</options>");
    }

    private void buildDatabaseOption(StringBuilder options, String option, String database) {
        if (database == null || database.isEmpty()) {
            return;
        }

        final String selector = "{xdmp:database(\"" + database + "\")}";
        buildOption(options, option, selector);
    }

    private void buildOption(StringBuilder options, String option, String value) {
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
