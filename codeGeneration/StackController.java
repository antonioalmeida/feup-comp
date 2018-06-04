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
        costs.put(YalInstructions.ICONST, 1);
        costs.put(YalInstructions.GETSTATIC, 1);
        costs.put(YalInstructions.IF, -2);
        costs.put(YalInstructions.OPERATION, -1);
        costs.put(YalInstructions.ISTORE, -1);
        costs.put(YalInstructions.IASTORE, -3);
        costs.put(YalInstructions.ASTORE, -1);
        costs.put(YalInstructions.IALOAD, -1);
        costs.put(YalInstructions.PUTSTATIC, -1);
        costs.put(YalInstructions.BIPUSH, 1);
        costs.put(YalInstructions.SIPUSH, 1);
        costs.put(YalInstructions.LDC, 1);
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

    public void addInstruction(int cost) {
        currentSize += cost;

        if(currentSize > maxSize)
            maxSize = currentSize;
    }

    public int getMax() {
        return maxSize;
    }

}
