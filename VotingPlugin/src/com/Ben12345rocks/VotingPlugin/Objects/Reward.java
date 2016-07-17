package com.Ben12345rocks.VotingPlugin.Objects;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.Ben12345rocks.VotingPlugin.Main;
import com.Ben12345rocks.VotingPlugin.Utils;
import com.Ben12345rocks.VotingPlugin.Config.ConfigFormat;
import com.Ben12345rocks.VotingPlugin.Config.ConfigRewards;
import com.Ben12345rocks.VotingPlugin.Data.Data;

public class Reward {
	static Main plugin = Main.plugin;

	public String name;

	private boolean delayEnabled;
	private int delayHours;
	private int delayMinutes;

	private boolean timedEnabled;
	private int timedHour;
	private int timedMinute;

	private double chance;

	private double randomChance;

	private ArrayList<String> randomRewards;

	private ArrayList<String> randomFallBack;

	private boolean requirePermission;

	private ArrayList<String> worlds;

	private boolean giveInEachWorld;

	private Set<String> items;

	private HashMap<String, String> itemMaterial;
	private HashMap<String, String> itemSkull;

	private HashMap<String, Integer> itemData;

	private HashMap<String, Integer> itemDurabilty;

	private HashMap<String, Integer> itemAmount;

	private HashMap<String, Integer> itemMinAmount;
	private HashMap<String, Integer> itemMaxAmount;
	private HashMap<String, String> itemName;
	private HashMap<String, ArrayList<String>> itemLore;

	private HashMap<String, HashMap<String, Integer>> itemEnchants;

	private int money;

	private int MinMoney;

	private int MaxMoney;

	private int exp;

	private ArrayList<String> consoleCommands;
	private ArrayList<String> playerCommands;

	private Set<String> potions;
	private HashMap<String, Integer> potionsDuration;
	private HashMap<String, Integer> potionsAmplifier;

	private String rewardMsg;

	public Reward(Main plugin) {
		Reward.plugin = plugin;
	}

	public Reward(String reward) {
		name = reward;

		setDelayEnabled(ConfigRewards.getInstance().getDelayedEnabled(reward));
		setDelayHours(ConfigRewards.getInstance().getDelayedHours(reward));
		setDelayMinutes(ConfigRewards.getInstance().getDelayedMinutes(reward));

		setTimedEnabled(ConfigRewards.getInstance().getTimedEnabled(reward));
		setTimedHour(ConfigRewards.getInstance().getTimedHour(reward));
		setTimedMinute(ConfigRewards.getInstance().getTimedMinute(reward));

		setChance(ConfigRewards.getInstance().getChance(reward));
		setRandomChance(ConfigRewards.getInstance().getRandomChance(reward));
		setRandomRewards(ConfigRewards.getInstance().getRandomRewards(reward));
		setRandomFallBack(ConfigRewards.getInstance().getRandomFallBack(reward));

		setRequirePermission(ConfigRewards.getInstance().getRequirePermission(
				reward));
		setWorlds(ConfigRewards.getInstance().getWorlds(reward));
		setGiveInEachWorld(ConfigRewards.getInstance().getGiveInEachWorld(
				reward));

		setItems(ConfigRewards.getInstance().getItems(reward));
		itemMaterial = new HashMap<String, String>();
		itemData = new HashMap<String, Integer>();
		itemSkull = new HashMap<String, String>();
		itemDurabilty = new HashMap<String, Integer>();
		itemAmount = new HashMap<String, Integer>();
		itemMinAmount = new HashMap<String, Integer>();
		itemMaxAmount = new HashMap<String, Integer>();
		itemName = new HashMap<String, String>();
		itemLore = new HashMap<String, ArrayList<String>>();
		itemName = new HashMap<String, String>();
		itemEnchants = new HashMap<String, HashMap<String, Integer>>();
		for (String item : ConfigRewards.getInstance().getItems(reward)) {
			itemMaterial.put(item,
					ConfigRewards.getInstance().getItemMaterial(reward, item));
			itemData.put(item,
					ConfigRewards.getInstance().getItemData(reward, item));
			itemAmount.put(item,
					ConfigRewards.getInstance().getItemAmount(reward, item));
			itemMinAmount.put(item, ConfigRewards.getInstance()
					.getItemMinAmount(reward, item));
			itemMaxAmount.put(item, ConfigRewards.getInstance()
					.getItemMaxAmount(reward, item));
			itemName.put(item,
					ConfigRewards.getInstance().getItemName(reward, item));
			itemLore.put(item,
					ConfigRewards.getInstance().getItemLore(reward, item));
			itemDurabilty.put(item, ConfigRewards.getInstance()
					.getItemDurability(reward, item));
			itemSkull.put(item,
					ConfigRewards.getInstance().getItemSkull(reward, item));
			HashMap<String, Integer> enchants = new HashMap<String, Integer>();
			for (String enchant : ConfigRewards.getInstance().getItemEnchants(
					reward, item)) {
				enchants.put(enchant, ConfigRewards.getInstance()
						.getItemEnchantsLevel(reward, item, enchant));

			}
			itemEnchants.put(item, enchants);
		}

		setMoney(ConfigRewards.getInstance().getMoney(reward));
		setMinMoney(ConfigRewards.getInstance().getMinMoney(reward));
		setMaxMoney(ConfigRewards.getInstance().getMaxMoney(reward));

		setExp(ConfigRewards.getInstance().getEXP(reward));

		setConsoleCommands(ConfigRewards.getInstance().getCommandsConsole(
				reward));
		setPlayerCommands(ConfigRewards.getInstance().getCommandsPlayer(reward));

		potions = ConfigRewards.getInstance().getPotions(reward);
		potionsDuration = new HashMap<String, Integer>();
		potionsAmplifier = new HashMap<String, Integer>();
		for (String potion : potions) {
			potionsDuration.put(potion, ConfigRewards.getInstance()
					.getPotionsDuration(reward, potion));
			potionsAmplifier.put(potion, ConfigRewards.getInstance()
					.getPotionsAmplifier(reward, potion));
		}

		setRewardMsg(ConfigRewards.getInstance().getMessagesReward(reward));

	}

