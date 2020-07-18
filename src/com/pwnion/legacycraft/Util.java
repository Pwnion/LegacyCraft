package com.pwnion.legacycraft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

public class Util {
	
	public static final void br(String message) {
		Bukkit.broadcastMessage("[C] " + message);
	}
	
	public static final void br(Object message) {
		if(message.equals(null)) {
			br("null");
		} else {
			br(message.toString());
		}
	}
	
	public static final void print(Exception e) {
		e.printStackTrace();
		StackTraceElement st[] = e.getStackTrace();
	    for(int i = 8; i != 0; i--) {
	    	StackTraceElement el = e.getStackTrace()[i];
	    	Util.br(el.toString());
	    }
	    Util.br(e.toString());
	}
	
	private static final char[] posbChars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	
	//Generates UID with 36^n combinations using alphanumeric characters
	public static String generateUID(int length) {
		Random rnd = new Random();
		String out = "";
	    for(int i = 0; i < length; i++) {
	    	out += posbChars[rnd.nextInt(posbChars.length)];
	    }
	    return out;
	}
	
	public static final void spawnBlocks(HashSet<Block> blocks) {
		for(Block block : blocks) {
			block.setType(Material.STONE);
		}
	}
	
	public static final Vector getRelativeVec(Location centre, Location pos) {
		if(centre.getWorld() != pos.getWorld()) {
			return null;
		}

		return new Vector(pos.getX() - centre.getX(), pos.getY() - centre.getY(), pos.getZ() - centre.getZ());
	}
	
	public static final HashSet<Vector> getRelativeVecArea(Location centre, Collection<Location> area) {
		HashSet<Vector> relativeArea = new HashSet<Vector>();
		for(Location loc : area) {
			relativeArea.add(getRelativeVec(centre, loc));
		}
		return relativeArea;
	}
	
	public static final Location getRelativeLoc(Location centre, Location pos) {
		if(centre.getWorld() != pos.getWorld()) {
			return null;
		}

		return new Location(pos.getWorld(), pos.getX() - centre.getX(), pos.getY() - centre.getY(), pos.getZ() - centre.getZ());
	}
	
	public static final HashSet<Block> approxBlock(World world, Vector vector) {
		HashSet<Block> output = new HashSet<Block>();
		for(BlockVector blockVec : approxBlock(vector)) {
			output.add(blockVec.toLocation(world).getBlock());
		}
		return output;
	}
	
	public static final HashSet<Block> approxBlocks(World world, Collection<Vector> vectors) {
		HashSet<Block> output = new HashSet<Block>();
		for(Vector vector : vectors) {
			output.addAll(approxBlock(world, vector));
		}
		return output;
	}
	
	public static final HashSet<BlockVector> approxBlocks(Collection<Vector> vectors) {
		HashSet<BlockVector> output = new HashSet<BlockVector>();
		for(Vector vector : vectors) {
			output.addAll(approxBlock(vector));
		}
		return output;
	}
	
	public static final HashSet<BlockVector> approxBlock(Vector vector) {
		HashSet<BlockVector> output = new HashSet<BlockVector>();
		
		double overlapAmount = 0.2;
		
		double x = vector.getX();
		double y = vector.getY();
		double z = vector.getZ();
		
		int blockX = vector.getBlockX();
		int blockY = vector.getBlockY();
		int blockZ = vector.getBlockZ();
		
		double localX = x - blockX;
		double localY = y - blockY;
		double localZ = z - blockZ;
		
		int modX = 1;
		int modY = 1;
		int modZ = 1;
		
		if(x < 0) {
			localX -= blockX * 2;
			modX = -1;
		}
		
		if(y < 0) {
			localY -= blockY * 2;
			modY = -1;
		}
		
		if(z < 0) {
			localZ -= blockZ * 2;
			modZ = -1;
		}
		
		output.add(vector.toBlockVector());
		
		if(localX > 1 - overlapAmount) {
			output.add(new BlockVector(blockX + modX, blockY, blockZ));
		}
		
		if(localY > 1 - overlapAmount) {
			output.add(new BlockVector(blockX, blockY + modY, blockZ));
		}
		
		if(localZ > 1 - overlapAmount) {
			output.add(new BlockVector(blockX, blockY, blockZ + modZ));
		}
		
		modX *= -1;
		modY *= -1;
		modZ *= -1;
		
		if(localX < overlapAmount) {
			output.add(new BlockVector(blockX + modX, blockY, blockZ));
		}
		
		if(localY < overlapAmount) {
			output.add(new BlockVector(blockX, blockY + modY, blockZ));
		}
		
		if(localZ < overlapAmount) {
			output.add(new BlockVector(blockX, blockY, blockZ + modZ));
		}
		
		return output;
	}
	
	public static final HashSet<Block> getBlocks(Collection<Location> area) {
		HashSet<Block> output = new HashSet<Block>();
		for(Location loc : area) {
			output.add(loc.getBlock());
		}
		return output;
	}
	
