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
package uk.co.reecedunn.intellij.plugin.marklogic.api

import uk.co.reecedunn.intellij.plugin.marklogic.ui.resources.MarkLogicBundle

enum class RDFFormat private constructor(private val displayName: String, val markLogicName: String, val contentType: String, val fileExtension: String) {
    N3(MarkLogicBundle.message("format.n3"), "n3", "text/n3", "n3"),
    N_QUADS(MarkLogicBundle.message("format.n-quads"), "nquad", "application/n-quads", "nq"),
    N_TRIPLES(MarkLogicBundle.message("format.n-triples"), "ntriple", "application/n-triples", "nt"),
    RDF_JSON(MarkLogicBundle.message("format.rdf-json"), "rdfjson", "application/rdf+json", "rj"),
    RDF_XML(MarkLogicBundle.message("format.rdf-xml"), "rdfxml", "application/rdf+xml", "rdf"),
    SEM_TRIPLE(MarkLogicBundle.message("format.sem.triple"), "sem:triple", "application/xquery", "xq"),
    TRIG(MarkLogicBundle.message("format.trig"), "trig", "application/trig", "trig"),
    TRIPLES_XML(MarkLogicBundle.message("format.triples-xml"), "triplexml", "application/vnd.marklogic.triples+xml", "xml"),
    TURTLE(MarkLogicBundle.message("format.turtle"), "turtle", "text/turtle", "ttl");

    override fun toString(): String =
        displayName

    companion object {
        fun parse(value: String): RDFFormat? {
            if (value == "n3") return N3
            if (value == "nquad") return N_QUADS
            if (value == "ntriple") return N_TRIPLES
            if (value == "rdfjson") return RDF_JSON
            if (value == "rdfxml") return RDF_XML
            if (value == "sem:triple") return SEM_TRIPLE
            if (value == "trig") return TRIG
            if (value == "triplexml") return TRIPLES_XML
            if (value == "turtle") return TURTLE
            return null
        }
    }
}
