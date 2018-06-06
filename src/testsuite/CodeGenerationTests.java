package testsuite;

import org.junit.Test;
import parser.Yal;

import java.io.FileInputStream;

import static org.junit.Assert.*;

public class CodeGenerationTests {

    @Test
    public void callMain() {

        String testName = "testsuite/code_generation/callMain.yal";
        String targetName = "testsuite/code_generation/callMain.j";

        FileInputStream testStream = TestUtils.openFile(testName);
        FileInputStream targetStream = TestUtils.openFile(targetName);

        String generatedCode = TestUtils.normalize(Yal.run(testStream));
        String targetCode = TestUtils.getFileContent(targetStream);

        assertEquals(targetCode, generatedCode);
    }

    @Test
    public void constantRanges() throws Exception {

        String testName = "testsuite/code_generation/constantRanges.yal";
        String targetName = "testsuite/code_generation/constantRanges.j";

        FileInputStream testStream = TestUtils.openFile(testName);
        FileInputStream targetStream = TestUtils.openFile(targetName);

        String generatedCode = TestUtils.normalize(Yal.run(testStream));
        String targetCode = TestUtils.getFileContent(targetStream);

        assertEquals(targetCode, generatedCode);
    }

    @Test
    public void inputAsOutput() throws Exception {

        String testName = "testsuite/code_generation/inputAsOutput.yal";
        String targetName = "testsuite/code_generation/inputAsOutput.j";

        FileInputStream testStream = TestUtils.openFile(testName);
        FileInputStream targetStream = TestUtils.openFile(targetName);

        String generatedCode = TestUtils.normalize(Yal.run(testStream));
        String targetCode = TestUtils.getFileContent(targetStream);

        assertEquals(targetCode, generatedCode);
    }
}
