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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.api.rest;

import junit.framework.TestCase;
import org.apache.http.Header;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicStatusLine;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Item;
import uk.co.reecedunn.intellij.plugin.marklogic.api.mime.MimeResponse;
import uk.co.reecedunn.intellij.plugin.marklogic.api.rest.RestResponse;
import uk.co.reecedunn.intellij.plugin.marklogic.tests.Query;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class RestResponseTest extends TestCase {
    private static ProtocolVersion HTTP1 = new ProtocolVersion("HTTP", 1, 0);
    private static StatusLine OK = new BasicStatusLine(HTTP1, 200, "OK");

    @Query("()")
    public void testNoResults() throws IOException {
        String body =
            "";
        Header[] headers = new Header[] {
            new BasicHeader("Content-Length", Integer.toString(body.length()))
        };

        RestResponse response = new RestResponse(new MimeResponse(OK, headers, body));
        Item[] items = response.getItems();
        response.close();

        assertThat(items.length, is(1));

        assertThat(items[0].getContent(), is("()"));
        assertThat(items[0].getContentType(), is("text/plain"));
        assertThat(items[0].getItemType(), is("empty-sequence()"));
    }

    @Query("15")
    public void testSingleItem() throws IOException {
        String body =
            "\r\n" +
            "--10969f16ad1aac04\r\n" +
            "Content-Type: text/plain\r\n" +
            "X-Primitive: integer\r\n" +
            "\r\n" +
            "15\r\n" +
            "--10969f16ad1aac04--\r\n";
        Header[] headers = new Header[] {
            new BasicHeader("Content-Length", Integer.toString(body.length())),
            new BasicHeader("Content-Type", "multipart/mixed; boundary=10969f16ad1aac04")
        };

        RestResponse response = new RestResponse(new MimeResponse(OK, headers, body));
        Item[] items = response.getItems();
        response.close();

        assertThat(items.length, is(1));

        assertThat(items[0].getContent(), is("15"));
        assertThat(items[0].getContentType(), is("text/plain"));
        assertThat(items[0].getItemType(), is("integer"));
    }

    @Query("(1, 5)")
    public void testMultipleItems() throws IOException {
        String body =
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
            "--3cbb993599426124--\r\n";
        Header[] headers = new Header[] {
            new BasicHeader("Content-Length", Integer.toString(body.length())),
            new BasicHeader("Content-Type", "multipart/mixed; boundary=3cbb993599426124")
        };

        RestResponse response = new RestResponse(new MimeResponse(OK, headers, body));
        Item[] items = response.getItems();
        response.close();

        assertThat(items.length, is(2));

        assertThat(items[0].getContent(), is("1"));
        assertThat(items[0].getContentType(), is("text/plain"));
        assertThat(items[0].getItemType(), is("integer"));

        assertThat(items[1].getContent(), is("5"));
        assertThat(items[1].getContentType(), is("text/plain"));
        assertThat(items[1].getItemType(), is("integer"));
    }

    @Query("DESCRIBE <>")
    public void testRdfTripleFormat() throws IOException {
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

        RestResponse response = new RestResponse(new MimeResponse(OK, headers, body));
        Item[] items = response.getItems();
        response.close();

        assertThat(items.length, is(1));

        final String triples =
            "@prefix p0: <http://example.co.uk/test> .\r\n" +
            "@prefix skos: <http://www.w3.org/2004/02/skos/core#> .\r\n" +
            "\r\n" +
            "p0:case a skos:Concept .\r\n";
        assertThat(items[0].getContent(), is(triples));
        assertThat(items[0].getContentType(), is("text/turtle"));
        assertThat(items[0].getItemType(), is("string"));
    }
}
