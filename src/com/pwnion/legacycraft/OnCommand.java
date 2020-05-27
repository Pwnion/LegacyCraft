package com.pwnion.legacycraft;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import com.pwnion.legacycraft.abilities.areas.Selection;
import com.pwnion.legacycraft.abilities.inventory.CharacterBuildMenuInv;
import com.pwnion.legacycraft.abilities.ooc.Portal;
import com.pwnion.legacycraft.abilities.proficiencies.AquaVanguardProficiency1;
import com.pwnion.legacycraft.abilities.proficiencies.TerraVanguardProficiency1;
import com.pwnion.legacycraft.abilities.targets.Point;
import com.pwnion.legacycraft.levelling.Experience;
import com.pwnion.legacycraft.levelling.ExperienceType;
import com.pwnion.legacycraft.npcs.NPCHomeWork;
import com.pwnion.legacycraft.quests.Quest;
import com.pwnion.legacycraft.quests.QuestBook;
import com.pwnion.legacycraft.quests.QuestManager;

public class OnCommand implements CommandExecutor {
	private static final String deniedMsg = ChatColor.RED + "I'm sorry, but you do not have permission to perform this command.";
	private static final HashMap<UUID, Selection> playerToSelection = new HashMap<UUID, Selection>();

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String lbl, String[] args) {
		Player p;
		UUID playerUUID;
		Point ep;
		//Ensure the command sender is a player
		if(cs instanceof Player) {
			p = (Player) cs;
			playerUUID = p.getUniqueId();
		} else {
			return false;
		}

		//Manage legacy craft commands
		if(p.hasPermission("legacycraft.op")) {
			if(lbl.equals("legacycraft") || cmd.getAliases().contains(lbl)) {
				if(args.length == 0) {
					p.sendMessage(ChatColor.RED + "Try being more specific...");
					return false;
				} else {
					switch(args[0].toLowerCase()) {
					case "class":
						if(p.getGameMode().equals(GameMode.ADVENTURE)) {
							CharacterBuildMenuInv.load(p);
						} else {
							p.sendMessage(ChatColor.RED + "You must be in adventure mode to do that!");
						}
						break;
					case "pos1":
						if(!playerToSelection.keySet().contains(playerUUID)) {
							playerToSelection.put(playerUUID, new Selection(p));
						}
						p.sendMessage(playerToSelection.get(playerUUID).setPos1());
						break;
					case "pos2":
						if(!playerToSelection.keySet().contains(playerUUID)) {
							playerToSelection.put(playerUUID, new Selection(p));
						}
						p.sendMessage(playerToSelection.get(playerUUID).setPos2());
						break;
					case "export":
						p.sendMessage(playerToSelection.get(playerUUID).export(args[1]));
						break;
					case "portal":
						try {
							Portal.valueOf(args[1].toUpperCase()).activate(p);
						} catch(Exception e) {
							p.sendMessage(ChatColor.RED + "Invalid portal type!");
						}
						break;
					case "save":
						ConfigAccessor configSave = new ConfigAccessor(args[1]);
						ConfigurationSection csSave = configSave.getRoot();

						switch(args[1]) {
						case "config.yml":
							p.sendMessage(ChatColor.GOLD + "You have 10 seconds to open the inventory that will be saved!");
							try {
								Bukkit.getScheduler().runTaskLater(LegacyCraft.getPlugin(), new Runnable() {
									public void run() {
										csSave.set(args[2].toUpperCase(), null);

										InventoryView invView = p.getOpenInventory();
										Inventory inv = invView.getTopInventory();
										csSave.set(args[2].toUpperCase() + ".title", invView.getTitle());
										csSave.set(args[2].toUpperCase() + ".size", inv.getSize());
										csSave.set(args[2].toUpperCase() + ".contents", inv.getContents());
										configSave.saveCustomConfig();

										p.sendMessage(ChatColor.GREEN + "Successfully saved inventory to file!");
									}
								}, 200);
							} catch(Exception ex) {
								p.sendMessage(ChatColor.RED + "Invalid command!");
							}

							break;
						case "player-data-template.yml":
							try {
								ItemStack contents[] = p.getInventory().getContents();
								csSave.set(args[2].toUpperCase() + ".save.inventory", contents);
								configSave.saveCustomConfig();

								p.sendMessage(ChatColor.GREEN + "Successfully saved inventory to file!");
							} catch(Exception ex) {
								p.sendMessage(ChatColor.RED + "Invalid command!");
							}

							break;
						}

						break;
					case "load":
						ConfigAccessor configLoad = new ConfigAccessor(args[1]);
						ConfigurationSection csLoad = configLoad.getRoot();

						switch(args[1]) {
						case "config.yml":
							String title = csLoad.getString(args[2].toUpperCase() + ".title");
							int size = csLoad.getInt(args[2].toUpperCase() + ".size");
							ItemStack contents[] = csLoad.getList(args[2].toUpperCase() + ".contents").toArray(new ItemStack[0]);

							Inventory invToOpen;
							if(size % 9 == 0) {
								invToOpen = Bukkit.createInventory(null, size, title);
							} else {
								invToOpen = Bukkit.createInventory(null, InventoryType.HOPPER, title);
							}

							invToOpen.setContents(contents);

							p.openInventory(invToOpen);

							break;
						case "player-data-template.yml":
							ItemStack inv[] = csLoad.getList(args[2].toUpperCase() + ".save.inventory").toArray(new ItemStack[0]);
							p.getInventory().setContents(inv);

							break;
						}

						break;
					case "home": //TODO not working
						p.sendMessage(NPCHomeWork.setHome(p, p.getLocation()));
						break;
					case "work": //TODO not working
						p.sendMessage(NPCHomeWork.setWork(p, p.getLocation()));
						break;
					case "complete":
						for(Quest quest : QuestManager.getActiveQuests(p)) {
							QuestManager.forceComplete(p, quest);
						}
						break;
					case "reset":
						//QuestManager.resetQuests(p, true);
						break;
					case "aquav":
						AquaVanguardProficiency1.activate(p);
						break;
					case "terrav":
						TerraVanguardProficiency1.activate(p, 2);
						break;
					default:
						p.sendMessage(ChatColor.RED + "Invalid Command");
						return false;
					}
				}
			} else if(lbl.equals("test")) {
				try {
					
					Experience playerExperience = (Experience) LegacyCraft.getPlayerData(p.getUniqueId(), PlayerData.EXPERIENCE);
					
					Util.br(playerExperience);
					Util.br(playerExperience.getAllExperience());
					
					if(args.length > 0) {
						Util.br(((Experience) LegacyCraft.getPlayerData(playerUUID, PlayerData.EXPERIENCE)).getExperienceFromLevel(Integer.parseInt(args[0]), ExperienceType.PLAYER));
					}

					//QuestBook.open(p);
					
				} catch(Exception e) {
					Util.print(e);
				}
			}
			return true;
		} else {
			p.sendMessage(deniedMsg);
			return false;
		}
	}
}
