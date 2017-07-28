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

public class Item {
    private static String[] BINARY_ITEM_TYPES = new String[] { "binary()" };
    private static String[] JSON_ITEM_TYPES = new String[] { "array-node()", "boolean-node()", "null-node()", "number-node()", "object-node()" };
    private static String[] XML_ITEM_TYPES = new String[] { "document-node()", "element()" };
    private static String UNKNOWN_CONTENT_TYPE = "application/x-unknown-content-type";

    private String content;
    private String contentType;
    private String itemType;

    public static Item create(String content, String contentType, String itemType) {
        return new Item(content, contentType, itemType);
    }

    public static Item create(String content, String itemType) {
        return new Item(content, getContentTypeForItemType(itemType), itemType);
    }

    private Item(String content, String contentType, String itemType) {
        this.content = content;
        if (UNKNOWN_CONTENT_TYPE.equals(contentType)) {
            this.contentType = "application/octet-stream";
        } else {
            this.contentType = contentType;
        }
        this.itemType = itemType;
    }

    public String getContent() {
        return this.content;
    }

    public String getContentType() {
        return this.contentType;
    }

    public String getItemType() {
        return this.itemType;
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
