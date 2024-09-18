package com.reandroid.jcommand;

import com.reandroid.jcommand.exceptions.CommandFormatException;
import com.reandroid.jcommand.exceptions.DuplicateOptionException;
import com.reandroid.jcommand.exceptions.MissingValueException;
import com.reandroid.jcommand.exceptions.UnknownOptionException;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

public class SubCommandParserTest {

    @Test
    public void testParser() {
        String[] args = new String[] {
                "-i", "/in/path",
                "-o", "/out/path",
                "--force",
                "-q", "/path4",
                "-max", "1234",
                "--option1",
                "value1",
                "-k", "item1",
                "-k", "item2",
                "-q", "/path2",
                "-k", "item3",
                "-l", "bbb",
                "-m", "three",
                "-q", "/path1",
                "-q", "/path3",
                "-r", "123",
                "-r", "456",
                "-r", "0x1abc",
                "-s", "four",
                "-s", "two"
        };

        OptionA optionA = SubCommandParser.parse(OptionA.class, args);

        Assert.assertNotNull(optionA);

        Assert.assertEquals("/in/path", optionA.mInput);
        Assert.assertEquals("/out/path", optionA.mOutput);
        Assert.assertEquals(Boolean.TRUE, optionA.mForce);
        Assert.assertEquals(1234, optionA.mMaximum);
        Assert.assertEquals("value1", optionA.mOpt1);
        Assert.assertNotNull(optionA.mStringList);
        Assert.assertEquals(3, optionA.mStringList.size());
        Assert.assertTrue(optionA.mStringList.contains("item1"));
        Assert.assertTrue(optionA.mStringList.contains("item2"));
        Assert.assertTrue(optionA.mStringList.contains("item3"));

        Assert.assertNotNull(optionA.mFileList);
        Assert.assertEquals(4, optionA.mFileList.size());
        Assert.assertEquals(4, optionA.mFileList.toArray(new File[0]).length);

        Assert.assertNotNull(optionA.mIntegerList);
        Assert.assertEquals(3, optionA.mIntegerList.size());
        Assert.assertEquals(3, optionA.mIntegerList.toArray(new Integer[0]).length);

        Assert.assertNotNull(optionA.mEnumList);
        Assert.assertEquals(2, optionA.mEnumList.size());
        Assert.assertEquals(2, optionA.mEnumList.toArray(new SomeEnum[0]).length);

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
        SubCommandParser.parse(OptionA.class, args);
    }

    @Test(expected = DuplicateOptionException.class)
    public void testDuplicateOption() {
        String[] args = new String[] {
                "-i", "path1",
                "--input-path", "path2",
        };
        SubCommandParser.parse(OptionA.class, args);
    }

    @Test(expected = CommandFormatException.class)
    public void testFormatOption() {
        String[] args = new String[] {
                "-max", "12xyz"
        };
        SubCommandParser.parse(OptionA.class, args);
    }

    @Test(expected = MissingValueException.class)
    public void testMissingValue() {
        String[] args = new String[] {
                "-i"
        };
        SubCommandParser.parse(OptionA.class, args);
    }
}
