package com.weaver.accurate.util;

public class MethodInstruction {
    private String owner;
    private String name;
    private String desc;
    private boolean itf;
    private int opcode;


    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean getItf() {
        return itf;
    }

    public void setItf(boolean itf) {
        this.itf = itf;
    }

    public int getOpcode() {
        return opcode;
    }

    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }



    @Override
    public String toString() {
        return "MethodInstruction{" +
                "owner='" + owner + '\'' +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}