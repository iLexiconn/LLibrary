package net.ilexiconn.llibrary.server.asm;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.FrameNode;
import org.objectweb.asm.tree.MethodNode;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public interface PostProcessor {
    void process(String cls, MethodNode node);

    /**
     * @deprecated Should no longer be needed, as COMPUTE_FRAMES is now called safely for every patcher.
     */
    @Deprecated
    class PreserveFrames implements PostProcessor {
        private static final Map<String, Object> PRIMITIVES = new HashMap<>();

        static {
            PRIMITIVES.put("int", Opcodes.INTEGER);
            PRIMITIVES.put("float", Opcodes.FLOAT);
            PRIMITIVES.put("double", Opcodes.DOUBLE);
            PRIMITIVES.put("long", Opcodes.LONG);
            PRIMITIVES.put("null", Opcodes.NULL);
        }

        private final int frameCount;

        /**
         * Ensures the given frames are never chopped from the stackmap frame
         *
         * @param upperFrame the upper frames index to prevent dropping
         */
        public PreserveFrames(int upperFrame) {
            this.frameCount = upperFrame + 1;
        }

        @Override
        public void process(String cls, MethodNode node) {
            String[] params = Descriptors.parseMethod(node.desc);
            Object[] topFrame = new Object[Math.max(params.length - 1, 0)];
            for (int i = 0; i < topFrame.length; i++) {
                String param = params[i];
                topFrame[i] = PRIMITIVES.getOrDefault(param, param);
            }

            if (!Modifier.isStatic(node.access)) {
                Object[] newTop = new Object[topFrame.length + 1];
                newTop[0] = cls;
                System.arraycopy(topFrame, 0, newTop, 1, topFrame.length);
                topFrame = newTop;
            }

            if (this.frameCount > topFrame.length) {
                throw new IllegalArgumentException("Cannot preserve frames higher than the initial frame length");
            }

            Object[] currentFrame = Arrays.copyOf(topFrame, topFrame.length);

            AbstractInsnNode[] instructions = node.instructions.toArray();

            for (AbstractInsnNode instruction : instructions) {
                if (instruction instanceof FrameNode) {
                    FrameNode frame = (FrameNode) instruction;
                    if (frame.local != null) {
                        switch (frame.type) {
                            case Opcodes.F_APPEND:
                            case Opcodes.F_SAME1:
                            case Opcodes.F_FULL:
                                Object[] locals = frame.local.toArray();
                                this.replaceTop(locals, topFrame);
                                frame.local = new ArrayList<>();
                                Collections.addAll(frame.local, locals);
                                break;
                        }
                    }
                }
            }

            for (AbstractInsnNode instruction : instructions) {
                if (instruction instanceof FrameNode) {
                    FrameNode frame = (FrameNode) instruction;
                    if (frame.local != null) {
                        int type = frame.type;
                        Object[] newFrame = currentFrame;
                        switch (type) {
                            case Opcodes.F_APPEND:
                            case Opcodes.F_SAME1:
                                newFrame = this.append(currentFrame, frame.local.toArray());
                                break;
                            case Opcodes.F_CHOP:
                                int count = frame.local.size();
                                if (currentFrame.length - count < this.frameCount) {
                                    int newLength = currentFrame.length - this.frameCount;
                                    frame.local = new ArrayList<>(newLength);
                                    for (int i = 0; i < newLength; i++) {
                                        frame.local.add(null);
                                    }
                                }
                                int chop = frame.local.size();
                                newFrame = new Object[currentFrame.length - chop];
                                System.arraycopy(currentFrame, 0, newFrame, 0, newFrame.length);
                                break;
                            case Opcodes.F_FULL:
                                if (frame.local.size() < this.frameCount) {
                                    newFrame = new Object[this.frameCount];
                                    System.arraycopy(currentFrame, 0, newFrame, 0, this.frameCount);
                                    System.arraycopy(frame.local.toArray(), 0, newFrame, 0, frame.local.size());
                                } else {
                                    newFrame = frame.local.toArray();
                                }
                                frame.local = new ArrayList<>();
                                Collections.addAll(frame.local, newFrame);
                                break;
                        }
                        currentFrame = newFrame;
                    }
                }
            }
        }

        private Object[] append(Object[] frame, Object[] v) {
            Object[] newFrame = new Object[frame.length + v.length];
            System.arraycopy(frame, 0, newFrame, 0, frame.length);
            System.arraycopy(v, 0, newFrame, frame.length, v.length);
            return newFrame;
        }

        private void replaceTop(Object[] frame, Object[] topFrame) {
            for (int i = 0; i < Math.min(frame.length, topFrame.length); i++) {
                Object value = frame[i];
                if (value == Opcodes.TOP) {
                    frame[i] = topFrame[i];
                }
            }
        }
    }
}
