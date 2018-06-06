package testsuite;

import org.junit.Test;
import parser.ParseException;
import parser.Yal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class ConstantPropagationTest {

    @Test
    public void test1() {

        String filename = "testsuite/constant_propagation/test1.yal";
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Yal yal = new Yal(stream);
        String generatedCode = Yal.run(yal);

        assertEquals("ola", generatedCode);
    }

}