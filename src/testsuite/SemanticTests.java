package testsuite;

import org.junit.Test;
import parser.Yal;

import java.io.FileInputStream;

import static org.junit.Assert.*;

public class SemanticTests {

    @Test
    public void testIfs1() {

        String testName = "testsuite/semantic_analysis/testIfs1.yal";

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

    @Test
    public void testIfs2() {

        String testName = "testsuite/semantic_analysis/testIfs2.yal";

        FileInputStream testStream = TestUtils.openFile(testName);

        int nErrors = Yal.runAndGetErrors(testStream);

        assertEquals(2, nErrors);
    }

    @Test
    public void testAssigns() {

        String testName = "testsuite/semantic_analysis/testAssigns.yal";

        FileInputStream testStream = TestUtils.openFile(testName);

        int nErrors = Yal.runAndGetErrors(testStream);

        assertEquals(25, nErrors);
    }

    @Test
    public void testGlobalReassign() {

        String testName = "testsuite/semantic_analysis/testGlobalReassign.yal";

        FileInputStream testStream = TestUtils.openFile(testName);

        int nErrors = Yal.runAndGetErrors(testStream);

        assertEquals(5, nErrors);
    }

    @Test
    public void testReturnInit() {

        String testName = "testsuite/semantic_analysis/testReturnInit.yal";

        FileInputStream testStream = TestUtils.openFile(testName);

        int nErrors = Yal.runAndGetErrors(testStream);

        assertEquals(1, nErrors);
    }

    @Test
    public void testFunctionCall() {

        String testName = "testsuite/semantic_analysis/testFunctionCall.yal";

        FileInputStream testStream = TestUtils.openFile(testName);

        int nErrors = Yal.runAndGetErrors(testStream);

        assertEquals(3, nErrors);
    }

    @Test
    public void testIfs3() {

        String testName = "testsuite/semantic_analysis/testIfs3.yal";

        FileInputStream testStream = TestUtils.openFile(testName);

        int nErrors = Yal.runAndGetErrors(testStream);

        assertEquals(3, nErrors);
    }

    @Test
    public void testFunctionReturn() {

        String testName = "testsuite/semantic_analysis/testFunctionReturn.yal";

        FileInputStream testStream = TestUtils.openFile(testName);

        int nErrors = Yal.runAndGetErrors(testStream);

        assertEquals(3, nErrors);
    }

    @Test
    public void testComparison() {

        String testName = "testsuite/semantic_analysis/testComparison.yal";

        FileInputStream testStream = TestUtils.openFile(testName);

        int nErrors = Yal.runAndGetErrors(testStream);

        assertEquals(1, nErrors);
    }

    @Test
    public void testIfs4() {

        String testName = "testsuite/semantic_analysis/testIfs4.yal";

        FileInputStream testStream = TestUtils.openFile(testName);

        int nErrors = Yal.runAndGetErrors(testStream);

        assertEquals(3, nErrors);
    }

}