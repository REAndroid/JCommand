package com.reandroid.jcommand;

import com.reandroid.jcommand.annotations.OptionArg;

public class BaseOption {

    @OptionArg(name = "-h", alternates = {"-help"}, flag = true, description = "Displays this help and exit")
    public boolean mHelp;

}
