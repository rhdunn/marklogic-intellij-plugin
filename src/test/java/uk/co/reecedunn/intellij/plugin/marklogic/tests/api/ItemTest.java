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
    @Query("<a/>/@*")
    public void testNoResults() {
        // NOTE: XCC does not return any results in this case.

        Item rest = new Item("", "application/octet-stream", null);
        assertThat(rest.getContent(), is(""));
        assertThat(rest.getContentType(), is("application/octet-stream"));
        assertThat(rest.getItemType(), is(nullValue()));
    }

    // region Lexical Types

    @Query("()")
    public void testEmptySequence() {
        Item xcc = new Item("()", "empty-sequence()");
        assertThat(xcc.getContent(), is("()"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("empty-sequence()"));

        Item rest = new Item("()", "text/plain", "empty-sequence()");
        assertThat(rest.getContent(), is("()"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("empty-sequence()"));
    }

    @Query("1.5")
    public void testXsDecimal() {
        Item xcc = new Item("1.5", "xs:decimal");
        assertThat(xcc.getContent(), is("1.5"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("xs:decimal"));

        Item rest = new Item("1.5", "text/plain", "decimal");
        assertThat(rest.getContent(), is("1.5"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("decimal"));
    }

    @Query("1e5")
    public void testXsDouble() {
        Item xcc = new Item("1e5", "xs:double");
        assertThat(xcc.getContent(), is("1e5"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("xs:double"));

        Item rest = new Item("1e5", "text/plain", "double");
        assertThat(rest.getContent(), is("1e5"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("double"));
    }

    @Query("15")
    public void testXsInteger() {
        Item xcc = new Item("15", "xs:integer");
        assertThat(xcc.getContent(), is("15"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("xs:integer"));

        Item rest = new Item("15", "text/plain", "integer");
        assertThat(rest.getContent(), is("15"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("integer"));
    }

    @Query("'abc'")
    public void testXsString() {
        Item xcc = new Item("abc", "xs:string");
        assertThat(xcc.getContent(), is("abc"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("xs:string"));

        Item rest = new Item("abc", "text/plain", "string");
        assertThat(rest.getContent(), is("abc"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("string"));
    }

    // endregion
    // region Node Construction

    @Query("array-node { 1, 2 }")
    public void testArrayNode() {
        Item xcc = new Item("[1, 2]", "array-node()");
        assertThat(xcc.getContent(), is("[1, 2]"));
        assertThat(xcc.getContentType(), is("application/json"));
        assertThat(xcc.getItemType(), is("array-node()"));

        Item rest = new Item("[1, 2]", "application/json", "array-node()");
        assertThat(rest.getContent(), is("[1, 2]"));
        assertThat(rest.getContentType(), is("application/json"));
        assertThat(rest.getItemType(), is("array-node()"));
    }

    @Query("<a b='c'/>/@*")
    public void testAttribute() {
        Item xcc = new Item("c", "attribute()");
        assertThat(xcc.getContent(), is("c"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("attribute()"));

        Item rest = new Item("c", "text/plain", "attribute()");
        assertThat(rest.getContent(), is("c"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("attribute()"));
    }

    @Query("binary { 'ab' }")
    public void testBinary() {
        Item xcc = new Item("«", "binary()");
        assertThat(xcc.getContent(), is("«"));
        assertThat(xcc.getContentType(), is("application/octet-stream"));
        assertThat(xcc.getItemType(), is("binary()"));

        Item rest = new Item("«", "application/x-unknown-content-type", "binary()");
        assertThat(rest.getContent(), is("«"));
        assertThat(rest.getContentType(), is("application/octet-stream"));
        assertThat(rest.getItemType(), is("binary()"));
    }

    @Query("boolean-node { true() }")
    public void testBooleanNode() {
        Item xcc = new Item("true", "boolean-node()");
        assertThat(xcc.getContent(), is("true"));
        assertThat(xcc.getContentType(), is("application/json"));
        assertThat(xcc.getItemType(), is("boolean-node()"));

        Item rest = new Item("true", "application/json", "boolean-node()");
        assertThat(rest.getContent(), is("true"));
        assertThat(rest.getContentType(), is("application/json"));
        assertThat(rest.getItemType(), is("boolean-node()"));
    }

    @Query("<!-- a -->")
    public void testComment() {
        Item xcc = new Item("<!-- a -->", "comment()");
        assertThat(xcc.getContent(), is("<!-- a -->"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("comment()"));

        Item rest = new Item("<!-- a -->", "text/plain", "comment()");
        assertThat(rest.getContent(), is("<!-- a -->"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("comment()"));
    }

    @Query("document { <a/> }")
    public void testDocument() {
        Item xcc = new Item("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a/>", "document-node()");
        assertThat(xcc.getContent(), is("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a/>"));
        assertThat(xcc.getContentType(), is("application/xml"));
        assertThat(xcc.getItemType(), is("document-node()"));

        Item rest = new Item("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a/>", "application/xml", "document-node()");
        assertThat(rest.getContent(), is("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a/>"));
        assertThat(rest.getContentType(), is("application/xml"));
        assertThat(rest.getItemType(), is("document-node()"));
    }

    @Query("<a/>")
    public void testElement() {
        Item xcc = new Item("<a/>", "element()");
        assertThat(xcc.getContent(), is("<a/>"));
        assertThat(xcc.getContentType(), is("application/xml"));
        assertThat(xcc.getItemType(), is("element()"));

        Item rest = new Item("<a/>", "application/xml", "element()");
        assertThat(rest.getContent(), is("<a/>"));
        assertThat(rest.getContentType(), is("application/xml"));
        assertThat(rest.getItemType(), is("element()"));
    }

    @Query("null-node {}")
    public void testNullNode() {
        Item xcc = new Item("null", "null-node()");
        assertThat(xcc.getContent(), is("null"));
        assertThat(xcc.getContentType(), is("application/json"));
        assertThat(xcc.getItemType(), is("null-node()"));

        Item rest = new Item("null", "application/json", "null-node()");
        assertThat(rest.getContent(), is("null"));
        assertThat(rest.getContentType(), is("application/json"));
        assertThat(rest.getItemType(), is("null-node()"));
    }

    @Query("number-node { 2 }")
    public void testNumberNode() {
        Item xcc = new Item("2", "number-node()");
        assertThat(xcc.getContent(), is("2"));
        assertThat(xcc.getContentType(), is("application/json"));
        assertThat(xcc.getItemType(), is("number-node()"));

        Item rest = new Item("2", "application/json", "number-node()");
        assertThat(rest.getContent(), is("2"));
        assertThat(rest.getContentType(), is("application/json"));
        assertThat(rest.getItemType(), is("number-node()"));
    }

    @Query("object-node { 1: 2 }")
    public void testObjectNode() {
        Item xcc = new Item("{\"1\": 2}", "object-node()");
        assertThat(xcc.getContent(), is("{\"1\": 2}"));
        assertThat(xcc.getContentType(), is("application/json"));
        assertThat(xcc.getItemType(), is("object-node()"));

        Item rest = new Item("{\"1\": 2}", "application/json", "object-node()");
        assertThat(rest.getContent(), is("{\"1\": 2}"));
        assertThat(rest.getContentType(), is("application/json"));
        assertThat(rest.getItemType(), is("object-node()"));
    }

    @Query("<?a?>")
    public void testProcessingInstruction() {
        Item xcc = new Item("<?a?>", "processing-instruction()");
        assertThat(xcc.getContent(), is("<?a?>"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("processing-instruction()"));

        Item rest = new Item("<?a?>", "text/plain", "processing-instruction()");
        assertThat(rest.getContent(), is("<?a?>"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("processing-instruction()"));
    }

    @Query("<a>abc</a>/text()")
    public void testText() {
        Item xcc = new Item("abc", "text()");
        assertThat(xcc.getContent(), is("abc"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("text()"));

        Item rest = new Item("abc", "text/plain", "text()");
        assertThat(rest.getContent(), is("abc"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("text()"));
    }

    // endregion
    // region Function Return Types

    @Query("true()")
    public void testXsBoolean() {
        Item xcc = new Item("true", "xs:boolean");
        assertThat(xcc.getContent(), is("true"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("xs:boolean"));

        Item rest = new Item("true", "text/plain", "boolean");
        assertThat(rest.getContent(), is("true"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("boolean"));
    }

    @Query("map:map()")
    public void testMapMap() {
        // NOTE: Although this is JSON data, the REST API does not give enough
        // information to identify it as JSON, and the XCC API does not identify
        // it as a map:map.

        Item xcc = new Item("{}", "json:object");
        assertThat(xcc.getContent(), is("{}"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("json:object"));

        Item rest = new Item("{}", "text/plain", "map");
        assertThat(rest.getContent(), is("{}"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("map"));
    }

    // endregion
    // region Type Constructors

    @Query("xs:float(1.5)")
    public void testXsFloat() {
        Item xcc = new Item("1.5", "xs:float");
        assertThat(xcc.getContent(), is("1.5"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("xs:float"));

        Item rest = new Item("1.5", "text/plain", "float");
        assertThat(rest.getContent(), is("1.5"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("float"));
    }

    @Query("xs:yearMonthDuration('P2Y')")
    public void testXsYearMonthDuration() {
        Item xcc = new Item("P2Y", "xs:yearMonthDuration");
        assertThat(xcc.getContent(), is("P2Y"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getItemType(), is("xs:yearMonthDuration"));

        Item rest = new Item("P2Y", "text/plain", "yearMonthDuration");
        assertThat(rest.getContent(), is("P2Y"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getItemType(), is("yearMonthDuration"));
    }

    // endregion
}
