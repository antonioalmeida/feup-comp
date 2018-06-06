package testsuite;

import org.junit.Test;
import parser.Yal;

import java.io.FileInputStream;

import static org.junit.Assert.*;

public class ConstantPropagationTest {

    @Test
    public void constPropagation1() {

        String testName = "testsuite/constant_propagation/constPropagation1.yal";
        String targetName = "testsuite/constant_propagation/constPropagation1.j";

        FileInputStream testStream = TestUtils.openFile(testName);
        FileInputStream targetStream = TestUtils.openFile(targetName);

        String generatedCode = TestUtils.normalize(Yal.run(testStream));

        String targetCode = TestUtils.getFileContent(targetStream);

        assertEquals(targetCode, generatedCode);
    }

    @Test
    public void constPropagation2() {

        String testName = "testsuite/constant_propagation/constPropagation2.yal";
        String targetName = "testsuite/constant_propagation/constPropagation2.j";

        FileInputStream testStream = TestUtils.openFile(testName);
        FileInputStream targetStream = TestUtils.openFile(targetName);

        String generatedCode = TestUtils.normalize(Yal.run(testStream));

        String targetCode = TestUtils.getFileContent(targetStream);

        assertEquals(targetCode, generatedCode);
    }

    @Test
    public void constPropagation3() {

        String testName = "testsuite/constant_propagation/constPropagation3.yal";
        String targetName = "testsuite/constant_propagation/constPropagation3.j";

        FileInputStream testStream = TestUtils.openFile(testName);
        FileInputStream targetStream = TestUtils.openFile(targetName);

        String generatedCode = TestUtils.normalize(Yal.run(testStream));

        String targetCode = TestUtils.getFileContent(targetStream);

        assertEquals(targetCode, generatedCode);
    }

}