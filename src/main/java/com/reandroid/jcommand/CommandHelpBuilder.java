/*
 *  Copyright (C) 2022 github.com/REAndroid
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.reandroid.jcommand;

import com.reandroid.jcommand.annotations.ChoiceArg;
import com.reandroid.jcommand.annotations.CommandOptions;
import com.reandroid.jcommand.annotations.LastArgs;
import com.reandroid.jcommand.annotations.OptionArg;
import com.reandroid.jcommand.utils.CommandUtil;
import com.reandroid.jcommand.utils.ReflectionUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandHelpBuilder {

    private final CommandStringResource stringResource;
    private final CommandOptions commandOptions;
    private final List<OptionArg> optionArgList;
    private final List<ChoiceArg> choiceArgList;
    private final LastArgs lastArgs;

    private int maxWidth = 100;
    private StringBuilder builder;
    private String tab2 = "   ";

    public CommandHelpBuilder(Class<?> clazz, CommandStringResource stringResource) {
        this.stringResource = stringResource;
        this.commandOptions = clazz.getAnnotation(CommandOptions.class);
        this.optionArgList = ReflectionUtil.listOptionArgs(clazz);
        this.choiceArgList = ReflectionUtil.listChoiceArgs(clazz);
        this.lastArgs = ReflectionUtil.getLastArgs(clazz);
    }
    public CommandHelpBuilder(Class<?> clazz) {
        this(clazz, defaultStringResource());
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }
    public void setTab2(String tab2) {
        this.tab2 = tab2;
    }

    public String build() {
        this.builder = new StringBuilder();
        appendHeading();
        appendUsage();
        appendOptionArgs(getOptionArgList(), CommandStrings.title_options);
        appendChoiceArgs();
        appendOptionArgs(getFlagList(), CommandStrings.title_flags);
        appendExamples();
        return builder.toString();
    }
    private void appendOptionArgs(List<OptionArg> optionArgList, String title) {
        if(optionArgList.isEmpty()) {
            return;
        }
        newLine();
        builder.append(stringResource.getString(title));
        newLine();
        int length = optionArgList.size();
        String[][] table = new String[length][2];
        for(int i = 0; i < length; i++) {
            OptionArg optionArg = optionArgList.get(i);
            StringBuilder name = new StringBuilder();
            name.append(optionArg.name());
            for(String l : optionArg.alternates()) {
                name.append(" | ");
                name.append(l);
            }
            String[] row = table[i];
            row[0] = name.toString();
            row[1] = stringResource.getString(optionArg.description());
        }
        CommandUtil.printTwoColumns(builder, tab2, maxWidth, table);
    }
    private void appendChoiceArgs() {
        List<ChoiceArg> choiceArgList = this.choiceArgList;
        if(choiceArgList.isEmpty()) {
            return;
        }
        newLine();
        if(optionArgList.isEmpty()) {
            builder.append(stringResource.getString(CommandStrings.title_options));
            newLine();
        }
        int length = choiceArgList.size();
        String[][] table = new String[length][2];
        for(int i = 0; i < length; i++) {
            ChoiceArg choiceArg = choiceArgList.get(i);
            StringBuilder name = new StringBuilder();
            name.append(choiceArg.name());
            for(String l : choiceArg.alternates()) {
                name.append(" | ");
                name.append(l);
            }
            String[] row = table[i];
            row[0] = name.toString();
            String description = stringResource.getString(choiceArg.description()) + "\n"
                    + CommandUtil.asString(choiceArg.values());
            row[1] = description;
        }
        CommandUtil.printTwoColumns(builder, tab2, maxWidth, table);
    }
    private void appendExamples() {
        CommandOptions commandOptions = this.commandOptions;
        if(commandOptions == null) {
            return;
        }
        String[] examples = commandOptions.examples();
        int length = examples.length;
        if(length == 0) {
            return;
        }
        newLine();
        builder.append(stringResource.getString(CommandStrings.title_example));
        newLine();
        String[][] table = new String[length][2];
        for(int i = 0; i < length; i++) {
            String[] row = table[i];
            row[0] = (i + 1) + ")";
            row[1] = stringResource.getString(examples[i]);
        }
        CommandUtil.printTwoColumns(builder, tab2, maxWidth * 2, table);
    }
    private void appendUsage() {
        CommandOptions options = this.commandOptions;
        if(options == null) {
            return;
        }
        String usage = options.usage();
        if(CommandUtil.isEmpty(usage)) {
            return;
        }
        newLine();
        builder.append(stringResource.getString(CommandStrings.title_usage));
        newLine();
        builder.append(tab2);
        builder.append(stringResource.getString(options.usage()));
    }
    private void appendHeading() {
        CommandOptions options = this.commandOptions;
        if(options == null) {
            return;
        }
        newLine();
        builder.append(stringResource.getString(options.description()));
    }
    private void newLine() {
        int length = builder.length();
        if(length == 0 || builder.charAt(length - 1) == '\n') {
            return;
        }
        builder.append("\n");
    }

    private List<OptionArg> getOptionArgList() {
        List<OptionArg> results = new ArrayList<>();
        for(OptionArg optionArg : this.optionArgList) {
            if(!optionArg.flag()) {
                results.add(optionArg);
            }
        }
        return results;
    }
    private List<OptionArg> getFlagList() {
        List<OptionArg> results = new ArrayList<>();
        for(OptionArg optionArg : this.optionArgList) {
            if(optionArg.flag()) {
                results.add(optionArg);
            }
        }
        return results;
    }
    private static CommandStringResource defaultStringResource() {
        Map<String, String> map = new HashMap<>();
        map.put(CommandStrings.title_options, "Options:");
        map.put(CommandStrings.title_flags, "Flags:");
        map.put(CommandStrings.title_usage, "Usage:");
        map.put(CommandStrings.title_example, "Examples");
        return resourceName -> {
            String str = map.get(resourceName);
            if(str == null) {
                str = resourceName;
            }
            return str;
        };
    }
}
