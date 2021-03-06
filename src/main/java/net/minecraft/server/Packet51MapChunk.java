package net.minecraft.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Packet51MapChunk extends Packet {

    public int a;
    public int b;
    public int c;
    public int d;
    private byte[] buffer;
    private byte[] inflatedBuffer;
    public boolean e;
    private int size;
    private static byte[] buildBuffer = new byte[196864];
    private static final byte[] unloadSequence = new byte[]{0x78, (byte) 0x9C, 0x63, 0x64, 0x1C, (byte) 0xD9, 0x00, 0x00, (byte) 0x81, (byte) 0x80, 0x01, 0x01}; // Spigot

    public Packet51MapChunk() {
        this.lowPriority = true;
    }

    // Spigot start - add constructor for chunk removals for the client
    public Packet51MapChunk(int x, int z) {
        this.a = x;
        this.b = z;
        this.e = true;
        this.c = 0;
        this.d = 0;
        this.size = unloadSequence.length;
        this.buffer = unloadSequence;
    }
    // Spigot end

    public Packet51MapChunk(Chunk chunk, boolean flag, int i) {
        this.lowPriority = true;
        this.a = chunk.x;
        this.b = chunk.z;
        this.e = flag;
        ChunkMap chunkmap = a(chunk, flag, i);
        Deflater deflater = new Deflater(4); // Spigot 4 -> -1

        this.d = chunkmap.c;
        this.c = chunkmap.b;
        chunk.world.spigotConfig.antiXrayInstance.obfuscateSync(chunk.x, chunk.z, i, chunkmap.a, chunk.world); // Spigot

        try {
            this.inflatedBuffer = chunkmap.a;
            deflater.setInput(chunkmap.a, 0, chunkmap.a.length);
            deflater.finish();
            this.buffer = new byte[chunkmap.a.length];
            this.size = deflater.deflate(this.buffer);
        } finally {
            deflater.end();
        }
    }

    public void a(DataInputStream datainputstream) throws IOException { // CraftBukkit - throws IOException
        this.a = datainputstream.readInt();
        this.b = datainputstream.readInt();
        this.e = datainputstream.readBoolean();
        this.c = datainputstream.readShort();
        this.d = datainputstream.readShort();
        this.size = datainputstream.readInt();
        if (buildBuffer.length < this.size) {
            buildBuffer = new byte[this.size];
        }

        datainputstream.readFully(buildBuffer, 0, this.size);
        int i = 0;

        int j;

        for (j = 0; j < 16; ++j) {
            i += this.c >> j & 1;
        }

        j = 12288 * i;
        if (this.e) {
            j += 256;
        }

        this.inflatedBuffer = new byte[j];
        Inflater inflater = new Inflater();

        inflater.setInput(buildBuffer, 0, this.size);

        try {
            inflater.inflate(this.inflatedBuffer);
        } catch (DataFormatException dataformatexception) {
            throw new IOException("Bad compressed data format");
        } finally {
            inflater.end();
        }
    }

    public void a(DataOutputStream dataoutputstream) throws IOException { // CraftBukkit - throws IOException
        dataoutputstream.writeInt(this.a);
        dataoutputstream.writeInt(this.b);
        dataoutputstream.writeBoolean(this.e);
        dataoutputstream.writeShort((short) (this.c & '\uffff'));
        dataoutputstream.writeShort((short) (this.d & '\uffff'));
        dataoutputstream.writeInt(this.size);
        dataoutputstream.write(this.buffer, 0, this.size);
    }

    public void handle(Connection connection) {
        connection.a(this);
    }

    public int a() {
        return 17 + this.size;
    }

    public static ChunkMap a(Chunk chunk, boolean flag, int i) {
        int j = 0;
        ChunkSection[] achunksection = chunk.i();
        int k = 0;
        ChunkMap chunkmap = new ChunkMap();
        byte[] abyte = buildBuffer;

        if (flag) {
            chunk.seenByPlayer = true;
        }

        int l;

        for (l = 0; l < achunksection.length; ++l) {
            if (achunksection[l] != null && (!flag || !achunksection[l].isEmpty()) && (i & 1 << l) != 0) {
                chunkmap.b |= 1 << l;
                if (achunksection[l].getExtendedIdArray() != null) {
                    chunkmap.c |= 1 << l;
                    ++k;
                }
            }
        }

        for (l = 0; l < achunksection.length; ++l) {
            if (achunksection[l] != null && (!flag || !achunksection[l].isEmpty()) && (i & 1 << l) != 0) {
                byte[] abyte1 = achunksection[l].getIdArray();

                System.arraycopy(abyte1, 0, abyte, j, abyte1.length);
                j += abyte1.length;
            }
        }

        NibbleArray nibblearray;

        for (l = 0; l < achunksection.length; ++l) {
            if (achunksection[l] != null && (!flag || !achunksection[l].isEmpty()) && (i & 1 << l) != 0) {
                nibblearray = achunksection[l].getDataArray();
                // Spigot start
                // System.arraycopy(nibblearray.a, 0, abyte, j, nibblearray.a.length);
                // j += nibblearray.a.length;
                j = nibblearray.copyToByteArray(abyte, j);
                // Spigot end
            }
        }

        for (l = 0; l < achunksection.length; ++l) {
            if (achunksection[l] != null && (!flag || !achunksection[l].isEmpty()) && (i & 1 << l) != 0) {
                nibblearray = achunksection[l].getEmittedLightArray();
                // Spigot start
                // System.arraycopy(nibblearray.a, 0, abyte, j, nibblearray.a.length);
                // j += nibblearray.a.length;
                j = nibblearray.copyToByteArray(abyte, j);
                // Spigot end
            }
        }

        if (!chunk.world.worldProvider.f) {
            for (l = 0; l < achunksection.length; ++l) {
                if (achunksection[l] != null && (!flag || !achunksection[l].isEmpty()) && (i & 1 << l) != 0) {
                    nibblearray = achunksection[l].getSkyLightArray();
                    // Spigot start
                    // System.arraycopy(nibblearray.a, 0, abyte, j, nibblearray.a.length);
                    // j += nibblearray.a.length;
                    j = nibblearray.copyToByteArray(abyte, j);
                    // Spigot end
                }
            }
        }

        if (k > 0) {
            for (l = 0; l < achunksection.length; ++l) {
                if (achunksection[l] != null && (!flag || !achunksection[l].isEmpty()) && achunksection[l].getExtendedIdArray() != null && (i & 1 << l) != 0) {
                    nibblearray = achunksection[l].getExtendedIdArray();
                    // Spigot start
                    //System.arraycopy(nibblearray.a, 0, abyte, j, nibblearray.a.length);
                    //j += nibblearray.a.length;
                    j = nibblearray.copyToByteArray(abyte, j);
                    // Spigot end
                }
            }
        }

        if (flag) {
            byte[] abyte2 = chunk.m();

            System.arraycopy(abyte2, 0, abyte, j, abyte2.length);
            j += abyte2.length;
        }

        chunkmap.a = new byte[j];
        System.arraycopy(abyte, 0, chunkmap.a, 0, j);
        return chunkmap;
    }
}
