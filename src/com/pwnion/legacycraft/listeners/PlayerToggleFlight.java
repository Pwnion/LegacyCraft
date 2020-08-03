package com.pwnion.legacycraft.listeners;

import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;

import com.pwnion.legacycraft.PlayerData;
import com.pwnion.legacycraft.abilities.SkillTree;
import com.pwnion.legacycraft.abilities.SkillTree.Aspect;
import com.pwnion.legacycraft.abilities.jumps.RogueJump1;
import com.pwnion.legacycraft.abilities.jumps.RogueJump2;
import com.pwnion.legacycraft.abilities.jumps.ShamanJump1;
import com.pwnion.legacycraft.abilities.jumps.ShamanJump2;
import com.pwnion.legacycraft.abilities.jumps.StrikerJump1;
import com.pwnion.legacycraft.abilities.jumps.StrikerJump2;
import com.pwnion.legacycraft.abilities.jumps.VanguardJump1;
import com.pwnion.legacycraft.abilities.jumps.VanguardJump2;

public class PlayerToggleFlight implements Listener {
	
	@EventHandler
	public void onPlayerToggleFlight(PlayerToggleFlightEvent e) {
		Player p = e.getPlayer();
		UUID playerUUID = p.getUniqueId();
		
		SkillTree skillTree = PlayerData.getSkillTree(playerUUID);

		//Handle ability jumps
		if(p.getGameMode().equals(GameMode.ADVENTURE)) {
			e.setCancelled(true);
			if(e.isFlying() && !skillTree.getAspect().equals(Aspect.NONE)) {
				switch(skillTree.getPlayerClass()) {
					case STRIKER:
						switch(skillTree.getEquippedJump()) {
						case ONE:
							StrikerJump1.activate(p);
							break;
						case TWO:
							StrikerJump2.activate(p);
							break;
						default:
							break;
						}
						break;
						
					case VANGUARD:
						switch(skillTree.getEquippedJump()) {
						case ONE:
							VanguardJump1.activate(p);
							break;
						case TWO:
							VanguardJump2.activate(p);
							break;
						default:
							break;
						}
						break;
						
					case ROGUE:
						switch(skillTree.getEquippedJump()) {
						case ONE:
							RogueJump1.activate(p);
							break;
						case TWO:
							RogueJump2.activate(p);
							break;
						default:
							break;
						}
						break;
						
					case SHAMAN:
						switch(skillTree.getEquippedJump()) {
						case ONE:
							ShamanJump1.activate(p);
							break;
						case TWO:
							ShamanJump2.activate(p);
							break;
						default:
							break;
						}
						break;
				default:
					break;
				}
			}
		}
	}	
}
