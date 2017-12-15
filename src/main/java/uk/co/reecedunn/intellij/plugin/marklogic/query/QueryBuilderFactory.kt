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

import com.intellij.util.ArrayUtil
import com.intellij.util.PathUtil

object QueryBuilderFactory {
    val EXTENSIONS = arrayOf("xq", "xqy", "xquery", "xql", "xqu", "js", "sjs", "sql", "sparql", "rq", "ru", "xsl", "xslt")

    private val BUILDERS = arrayOf(XQueryBuilder.INSTANCE, XQueryBuilder.INSTANCE, XQueryBuilder.INSTANCE, XQueryBuilder.INSTANCE, XQueryBuilder.INSTANCE, JavaScriptBuilder.INSTANCE, JavaScriptBuilder.INSTANCE, SQLBuilder.INSTANCE, SPARQLQueryBuilder.INSTANCE, SPARQLQueryBuilder.INSTANCE, SPARQLUpdateBuilder.INSTANCE, XSLTBuilder.INSTANCE, XSLTBuilder.INSTANCE)

    fun createQueryBuilderForFile(filename: String): QueryBuilder? {
        val ext = PathUtil.getFileExtension(filename)
        val index = ArrayUtil.indexOf(EXTENSIONS, ext)
        return if (index < 0) {
            null
        } else {
            BUILDERS[index]
        }

    }
}
