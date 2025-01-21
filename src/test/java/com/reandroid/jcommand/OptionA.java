package com.reandroid.jcommand;

import com.reandroid.jcommand.annotations.ChoiceArg;
import com.reandroid.jcommand.annotations.CommandOptions;
import com.reandroid.jcommand.annotations.OptionArg;

import java.io.File;
import java.util.List;

@CommandOptions(
        name = "d",
        alternates = {"decode"},
        description = "Description about command",
        usage = "d [options ...] , [flags ...]",
        examples = {
                "java -jar myJar.jar -i /in/path",
                "java -jar myJar.jar -o /out/path"
        },
        notes = {
                "Additional notes about command - 1",
                "Additional notes about command - 2"
        }
)
public class OptionA extends BaseOption {

    @OptionArg(name = "-i", alternates = {"--input-path"}, description = "Input file path")
    public String mInput;
    @OptionArg(name = "-o", alternates = {"--out-path"}, description = "Output file path")
    public String mOutput = "/initial/value";
    @OptionArg(name = "-max", description = "Maximum try count")
    public int mMaximum = 7;

    @OptionArg(name = "-f", alternates = {"--force"}, flag = true, description = "Force delete file")
    public Boolean mForce;

    @OptionArg(name = "-g", alternates = {"--opt1", "--option1"}, description = "Option1 description")
    public String mOpt1;

    @OptionArg(name = "-k", description = "K value (can be multiple)")
    public List<String> mStringList;

    @OptionArg(name = "-q", description = "Q value (can be multiple)")
    public List<File> mFileList;

    @OptionArg(name = "-r", description = "R value (can be multiple)")
    public List<Integer> mIntegerList;

    @OptionArg(name = "-s", description = "s value (can be multiple)")
    public List<SomeEnum> mEnumList;

    @ChoiceArg(name = "-l", values = {"aaa", "bbb", "ccc"}, description = "Choose one of:")
    public String mOneOfValue;

    @ChoiceArg(name = "-m", values = {"one", "two", "three"}, description = "Choose one of:")
    public SomeEnum mSomeEnum;

    public String mOtherField1 = "A";
    public static String mOtherField2 = "B";
}
