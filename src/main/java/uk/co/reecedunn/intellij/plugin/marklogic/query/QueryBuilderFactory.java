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

import com.intellij.util.ArrayUtil;
import com.intellij.util.PathUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class QueryBuilderFactory {
    public static final String[] EXTENSIONS = new String[]{
        "xq", "xqy", "xquery", "xql", "xqu",
        "js", "sjs",
        "sql",
        "sparql", "rq",
        "ru",
        "xsl", "xslt",
    };

    private static final QueryBuilder[] BUILDERS = new QueryBuilder[] {
        XQueryBuilder.INSTANCE, XQueryBuilder.INSTANCE, XQueryBuilder.INSTANCE, XQueryBuilder.INSTANCE, XQueryBuilder.INSTANCE,
        JavaScriptBuilder.INSTANCE, JavaScriptBuilder.INSTANCE,
        SQLBuilder.INSTANCE,
        SPARQLQueryBuilder.INSTANCE, SPARQLQueryBuilder.INSTANCE,
        SPARQLUpdateBuilder.INSTANCE,
        XSLTBuilder.INSTANCE, XSLTBuilder.INSTANCE,
    };

    @Nullable
    public static QueryBuilder createQueryBuilderForFile(@NotNull String filename) {
        final String ext = PathUtil.getFileExtension(filename);
        final int index = ArrayUtil.indexOf(EXTENSIONS, ext);
        if (index < 0) {
            return null;
        }

        return BUILDERS[index];
    }
}
