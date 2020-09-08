package com.pwnion.legacycraft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.regex.*;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

import com.pwnion.legacycraft.items.ItemStat;

public class Util {
	
	/**
	 * Broadcasts a message to chat
	 * 
	 * @param message
	 */
	public static void br(String message) {
		Bukkit.broadcastMessage("[C] " + message);
	}
	
	/**
	 * Broadcasts a message to chat
	 * 
	 * @param message
	 */
	public static void br(Object message) {
		if(message == null) {
			br("null");
		} else {
			br(message.toString());
		}
	}
	
	/**
	 * Prints an exception to chat
	 * 
	 * @param e
	 */
	public static void print(Exception e) {
		e.printStackTrace();
		StackTraceElement st[] = e.getStackTrace();
	    for(int i = 8; i != 0; i--) {
	    	StackTraceElement el = e.getStackTrace()[i];
	    	Util.br(el.toString());
	    }
	    Util.br(e.toString());
	}
	
	/**
	 * gets the Enum from a string. IgnoresCase, Ignores Leading/Trailing whitespace, Spaces treated as '_'
	 * 
	 * @param name
	 * @return
	 * 
	 * @Nullable if none found
	 */
	public static <T extends Enum<T>> T getEnumFromString(Class<T> c, String string) {
		string = string.trim().replace(" ", "_");
	    for (T enumValue : c.getEnumConstants()) {
	        if (enumValue.name().equalsIgnoreCase(string)) {
	            return enumValue;
	        }
	    }
	    return null;
	}
	
	/**
	 * Random double between the given range [min,max)
	 * 
	 * @param min	minimum value inclusive
	 * @param max	maximum value exclusive
	 * @return		random double
	 * 
	 * @throws IllegalArgumentException when min >= max
	 * 
	 * @see Math.random()
	 */
	public static double random(double min, double max) throws IllegalArgumentException {
		if(min >= max) {
			throw new IllegalArgumentException("The maximum must be greater than the minimum!");
		}
		return min + (Math.random() * (max - min));
	}
	
	/**
	 * Random int between the given range [min,max]
	 * 
	 * @param min	minimum value inclusive
	 * @param max	maximum value inclusive
	 * @return
	 * 
	 * @throws IllegalArgumentException when min >= max
	 */
	public static int randomInt(int min, int max) throws IllegalArgumentException {
		if(min >= max) {
			throw new IllegalArgumentException("The maximum must be greater than the minimum!");
		}
		Random rnd = new Random();
		return min + rnd.nextInt(max - min + 1);
	}
	
	/**
	 * Whole string is lowercase except those after spaces or underscores (including first character)
	 * 
	 * @param input
	 * @return
	 */
	public static String toTitleCase(String input) {
		String out = "";
		for(String str : split(input, " |_")) {
			out += str.toString().substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
		}
		return out;
	}
	
	/**
     * Splits a String according to a regex, keeping the splitter at the end of each substring
     * 
     * @param input The input String
     * @param regex The regular expression upon which to split the input
     * @return An array of Strings
     */
	public static String[] split(String input, String regex) {
        ArrayList<String> res = new ArrayList<String>();
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(input);
        int pos = 0;
        while (m.find()) {
            res.add(input.substring(pos, m.end()));
            pos = m.end();
        }
        if(pos < input.length()) {
        	res.add(input.substring(pos));
        }
        return res.toArray(new String[res.size()]);
    }
	
