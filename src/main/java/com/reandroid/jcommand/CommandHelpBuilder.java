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
import com.reandroid.jcommand.utils.SpreadSheet;
import com.reandroid.jcommand.utils.TwoColumnTable;

import java.util.*;

public class CommandHelpBuilder {

    private final CommandStringResource stringResource;
    private final TwoColumnTable twoColumnTable;
    private final CommandOptions commandOptions;
    private final List<OptionArg> optionArgList;
    private final List<ChoiceArg> choiceArgList;
    private final LastArgs lastArgs;

    public CommandHelpBuilder(Class<?> clazz, CommandStringResource stringResource) {
        this.stringResource = stringResource;
        this.twoColumnTable = new TwoColumnTable();
        this.commandOptions = clazz.getAnnotation(CommandOptions.class);
        this.optionArgList = ReflectionUtil.listOptionArgs(clazz);
        this.choiceArgList = ReflectionUtil.listChoiceArgs(clazz);
        this.lastArgs = ReflectionUtil.getLastArgs(clazz);
    }
    public CommandHelpBuilder(Class<?> clazz) {
        this(clazz, defaultStringResource());
    }

    public void setMaxWidth(int maxWidth) {
        twoColumnTable.setMaxWidth(maxWidth);
    }
    public void setTab2(String tab2) {
        twoColumnTable.setTab2(tab2);
    }
    public void setColumnSeparator(String columnSeparator) {
        twoColumnTable.setColumnSeparator(columnSeparator);
    }
    public void setDrawBorder(boolean headerSeparators) {
        twoColumnTable.setDrawBorder(headerSeparators);
    }

    public String build() {
        return buildTable().toString();
    }
    public SpreadSheet buildTable() {
        twoColumnTable.clear();
        appendHeading();
        appendUsage();
        appendOptionArgs(getOptionArgList(), CommandStrings.title_options);
        appendChoiceArgs();
        appendOptionArgs(getFlagList(), CommandStrings.title_flags);
        appendExamples();
        twoColumnTable.addSeparator();
        return twoColumnTable.buildTable();
    }
    private void appendOptionArgs(List<OptionArg> optionArgList, String title) {
        if(optionArgList.isEmpty()) {
            return;
        }
        sortOptionArgList(optionArgList);
        twoColumnTable.addMergedRow(stringResource.getString(title));
        int length = optionArgList.size();
        for(int i = 0; i < length; i++) {
            OptionArg optionArg = optionArgList.get(i);
            StringBuilder name = new StringBuilder();
            name.append(optionArg.name());
            for(String l : optionArg.alternates()) {
                name.append(" | ");
                name.append(l);
            }
            twoColumnTable.addRow(name.toString(), stringResource.getString(optionArg.description()));
        }
    }
    private void appendChoiceArgs() {
        List<ChoiceArg> choiceArgList = this.choiceArgList;
        if(choiceArgList.isEmpty()) {
            return;
        }
        if(optionArgList.isEmpty()) {
            twoColumnTable.addMergedRow(stringResource.getString(CommandStrings.title_options));
        }
        int length = choiceArgList.size();
        for(int i = 0; i < length; i++) {
            ChoiceArg choiceArg = choiceArgList.get(i);
            StringBuilder name = new StringBuilder();
            name.append(choiceArg.name());
            for(String l : choiceArg.alternates()) {
                name.append(" | ");
                name.append(l);
            }
            String description = stringResource.getString(choiceArg.description()) + "\n"
                    + CommandUtil.asString(choiceArg.values());
            twoColumnTable.addRow(name.toString(), description);
        }
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
        twoColumnTable.addSeparator();
        twoColumnTable.addMergedRow(stringResource.getString(CommandStrings.title_example));
        for(int i = 0; i < length; i++) {
            String col1 = (i + 1) + ")  " + stringResource.getString(examples[i]);
            twoColumnTable.addMergedRowTabbed(col1);
        }
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
        twoColumnTable.addMergedRow(stringResource.getString(CommandStrings.title_usage));
        twoColumnTable.addMergedRowTabbed(stringResource.getString(options.usage()));
        twoColumnTable.addSeparator();
    }
    private void appendHeading() {
        twoColumnTable.addFirstSeparator();
        CommandOptions options = this.commandOptions;
        if(options == null) {
            return;
        }
        twoColumnTable.addMergedRow(stringResource.getString(options.description()));
        twoColumnTable.addSeparator();
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
    private static void sortOptionArgList(List<OptionArg> optionArgList) {
        optionArgList.sort(new Comparator<OptionArg>() {
            @Override
            public int compare(OptionArg arg1, OptionArg arg2) {
                return arg1.name().compareTo(arg2.name());
            }
        });
    }
    private static CommandStringResource defaultStringResource() {
        Map<String, String> map = new HashMap<>();
        map.put(CommandStrings.title_options, "Options:");
        map.put(CommandStrings.title_flags, "Flags:");
        map.put(CommandStrings.title_usage, "Usage:");
        map.put(CommandStrings.title_example, "Examples:");
        return resourceName -> {
            String str = map.get(resourceName);
            if(str == null) {
                str = resourceName;
            }
            return str;
        };
    }
}
