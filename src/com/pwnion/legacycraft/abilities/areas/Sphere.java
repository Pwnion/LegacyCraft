package com.pwnion.legacycraft.abilities.areas;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class Sphere {
	public static ArrayList<Block> get(Location pos, double radius) {
        ArrayList<Block> sphere = new ArrayList<Block>();
        radius += 0.5;

        final double invRadius = 1 / radius;
        final int ceilRadius = (int) Math.ceil(radius);

        double nextXn = 0;
        forX: for (int x = 0; x <= ceilRadius; ++x) {
            final double xn = nextXn;
            nextXn = (x + 1) * invRadius;
            double nextYn = 0;
            forY: for (int y = 0; y <= ceilRadius; ++y) {
                final double yn = nextYn;
                nextYn = (y + 1) * invRadius;
                double nextZn = 0;
                forZ: for (int z = 0; z <= ceilRadius; ++z) {
                    final double zn = nextZn;
                    nextZn = (z + 1) * invRadius;

                    double distanceSq = lengthSq(xn, yn, zn);
                    if (distanceSq > 1) {
                        if (z == 0) {
                            if (y == 0) {
                                break forX;
                            }
                            break forY;
                        }
                        break forZ;
                    }

                    sphere.add(pos.add(x, y, z).getBlock());
                    sphere.add(pos.add(-x, y, z).getBlock());
                    sphere.add(pos.add(x, -y, z).getBlock());
                    sphere.add(pos.add(x, y, -z).getBlock());
                    sphere.add(pos.add(-x, -y, z).getBlock());
                    sphere.add(pos.add(x, -y, -z).getBlock());
                    sphere.add(pos.add(-x, y, -z).getBlock());
                    sphere.add(pos.add(-x, -y, -z).getBlock());
                }
            }
        }

        return sphere;
    }
	
	private static double lengthSq(double x, double y, double z) {
        return (x * x) + (y * y) + (z * z);
    }
}
