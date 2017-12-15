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

// region Query Types

data class QueryType(val mimeType: String, val defaultExtensions: Array<String>)

val XQueryType: QueryType = QueryType("application/xquery", arrayOf("xq", "xqy", "xquery", "xql", "xqu"))

val JavaScriptType: QueryType = QueryType("application/javascript", arrayOf("js", "sjs"))

val SQLType: QueryType = QueryType("application/sql", arrayOf("sql"))

val SPARQLQueryType: QueryType = QueryType("application/sparql-query", arrayOf("sparql", "rq"))

val SPARQLUpdateType: QueryType = QueryType("application/sparql-update", arrayOf("ru"))

// endregion