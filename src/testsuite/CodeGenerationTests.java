package testsuite;

import org.junit.Test;
import parser.Yal;
import utils.Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class CodeGenerationTests {

    @Test
    public void callMain() {

        String filename = "testsuite/code_generation/callMain.yal";
        String targetname = "testsuite/code_generation/callMain.j";
        FileInputStream stream = null;
        FileInputStream target = null;
        try {
            stream = new FileInputStream(filename);
            target = new FileInputStream(targetname);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Yal yal = new Yal(stream);
        String generatedCode = Utils.normalize(Yal.run(yal));

        String targetCode = Utils.getFileContent(target, "UTF-8").trim().replaceAll(" +", " ");

        assertEquals(targetCode, generatedCode);
    }

}
