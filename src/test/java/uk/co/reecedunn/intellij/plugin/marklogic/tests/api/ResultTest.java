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
import uk.co.reecedunn.intellij.plugin.marklogic.api.Result;
import uk.co.reecedunn.intellij.plugin.marklogic.tests.Query;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ResultTest extends TestCase {
    @Query("<a/>/@*")
    public void testNoResults() {
        // NOTE: XCC does not return any results in this case.

        Result rest = new Result("", "application/octet-stream", null);
        assertThat(rest.getContent(), is(""));
        assertThat(rest.getContentType(), is("application/octet-stream"));
        assertThat(rest.getPrimitive(), is(nullValue()));
    }

    // region Lexical Types

    @Query("1.5")
    public void testXsDecimal() {
        Result xcc = new Result("1.5", "xs:decimal");
        assertThat(xcc.getContent(), is("1.5"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getPrimitive(), is("xs:decimal"));

        Result rest = new Result("1.5", "text/plain", "decimal");
        assertThat(rest.getContent(), is("1.5"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getPrimitive(), is("decimal"));
    }

    @Query("1e5")
    public void testXsDouble() {
        Result xcc = new Result("1e5", "xs:double");
        assertThat(xcc.getContent(), is("1e5"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getPrimitive(), is("xs:double"));

        Result rest = new Result("1e5", "text/plain", "double");
        assertThat(rest.getContent(), is("1e5"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getPrimitive(), is("double"));
    }

    @Query("15")
    public void testXsInteger() {
        Result xcc = new Result("15", "xs:integer");
        assertThat(xcc.getContent(), is("15"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getPrimitive(), is("xs:integer"));

        Result rest = new Result("15", "text/plain", "integer");
        assertThat(rest.getContent(), is("15"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getPrimitive(), is("integer"));
    }

    @Query("'abc'")
    public void testXsString() {
        Result xcc = new Result("abc", "xs:string");
        assertThat(xcc.getContent(), is("abc"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getPrimitive(), is("xs:string"));

        Result rest = new Result("abc", "text/plain", "string");
        assertThat(rest.getContent(), is("abc"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getPrimitive(), is("string"));
    }

    // endregion
    // region Node Construction

    @Query("array-node { 1, 2 }")
    public void testArrayNode() {
        Result xcc = new Result("[1, 2]", "array-node()");
        assertThat(xcc.getContent(), is("[1, 2]"));
        assertThat(xcc.getContentType(), is("application/json"));
        assertThat(xcc.getPrimitive(), is("array-node()"));

        Result rest = new Result("[1, 2]", "application/json", "array-node()");
        assertThat(rest.getContent(), is("[1, 2]"));
        assertThat(rest.getContentType(), is("application/json"));
        assertThat(rest.getPrimitive(), is("array-node()"));
    }

    @Query("<a b='c'/>/@*")
    public void testAttribute() {
        Result xcc = new Result("c", "attribute()");
        assertThat(xcc.getContent(), is("c"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getPrimitive(), is("attribute()"));

        Result rest = new Result("c", "text/plain", "attribute()");
        assertThat(rest.getContent(), is("c"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getPrimitive(), is("attribute()"));
    }

    @Query("binary { 'ab' }")
    public void testBinary() {
        Result xcc = new Result("«", "binary()");
        assertThat(xcc.getContent(), is("«"));
        assertThat(xcc.getContentType(), is("application/x-unknown-content-type"));
        assertThat(xcc.getPrimitive(), is("binary()"));

        Result rest = new Result("«", "application/x-unknown-content-type", "binary()");
        assertThat(rest.getContent(), is("«"));
        assertThat(rest.getContentType(), is("application/x-unknown-content-type"));
        assertThat(rest.getPrimitive(), is("binary()"));
    }

    @Query("boolean-node { true() }")
    public void testBooleanNode() {
        Result xcc = new Result("true", "boolean-node()");
        assertThat(xcc.getContent(), is("true"));
        assertThat(xcc.getContentType(), is("application/json"));
        assertThat(xcc.getPrimitive(), is("boolean-node()"));

        Result rest = new Result("true", "application/json", "boolean-node()");
        assertThat(rest.getContent(), is("true"));
        assertThat(rest.getContentType(), is("application/json"));
        assertThat(rest.getPrimitive(), is("boolean-node()"));
    }

    @Query("<!-- a -->")
    public void testComment() {
        Result xcc = new Result("<!-- a -->", "comment()");
        assertThat(xcc.getContent(), is("<!-- a -->"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getPrimitive(), is("comment()"));

        Result rest = new Result("<!-- a -->", "text/plain", "comment()");
        assertThat(rest.getContent(), is("<!-- a -->"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getPrimitive(), is("comment()"));
    }

    @Query("document { <a/> }")
    public void testDocument() {
        Result xcc = new Result("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a/>", "document-node()");
        assertThat(xcc.getContent(), is("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a/>"));
        assertThat(xcc.getContentType(), is("application/xml"));
        assertThat(xcc.getPrimitive(), is("document-node()"));

        Result rest = new Result("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a/>", "application/xml", "document-node()");
        assertThat(rest.getContent(), is("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<a/>"));
        assertThat(rest.getContentType(), is("application/xml"));
        assertThat(rest.getPrimitive(), is("document-node()"));
    }

    @Query("<a/>")
    public void testElement() {
        Result xcc = new Result("<a/>", "element()");
        assertThat(xcc.getContent(), is("<a/>"));
        assertThat(xcc.getContentType(), is("application/xml"));
        assertThat(xcc.getPrimitive(), is("element()"));

        Result rest = new Result("<a/>", "application/xml", "element()");
        assertThat(rest.getContent(), is("<a/>"));
        assertThat(rest.getContentType(), is("application/xml"));
        assertThat(rest.getPrimitive(), is("element()"));
    }

    @Query("null-node {}")
    public void testNullNode() {
        Result xcc = new Result("null", "null-node()");
        assertThat(xcc.getContent(), is("null"));
        assertThat(xcc.getContentType(), is("application/json"));
        assertThat(xcc.getPrimitive(), is("null-node()"));

        Result rest = new Result("null", "application/json", "null-node()");
        assertThat(rest.getContent(), is("null"));
        assertThat(rest.getContentType(), is("application/json"));
        assertThat(rest.getPrimitive(), is("null-node()"));
    }

    @Query("number-node { 2 }")
    public void testNumberNode() {
        Result xcc = new Result("2", "number-node()");
        assertThat(xcc.getContent(), is("2"));
        assertThat(xcc.getContentType(), is("application/json"));
        assertThat(xcc.getPrimitive(), is("number-node()"));

        Result rest = new Result("2", "application/json", "number-node()");
        assertThat(rest.getContent(), is("2"));
        assertThat(rest.getContentType(), is("application/json"));
        assertThat(rest.getPrimitive(), is("number-node()"));
    }

    @Query("object-node { 1: 2 }")
    public void testObjectNode() {
        Result xcc = new Result("{\"1\": 2}", "object-node()");
        assertThat(xcc.getContent(), is("{\"1\": 2}"));
        assertThat(xcc.getContentType(), is("application/json"));
        assertThat(xcc.getPrimitive(), is("object-node()"));

        Result rest = new Result("{\"1\": 2}", "application/json", "object-node()");
        assertThat(rest.getContent(), is("{\"1\": 2}"));
        assertThat(rest.getContentType(), is("application/json"));
        assertThat(rest.getPrimitive(), is("object-node()"));
    }

    @Query("<?a?>")
    public void testProcessingInstruction() {
        Result xcc = new Result("<?a?>", "processing-instruction()");
        assertThat(xcc.getContent(), is("<?a?>"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getPrimitive(), is("processing-instruction()"));

        Result rest = new Result("<?a?>", "text/plain", "processing-instruction()");
        assertThat(rest.getContent(), is("<?a?>"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getPrimitive(), is("processing-instruction()"));
    }

    @Query("<a>abc</a>/text()")
    public void testText() {
        Result xcc = new Result("abc", "text()");
        assertThat(xcc.getContent(), is("abc"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getPrimitive(), is("text()"));

        Result rest = new Result("abc", "text/plain", "text()");
        assertThat(rest.getContent(), is("abc"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getPrimitive(), is("text()"));
    }

    // endregion
    // region Function Return Types

    @Query("true()")
    public void testXsBoolean() {
        Result xcc = new Result("true", "xs:boolean");
        assertThat(xcc.getContent(), is("true"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getPrimitive(), is("xs:boolean"));

        Result rest = new Result("true", "text/plain", "boolean");
        assertThat(rest.getContent(), is("true"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getPrimitive(), is("boolean"));
    }

    @Query("map:map()")
    public void testMapMap() {
        // NOTE: Although this is JSON data, the REST API does not give enough
        // information to identify it as JSON, and the XCC API does not identify
        // it as a map:map.

        Result xcc = new Result("{}", "json:object");
        assertThat(xcc.getContent(), is("{}"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getPrimitive(), is("json:object"));

        Result rest = new Result("{}", "text/plain", "map");
        assertThat(rest.getContent(), is("{}"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getPrimitive(), is("map"));
    }

    // endregion
    // region Type Constructors

    @Query("xs:float(1.5)")
    public void testXsFloat() {
        Result xcc = new Result("1.5", "xs:float");
        assertThat(xcc.getContent(), is("1.5"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getPrimitive(), is("xs:float"));

        Result rest = new Result("1.5", "text/plain", "float");
        assertThat(rest.getContent(), is("1.5"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getPrimitive(), is("float"));
    }

    @Query("xs:yearMonthDuration('P2Y')")
    public void testXsYearMonthDuration() {
        Result xcc = new Result("P2Y", "xs:yearMonthDuration");
        assertThat(xcc.getContent(), is("P2Y"));
        assertThat(xcc.getContentType(), is("text/plain"));
        assertThat(xcc.getPrimitive(), is("xs:yearMonthDuration"));

        Result rest = new Result("P2Y", "text/plain", "yearMonthDuration");
        assertThat(rest.getContent(), is("P2Y"));
        assertThat(rest.getContentType(), is("text/plain"));
        assertThat(rest.getPrimitive(), is("yearMonthDuration"));
    }

    // endregion
}
