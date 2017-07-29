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
package uk.co.reecedunn.intellij.plugin.marklogic.api.mime;

import org.apache.http.Header;
import org.apache.http.StatusLine;
import org.apache.http.message.BasicHeader;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MimeResponse {
    private final StatusLine status;
    private final Message message;
    private final Message[] parts;

    public MimeResponse(@NotNull StatusLine status, @NotNull Header[] headers, @NotNull String body) {
        this.status = status;
        this.message = new Message(headers, body);

        List<Message> messages = new ArrayList<>();
        final String contentType = message.getHeader("Content-Type");
        if (contentType != null && contentType.startsWith("multipart/mixed; boundary=")) {
            for (String part : body.split("\r\n--" + contentType.split("boundary=")[1])) {
                if (part.isEmpty() || part.equals("--\r\n")) {
                    continue;
                }

                String[] headersContent = part.split("\r\n\r\n", 2);
                messages.add(new Message(parseHeaders(headersContent[0]), headersContent[1]));
            }
        } else {
            messages.add(message);
        }
        this.parts = messages.toArray(new Message[messages.size()]);
    }

    public StatusLine getStatus() {
        return status;
    }

    public String getHeader(String header) {
        return message.getHeader(header);
    }

    public Message[] getParts() {
        return parts;
    }

    private Header[] parseHeaders(String content) {
        List<Header> headers = new ArrayList<>();
        for (String header : content.split("\r\n")) {
            if (header.isEmpty()) {
                continue;
            }

            String[] nameValue = header.split(":\\s+");
            headers.add(new BasicHeader(nameValue[0], nameValue[1]));
        }
        return headers.toArray(new Header[headers.size()]);
    }
}
