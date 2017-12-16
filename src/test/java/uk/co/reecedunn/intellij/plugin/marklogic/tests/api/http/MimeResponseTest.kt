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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.api.http

import junit.framework.TestCase
import org.apache.http.Header
import org.apache.http.ProtocolVersion
import org.apache.http.StatusLine
import org.apache.http.message.BasicHeader
import org.apache.http.message.BasicStatusLine
import uk.co.reecedunn.intellij.plugin.marklogic.api.mime.MimeResponse

import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat

private val HTTP1 = ProtocolVersion("HTTP", 1, 0)
private val OK = BasicStatusLine(HTTP1, 200, "OK")

class MimeResponseTest : TestCase() {
    fun testEmptyResponse() {
        val headers = arrayOf<Header>(BasicHeader("Content-Length", "0"))
        val body = ""
        val response = MimeResponse(OK, headers, body)

        assertThat(response.status, `is`<StatusLine>(OK))
        assertThat(response.parts.size, `is`(1))
        assertThat<String>(response.getHeader("Content-Length"), `is`("0"))
        assertThat<String>(response.getHeader("Content-Type"), `is`(nullValue()))

        assertThat<String>(response.parts[0].getHeader("Content-Length"), `is`("0"))
        assertThat<String>(response.parts[0].getHeader("Content-Type"), `is`(nullValue()))
        assertThat<String>(response.parts[0].getHeader("X-Primitive"), `is`(nullValue()))
        assertThat<String>(response.parts[0].getHeader("X-Path"), `is`(nullValue()))
        assertThat(response.parts[0].body, `is`(""))
    }

    fun testSimpleMessage() {
        val headers = arrayOf<Header>(BasicHeader("Content-Length", "5"), BasicHeader("Content-Type", "text/plain"))
        val body = "Hello"
        val response = MimeResponse(OK, headers, body)

        assertThat(response.status, `is`<StatusLine>(OK))
        assertThat(response.parts.size, `is`(1))
        assertThat<String>(response.getHeader("Content-Length"), `is`("5"))
        assertThat<String>(response.getHeader("Content-Type"), `is`("text/plain"))

        assertThat<String>(response.parts[0].getHeader("Content-Length"), `is`("5"))
        assertThat<String>(response.parts[0].getHeader("Content-Type"), `is`("text/plain"))
        assertThat<String>(response.parts[0].getHeader("X-Primitive"), `is`(nullValue()))
        assertThat<String>(response.parts[0].getHeader("X-Path"), `is`(nullValue()))
        assertThat(response.parts[0].body, `is`(body))
    }

    fun testMultipartSinglePart() {
        val headers = arrayOf<Header>(BasicHeader("Content-Length", "98"), BasicHeader("Content-Type", "multipart/mixed; boundary=212ab95a34643c9d"))
        val body =
            "\r\n" +
            "--212ab95a34643c9d\r\n" +
            "Content-Type: text/plain\r\n" +
            "X-Primitive: integer\r\n" +
            "\r\n" +
            "15\r\n" +
            "--212ab95a34643c9d--\r\n"
        val response = MimeResponse(OK, headers, body)

        assertThat(response.status, `is`<StatusLine>(OK))
        assertThat(response.parts.size, `is`(1))
        assertThat<String>(response.getHeader("Content-Length"), `is`("98"))
        assertThat<String>(response.getHeader("Content-Type"), `is`("multipart/mixed; boundary=212ab95a34643c9d"))

        assertThat<String>(response.parts[0].getHeader("Content-Type"), `is`("text/plain"))
        assertThat<String>(response.parts[0].getHeader("X-Primitive"), `is`("integer"))
        assertThat<String>(response.parts[0].getHeader("X-Path"), `is`(nullValue()))
        assertThat(response.parts[0].body, `is`("15"))
    }

    fun testMultipartMultipleParts() {
        val headers = arrayOf<Header>(BasicHeader("Content-Length", "205"), BasicHeader("Content-Type", "multipart/mixed; boundary=47c813e0bbfa09d4"))
        val body =
            "\r\n" +
            "--47c813e0bbfa09d4\r\n" +
            "Content-Type: text/plain\r\n" +
            "X-Primitive: integer\r\n" +
            "\r\n" +
            "1\r\n" +
            "--47c813e0bbfa09d4\r\n" +
            "Content-Type: application/json\r\n" +
            "X-Primitive: number-node()\r\n" +
            "X-Path: number-node()\r\n" +
            "\r\n" +
            "5\r\n" +
            "--47c813e0bbfa09d4--\r\n"
        val response = MimeResponse(OK, headers, body)

        assertThat(response.status, `is`<StatusLine>(OK))
        assertThat(response.parts.size, `is`(2))
        assertThat<String>(response.getHeader("Content-Length"), `is`("205"))
        assertThat<String>(response.getHeader("Content-Type"), `is`("multipart/mixed; boundary=47c813e0bbfa09d4"))

        assertThat<String>(response.parts[0].getHeader("Content-Type"), `is`("text/plain"))
        assertThat<String>(response.parts[0].getHeader("X-Primitive"), `is`("integer"))
        assertThat<String>(response.parts[0].getHeader("X-Path"), `is`(nullValue()))
        assertThat(response.parts[0].body, `is`("1"))

        assertThat<String>(response.parts[1].getHeader("Content-Type"), `is`("application/json"))
        assertThat<String>(response.parts[1].getHeader("X-Primitive"), `is`("number-node()"))
        assertThat<String>(response.parts[1].getHeader("X-Path"), `is`("number-node()"))
        assertThat(response.parts[1].body, `is`("5"))
    }

    fun testMultipartWithBlankLineInBody() {
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
        val headers = arrayOf<Header>(BasicHeader("Content-Length", Integer.toString(body.length)), BasicHeader("Content-Type", "multipart/mixed; boundary=31c406fa29f12029"), BasicHeader("X-Content-Type", "text/turtle"))
        val response = MimeResponse(OK, headers, body)

        assertThat(response.status, `is`<StatusLine>(OK))
        assertThat(response.parts.size, `is`(1))
        assertThat<String>(response.getHeader("Content-Length"), `is`("222"))
        assertThat<String>(response.getHeader("Content-Type"), `is`("multipart/mixed; boundary=31c406fa29f12029"))

        val part1 =
            "@prefix p0: <http://example.co.uk/test> .\r\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\r\n" +
            "\r\n" +
            "p0:case a skos:Concept .\r\n"
        assertThat<String>(response.parts[0].getHeader("Content-Type"), `is`("text/plain"))
        assertThat<String>(response.parts[0].getHeader("X-Primitive"), `is`("string"))
        assertThat<String>(response.parts[0].getHeader("X-Path"), `is`(nullValue()))
        assertThat(response.parts[0].body, `is`(part1))
    }
}
