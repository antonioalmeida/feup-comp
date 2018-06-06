package testsuite;

import org.junit.Test;
import parser.Yal;

import java.io.FileInputStream;

import static org.junit.Assert.*;

public class ConstantPropagationTest {

    @Test
    public void test1() {

        String testName = "testsuite/constant_propagation/test1.yal";
        String targetName = "testsuite/constant_propagation/test1.j";

        FileInputStream testStream = TestUtils.openFile(testName);
        FileInputStream targetStream = TestUtils.openFile(targetName);

        String generatedCode = TestUtils.normalize(Yal.run(testStream));

        String targetCode = TestUtils.getFileContent(targetStream);

        assertEquals(targetCode, generatedCode);
    }

}