package com.reandroid.jcommand;


import com.reandroid.jcommand.annotations.MainCommand;
import com.reandroid.jcommand.annotations.OnOptionSelected;
import com.reandroid.jcommand.annotations.OtherOption;
import com.reandroid.jcommand.exceptions.CommandException;
import org.junit.Assert;
import org.junit.Test;

@MainCommand(
        headers = {"APKEditor", "Main program test - static", "Version: x.x.x"},
        options = {
                OptionA.class,
                OptionB.class
        }
)
public class MainProgramTestStatic {

    private static boolean mOnVersionCalled;
    private static boolean mOnHelpCalled;
    private static Object mOption;
    private static boolean mOptionArgsEmpty;

    @Test
    public void testHelpCommand() {
        reset();
        CommandParser parser = new CommandParser(MainProgramTestStatic.class);
        try {
            parser.parse(null, "-h");
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
        CommandParser parser = new CommandParser(MainProgramTestStatic.class);
        try {
            parser.parse(null, "-v");
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
        CommandParser parser = new CommandParser(MainProgramTestStatic.class);
        try {
            parser.parse(null, "d");
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
        CommandParser parser = new CommandParser(MainProgramTestStatic.class);
        try {
            parser.parse(null, "d", "-i", "/path/test", "-max", "123456");
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
    }
    @Test
    public void testOptionB() {
        reset();
        CommandParser parser = new CommandParser(MainProgramTestStatic.class);

        try {
            parser.parse(null, "b", "-i", "/path/test", "-max", "123456");
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
        CommandParser parser = new CommandParser(MainProgramTestStatic.class);
        parser.parse(null, "xyz");
    }

    private static void reset() {
        mOnVersionCalled = false;
        mOnHelpCalled = false;
        mOption = null;
        mOptionArgsEmpty = false;
    }

    @OtherOption(names = {"-v", "-version"}, description = "Displays version")
    private static void onVersion() {
        mOnVersionCalled = true;
    }
    @OtherOption(names = {"-h", "-help"}, description = "Displays this help and exit")
    static void onHelp() {
        mOnHelpCalled = true;
    }
    @OnOptionSelected
    public static void onOption(Object option, boolean emptyArgs) {
        mOption = option;
        mOptionArgsEmpty = emptyArgs;
    }
}