	public static final HashSet<Location> getLocations(Collection<Block> area) {
		HashSet<Location> output = new HashSet<Location>();
		for(Block loc : area) {
			output.add(loc.getLocation());
		}
		return output;
	}

	public static final Vector vectorCalc(double yaw, double pitch, double dist) {
		pitch = Math.toRadians(pitch + 90);
		yaw  = Math.toRadians(yaw + 90);
		double x = Math.sin(pitch) * Math.cos(yaw);
		double y = Math.cos(pitch);
		double z = Math.sin(pitch) * Math.sin(yaw);
		Vector vector = new Vector(x, y, z);
		vector.multiply(dist);
		return vector;
	}

	public static final Vector vectorCalc(Entity e, double dist) {
		return vectorCalc(e.getLocation().getYaw(), e.getLocation().getPitch(), dist);
	}

	public static final Vector vectorCalc(Location pos1, Location pos2) {
		if(pos1.getWorld() != pos2.getWorld()) {
			return null;
		}
		return new Vector(pos2.getX() - pos1.getX(), pos2.getY() - pos1.getY(), pos2.getZ() - pos1.getZ());
	}
	
	public static final double getYaw(Vector vec) {
		double yaw = 0;
		if(vec.getX() > 0) {
			yaw = Math.toDegrees(vec.angle(new Vector(0, 0, -1))) + 180;
		} else {
			yaw = Math.toDegrees(vec.angle(new Vector(0, 0, 1)));
		}
		return yaw;
	}
	
	public static final double getPitch(Vector vec) {
		return Math.toDegrees(vec.angle(new Vector(0, 1, 0))) - 90;
	}

	public static final Location addY(Location loc, double plusY) {
		return loc.clone().add(0, plusY, 0);
	}

	public static final Vector addY(Vector vec, double plusY) {
		return vec.clone().add(new Vector(0, plusY, 0));
	}

	public static final ArrayList<Location> spiral(Location centre, double radius, double rotation, int steps) {

		double radiusPerStep = radius / (steps - 1);
		double rotPerStep = Math.toRadians(rotation / (steps - 1));

		Vector axis = vectorCalc(centre.getYaw(), centre.getPitch(), 1);
		Vector up = new Vector(0, 1, 0);
		Vector cross = axis.clone().crossProduct(up);
		if(cross.length() == 0) {
			cross = new Vector(1, 0, 0);
		}

		up.rotateAroundAxis(cross, Math.toRadians(90) - axis.angle(up));

		ArrayList<Location> spiral = new ArrayList<Location>(steps + 1);

		for(int i = 0; i <= steps; i++) {
			Vector pointer = up.clone();
			pointer.rotateAroundAxis(axis, rotPerStep * i);
			pointer.multiply(radiusPerStep * i);

			spiral.add(centre.clone().add(pointer));
		}
		
		return spiral;
	}

	public static final ArrayList<Location> circle(Location centre, Vector pointer, double rotation, int steps) {
		ArrayList<Location> circle = new ArrayList<Location>(steps + 1);

		double rotPerStep = Math.toRadians(rotation / (steps - 1));

		Vector axis = vectorCalc(centre.getYaw(), centre.getPitch(), 1);

		for(int i = 0; i<= steps; i++) {
			Vector pointerClone = pointer.clone();
			pointerClone.rotateAroundAxis(axis, rotPerStep * i);

			circle.add(centre.clone().add(pointerClone));
		}

		return circle;
	}

	public static final void playSoundWithoutConflict(Location centre, Sound sound, SoundCategory soundCategory, float volume, float pitch, double radius) {
		centre.getNearbyEntities(radius, radius, radius).forEach((e) -> {
			if(e instanceof Player) {
				((Player) e).playSound(centre, sound, soundCategory, volume, pitch);
			}
		});
	}
	
	public static final void playSoundWithoutConflict(Location centre, Sound sound, SoundCategory soundCategory, float volume, float pitch) {
		double radius = volume < 1f ? 16D : 16D * volume;
		playSoundWithoutConflict(centre, sound, soundCategory, volume, pitch, radius);
	}
	
	public static final void playSoundWithoutConflict(Location centre, Sound sound, float volume, float pitch) {
		playSoundWithoutConflict(centre, sound, SoundCategory.PLAYERS, volume, pitch);
	}
	
	public static final void stopSurroundingSound(Sound sound, SoundCategory soundCategory, Location centre, double radius) {
		centre.getNearbyEntities(radius, radius, radius).forEach((e) -> {
			if(e instanceof Player) {
				((Player) e).stopSound(sound, soundCategory);
			}
		});
	}
	
	public static final void stopSurroundingSound(Location centre, Sound sound, double radius) {
		stopSurroundingSound(sound, SoundCategory.PLAYERS, centre, radius);
	}
	
	public static final void stopSurroundingSound(Location centre, Sound sound) {
		stopSurroundingSound(sound, SoundCategory.PLAYERS, centre, 16);
	}
}
