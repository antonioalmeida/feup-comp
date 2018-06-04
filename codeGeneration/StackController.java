package codeGeneration;

import java.util.HashMap;

public class StackController {

    private static HashMap<YalInstructions, Integer> costs;

    private int maxSize;

    private int currentSize;

    static {
        costs = new HashMap();
        costs.put(YalInstructions.ILOAD, 1);
        costs.put(YalInstructions.ALOAD, 1);
    }

    public StackController() {
        maxSize = 0;
        currentSize = 0;
    }

    public void addInstruction(YalInstructions instruction) {
        currentSize += costs.get(instruction);

        if(currentSize > maxSize)
            maxSize = currentSize;
    }

    public int getMax() {
        return maxSize;
    }

}
