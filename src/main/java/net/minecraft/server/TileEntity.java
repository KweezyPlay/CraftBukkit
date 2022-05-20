package net.minecraft.server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.spigotmc.CustomTimingsHandler; // Spigot
import org.bukkit.inventory.InventoryHolder; // CraftBukkit

public class TileEntity {

    public CustomTimingsHandler tickTimer = org.bukkit.craftbukkit.SpigotTimings.getTileEntityTimings(this); // Spigot
    private static Map a = new HashMap();
    private static Map b = new HashMap();
    protected World world;
    public int x;
    public int y;
    public int z;
    protected boolean o;
    public int p = -1;
    public Block q;

    public TileEntity() {}

    private static void a(Class oclass, String s) {
        if (a.containsKey(s)) {
            throw new IllegalArgumentException("Duplicate id: " + s);
        } else {
            a.put(s, oclass);
            b.put(oclass, s);
        }
    }

    public void b(World world) {
        this.world = world;
    }

    public World getWorld() {
        return this.world;
    }

    public boolean o() {
        return this.world != null;
    }

    public void a(NBTTagCompound nbttagcompound) {
        this.x = nbttagcompound.getInt("x");
        this.y = nbttagcompound.getInt("y");
        this.z = nbttagcompound.getInt("z");
    }

    public void b(NBTTagCompound nbttagcompound) {
        String s = (String) b.get(this.getClass());

        if (s == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        } else {
            nbttagcompound.setString("id", s);
            nbttagcompound.setInt("x", this.x);
            nbttagcompound.setInt("y", this.y);
            nbttagcompound.setInt("z", this.z);
        }
    }

    public void h() {}

    public static TileEntity c(NBTTagCompound nbttagcompound) {
        TileEntity tileentity = null;

        try {
            Class oclass = (Class) a.get(nbttagcompound.getString("id"));

            if (oclass != null) {
                tileentity = (TileEntity) oclass.newInstance();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if (tileentity != null) {
            tileentity.a(nbttagcompound);
        } else {
            MinecraftServer.getServer().getLogger().warning("Skipping TileEntity with id " + nbttagcompound.getString("id"));
        }

        return tileentity;
    }

    public int p() {
        if (this.p == -1) {
            this.p = this.world.getData(this.x, this.y, this.z);
        }

        return this.p;
    }

    public void update() {
        if (this.world != null) {
            this.p = this.world.getData(this.x, this.y, this.z);
            this.world.b(this.x, this.y, this.z, this);
            if (this.q() != null) {
                this.world.m(this.x, this.y, this.z, this.q().id);
            }
        }
    }

    public Block q() {
        if (this.q == null) {
            this.q = Block.byId[this.world.getTypeId(this.x, this.y, this.z)];
        }

        return this.q;
    }

    public Packet getUpdatePacket() {
        return null;
    }

    public boolean r() {
        return this.o;
    }

    public void w_() {
        this.o = true;
    }

    public void s() {
        this.o = false;
    }

    public boolean b(int i, int j) {
        return false;
    }

    public void i() {
        this.q = null;
        this.p = -1;
    }

    public void a(CrashReportSystemDetails crashreportsystemdetails) {
        crashreportsystemdetails.a("Name", (Callable) (new CrashReportTileEntityName(this)));
        CrashReportSystemDetails.a(crashreportsystemdetails, this.x, this.y, this.z, this.q().id, this.p());
        crashreportsystemdetails.a("Actual block type", (Callable) (new CrashReportTileEntityType(this)));
        crashreportsystemdetails.a("Actual block data value", (Callable) (new CrashReportTileEntityData(this)));
    }

    static Map t() {
        return b;
    }

    static {
        a(TileEntityFurnace.class, "Furnace");
        a(TileEntityChest.class, "Chest");
        a(TileEntityEnderChest.class, "EnderChest");
        a(TileEntityRecordPlayer.class, "RecordPlayer");
        a(TileEntityDispenser.class, "Trap");
        a(TileEntityDropper.class, "Dropper");
        a(TileEntitySign.class, "Sign");
        a(TileEntityMobSpawner.class, "MobSpawner");
        a(TileEntityNote.class, "Music");
        a(TileEntityPiston.class, "Piston");
        a(TileEntityBrewingStand.class, "Cauldron");
        a(TileEntityEnchantTable.class, "EnchantTable");
        a(TileEntityEnderPortal.class, "Airportal");
        a(TileEntityCommand.class, "Control");
        a(TileEntityBeacon.class, "Beacon");
        a(TileEntitySkull.class, "Skull");
        a(TileEntityLightDetector.class, "DLDetector");
        a(TileEntityHopper.class, "Hopper");
        a(TileEntityComparator.class, "Comparator");
    }

    // CraftBukkit start
    public InventoryHolder getOwner() {
        org.bukkit.block.BlockState state = world.getWorld().getBlockAt(x, y, z).getState();
        if (state instanceof InventoryHolder) return (InventoryHolder) state;
        return null;
    }
    // CraftBukkit end
}
