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

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class MimeResponseTest extends TestCase {
    private static ProtocolVersion HTTP1 = new ProtocolVersion("HTTP", 1, 0);
    private static StatusLine OK = new BasicStatusLine(HTTP1, 200, "OK");

    public void testEmptyResponse() throws IOException {
        Header[] headers = new Header[] {
            new BasicHeader("Content-Length", "0")
        };
        String body = "";
        MimeResponse response = new MimeResponse(OK, headers, body);

        assertThat(response.getStatus(), is(OK));
        assertThat(response.getMessages().length, is(1));

        assertThat(response.getMessages()[0].getHeader("Content-Length"), is("0"));
        assertThat(response.getMessages()[0].getHeader("Content-Type"), is(nullValue()));
        assertThat(response.getMessages()[0].getBody(), is(""));
    }

    public void testSimpleMessage() throws IOException {
        Header[] headers = new Header[] {
            new BasicHeader("Content-Length", "5"),
            new BasicHeader("Content-Type", "text/plain")
        };
        String body = "Hello";
        MimeResponse response = new MimeResponse(OK, headers, body);

        assertThat(response.getStatus(), is(OK));
        assertThat(response.getMessages().length, is(1));

        assertThat(response.getMessages()[0].getHeader("Content-Length"), is("5"));
        assertThat(response.getMessages()[0].getHeader("Content-Type"), is("text/plain"));
        assertThat(response.getMessages()[0].getBody(), is(body));
    }
}
