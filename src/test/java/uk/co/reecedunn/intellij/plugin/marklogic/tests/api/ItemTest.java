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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.api;

import junit.framework.TestCase;
import uk.co.reecedunn.intellij.plugin.marklogic.api.Item;
import uk.co.reecedunn.intellij.plugin.marklogic.tests.Query;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ItemTest extends TestCase {
    // region Lexical Types

    @Query("()")
    public void testEmptySequence() {
        Item xcc = Item.create("()", "empty-sequence()");
        assertThat(xcc.getContent(), is("()"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("empty-sequence()"));

        Item rest = Item.create("()", "text/plain", "empty-sequence()");
        assertThat(rest.getContent(), is("()"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("empty-sequence()"));
    }

    @Query("1.5")
    public void testXsDecimal() {
        Item xcc = Item.create("1.5", "xs:decimal");
        assertThat(xcc.getContent(), is("1.5"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("xs:decimal"));

        Item rest = Item.create("1.5", "text/plain", "decimal");
        assertThat(rest.getContent(), is("1.5"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("decimal"));
    }

    @Query("1e5")
    public void testXsDouble() {
        Item xcc = Item.create("1e5", "xs:double");
        assertThat(xcc.getContent(), is("1e5"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("xs:double"));

        Item rest = Item.create("1e5", "text/plain", "double");
        assertThat(rest.getContent(), is("1e5"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("double"));
    }

    @Query("15")
    public void testXsInteger() {
        Item xcc = Item.create("15", "xs:integer");
        assertThat(xcc.getContent(), is("15"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("xs:integer"));

        Item rest = Item.create("15", "text/plain", "integer");
        assertThat(rest.getContent(), is("15"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("integer"));
    }

    @Query("'abc'")
    public void testXsString() {
        Item xcc = Item.create("abc", "xs:string");
        assertThat(xcc.getContent(), is("abc"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("xs:string"));

        Item rest = Item.create("abc", "text/plain", "string");
        assertThat(rest.getContent(), is("abc"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("string"));
    }

    // endregion
    // region Node Construction

    @Query("array-node { 1, 2 }")
    public void testArrayNode() {
        Item xcc = Item.create("[1, 2]", "array-node()");
        assertThat(xcc.getContent(), is("[1, 2]"));
        assertThat(xcc.getContentType(), is("application/json"));
        assertThat(xcc.getItemType(), is("array-node()"));

        Item rest = Item.create("[1, 2]", "application/json", "array-node()");
        assertThat(rest.getContent(), is("[1, 2]"));
        assertThat(rest.getContentType(), is("application/json"));
        assertThat(rest.getItemType(), is("array-node()"));
    }

    @Query("<a b='c'/>/@*")
    public void testAttribute() {
        Item xcc = Item.create("c", "attribute()");
        assertThat(xcc.getContent(), is("c"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("attribute()"));

        Item rest = Item.create("c", "text/plain", "attribute()");
        assertThat(rest.getContent(), is("c"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("attribute()"));
    }

    @Query("binary { 'ab' }")
    public void testBinary() {
        Item xcc = Item.create("«", "binary()");
        assertThat(xcc.getContent(), is("«"));
        assertThat(xcc.getContentType(), is("application/octet-stream"));
        assertThat(xcc.getItemType(), is("binary()"));

        Item rest = Item.create("«", "application/x-unknown-content-type", "binary()");
        assertThat(rest.getContent(), is("«"));
        assertThat(rest.getContentType(), is("application/octet-stream"));
        assertThat(rest.getItemType(), is("binary()"));
    }

    @Query("boolean-node { true() }")
    public void testBooleanNode() {
        Item xcc = Item.create("true", "boolean-node()");
        assertThat(xcc.getContent(), is("true"));
        assertThat(xcc.getContentType(), is("application/json"));
        assertThat(xcc.getItemType(), is("boolean-node()"));

        Item rest = Item.create("true", "application/json", "boolean-node()");
        assertThat(rest.getContent(), is("true"));
        assertThat(rest.getContentType(), is("application/json"));
        assertThat(rest.getItemType(), is("boolean-node()"));
    }

    @Query("<!-- a -->")
    public void testComment() {
        Item xcc = Item.create("<!-- a -->", "comment()");
        assertThat(xcc.getContent(), is("<!-- a -->"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("comment()"));

        Item rest = Item.create("<!-- a -->", "text/plain", "comment()");
        assertThat(rest.getContent(), is("<!-- a -->"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("comment()"));
    }

    @Query("document { <a/> }")
    public void testDocument() {
        Item xcc = Item.create("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a/>", "document-node()");
        assertThat(xcc.getContent(), is("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a/>"));
        assertThat(xcc.getContentType(), is("application/xml"));
        assertThat(xcc.getItemType(), is("document-node()"));

        Item rest = Item.create("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a/>", "application/xml", "document-node()");
        assertThat(rest.getContent(), is("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a/>"));
        assertThat(rest.getContentType(), is("application/xml"));
        assertThat(rest.getItemType(), is("document-node()"));
    }

    @Query("<a/>")
    public void testElement() {
        Item xcc = Item.create("<a/>", "element()");
        assertThat(xcc.getContent(), is("<a/>"));
        assertThat(xcc.getContentType(), is("application/xml"));
        assertThat(xcc.getItemType(), is("element()"));

        Item rest = Item.create("<a/>", "application/xml", "element()");
        assertThat(rest.getContent(), is("<a/>"));
        assertThat(rest.getContentType(), is("application/xml"));
        assertThat(rest.getItemType(), is("element()"));
    }

    @Query("null-node {}")
    public void testNullNode() {
        Item xcc = Item.create("null", "null-node()");
        assertThat(xcc.getContent(), is("null"));
        assertThat(xcc.getContentType(), is("application/json"));
        assertThat(xcc.getItemType(), is("null-node()"));

        Item rest = Item.create("null", "application/json", "null-node()");
        assertThat(rest.getContent(), is("null"));
        assertThat(rest.getContentType(), is("application/json"));
        assertThat(rest.getItemType(), is("null-node()"));
    }

    @Query("number-node { 2 }")
    public void testNumberNode() {
        Item xcc = Item.create("2", "number-node()");
        assertThat(xcc.getContent(), is("2"));
        assertThat(xcc.getContentType(), is("application/json"));
        assertThat(xcc.getItemType(), is("number-node()"));

        Item rest = Item.create("2", "application/json", "number-node()");
        assertThat(rest.getContent(), is("2"));
        assertThat(rest.getContentType(), is("application/json"));
        assertThat(rest.getItemType(), is("number-node()"));
    }

    @Query("object-node { 1: 2 }")
    public void testObjectNode() {
        Item xcc = Item.create("{\"1\": 2}", "object-node()");
        assertThat(xcc.getContent(), is("{\"1\": 2}"));
        assertThat(xcc.getContentType(), is("application/json"));
        assertThat(xcc.getItemType(), is("object-node()"));

        Item rest = Item.create("{\"1\": 2}", "application/json", "object-node()");
        assertThat(rest.getContent(), is("{\"1\": 2}"));
        assertThat(rest.getContentType(), is("application/json"));
        assertThat(rest.getItemType(), is("object-node()"));
    }

    @Query("<?a?>")
    public void testProcessingInstruction() {
        Item xcc = Item.create("<?a?>", "processing-instruction()");
        assertThat(xcc.getContent(), is("<?a?>"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("processing-instruction()"));

        Item rest = Item.create("<?a?>", "text/plain", "processing-instruction()");
        assertThat(rest.getContent(), is("<?a?>"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("processing-instruction()"));
    }

    @Query("<a>abc</a>/text()")
    public void testText() {
        Item xcc = Item.create("abc", "text()");
        assertThat(xcc.getContent(), is("abc"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("text()"));

        Item rest = Item.create("abc", "text/plain", "text()");
        assertThat(rest.getContent(), is("abc"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("text()"));
    }

    // endregion
    // region Function Return Types

    @Query("true()")
    public void testXsBoolean() {
        Item xcc = Item.create("true", "xs:boolean");
        assertThat(xcc.getContent(), is("true"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("xs:boolean"));

        Item rest = Item.create("true", "text/plain", "boolean");
        assertThat(rest.getContent(), is("true"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("boolean"));
    }

    @Query("map:map()")
    public void testMapMap() {
        // NOTE: Although this is JSON data, the REST API does not give enough
        // information to identify it as JSON, and the XCC API does not identify
        // it as a map:map.

        Item xcc = Item.create("{}", "json:object");
        assertThat(xcc.getContent(), is("{}"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("json:object"));

        Item rest = Item.create("{}", "text/plain", "map");
        assertThat(rest.getContent(), is("{}"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("map"));
    }

    // endregion
    // region Type Constructors

    @Query("xs:float(1.5)")
    public void testXsFloat() {
        Item xcc = Item.create("1.5", "xs:float");
        assertThat(xcc.getContent(), is("1.5"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("xs:float"));

        Item rest = Item.create("1.5", "text/plain", "float");
        assertThat(rest.getContent(), is("1.5"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("float"));
    }

    @Query("xs:yearMonthDuration('P2Y')")
    public void testXsYearMonthDuration() {
        Item xcc = Item.create("P2Y", "xs:yearMonthDuration");
        assertThat(xcc.getContent(), is("P2Y"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("xs:yearMonthDuration"));

        Item rest = Item.create("P2Y", "text/plain", "yearMonthDuration");
        assertThat(rest.getContent(), is("P2Y"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("yearMonthDuration"));
    }

    // endregion
}
