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

import org.apache.commons.compress.utils.IOUtils

// region Server Queries

val MARKLOGIC_VERSION_XQUERY = MarkLogicQuery("queries/server/marklogic-version.xqy")

val LIST_APPSERVERS_XQUERY = MarkLogicQuery("queries/server/list-appservers.xqy")

val LIST_DATABASES_XQUERY = MarkLogicQuery("queries/server/list-databases.xqy")

// endregion
// region Run Configuration Queries

val RUN_QUERY = MarkLogicQuery("queries/run/run-query.xqy")

val RUN_QUERY_AS_RDF = MarkLogicQuery("queries/run/run-query-as-rdf.xqy")

// endregion

class MarkLogicQuery(val path: String) {
    val query: String

    init {
        val loader = this::class.java.classLoader
        val data = loader.getResourceAsStream(path)
        query = String(IOUtils.toByteArray(data), Charsets.UTF_8)
    }
}
