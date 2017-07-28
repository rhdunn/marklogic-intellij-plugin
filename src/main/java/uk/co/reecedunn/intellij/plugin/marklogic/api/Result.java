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

import com.intellij.util.ArrayUtil;

public class Result {
    private static String[] BINARY_ITEM_TYPES = new String[] { "binary()" };
    private static String[] JSON_ITEM_TYPES = new String[] { "array-node()", "boolean-node()", "null-node()", "number-node()", "object-node()" };
    private static String[] XML_ITEM_TYPES = new String[] { "document-node()", "element()" };

    private String content;
    private String contentType;
    private String primitive;

    public Result(String content, String contentType, String primitive) {
        this.content = content;
        this.contentType = contentType;
        this.primitive = primitive;
    }

    public Result(String content, String itemType) {
        this(content, getContentTypeForItemType(itemType), itemType);
    }

    public String getContent() {
        return this.content;
    }

    public String getContentType() {
        return this.contentType;
    }

    public String getPrimitive() {
        return this.primitive;
    }

    private static String getContentTypeForItemType(String itemType) {
        if (itemType == null) {
            return null;
        }
        if (ArrayUtil.contains(itemType, BINARY_ITEM_TYPES)) {
            return "application/x-unknown-content-type";
        }
        if (ArrayUtil.contains(itemType, JSON_ITEM_TYPES)) {
            return "application/json";
        }
        if (ArrayUtil.contains(itemType, XML_ITEM_TYPES)) {
            return "application/xml";
        }
        return "text/plain";
    }
}
