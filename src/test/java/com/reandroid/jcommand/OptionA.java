package com.reandroid.jcommand;

import com.reandroid.jcommand.annotations.ChoiceArg;
import com.reandroid.jcommand.annotations.CommandOptions;
import com.reandroid.jcommand.annotations.OptionArg;

import java.util.List;

@CommandOptions(
        name = "d",
        alternates = {"decode"},
        description = "Description about command",
        usage = "d [options ...] , [flags ...]",
        examples = {
                "java -jar myJar.jar -i /in/path",
                "java -jar myJar.jar -o /out/path"
        }
)
public class OptionA {

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
    public List<String> mList;

    @ChoiceArg(name = "-l", values = {"aaa", "bbb", "ccc"}, description = "Choose one of:")
    public String mOneOfValue;

    @ChoiceArg(name = "-m", values = {"one", "two", "three"}, description = "Choose one of:")
    public SomeEnum mSomeEnum;

    public String mOtherField1 = "A";
    public static String mOtherField2 = "B";
}
