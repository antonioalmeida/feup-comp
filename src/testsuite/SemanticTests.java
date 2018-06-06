package testsuite;

import org.junit.Test;
import parser.Yal;

import java.io.FileInputStream;

import static org.junit.Assert.*;

public class SemanticTests {

    @Test
    public void test1() {

        String testName = "testsuite/semantic_analysis/test1.yal";

        FileInputStream testStream = TestUtils.openFile(testName);

        int nErrors = Yal.runAndGetErrors(testStream);

        assertEquals(6, nErrors);
    }

    @Test
    public void testReturnOverload() {

        String testName = "testsuite/semantic_analysis/testReturnOverload.yal";

        FileInputStream testStream = TestUtils.openFile(testName);

        int nErrors = Yal.runAndGetErrors(testStream);

        assertEquals(4, nErrors);
    }
}