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
package uk.co.reecedunn.intellij.plugin.marklogic.tests.query;

import uk.co.reecedunn.intellij.plugin.marklogic.configuration.MarkLogicRunConfiguration;
import uk.co.reecedunn.intellij.plugin.marklogic.query.Function;
import uk.co.reecedunn.intellij.plugin.marklogic.query.QueryBuilder;
import uk.co.reecedunn.intellij.plugin.marklogic.query.QueryBuilderFactory;
import uk.co.reecedunn.intellij.plugin.marklogic.tests.configuration.ConfigurationTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SuppressWarnings("ConstantConditions")
public class FunctionTest extends ConfigurationTestCase {
    public void testEval() {
        final String query = "(1, 2, 3)";
        final MarkLogicRunConfiguration configuration = createConfiguration();
        configuration.setMainModuleFile(createVirtualFile("test.xqy", query));

        QueryBuilder queryBuilder = QueryBuilderFactory.createQueryBuilderForFile(configuration.getMainModulePath());
        Function function = queryBuilder.createEvalBuilder(QueryBuilder.ExecMode.Run, 5.0);

        StringBuilder builder = new StringBuilder();
        function.buildQuery(builder, configuration);

        final String expected =
            "let $query := \"(1, 2, 3)\"\n" +
            "let $vars := ()\n" +
            "let $options := ()\n" +
            "return xdmp:eval($query, $vars, $options)\n";
        assertThat(builder.toString(), is(expected));
    }
}
