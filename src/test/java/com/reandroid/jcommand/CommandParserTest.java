package com.reandroid.jcommand;

import com.reandroid.jcommand.exceptions.CommandFormatException;
import com.reandroid.jcommand.exceptions.DuplicateOptionException;
import com.reandroid.jcommand.exceptions.MissingValueException;
import com.reandroid.jcommand.exceptions.UnknownOptionException;
import org.junit.Assert;
import org.junit.Test;

public class CommandParserTest {

    @Test
    public void testParser() {
        String[] args = new String[] {
                "-i", "/in/path",
                "-o", "/out/path",
                "--force",
                "-max", "1234",
                "--option1",
                "value1",
                "-k", "item1",
                "-k", "item2",
                "-k", "item3",
                "-l", "bbb",
                "-m", "three"
        };

        OptionA optionA = CommandParser.parse(OptionA.class, args);

        Assert.assertNotNull(optionA);

        Assert.assertEquals("/in/path", optionA.mInput);
        Assert.assertEquals("/out/path", optionA.mOutput);
        Assert.assertEquals(Boolean.TRUE, optionA.mForce);
        Assert.assertEquals(1234, optionA.mMaximum);
        Assert.assertEquals("value1", optionA.mOpt1);
        Assert.assertNotNull(optionA.mList);
        Assert.assertEquals(3, optionA.mList.size());
        Assert.assertTrue(optionA.mList.contains("item1"));
        Assert.assertTrue(optionA.mList.contains("item2"));
        Assert.assertTrue(optionA.mList.contains("item3"));
        Assert.assertEquals("bbb", optionA.mOneOfValue);
        Assert.assertEquals(SomeEnum.THREE, optionA.mSomeEnum);

        Assert.assertEquals("A", optionA.mOtherField1);
        Assert.assertEquals("B", OptionA.mOtherField2);
    }

    @Test(expected = UnknownOptionException.class)
    public void testUnknownOption() {
        String[] args = new String[] {
                "-xyz", "xyz value"
        };
        CommandParser.parse(OptionA.class, args);
    }

    @Test(expected = DuplicateOptionException.class)
    public void testDuplicateOption() {
        String[] args = new String[] {
                "-i", "path1",
                "--input-path", "path2",
        };
        CommandParser.parse(OptionA.class, args);
    }

    @Test(expected = CommandFormatException.class)
    public void testFormatOption() {
        String[] args = new String[] {
                "-max", "12xyz"
        };
        CommandParser.parse(OptionA.class, args);
    }

    @Test(expected = MissingValueException.class)
    public void testMissingValue() {
        String[] args = new String[] {
                "-i"
        };
        CommandParser.parse(OptionA.class, args);
    }
}
