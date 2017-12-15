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
package uk.co.reecedunn.intellij.plugin.marklogic.ui.configuration;

import com.intellij.util.xmlb.Converter;
import com.intellij.util.xmlb.annotations.OptionTag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import uk.co.reecedunn.compat.intellij.execution.configurations.RunConfigurationOptions;
import uk.co.reecedunn.intellij.plugin.marklogic.api.RDFFormatKt;
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersion;
import uk.co.reecedunn.intellij.plugin.marklogic.server.MarkLogicVersionKt;

public class MarkLogicRunConfigurationData extends RunConfigurationOptions {
    public static class MarkLogicVersionConverter extends Converter<MarkLogicVersion> {
        @Nullable
        @Override
        public MarkLogicVersion fromString(@NotNull String version) {
            return MarkLogicVersion.Companion.parse(version);
        }

        @NotNull
        @Override
        public String toString(@NotNull MarkLogicVersion version) {
            return version.toString();
        }
    }

    public String serverHost = "localhost";

    @OptionTag(converter = MarkLogicVersionConverter.class)
    public MarkLogicVersion markLogicVersion = MarkLogicVersionKt.getMARKLOGIC_7();

    public String contentDatabase = null;

    public String moduleDatabase = null;

    public String moduleRoot = "/";

    public String mainModulePath = "";

    public String tripleFormat = RDFFormatKt.getSEM_TRIPLE().getMarkLogicName();
}
