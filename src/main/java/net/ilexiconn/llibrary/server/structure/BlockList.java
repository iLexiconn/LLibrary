package net.ilexiconn.llibrary.server.structure;

import net.minecraft.block.state.IBlockState;

import java.util.Arrays;
import java.util.Random;

/**
 * @author jglrxavpok
 * @since 1.1.0
 */
public class BlockList {
    private final float[] probabilities;
    private final IBlockState[] states;

    public BlockList(IBlockState blockState) {
        this(new IBlockState[]{blockState}, new float[]{1f});
    }

    public BlockList(IBlockState[] blockStates, float[] probabilities) {
        this.states = blockStates;
        this.probabilities = probabilities;
    }

    public IBlockState getRandom(Random rand) {
        float chosen = rand.nextFloat();
        IBlockState result = null;
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
        IBlockState[] newStates = Arrays.copyOf(states, states.length);
        float[] newProbabilities = Arrays.copyOf(probabilities, probabilities.length);
        return new BlockList(newStates, newProbabilities);
    }

    public IBlockState[] getStates() {
        return states;
    }

    public float[] getProbabilities() {
        return probabilities;
    }
}
