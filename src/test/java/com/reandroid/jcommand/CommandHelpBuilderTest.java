package com.reandroid.jcommand;

import org.junit.Assert;
import org.junit.Test;

public class CommandHelpBuilderTest {

    @Test()
    public void testHelpBuilder() {
        CommandHelpBuilder builder = new CommandHelpBuilder(OptionA.class);
        String help = builder.build();
        Assert.assertNotNull(help);
    }
}