	public boolean checkChance() {
		double chance = getChance();

		if ((chance == 0) || (chance == 100)) {
			return true;
		}

		double randomNum = (Math.random() * 100) + 1;

		plugin.debug("Random: " + randomNum + ", Chance: " + chance);

		if (randomNum <= chance) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkDelayed(User user) {
		if (!isDelayEnabled()) {
			return false;
		}

		Date time = new Date();
		time = DateUtils.addHours(time, getDelayHours());
		time = DateUtils.addMinutes(time, getDelayMinutes());
		user.setTimedReward(this, time.getTime());

		plugin.debug("Giving reward " + name + " in " + getDelayHours()
				+ " hours " + getDelayMinutes() + " minutes ("
				+ time.toString() + ")");
		return true;

	}

	public boolean checkRandomChance() {
		double chance = getRandomChance();

		if ((chance == 0) || (chance == 100)) {
			return true;
		}

		double randomNum = (Math.random() * 100) + 1;

		plugin.debug("Random: Random: " + randomNum + ", Chance: " + chance);

		if (randomNum <= chance) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressWarnings("deprecation")
	public boolean checkTimed(User user) {
		if (!isTimedEnabled()) {
			return false;
		}

		Date time = new Date();
		time.setHours(getTimedHour());
		time.setMinutes(getTimedMinute());
		if (new Date().after(time)) {
			time = DateUtils.addDays(time, 1);
		}
		user.setTimedReward(this, time.getTime());

		plugin.debug("Giving reward " + name + " at " + time.toString());
		return true;
	}

	public double getChance() {
		return chance;
	}

	public ArrayList<String> getConsoleCommands() {
		return consoleCommands;
	}

	public int getDelayHours() {
		return delayHours;
	}

	public int getDelayMinutes() {
		return delayMinutes;
	}

	public int getExp() {
		return exp;
	}

	public HashMap<String, Integer> getItemAmount() {
		return itemAmount;
	}

	public int getItemAmount(String item) {
		int amount = getItemAmount().get(item);
		int maxAmount = getItemMaxAmount().get(item);
		int minAmount = getItemMinAmount().get(item);
		if ((maxAmount == 0) && (minAmount == 0)) {
			return amount;
		} else {
			int num = (int) (Math.random() * maxAmount);
			num++;
			if (num < minAmount) {
				num = minAmount;
			}
			return num;
		}
	}

	public HashMap<String, Integer> getItemData() {
		return itemData;
	}

	public HashMap<String, Integer> getItemDurabilty() {
		return itemDurabilty;
	}

	public HashMap<String, HashMap<String, Integer>> getItemEnchants() {
		return itemEnchants;
	}

	public HashMap<String, ArrayList<String>> getItemLore() {
		return itemLore;
	}

	public HashMap<String, String> getItemMaterial() {
		return itemMaterial;
	}

	public HashMap<String, Integer> getItemMaxAmount() {
		return itemMaxAmount;
	}

	public HashMap<String, Integer> getItemMinAmount() {
		return itemMinAmount;
	}

	public HashMap<String, String> getItemName() {
		return itemName;
	}

	public Set<String> getItems() {
		return items;
	}

	public HashMap<String, String> getItemSkull() {
		return itemSkull;
	}

	public int getMaxMoney() {
		return MaxMoney;
	}

	public int getMinMoney() {
		return MinMoney;
	}

	public int getMoney() {
		return money;
	}

	public int getMoneyToGive() {
		int amount = getMoney();
		int maxAmount = getMaxMoney();
		int minAmount = getMinMoney();
		if ((maxAmount == 0) && (minAmount == 0)) {
			return amount;
		} else {
			int num = (int) (Math.random() * maxAmount);
			num++;
			if (num < minAmount) {
				num = minAmount;
			}
			return num;
		}
	}

	public ArrayList<String> getPlayerCommands() {
		return playerCommands;
	}

	public Set<String> getPotions() {
		return potions;
	}

	public HashMap<String, Integer> getPotionsAmplifier() {
		return potionsAmplifier;
	}

	public HashMap<String, Integer> getPotionsDuration() {
		return potionsDuration;
	}

	public double getRandomChance() {
		return randomChance;
	}

	public ArrayList<String> getRandomFallBack() {
		return randomFallBack;
	}

	public ArrayList<String> getRandomRewards() {
		return randomRewards;
	}

	public String getRewardMsg() {
		return rewardMsg;
	}

	public String getRewardName() {
		return name;
	}

	public int getTimedHour() {
		return timedHour;
	}

	public int getTimedMinute() {
		return timedMinute;
	}

	public ArrayList<String> getWorlds() {
		return worlds;
	}

	public void giveExp(User user) {
		user.giveExp(getExp());
	}

	public void giveItems(User user) {
		for (String item : getItems()) {
			ItemStack itemStack = new ItemStack(
					Material.valueOf(getItemMaterial().get(item)),
					getItemAmount(item), Short.valueOf(Integer
							.toString(getItemData().get(item))));
			String name = getItemName().get(item);
			if (name != null) {
				itemStack = Utils.getInstance().nameItem(itemStack,
						name.replace("%Player%", user.getPlayerName()));
			}
			itemStack = Utils.getInstance().addLore(
					itemStack,
					Utils.getInstance().replace(getItemLore().get(item),
							"%Player%", user.getPlayerName()));
			itemStack = Utils.getInstance().addEnchants(itemStack,
					getItemEnchants().get(item));
			itemStack = Utils.getInstance().setDurabilty(itemStack,
					getItemDurabilty().get(item));
			String skull = getItemSkull().get(item);
			if (skull != null) {
				itemStack = Utils.getInstance().setSkullOwner(itemStack,
						skull.replace("%Player%", user.getPlayerName()));
			}
			user.giveItem(itemStack);
		}
	}

	public void giveMoney(User user) {
		user.giveMoney(getMoneyToGive());
	}

	public void givePotions(User user) {
		for (String potionName : getPotions()) {
			user.givePotionEffect(potionName,
					getPotionsDuration().get(potionName), getPotionsAmplifier()
							.get(potionName));
		}
	}

	public void giveRandom(User user) {
		if (checkRandomChance()) {
			ArrayList<String> rewards = getRandomRewards();
			if (rewards != null) {
				if (rewards.size() > 0) {
					String reward = rewards.get((int) Math.random()
							* rewards.size());
					if (reward.equalsIgnoreCase("")) {
						user.giveReward(ConfigRewards.getInstance().getReward(
								reward));
					}
				}
			}
		} else {
			for (String reward : getRandomFallBack()) {
				if (reward.equalsIgnoreCase("")) {
					user.giveReward(ConfigRewards.getInstance().getReward(
							reward));
				}
			}
		}
	}

	public void giveReward(User user) {

		if (checkDelayed(user)) {
			return;
		}

		if (checkTimed(user)) {
			return;
		}

		giveRewardReward(user);
	}

	public void giveRewardReward(User user) {
		plugin.debug("Attempting to give " + user.getPlayerName() + " reward "
				+ name);

		if (checkChance()) {
			ArrayList<String> worlds = getWorlds();
			Player player = Bukkit.getPlayer(user.getPlayerName());
			if ((player != null) && (worlds != null)) {
				if (isGiveInEachWorld()) {
					for (String world : worlds) {
						if (player.getWorld().getName().equals(world)) {
							giveRewardUser(user);
						} else {
							Data.getInstance().setOfflineVotesSiteWorld(
									user,
									name,
									world,
									Data.getInstance()
											.getOfflineVotesSiteWorld(user,
													name, world) + 1);
						}
					}
				} else {
					if (worlds.contains(player.getWorld().getName())) {
						giveRewardUser(user);
					} else {
						Data.getInstance().setOfflineVotesSiteWorld(
								user,
								name,
								null,
								Data.getInstance().getOfflineVotesSiteWorld(
										user, name, null) + 1);
					}
				}
			} else {
				giveRewardUser(user);
			}
		}
		giveRandom(user);
	}

	/**
	 * Give the user rewards
	 *
	 * @param user
	 *            User to give rewards to
	 */
	public void giveRewardUser(User user) {
		Player player = Bukkit.getPlayer(user.getPlayerName());
		if (player != null) {
			if (!isRequirePermission()
					|| player.hasPermission("VotingPlugin.Reward." + name)) {
				giveMoney(user);
				giveItems(user);
				giveExp(user);
				runCommands(user);
				givePotions(user);
				sendMessage(user);
				sendTitle(user);
				playSound(user);
				playEffect(user);

				plugin.debug("Gave " + user.getPlayerName() + " reward " + name);

			}
		}
	}

	public boolean isDelayEnabled() {
		return delayEnabled;
	}

	public boolean isGiveInEachWorld() {
		return giveInEachWorld;
	}

	public boolean isRequirePermission() {
		return requirePermission;
	}

	public boolean isTimedEnabled() {
		return timedEnabled;
	}

	public void playEffect(User user) {
		if (ConfigRewards.getInstance().getEffectEnabled(name)) {
			user.playParticleEffect(ConfigRewards.getInstance()
					.getEffectEffect(name), ConfigRewards.getInstance()
					.getEffectData(name), ConfigRewards.getInstance()
					.getEffectParticles(name), ConfigRewards.getInstance()
					.getEffectRadius(name));
		}
	}

	public void playSound(User user) {
		if (ConfigRewards.getInstance().getSoundEnabled(name)) {
			try {
				user.playSound(ConfigRewards.getInstance().getSoundSound(name),
						ConfigRewards.getInstance().getSoundVolume(name),
						ConfigRewards.getInstance().getSoundPitch(name));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void runCommands(User user) {
		String playerName = user.getPlayerName();

		// Console commands
		ArrayList<String> consolecmds = getConsoleCommands();

		if (consolecmds != null) {
			for (String consolecmd : consolecmds) {
				if (consolecmd.length() > 0) {
					consolecmd = consolecmd.replace("%player%", playerName);
					Bukkit.getServer().dispatchCommand(
							Bukkit.getConsoleSender(), consolecmd);
				}
			}
		}

		// Player commands
		ArrayList<String> playercmds = getPlayerCommands();

		Player player = Bukkit.getPlayer(playerName);
		if (playercmds != null) {
			for (String playercmd : playercmds) {
				if ((player != null) && (playercmd.length() > 0)) {
					playercmd = playercmd.replace("%player%", playerName);
					player.performCommand(playercmd);
				}
			}
		}
	}

	public void sendMessage(User user) {
		if (rewardMsg != null) {
			user.sendMessage(rewardMsg);
		} else {
			user.sendMessage(ConfigFormat.getInstance().getRewardMsg());
		}
	}

	public void sendTitle(User user) {
		if (ConfigRewards.getInstance().getTitleEnabled(name)) {
			user.sendTitle(ConfigRewards.getInstance().getTitleTitle(name),

			ConfigRewards.getInstance().getTitleSubTitle(name),

			ConfigRewards.getInstance().getTitleFadeIn(name), ConfigRewards
					.getInstance().getTitleShowTime(name), ConfigRewards
					.getInstance().getTitleFadeOut(name));
		}
	}

	public void setChance(double chance) {
		this.chance = chance;
	}

	public void setConsoleCommands(ArrayList<String> consoleCommands) {
		this.consoleCommands = consoleCommands;
	}

	public void setDelayEnabled(boolean delayEnabled) {
		this.delayEnabled = delayEnabled;
	}

	public void setDelayHours(int delayHours) {
		this.delayHours = delayHours;
	}

	public void setDelayMinutes(int delayMinutes) {
		this.delayMinutes = delayMinutes;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public void setGiveInEachWorld(boolean giveInEachWorld) {
		this.giveInEachWorld = giveInEachWorld;
	}

	public void setItemAmount(HashMap<String, Integer> itemAmount) {
		this.itemAmount = itemAmount;
	}

	public void setItemData(HashMap<String, Integer> itemData) {
		this.itemData = itemData;
	}

	public void setItemDurabilty(HashMap<String, Integer> itemDurabilty) {
		this.itemDurabilty = itemDurabilty;
	}

	public void setItemEnchants(
			HashMap<String, HashMap<String, Integer>> itemEnchants) {
		this.itemEnchants = itemEnchants;
	}

	public void setItemLore(HashMap<String, ArrayList<String>> itemLore) {
		this.itemLore = itemLore;
	}

	public void setItemMaterial(HashMap<String, String> itemMaterial) {
		this.itemMaterial = itemMaterial;
	}

	public void setItemMaxAmount(HashMap<String, Integer> itemMaxAmount) {
		this.itemMaxAmount = itemMaxAmount;
	}

	public void setItemMinAmount(HashMap<String, Integer> itemMinAmount) {
		this.itemMinAmount = itemMinAmount;
	}

	public void setItemName(HashMap<String, String> itemName) {
		this.itemName = itemName;
	}

	public void setItems(Set<String> items) {
		this.items = items;
	}

	public void setItemSkull(HashMap<String, String> itemSkull) {
		this.itemSkull = itemSkull;
	}

	public void setMaxMoney(int maxMoney) {
		MaxMoney = maxMoney;
	}

	public void setMinMoney(int minMoney) {
		MinMoney = minMoney;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public void setPlayerCommands(ArrayList<String> playerCommands) {
		this.playerCommands = playerCommands;
	}

	public void setPotions(Set<String> potions) {
		this.potions = potions;
	}

	public void setPotionsAmplifier(HashMap<String, Integer> potionsAmplifier) {
		this.potionsAmplifier = potionsAmplifier;
	}

	public void setPotionsDuration(HashMap<String, Integer> potionsDuration) {
		this.potionsDuration = potionsDuration;
	}

	public void setRandomChance(double randomChance) {
		this.randomChance = randomChance;
	}

	public void setRandomFallBack(ArrayList<String> randomFallBack) {
		this.randomFallBack = randomFallBack;
	}

	public void setRandomRewards(ArrayList<String> randomRewards) {
		this.randomRewards = randomRewards;
	}

	public void setRequirePermission(boolean requirePermission) {
		this.requirePermission = requirePermission;
	}

	public void setRewardMsg(String rewardMsg) {
		this.rewardMsg = rewardMsg;
	}

	public void setTimedEnabled(boolean timedEnabled) {
		this.timedEnabled = timedEnabled;
	}

	public void setTimedHour(int timedHour) {
		this.timedHour = timedHour;
	}

	public void setTimedMinute(int timedMinute) {
		this.timedMinute = timedMinute;
	}

	public void setWorlds(ArrayList<String> worlds) {
		this.worlds = worlds;
	}
}