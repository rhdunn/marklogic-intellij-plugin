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

data class QueryType(val mimeType: String, val defaultExtensions: List<String>, val builder: QueryBuilder)

val XQUERY: QueryType = QueryType("application/xquery", listOf("xq", "xqy", "xquery", "xql", "xqu"), XQueryBuilder)

val JAVA_SCRIPT: QueryType = QueryType("application/javascript", listOf("js", "sjs"), JavaScriptBuilder)

val SQL: QueryType = QueryType("application/sql", listOf("sql"), SQLBuilder)

val SPARQL_QUERY: QueryType = QueryType("application/sparql-query", listOf("sparql", "rq"), SPARQLQueryBuilder)

val SPARQL_UPDATE: QueryType = QueryType("application/sparql-update", listOf("ru"), SPARQLUpdateBuilder)

val XSLT: QueryType = QueryType("application/xml", listOf("xsl", "xslt"), XSLTBuilder)

val QUERY_TYPES = listOf(
    XQUERY,
    JAVA_SCRIPT,
    SQL,
    SPARQL_QUERY,
    SPARQL_UPDATE,
    XSLT)

val QUERY_EXTENSIONS get(): List<String> =
    // XSLT is not fully supported, so exclude from the list of supported extensions.
    QUERY_TYPES.filter { type -> type !== XSLT }.flatMap { type -> type.defaultExtensions }
