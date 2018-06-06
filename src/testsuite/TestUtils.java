package testsuite;

import java.io.*;

public class TestUtils {

    public static FileInputStream openFile(String filename) {
        FileInputStream stream = null;
        try {
            stream = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            System.out.println("File " + filename + " not found");
        }

        return stream;
    }

    public static String getFileContent(FileInputStream fis)
    {
        StringBuilder sb = null;
        try( BufferedReader br =
                     new BufferedReader(new InputStreamReader(fis, "UTF-8")))
        {
            sb = new StringBuilder();
            String line;
            while(( line = br.readLine()) != null ) {
                sb.append( line );
                sb.append( '\n' );
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return normalize(sb.toString());
    }

    public static String normalize(String str) {
        return str.trim().replaceAll(" +", " ");
    }
}
