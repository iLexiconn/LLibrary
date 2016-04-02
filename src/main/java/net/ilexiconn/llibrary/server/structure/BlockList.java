package net.ilexiconn.llibrary.server.structure;

import java.util.Arrays;
import java.util.Random;

/**
 * @author jglrxavpok
 * @since 1.1.0
 */
public class BlockList {
    private final float[] probabilities;
    private final BlockState[] states;

    public BlockList(BlockState blockState) {
        this(new BlockState[]{blockState}, new float[]{1f});
    }

    public BlockList(BlockState[] blockStates, float[] probabilities) {
        this.states = blockStates;
        this.probabilities = probabilities;
    }

    public BlockState getRandom(Random rand) {
        float chosen = rand.nextFloat();
        BlockState result = null;
        int index = 0;
        while (chosen >= 0f) {
            if (index >= states.length) {
                return null;
            }
            chosen -= probabilities[index];
            result = states[index];
            index++;
        }
        return result;
    }

    public BlockList copy() {
        BlockState[] newStates = Arrays.copyOf(states, states.length);
        float[] newProbabilities = Arrays.copyOf(probabilities, probabilities.length);
        return new BlockList(newStates, newProbabilities);
    }

    public BlockState[] getStates() {
        return states;
    }

    public float[] getProbabilities() {
        return probabilities;
    }
}
