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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.api.rest

import junit.framework.TestCase
import org.apache.http.Header
import org.apache.http.ProtocolVersion
import org.apache.http.message.BasicHeader
import org.apache.http.message.BasicStatusLine
import uk.co.reecedunn.intellij.plugin.marklogic.api.mime.MimeResponse
import uk.co.reecedunn.intellij.plugin.marklogic.api.rest.RestResponse
import uk.co.reecedunn.intellij.plugin.marklogic.tests.Query

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat

private val HTTP1 = ProtocolVersion("HTTP", 1, 0)
private val OK = BasicStatusLine(HTTP1, 200, "OK")

class RestResponseTest : TestCase() {
    @Query("()")
    fun testNoResults() {
        val body = ""
        val headers = arrayOf<Header>(
            BasicHeader("Content-Length", Integer.toString(body.length)))

        val response = RestResponse(MimeResponse(OK, headers, body))
        val items = response.items
        response.close()

        assertThat(items.size, `is`(1))

        assertThat(items[0].content, `is`("()"))
        assertThat<String>(items[0].contentType, `is`("text/plain"))
        assertThat(items[0].itemType, `is`("empty-sequence()"))
    }

    @Query("15")
    fun testSingleItem() {
        val body =
            "\r\n" +
            "--10969f16ad1aac04\r\n" +
            "Content-Type: text/plain\r\n" +
            "X-Primitive: integer\r\n" +
            "\r\n" +
            "15\r\n" +
            "--10969f16ad1aac04--\r\n"
        val headers = arrayOf<Header>(
            BasicHeader("Content-Length", Integer.toString(body.length)),
            BasicHeader("Content-Type", "multipart/mixed; boundary=10969f16ad1aac04"))

        val response = RestResponse(MimeResponse(OK, headers, body))
        val items = response.items
        response.close()

        assertThat(items.size, `is`(1))

        assertThat(items[0].content, `is`("15"))
        assertThat<String>(items[0].contentType, `is`("text/plain"))
        assertThat(items[0].itemType, `is`("xs:integer"))
    }

    @Query("(1, 5)")
    fun testMultipleItems() {
        val body =
            "\r\n" +
            "--3cbb993599426124\r\n" +
            "Content-Type: text/plain\r\n" +
            "X-Primitive: integer\r\n" +
            "\r\n" +
            "1\r\n" +
            "--3cbb993599426124\r\n" +
            "Content-Type: text/plain\r\n" +
            "X-Primitive: integer\r\n" +
            "\r\n" +
            "5\r\n" +
            "--3cbb993599426124--\r\n"
        val headers = arrayOf<Header>(
            BasicHeader("Content-Length", Integer.toString(body.length)),
            BasicHeader("Content-Type", "multipart/mixed; boundary=3cbb993599426124"))

        val response = RestResponse(MimeResponse(OK, headers, body))
        val items = response.items
        response.close()

        assertThat(items.size, `is`(2))

        assertThat(items[0].content, `is`("1"))
        assertThat<String>(items[0].contentType, `is`("text/plain"))
        assertThat(items[0].itemType, `is`("xs:integer"))

        assertThat(items[1].content, `is`("5"))
        assertThat<String>(items[1].contentType, `is`("text/plain"))
        assertThat(items[1].itemType, `is`("xs:integer"))
    }

    @Query("DESCRIBE <>")
    fun testRdfTripleFormat() {
        val body =
            "\r\n" +
            "--31c406fa29f12029\r\n" +
            "Content-Type: text/plain\r\n" +
            "X-Primitive: string\r\n" +
            "\r\n" +
            "@prefix p0: <http://example.co.uk/test> .\r\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\r\n" +
            "\r\n" +
            "p0:case a skos:Concept .\r\n" +
            "\r\n" +
            "--31c406fa29f12029--\r\n"
        val headers = arrayOf<Header>(
            BasicHeader("Content-Length", Integer.toString(body.length)),
            BasicHeader("Content-Type", "multipart/mixed; boundary=31c406fa29f12029"),
            // NOTE: The run-query-as-rdf.xqy script adds this 'X-Content-Type'
            // header, since MarkLogic does not report the correct content type
            // for the sem:rdf-serialize output, and setting the 'Content-Type'
            // header overrides the above header, not the header of the part
            // containing the RDF.
            BasicHeader("X-Content-Type", "text/turtle"))

        val response = RestResponse(MimeResponse(OK, headers, body))
        val items = response.items
        response.close()

        assertThat(items.size, `is`(1))

        val triples =
            "@prefix p0: <http://example.co.uk/test> .\r\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\r\n" +
            "\r\n" +
            "p0:case a skos:Concept .\r\n"
        assertThat(items[0].content, `is`(triples))
        assertThat<String>(items[0].contentType, `is`("text/turtle"))
        assertThat(items[0].itemType, `is`("xs:string"))
    }
}
