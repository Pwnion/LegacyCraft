package com.pwnion.legacycraft.abilities.jumps;

import java.util.ArrayList;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.pwnion.legacycraft.abilities.targets.Point;

public class RogueJump2 extends Jump {
	//Initiates the second Rogue jump for a player
	public static void activate(Player p) {
		Location targetLoc = Point.fromEntityInFacingDir(p, 6, 4);
		
		ArrayList<Vector> dirs = new ArrayList<Vector>() {
			private static final long serialVersionUID = 1L;
			{
				add(targetLoc.getX() > p.getLocation().getX() ? new Vector(1, 0, 0) : new Vector(-1, 0, 0));
				add(targetLoc.getZ() > p.getLocation().getZ() ? new Vector(0, 0, 1) : new Vector(0, 0, -1));
				add(new Vector(0, 1, 0));
			}
		};
		
		if(p.getLocation().getBlock().getType().isSolid() && p.getTargetBlock(null, 100).getType().isSolid()) {
			p.sendMessage(ChatColor.RED + "Not enough space to blink!");
		} else {
			ArrayList<Location> dests = new ArrayList<Location>(3);
			for(int i = 0; i < 3; i++) {
				Location newLoc = p.getLocation();
				Location testLoc;
				Location oldLoc;
				
				int jLoopToSkip = -1;
				int skipCounter = 0;
				if(newLoc.getBlockX() == targetLoc.getBlockX()) {
					jLoopToSkip = i;
				} else if(newLoc.getBlockZ() == targetLoc.getBlockZ()) {
					jLoopToSkip = (i + 1 > 2 ? 0 : i + 1);
				}
				
				while(skipCounter != 3) {
					for(int j = 0; j < 3; j++) {
						while(true) {
							testLoc = newLoc.clone().add(dirs.get(j));
							
							if((j == jLoopToSkip) ||
									
							   (j == i && testLoc.getBlockX() == targetLoc.getBlockX()) || 
							   (j == (i + 1 > 2 ? 0 : i + 1) && testLoc.getBlockZ() == targetLoc.getBlockZ()) ||
							   (j == (2 - i * 2 == -2 ? 1 : 2 - i * 2) && testLoc.getBlockY() == targetLoc.getBlockY()) ||
							   
							   (testLoc.clone().add(new Vector(0, 1, 0)).getBlock().getType().isSolid()) ||
							   (testLoc.getBlock().getType().isSolid()))
							{
								skipCounter++;
								break;
							} else {
								oldLoc = newLoc;
								newLoc = testLoc;
								skipCounter = 0;
							}
						}
						if(skipCounter == 3) break;
					}
				}
				Vector dir0 = dirs.get(0);
				Vector dir1 = dirs.get(1);
				dirs.set(0, dirs.get(2));
				dirs.set(1, dir0);
				dirs.set(2, dir1);
				
				dests.add(newLoc);
			}
			
			Supplier<Location> closestDestToTarget = () -> {
				Location loc = dests.get(0).distance(targetLoc) < dests.get(1).distance(targetLoc) ? dests.get(0) : dests.get(1);
				loc = dests.get(2).distance(targetLoc) < loc.distance(targetLoc) ? dests.get(2) : loc;
				
				return loc;
			};
			
			final Location finalLoc = closestDestToTarget.get();
			if(p.getLocation().getBlock().equals(finalLoc.getBlock())) {
				p.sendMessage(ChatColor.RED + "Not enough space to blink!");
			} else {
				ArrayList<Block> surroundingFinal = new ArrayList<Block>();
				new ArrayList<Vector>() {
					private static final long serialVersionUID = 1L;
					{
						add(new Vector(0.5, 0, 0.5));
						add(new Vector(0.5, 0, -0.5));
						add(new Vector(-0.5, 0, 0.5));
						add(new Vector(-0.5, 0, -0.5));
					}
				}.forEach((v) -> {
					surroundingFinal.add(finalLoc.clone().add(v).getBlock());
				});

				Location adjustedFinalLoc = finalLoc.clone();
				for(Block block : surroundingFinal) {
					if(block.getType().isSolid() || block.getRelative(BlockFace.UP, 1).getType().isSolid()) {
						adjustedFinalLoc = finalLoc.getBlock().getLocation().add(0.5, 0, 0.5);
						break;
					}
					if(block.getRelative(BlockFace.UP, 1).getType().isSolid()) {
						adjustedFinalLoc.setY(block.getRelative(BlockFace.DOWN).getLocation().getY());
						break;
					}
				}
				
				adjustedFinalLoc.setYaw(p.getLocation().getYaw());
				adjustedFinalLoc.setPitch(p.getLocation().getPitch());
				
				p.teleport(adjustedFinalLoc);
			}
			
			activate(p, 0.5f, 0.4f, 2, Sound.ITEM_CHORUS_FRUIT_TELEPORT, 0.1f, 1f);
		}
	}
}
