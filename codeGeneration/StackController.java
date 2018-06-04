package codeGeneration;

import java.util.HashMap;

public class StackController {

    private static HashMap<YalInstructions, Integer> costs;

    static {
        costs = new HashMap();
        costs.put(YalInstructions.ILOAD, 1);
        costs.put(YalInstructions.ALOAD, 1);
    }

}
