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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.api.http;

import junit.framework.TestCase;
import org.apache.http.Header;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import uk.co.reecedunn.intellij.plugin.marklogic.api.mime.MimeResponse;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class MimeResponseTest extends TestCase {
    private static ProtocolVersion HTTP1 = new ProtocolVersion("HTTP", 1, 0);
    private static StatusLine OK = new BasicStatusLine(HTTP1, 200, "OK");

    public void testEmptyResponse() {
        Header[] headers = new Header[] {
            new BasicHeader("Content-Length", "0")
        };
        String body = "";
        MimeResponse response = new MimeResponse(OK, headers, body);

        assertThat(response.getStatus(), is(OK));
        assertThat(response.getParts().length, is(1));
        assertThat(response.getHeader("Content-Length"), is("0"));
        assertThat(response.getHeader("Content-Type"), is(nullValue()));

        assertThat(response.getParts()[0].getHeader("Content-Length"), is("0"));
        assertThat(response.getParts()[0].getHeader("Content-Type"), is(nullValue()));
        assertThat(response.getParts()[0].getHeader("X-Primitive"), is(nullValue()));
        assertThat(response.getParts()[0].getHeader("X-Path"), is(nullValue()));
        assertThat(response.getParts()[0].getBody(), is(""));
    }

    public void testSimpleMessage() {
        Header[] headers = new Header[] {
            new BasicHeader("Content-Length", "5"),
            new BasicHeader("Content-Type", "text/plain")
        };
        String body = "Hello";
        MimeResponse response = new MimeResponse(OK, headers, body);

        assertThat(response.getStatus(), is(OK));
        assertThat(response.getParts().length, is(1));
        assertThat(response.getHeader("Content-Length"), is("5"));
        assertThat(response.getHeader("Content-Type"), is("text/plain"));

        assertThat(response.getParts()[0].getHeader("Content-Length"), is("5"));
        assertThat(response.getParts()[0].getHeader("Content-Type"), is("text/plain"));
        assertThat(response.getParts()[0].getHeader("X-Primitive"), is(nullValue()));
        assertThat(response.getParts()[0].getHeader("X-Path"), is(nullValue()));
        assertThat(response.getParts()[0].getBody(), is(body));
    }

    public void testMultipartSinglePart() {
        Header[] headers = new Header[] {
            new BasicHeader("Content-Length", "98"),
            new BasicHeader("Content-Type", "multipart/mixed; boundary=212ab95a34643c9d")
        };
        String body =
            "\r\n" +
            "--212ab95a34643c9d\r\n" +
            "Content-Type: text/plain\r\n" +
            "X-Primitive: integer\r\n" +
            "\r\n" +
            "15\r\n" +
            "--212ab95a34643c9d--\r\n";
        MimeResponse response = new MimeResponse(OK, headers, body);

        assertThat(response.getStatus(), is(OK));
        assertThat(response.getParts().length, is(1));
        assertThat(response.getHeader("Content-Length"), is("98"));
        assertThat(response.getHeader("Content-Type"), is("multipart/mixed; boundary=212ab95a34643c9d"));

        assertThat(response.getParts()[0].getHeader("Content-Type"), is("text/plain"));
        assertThat(response.getParts()[0].getHeader("X-Primitive"), is("integer"));
        assertThat(response.getParts()[0].getHeader("X-Path"), is(nullValue()));
        assertThat(response.getParts()[0].getBody(), is("15"));
    }

    public void testMultipartMultipleParts() {
        Header[] headers = new Header[] {
            new BasicHeader("Content-Length", "205"),
            new BasicHeader("Content-Type", "multipart/mixed; boundary=47c813e0bbfa09d4")
        };
        String body =
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
            "--47c813e0bbfa09d4--\r\n";
        MimeResponse response = new MimeResponse(OK, headers, body);

        assertThat(response.getStatus(), is(OK));
        assertThat(response.getParts().length, is(2));
        assertThat(response.getHeader("Content-Length"), is("205"));
        assertThat(response.getHeader("Content-Type"), is("multipart/mixed; boundary=47c813e0bbfa09d4"));

        assertThat(response.getParts()[0].getHeader("Content-Type"), is("text/plain"));
        assertThat(response.getParts()[0].getHeader("X-Primitive"), is("integer"));
        assertThat(response.getParts()[0].getHeader("X-Path"), is(nullValue()));
        assertThat(response.getParts()[0].getBody(), is("1"));

        assertThat(response.getParts()[1].getHeader("Content-Type"), is("application/json"));
        assertThat(response.getParts()[1].getHeader("X-Primitive"), is("number-node()"));
        assertThat(response.getParts()[1].getHeader("X-Path"), is("number-node()"));
        assertThat(response.getParts()[1].getBody(), is("5"));
    }

    public void testMultipartWithBlankLineInBody() {
        String body =
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
            "--31c406fa29f12029--\r\n";
        Header[] headers = new Header[] {
            new BasicHeader("Content-Length", Integer.toString(body.length())),
            new BasicHeader("Content-Type", "multipart/mixed; boundary=31c406fa29f12029"),
            new BasicHeader("X-Content-Type", "text/turtle")
        };
        MimeResponse response = new MimeResponse(OK, headers, body);

        assertThat(response.getStatus(), is(OK));
        assertThat(response.getParts().length, is(1));
        assertThat(response.getHeader("Content-Length"), is("222"));
        assertThat(response.getHeader("Content-Type"), is("multipart/mixed; boundary=31c406fa29f12029"));

        final String part1 =
            "@prefix p0: <http://example.co.uk/test> .\r\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\r\n" +
            "\r\n" +
            "p0:case a skos:Concept .\r\n";
        assertThat(response.getParts()[0].getHeader("Content-Type"), is("text/plain"));
        assertThat(response.getParts()[0].getHeader("X-Primitive"), is("string"));
        assertThat(response.getParts()[0].getHeader("X-Path"), is(nullValue()));
        assertThat(response.getParts()[0].getBody(), is(part1));
    }
}
