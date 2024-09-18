package com.reandroid.jcommand;

import org.junit.Assert;
import org.junit.Test;

public class SubCommandHelpBuilderTest {

    @Test()
    public void testHelpBuilder() {
        SubCommandHelpBuilder builder = new SubCommandHelpBuilder(OptionA.class);
        String help = builder.build();
        Assert.assertNotNull(help);
    }
}
