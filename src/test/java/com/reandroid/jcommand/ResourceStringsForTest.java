package com.reandroid.jcommand;

import java.util.HashMap;
import java.util.Map;

public class ResourceStringsForTest implements CommandStringResource{

    private final Map<String, String> stringMap;

    public ResourceStringsForTest() {
        this.stringMap = loadTestMap();
    }
    @Override
    public String getString(String resourceName) {
        String str = stringMap.get(resourceName);
        if(str == null) {
            str = resourceName;
        }
        return str;
    }
    private static Map<String, String> loadTestMap() {
        Map<String, String> map = new HashMap<>();
        map.put(CommandStrings.title_options, "Options:");
        map.put(CommandStrings.title_other_options, "Other options:");
        map.put(CommandStrings.title_flags, "Flags:");
        map.put(CommandStrings.title_usage, "Usage:");
        map.put(CommandStrings.title_example, "Examples");
        map.put(CommandStrings.title_commands, "Commands");
        map.put(CommandStrings.unknown_command_exception, "Unknown command: '%s'");
        map.put(CommandStrings.unknown_option_exception, "Unknown option: '%s'");
        return map;
    }
}
