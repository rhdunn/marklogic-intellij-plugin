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
package uk.co.reecedunn.intellij.plugin.marklogic.api;

import org.jetbrains.annotations.NotNull;
import uk.co.reecedunn.intellij.plugin.marklogic.resources.MarkLogicBundle;

public enum RDFFormat {
    N3(MarkLogicBundle.message("format.n3"), "n3", "text/n3", "n3"),
    N_QUADS(MarkLogicBundle.message("format.n-quads"), "nquad", "application/n-quads", "nq"),
    N_TRIPLES(MarkLogicBundle.message("format.n-triples"), "ntriple", "application/n-triples", "nt"),
    RDF_JSON(MarkLogicBundle.message("format.rdf-json"), "rdfjson", "application/rdf+json", "rj"),
    RDF_XML(MarkLogicBundle.message("format.rdf-xml"), "rdfxml", "application/rdf+xml", "rdf"),
    SEM_TRIPLE(MarkLogicBundle.message("format.sem.triple"), "sem:triple", "application/xquery", "xq"),
    TRIG(MarkLogicBundle.message("format.trig"), "trig", "application/trig", "trig"),
    TRIPLES_XML(MarkLogicBundle.message("format.triples-xml"), "triplexml", "application/vnd.marklogic.triples+xml", "xml"),
    TURTLE(MarkLogicBundle.message("format.turtle"), "turtle", "text/turtle", "ttl");

    private final String name;
    private final String markLogicName;
    private final String contentType;
    private final String fileExtension;

    RDFFormat(String name, String markLogicName, String contentType, String fileExtension) {
        this.name = name;
        this.markLogicName = markLogicName;
        this.contentType = contentType;
        this.fileExtension = fileExtension;
    }

    public static RDFFormat parse(@NotNull String value) {
        if (value.equals("n3")) return N3;
        if (value.equals("nquad")) return N_QUADS;
        if (value.equals("ntriple")) return N_TRIPLES;
        if (value.equals("rdfjson")) return RDF_JSON;
        if (value.equals("rdfxml")) return RDF_XML;
        if (value.equals("sem:triple")) return SEM_TRIPLE;
        if (value.equals("trig")) return TRIG;
        if (value.equals("triplexml")) return TRIPLES_XML;
        if (value.equals("turtle")) return TURTLE;
        return null;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getMarkLogicName() {
        return markLogicName;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFileExtension() {
        return fileExtension;
    }
}
