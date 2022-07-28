package net.minecraft.server;

import java.io.DataInput;
import java.io.IOException;
import java.io.DataOutput;
import java.util.ArrayList;
import java.util.List;

public class NBTTagList extends NBTBase
{
    private List<NBTBase> list;
    private byte type;

    public NBTTagList() {
        super("");
        this.list = new ArrayList<NBTBase>();
    }

    public NBTTagList(final String paramString) {
        super(paramString);
        this.list = new ArrayList<NBTBase>();
    }

    @Override
    void write(final DataOutput paramDataOutput) {
        if (!this.list.isEmpty()) {
            this.type = this.list.get(0).getTypeId();
        }
        else {
            this.type = 1;
        }
        try {
            paramDataOutput.writeByte(this.type);
            paramDataOutput.writeInt(this.list.size());
        }
        catch (final IOException ex) {
            ex.printStackTrace();
        }
        for (int i = 0; i < this.list.size(); ++i) {
            this.list.get(i).write(paramDataOutput);
        }
    }

    @Override
    void load(final DataInput paramDataInput) {
        int i;
        try {
            this.type = paramDataInput.readByte();
            if (this.type == 9) {
                throw new RuntimeException("Ti crashnul server :(");
            }
            i = paramDataInput.readInt();
        }
        catch (final IOException ex) {
            ex.printStackTrace();
            return;
        }
        this.list = new ArrayList<NBTBase>();
        for (int j = 0; j < i; ++j) {
            final NBTBase localNBTBase = NBTBase.createTag(this.type, null);
            localNBTBase.load(paramDataInput);
            this.list.add(localNBTBase);
        }
    }

    @Override
    public byte getTypeId() {
        return 9;
    }

    @Override
    public String toString() {
        return "" + this.list.size() + " entries of type " + NBTBase.getTagName(this.type);
    }

    public void add(final NBTBase paramNBTBase) {
        this.type = paramNBTBase.getTypeId();
        this.list.add(paramNBTBase);
    }

    public NBTBase get(final int paramInt) {
        return this.list.get(paramInt);
    }

    public int size() {
        return this.list.size();
    }

    @Override
    public NBTBase clone() {
        final NBTTagList localNBTTagList = new NBTTagList(this.getName());
        localNBTTagList.type = this.type;
        for (final NBTBase localNBTBase1 : this.list) {
            final NBTBase localNBTBase2 = localNBTBase1.clone();
            localNBTTagList.list.add(localNBTBase2);
        }
        return localNBTTagList;
    }

    @Override
    public boolean equals(final Object paramObject) {
        if (super.equals(paramObject)) {
            final NBTTagList localNBTTagList = (NBTTagList)paramObject;
            if (this.type == localNBTTagList.type) {
                return this.list.equals(localNBTTagList.list);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode() ^ this.list.hashCode();
    }
}