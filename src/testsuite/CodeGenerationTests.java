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

    @Test
    public void nestedBranch() throws Exception {

        String testName = "testsuite/code_generation/nestedBranch.yal";
        String targetName = "testsuite/code_generation/nestedBranch.j";

        FileInputStream testStream = TestUtils.openFile(testName);
        FileInputStream targetStream = TestUtils.openFile(targetName);

        String generatedCode = TestUtils.normalize(Yal.run(testStream, 4));
        String targetCode = TestUtils.getFileContent(targetStream);

        assertEquals(targetCode, generatedCode);
    }

    @Test
    public void registerTest() throws Exception {

        String testName = "testsuite/code_generation/registerTest.yal";
        String targetName = "testsuite/code_generation/registerTest.j";

        FileInputStream testStream = TestUtils.openFile(testName);
        FileInputStream targetStream = TestUtils.openFile(targetName);

        String generatedCode = TestUtils.normalize(Yal.run(testStream, 1));
        String targetCode = TestUtils.getFileContent(targetStream);

        assertEquals(targetCode, generatedCode);
    }

    @Test
    public void stackSize() throws Exception {

        String testName = "testsuite/code_generation/stackSize.yal";
        String targetName = "testsuite/code_generation/stackSize.j";

        FileInputStream testStream = TestUtils.openFile(testName);
        FileInputStream targetStream = TestUtils.openFile(targetName);

        String generatedCode = TestUtils.normalize(Yal.run(testStream, 4));
        String targetCode = TestUtils.getFileContent(targetStream);

        assertEquals(targetCode, generatedCode);
    }

    @Test
    public void testArithmetic() throws Exception {

        String testName = "testsuite/code_generation/testArithmetic.yal";
        String targetName = "testsuite/code_generation/testArithmetic.j";

        FileInputStream testStream = TestUtils.openFile(testName);
        FileInputStream targetStream = TestUtils.openFile(targetName);

        String generatedCode = TestUtils.normalize(Yal.run(testStream));
        String targetCode = TestUtils.getFileContent(targetStream);

        assertEquals(targetCode, generatedCode);
    }

    @Test
    public void testIfs() throws Exception {

        String testName = "testsuite/code_generation/testIf.yal";
        String targetName = "testsuite/code_generation/testIf.j";

        FileInputStream testStream = TestUtils.openFile(testName);
        FileInputStream targetStream = TestUtils.openFile(targetName);

        String generatedCode = TestUtils.normalize(Yal.run(testStream));
        String targetCode = TestUtils.getFileContent(targetStream);

        assertEquals(targetCode, generatedCode);
    }

    @Test
    public void testIndex() throws Exception {

        String testName = "testsuite/code_generation/testIndex.yal";
        String targetName = "testsuite/code_generation/testIndex.j";

        FileInputStream testStream = TestUtils.openFile(testName);
        FileInputStream targetStream = TestUtils.openFile(targetName);

        String generatedCode = TestUtils.normalize(Yal.run(testStream));
        String targetCode = TestUtils.getFileContent(targetStream);

        assertEquals(targetCode, generatedCode);
    }


}
