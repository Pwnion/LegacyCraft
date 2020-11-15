package com.pwnion.legacycraft;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.pwnion.legacycraft.OnCommand.Tree.Node;
import com.pwnion.legacycraft.abilities.areas.Selection;
import com.pwnion.legacycraft.abilities.inventory.CharacterBuildMenuInv;
import com.pwnion.legacycraft.abilities.ooc.Portal;
import com.pwnion.legacycraft.items.ItemData;
import com.pwnion.legacycraft.items.ItemManager;
import com.pwnion.legacycraft.items.ItemStat;
import com.pwnion.legacycraft.items.ItemTier;
import com.pwnion.legacycraft.items.ItemType;
import com.pwnion.legacycraft.items.enhancements.Enhancement;
import com.pwnion.legacycraft.levelling.Experience;
import com.pwnion.legacycraft.levelling.ExperienceType;
import com.pwnion.legacycraft.mobs.LCEntity;
import com.pwnion.legacycraft.mobs.LCEntity.LCEntityType;
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
							//TODO: String meaningfulName = args[2].toUpperCase(); inventory name?
							
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
								//TODO: String meaningfulName = args[2].toUpperCase();
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
							//TODO: String meaningfulName = args[2].toUpperCase();
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
							//TODO: String meaningfulName = args[2].toUpperCase();
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
					case "quest":
					case "quests":
						QuestBook.open(p);
						break;
					case "complete":
						for(Quest quest : QuestManager.getActiveQuests(p)) {
							QuestManager.forceComplete(p, quest);
						}
						break;
						/*
					case "reset":
						QuestManager.resetQuests(p, true);
						break; //*/
					case "uid": 
						ItemStack item = p.getInventory().getItemInMainHand();
						String newUID = args[1];
						if(newUID.length() > 0) {
							if(ItemManager.changeUID(item, newUID)) {
								p.sendMessage("Changed to " + newUID);
							} else {
								p.sendMessage(ChatColor.RED + "ID taken");
							}
						}
						break;
					case "uitem": //update item
						ItemManager.updateLore(p.getInventory().getItemInMainHand());
						p.sendMessage("Updated");
						break;
					case "enhance":
						try {
							if (args[1].trim().equalsIgnoreCase("list")) {
								Util.br(args[1]);
								break;
							}
							ItemStack hand = p.getInventory().getItemInMainHand();
							String enh = Util.join(args, " ", 1);
							ItemManager.getItemData(hand).addEnhancement(enh, true);
							ItemManager.updateLore(hand);
							p.sendMessage("Success");
						} catch (Exception e) {
							p.sendMessage(ChatColor.RED + "Invalid Enhancement: /lc enhance <enhancement>");
							p.sendMessage(ChatColor.GRAY + "Enhancements: " + Util.join(Enhancement.allNames().toArray(), ", "));
							e.printStackTrace();
						}
						break;
					case "setstat":
						try {
							ItemStat stat = ItemStat.valueOf(args[1].toUpperCase());
							int value = Integer.parseInt(args[2]);
							
							ItemStack hand = p.getInventory().getItemInMainHand();
							ItemManager.getItemData(hand).setStat(stat, value);
							ItemManager.updateLore(hand);
							p.sendMessage("Success");
						} catch (Exception e) {
							p.sendMessage(ChatColor.RED + "Invalid Values: /lc setstat <" + Util.join(ItemStat.values(), "/").toLowerCase() + "> <value>");
							e.printStackTrace();
						}
						break;
					case "settier":
						try {
							ItemStack hand = p.getInventory().getItemInMainHand();
							String tierName = Util.join(args, " ", 1);
							ItemTier tier = ItemTier.fromString(tierName);
							ItemManager.getItemData(hand).setTier(tier);
							ItemManager.updateLore(hand);
							p.sendMessage("Success");
						} catch (Exception e) {
							p.sendMessage(ChatColor.RED + "Invalid Tier: /lc settier <" + Util.join(ItemTier.values(), "/").toLowerCase() + ">");
							e.printStackTrace();
						}
						break;
					case "settype":
						try {
							ItemStack hand = p.getInventory().getItemInMainHand();
							String typeName = Util.join(args, " ", 1);
							ItemType type = ItemType.fromString(typeName);
							ItemManager.getItemData(hand).setType(type);
							ItemManager.updateLore(hand);
							p.sendMessage("Success");
						} catch (Exception e) {
							p.sendMessage(ChatColor.RED + "Invalid Type: /lc settype <" + Util.join(ItemType.values(), "/").toLowerCase() + ">");
							e.printStackTrace();
						}
						break;
					case "desc":
						try {
							ItemStack hand = p.getInventory().getItemInMainHand();
							String desc = Util.join(args, " ", 1);
							ItemManager.getItemData(hand).setDesc(desc);
							ItemManager.updateLore(hand);
							p.sendMessage("Success");
						} catch (Exception e) {
							p.sendMessage(ChatColor.RED + "Invalid Values: /lc desc <description>");
							e.printStackTrace();
						}
						break;
					case "gen":
					case "generate":
						try {
							ItemStack itemLC = p.getInventory().getItemInMainHand();
							ItemData itemData = ItemManager.generateItem(itemLC, ItemTier.fromString(args[1]), ItemType.fromString(args[2]));
							ItemManager.updateLore(itemLC);
						} catch (Exception e) {
							p.sendMessage(ChatColor.RED + "Invalid Values: /lc generate <tier: " + Util.join(ItemTier.values(), "/").toLowerCase() + "> <type: " + Util.join(ItemType.values(), "/").toLowerCase() + ">");
							e.printStackTrace();
						}
						break;
					case "summon":
						try {
							new LCEntity(p.getLocation(), LCEntityType.FIRE_ELEMENTAL);
						} catch (Exception e) {
							p.sendMessage(ChatColor.RED + "Invalid Values: /lc summon <value>");
							e.printStackTrace();
						}
						break;
					default:
						p.sendMessage(ChatColor.RED + "Invalid Command");
						return false;
					}
				}
			} else if(lbl.equals("test")) {
				try {
					/*
					ItemStack item = p.getInventory().getItemInMainHand();
					ItemData itemData = ItemManager.generateItem(item, ItemTier.STABLE, ItemType.SHORTSWORD);
					ItemManager.updateLore(item);
					
					Experience playerExperience = PlayerData.getExperience(p.getUniqueId());
					
					Util.br("Experience: " + playerExperience.getAllExperience());
					
					if(args.length > 0) {
						Util.br(PlayerData.getExperience(p.getUniqueId()).getExperienceFromLevel(Integer.parseInt(args[0]), ExperienceType.PLAYER));
					}

					//QuestBook.open(p);
					
					ItemMeta itemMeta = p.getInventory().getItemInMainHand().getItemMeta();
					Util.br(String.valueOf(itemMeta.hasCustomModelData()));
					Util.br(itemMeta.getCustomModelData()); //*/
					
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
	
	public static class Tree<T> {
		Node<T> root;

	    public Tree(T rootData) {
	        root = new Node<T>(null, rootData);
	    }
	    
	    public Node<T> get(T[] args) {
	    	Node<T> curNode = root;
	    	for (T arg : args) {
	    		curNode = curNode.getChild(arg);
	    	}
	    	return curNode;
	    }
	    
	    public Node<T> get(T[] args, int fromIndex, int toIndex) {
	    	Node<T> curNode = root;
	    	for (int i = fromIndex; i < toIndex && curNode != null; i++) {
	    		curNode = curNode.getChild(args[i]);
	    	}
	    	return curNode;
	    }

	    public static class Node<T> {
	    	private T data;
	    	private Node<T> parent;
	    	public ArrayList<Node<T>> children = new ArrayList<Node<T>>();
	    	
	    	public Node(Node<T> parent, T data) {
	    		this.parent = parent;
				this.data = data;
			}
	    	
	    	public ArrayList<T> getChildren() {
	    		ArrayList<T> childrenData = new ArrayList<>(children.size());
	    		for (Node<T> child : children) {
	    			childrenData.add(child.data);
	    		}
	    		return childrenData;
	    	}
	    	
	    	public void linkSiblingNodes() {
	    		for (Node<T> sibling : parent.children) {
	    			sibling.children = children;
	    		}
	    	}
	    	
	    	public Node<T> getChild(T data) {
	    		for (Node<T> child : children) {
	    			if (child.data.equals(data)) {
	    				return child;
	    			}
	    		}
	    		return null;
	    	}

			public Node<T> up() {
	    		return parent;
	    	}
	    	
	    	public Node<T> down(T data) {
	    		Node<T> node = new Node<T>(this, data);
	    		children.add(node);
	    		return node;
	    	}
	    	
	    	public ArrayList<Node<T>> end(T... data) {
	    		for (T d : data) {
	    			down(d);
	    		}
	    		return children;
	    	}
	    	
	    	public ArrayList<Node<T>> end(Collection<T> data) {
	    		for (T d : data) {
	    			down(d);
	    		}
	    		return children;
	    	}
	    }
	}
	
	static Tree<String> tree = new Tree<>("lc");
	static {
		tree.root.end("class", "pos1", "pos2", "export", "save", "load", "home", "work", "quest", "quests", "complete", "uid", "uitem", "setstat", "desc");
		tree.root.down("portal").end(Util.toString(Portal.values()));
		tree.root.down("summon").end(Util.toString(LCEntityType.values()));
		tree.root.down("settype").end(Util.toString(ItemType.values()));
		tree.root.down("settier").end(Util.toString(ItemTier.values()));
		tree.root.down("enhance").end(Enhancement.allNames());
		
		tree.root.down("generate").end(Util.toString(ItemTier.values())).get(0).end(Util.toString(ItemType.values())).get(0).up().linkSiblingNodes();
	}
	
	public static class LCTabCompleter implements TabCompleter {
		
		@Override
		public List<String> onTabComplete(CommandSender cs, Command cmd, String lbl, String[] args) {
			Player p;
			UUID playerUUID;
			
			//Ensure the command sender is a player
			if(cs instanceof Player) {
				p = (Player) cs;
				playerUUID = p.getUniqueId();
			} else {
				return null;
			}

			if(p.hasPermission("legacycraft.op")) {
				if(lbl.equals("legacycraft") || cmd.getAliases().contains(lbl)) {
					Node<String> curNode = tree.get(args, 0, args.length - 1);
					if (curNode != null) {
						ArrayList<String> childrenData = new ArrayList<>(curNode.children.size());
			    		for (Node<String> child : curNode.children) {
			    			if (child.data.startsWith(args[args.length - 1])) {
			    				childrenData.add(child.data);
			    			}
			    		}
			    		return childrenData;
					}
				}
			}
			return null;
		}
		
	}
}
