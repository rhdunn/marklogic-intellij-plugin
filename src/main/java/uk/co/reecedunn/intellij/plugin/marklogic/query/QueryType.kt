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

data class QueryType(val mimeType: String, val defaultExtensions: Array<String>, val builder: QueryBuilder)

val XQUERY: QueryType = QueryType("application/xquery", arrayOf("xq", "xqy", "xquery", "xql", "xqu"), XQueryBuilder)

val JAVA_SCRIPT: QueryType = QueryType("application/javascript", arrayOf("js", "sjs"), JavaScriptBuilder)

val SQL: QueryType = QueryType("application/sql", arrayOf("sql"), SQLBuilder)

val SPARQL_QUERY: QueryType = QueryType("application/sparql-query", arrayOf("sparql", "rq"), SPARQLQueryBuilder)

val SPARQL_UPDATE: QueryType = QueryType("application/sparql-update", arrayOf("ru"), SPARQLUpdateBuilder)

val XSLT: QueryType = QueryType("application/xml", arrayOf("xsl", "xslt"), XSLTBuilder)

val QUERY_TYPES = listOf(
    XQUERY,
    JAVA_SCRIPT,
    SQL,
    SPARQL_QUERY,
    SPARQL_UPDATE,
    XSLT)