	/**
	 * Returns a new String composed of copies of the Object elements as strings joined together with a copy of the specified delimiter. 
	 * 
	 * @param delimiter
	 * @param elements
	 * @return
	 */
	public static String join(String delimiter, Object[] elements) {
		String out = "";
		for (Object obj : elements) {
			out += obj.toString() + delimiter;
		}
		return out.substring(0, out.length() - delimiter.length());
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
	
	//TEMP, used for testing Areas
	public static void spawnBlocks(HashSet<Block> blocks) {
		for(Block block : blocks) {
			block.setType(Material.STONE);
		}
	}
	
	/**
	 * Gets the vector that points from 'centre' to 'pos'
	 * 
	 * @param centre
	 * @param pos
	 * @return
	 */
	public static final Vector getRelativeVec(Location centre, Location pos) {
		if(centre.getWorld() != pos.getWorld()) {
			return null;
		}

		return new Vector(pos.getX() - centre.getX(), pos.getY() - centre.getY(), pos.getZ() - centre.getZ());
	}
	
	/**
	 * Gets a set of each of the vectors that point from 'centre' to each location in the area. <br>
	 * Subtracts the centre location from each point in area, returns results as vectors.
	 * 
	 * @param centre
	 * @param pos
	 * @return
	 */
	public static final HashSet<Vector> getRelativeVecArea(Location centre, Collection<Location> area) {
		HashSet<Vector> relativeArea = new HashSet<Vector>();
		for(Location loc : area) {
			relativeArea.add(getRelativeVec(centre, loc));
		}
		return relativeArea;
	}
	
	/**
	 * Checks if each vector is pointing to a location close to the edge of a block. If so also returns the block next to that edge.
	 * 
	 * @param vector
	 * @return
	 */
	public static final HashSet<BlockVector> approxBlocks(Collection<Vector> vectors) {
		HashSet<BlockVector> output = new HashSet<BlockVector>();
		for(Vector vector : vectors) {
			output.addAll(approxBlock(vector));
		}
		return output;
	}
	
	/**
	 * When the vector is pointing to a location close to the edge of a block also returns the block next to that edge.
	 * 
	 * @param vector
	 * @return
	 */
	public static final HashSet<BlockVector> approxBlock(Vector vector) {
		HashSet<BlockVector> output = new HashSet<BlockVector>();
		
		final double overlapAmount = 0.2;
		
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
	
	public static HashSet<Location> getLocations(Collection<Block> area) {
		HashSet<Location> output = new HashSet<Location>();
		for(Block loc : area) {
			output.add(loc.getLocation());
		}
		return output;
	}

	public static Vector vectorCalc(double pitch, double yaw, double dist) {
		pitch = Math.toRadians(pitch + 90);
		yaw  = Math.toRadians(yaw + 90);
		double x = Math.sin(pitch) * Math.cos(yaw);
		double y = Math.cos(pitch);
		double z = Math.sin(pitch) * Math.sin(yaw);
		Vector vector = new Vector(x, y, z);
		vector.multiply(dist);
		return vector;
	}
	
	public static Vector vectorCalc(Location loc, double dist) {
		return vectorCalc(loc.getPitch(), loc.getYaw(), dist);
	}

	public static Vector vectorCalc(Entity e, double dist) {
		return vectorCalc(e.getLocation().getPitch(), e.getLocation().getYaw(), dist);
	}
	
	/**
	 * Returns a new location that adds the vector the location is facing (pitch, yaw) by the distance
	 * 
	 * @param loc
	 * @param dist
	 * @return
	 */
	public static Location locationCalc(Location loc, double dist) {
		return loc.clone().add(vectorCalc(loc, dist));
	}
	
	/**
	 * Returns a new location that adds the vector the location is facing (yaw only) by the distance plus the yMod
	 * 
	 * @param loc
	 * @param dist
	 * @param yMod
	 * @return
	 */
	public static Location locationCalc(Location loc, double dist, double yMod) {
		return loc.clone().add(vectorCalc(0, loc.getYaw(), dist).setY(yMod));
	}
	
	public static double getYaw(Vector vec) {
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

	public static final Vector addY(Vector vec, double plusY) {
		return vec.clone().add(new Vector(0, plusY, 0));
	}

	public static final ArrayList<Location> spiral(Location centre, double radius, double rotation, int steps) {

		double radiusPerStep = radius / (steps - 1);
		double rotPerStep = Math.toRadians(rotation / (steps - 1));

		Vector axis = vectorCalc(centre.getPitch(), centre.getYaw(), 1);
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

		Vector axis = vectorCalc(centre.getPitch(), centre.getYaw(), 1);

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
