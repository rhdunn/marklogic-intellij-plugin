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
package uk.co.reecedunn.intellij.plugin.marklogic.query.vars;

import org.jetbrains.annotations.NotNull;

public class MapVarsBuilder implements VarsBuilder {
    public void start(@NotNull StringBuilder builder) {
        builder.append("let $vars := map:map()\n");
    }

    public void add(@NotNull StringBuilder builder, String key, String value) {
        builder.append("let $_ := map:put($vars, xdmp:key-from-QName(xs:QName(\"");
        builder.append(key);
        builder.append("\")), ");
        builder.append(value);
        builder.append(")\n");
    }

    public void end(@NotNull StringBuilder builder) {
        builder.append("return $vars\n");
    }
}
