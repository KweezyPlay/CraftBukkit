package net.minecraft.server;

import java.util.Random;

public class BlockCrops extends BlockFlower {

    protected BlockCrops(int i) {
        super(i);
        this.b(true);
        float f = 0.5F;

        this.a(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.25F, 0.5F + f);
        this.a((CreativeModeTab) null);
        this.c(0.0F);
        this.a(Block.i); // CraftBukkit - i -> Block.i, decompile error
        this.D();
    }

    protected boolean f_(int i) {
        return i == Block.SOIL.id;
    }

    public void a(World world, int i, int j, int k, Random random) {
        super.a(world, i, j, k, random);
        if (world.getLightLevel(i, j + 1, k) >= 9) {
            int l = world.getData(i, j, k);

            if (l < 7) {
                float f = this.k(world, i, j, k);

                if (random.nextInt((int) (world.growthOdds / world.spigotConfig.wheatModifier * (25.0F / f)) + 1) == 0) { // Spigot
                    org.bukkit.craftbukkit.event.CraftEventFactory.handleBlockGrowEvent(world, i, j, k, this.id, ++l); // CraftBukkit
                }
            }
        }
    }

    public void e_(World world, int i, int j, int k) {
        int l = world.getData(i, j, k) + MathHelper.nextInt(world.random, 2, 5);

        if (l > 7) {
            l = 7;
        }

        world.setData(i, j, k, l, 2);
    }

    private float k(World world, int i, int j, int k) {
        float f = 1.0F;
        int l = world.getTypeId(i, j, k - 1);
        int i1 = world.getTypeId(i, j, k + 1);
        int j1 = world.getTypeId(i - 1, j, k);
        int k1 = world.getTypeId(i + 1, j, k);
        int l1 = world.getTypeId(i - 1, j, k - 1);
        int i2 = world.getTypeId(i + 1, j, k - 1);
        int j2 = world.getTypeId(i + 1, j, k + 1);
        int k2 = world.getTypeId(i - 1, j, k + 1);
        boolean flag = j1 == this.id || k1 == this.id;
        boolean flag1 = l == this.id || i1 == this.id;
        boolean flag2 = l1 == this.id || i2 == this.id || j2 == this.id || k2 == this.id;

        for (int l2 = i - 1; l2 <= i + 1; ++l2) {
            for (int i3 = k - 1; i3 <= k + 1; ++i3) {
                int j3 = world.getTypeId(l2, j - 1, i3);
                float f1 = 0.0F;

                if (j3 == Block.SOIL.id) {
                    f1 = 1.0F;
                    if (world.getData(l2, j - 1, i3) > 0) {
                        f1 = 3.0F;
                    }
                }

                if (l2 != i || i3 != k) {
                    f1 /= 4.0F;
                }

                f += f1;
            }
        }

        if (flag2 || flag && flag1) {
            f /= 2.0F;
        }

        return f;
    }

    public int d() {
        return 6;
    }

    protected int j() {
        return Item.SEEDS.id;
    }

    protected int k() {
        return Item.WHEAT.id;
    }

    public void dropNaturally(World world, int i, int j, int k, int l, float f, int i1) {
        super.dropNaturally(world, i, j, k, l, f, 0);
        if (!world.isStatic) {
            if (l >= 7) {
                int j1 = 3 + i1;

                for (int k1 = 0; k1 < j1; ++k1) {
                    if (world.random.nextInt(15) <= l) {
                        this.b(world, i, j, k, new ItemStack(this.j(), 1, 0));
                    }
                }
            }
        }
    }

    public int getDropType(int i, Random random, int j) {
        return i == 7 ? this.k() : this.j();
    }

    public int a(Random random) {
        return 1;
    }
}
