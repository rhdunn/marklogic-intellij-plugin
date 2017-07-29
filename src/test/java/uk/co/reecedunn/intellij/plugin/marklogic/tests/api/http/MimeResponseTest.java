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
import uk.co.reecedunn.intellij.plugin.marklogic.api.Item;
import uk.co.reecedunn.intellij.plugin.marklogic.api.http.HttpResponse;
import uk.co.reecedunn.intellij.plugin.marklogic.tests.Query;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class HttpResponseTest extends TestCase {
    private static ProtocolVersion HTTP1 = new ProtocolVersion("HTTP", 1, 0);
    private static StatusLine OK = new BasicStatusLine(HTTP1, 200, "OK");

    @Query("<a/>/@*")
    public void testNoResults() throws IOException {
        Header[] headers = new Header[] {
            new BasicHeader("Content-Length", "0")
        };
        String body = "";
        HttpResponse response = new HttpResponse(OK, headers, body);

        Item[] items = response.getItems();
        response.close();

        assertThat(items.length, is(1));
        assertThat(items[0].getContent(), is("()"));
        assertThat(items[0].getContentType(), is("text/plain"));
        assertThat(items[0].getItemType(), is("empty-sequence()"));
    }
}
