package com.reandroid.jcommand;

import com.reandroid.jcommand.annotations.MainCommand;
import com.reandroid.jcommand.annotations.OnOptionSelected;
import com.reandroid.jcommand.annotations.OtherOption;
import com.reandroid.jcommand.exceptions.CommandException;
import org.junit.Assert;
import org.junit.Test;

@MainCommand(
        headers = {"APKEditor", "Main program test - instance", "Version: x.x.x"},
        usages = {"<command> <options>"},
        options = {
                OptionA.class,
                OptionB.class
        }
)
public class MainProgramTest {

    private boolean mOnVersionCalled;
    private boolean mOnHelpCalled;
    private Object mOption;
    private boolean mOptionArgsEmpty;

    @Test
    public void testHelpCommand() {
        reset();
        CommandParser parser = new CommandParser(MainProgramTest.class);
        try {
            parser.parse(this, "-h");
        } catch (CommandException e) {
            throw new RuntimeException(e.getMessage(new ResourceStringsForTest()));
        }
        Assert.assertTrue("onHelp not called", mOnHelpCalled);
        Assert.assertFalse("onVersion called", mOnVersionCalled);
        Assert.assertFalse("Option args not empty", mOptionArgsEmpty);
        Assert.assertNull("Option not null", mOption);
    }
    @Test
    public void testVersionCommand() {
        reset();
        CommandParser parser = new CommandParser(MainProgramTest.class);
        try {
            parser.parse(this, "-v");
        } catch (CommandException e) {
            throw new RuntimeException(e.getMessage(new ResourceStringsForTest()));
        }

        Assert.assertFalse("onHelp called", mOnHelpCalled);
        Assert.assertTrue("onVersion not called", mOnVersionCalled);
        Assert.assertFalse("Option args not empty", mOptionArgsEmpty);
        Assert.assertNull("Option not null", mOption);
    }
    @Test
    public void testEmptyOption() {
        reset();
        CommandParser parser = new CommandParser(MainProgramTest.class);
        try {
            parser.parse(this, "d");
        } catch (CommandException e) {
            throw new RuntimeException(e.getMessage(new ResourceStringsForTest()));
        }

        Assert.assertFalse("onHelp called", mOnHelpCalled);
        Assert.assertFalse("onVersion called", mOnVersionCalled);
        Assert.assertTrue("Option args not empty", mOptionArgsEmpty);
        Assert.assertNotNull("Null option", mOption);
        Assert.assertTrue("Expecting class: '" + OptionA.class
                + "', but found: '" + mOption.getClass() + "'", mOption instanceof OptionA);
    }

    @Test
    public void testOptionA() {
        reset();
        CommandParser parser = new CommandParser(MainProgramTest.class);
        try {
            parser.parse(this, "d", "-i", "/path/test", "-max", "123456");
        } catch (CommandException e) {
            throw new RuntimeException(e.getMessage(new ResourceStringsForTest()));
        }

        Assert.assertFalse("onHelp called", mOnHelpCalled);
        Assert.assertFalse("onVersion called", mOnVersionCalled);
        Assert.assertFalse("Option args are empty", mOptionArgsEmpty);
        Assert.assertNotNull("Null option", mOption);
        Assert.assertTrue("Expecting class: '" + OptionA.class
                + "', but found: '" + mOption.getClass() + "'", mOption instanceof OptionA);
        OptionA optionA = (OptionA) mOption;
        Assert.assertEquals("/path/test", optionA.mInput);
        Assert.assertEquals(123456, optionA.mMaximum);
        TestUtils.log(optionA.toString());
    }
    @Test
    public void testOptionA2() {
        reset();
        CommandParser parser = new CommandParser(MainProgramTest.class);
        try {
            parser.parse(this, "d", "-h");
        } catch (CommandException e) {
            throw new RuntimeException(e.getMessage(new ResourceStringsForTest()));
        }
    }
    @Test
    public void testOptionB() {
        reset();
        CommandParser parser = new CommandParser(MainProgramTest.class);

        try {
            parser.parse(this, "b", "-i", "/path/test", "-max", "123456");
        }catch (CommandException e) {
            throw new RuntimeException(e.getMessage(new ResourceStringsForTest()));
        }

        Assert.assertFalse("onHelp called", mOnHelpCalled);
        Assert.assertFalse("onVersion called", mOnVersionCalled);
        Assert.assertFalse("Option args are empty", mOptionArgsEmpty);
        Assert.assertNotNull("Null option", mOption);
        Assert.assertTrue("Expecting class: '" + OptionB.class
                + "', but found: '" + mOption.getClass() + "'", mOption instanceof OptionB);
        OptionB optionB = (OptionB) mOption;
        Assert.assertEquals("/path/test", optionB.mInput);
        Assert.assertEquals(123456, optionB.mMaximum);
    }

    @Test(expected = CommandException.class)
    public void testUnknownOption() {
        reset();
        CommandParser parser = new CommandParser(MainProgramTest.class);
        parser.parse(this, "xyz");
    }

    private void reset() {
        mOnVersionCalled = false;
        mOnHelpCalled = false;
        mOption = null;
        mOptionArgsEmpty = false;
    }

    @OtherOption(names = {"-v", "-version"}, description = "Displays version")
    private void onVersion() {
        mOnVersionCalled = true;
        TestUtils.log("Version = x.x.x");
    }
    @OtherOption(names = {"-h", "-help"}, description = "Displays this help and exit")
    void onHelp() {
        mOnHelpCalled = true;
        CommandHelpBuilder builder = new CommandHelpBuilder(
                new ResourceStringsForTest(), MainProgramTest.class);
        builder.setDrawBorder(true);
        builder.setFooters("To get help about each command run with:", "<command> -h");
        TestUtils.log(builder.build());
    }
    @OnOptionSelected
    public void onOption(Object option, boolean emptyArgs) {
        mOption = option;
        mOptionArgsEmpty = emptyArgs;
        if (emptyArgs) {
            TestUtils.log("Empty options, to see help run: <command> -h");
        } else if (option instanceof BaseOption) {
            BaseOption baseOption = (BaseOption) option;
            if(baseOption.mHelp) {
                SubCommandHelpBuilder builder = new SubCommandHelpBuilder(
                        new ResourceStringsForTest(), baseOption.getClass());
                TestUtils.log(builder.build());
            }
        }
    }
}
