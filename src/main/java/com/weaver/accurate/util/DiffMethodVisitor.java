package com.weaver.accurate.util;

import lombok.Getter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

@Getter
public class DiffMethodVisitor extends MethodVisitor {
    MethodInstruction instruction = new MethodInstruction();

    public DiffMethodVisitor(int api) {
        super(api);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        super.visitMethodInsn(opcode, owner, name, desc, itf);
        if (opcode != 0){
            instruction.setOpcode(opcode);
            instruction.setOwner(name);
            instruction.setName(desc);
            instruction.setDesc(desc);
            instruction.setItf(itf);
        }
    }

    @Override
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
        if ((bootstrapMethodHandle.getTag() == Opcodes.H_INVOKEVIRTUAL ||
             bootstrapMethodHandle.getTag() == Opcodes.H_INVOKESTATIC ||
                bootstrapMethodHandle.getTag() == Opcodes.H_INVOKESPECIAL ||
                bootstrapMethodHandle.getTag() == Opcodes.H_INVOKEINTERFACE) &&
                bootstrapMethodArguments.length > 2){
            String bootstrapMethodArgument = bootstrapMethodArguments[1] + "";
            int i2 = bootstrapMethodArgument.indexOf(".");
            int i = bootstrapMethodArgument.indexOf("(");
            int i1 = bootstrapMethodArgument.indexOf(";");
            instruction.setOpcode(bootstrapMethodHandle.getTag());
            instruction.setOwner(bootstrapMethodArgument.substring(0, i2));
            instruction.setName(bootstrapMethodArgument.substring(i2+1, i));
            instruction.setDesc(bootstrapMethodArgument.substring(i, i1));
        }
    }
}
