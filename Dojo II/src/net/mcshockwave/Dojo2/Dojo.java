package net.mcshockwave.Dojo2;

import net.mcshockwave.Dojo2.Clans.Clan;
import net.mcshockwave.Dojo2.Mats.Mat;
import net.mcshockwave.Dojo2.Zones.Zone;
import net.mcshockwave.MCS.Entities.CustomEntityRegistrar;
import net.mcshockwave.MCS.Utils.CustomSignUtils.CustomSign;
import net.mcshockwave.MCS.Utils.CustomSignUtils.SignRunnable;
import net.mcshockwave.MCS.Utils.LocUtils;
import net.mcshockwave.MCS.Utils.PacketUtils;
import net.mcshockwave.MCS.Utils.SchedulerUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;

import org.apache.commons.lang.WordUtils;

public class Dojo extends JavaPlugin {

	public static Dojo	ins;

	public String		border	= "§8-=-=-=-=-=-=-";

	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new DefaultListener(), this);

		ins = this;

		Bukkit.getScheduler().runTaskLater(this, new Runnable() {
			@SuppressWarnings("deprecation")
			public void run() {
				dw().setSpawnLocation(-66, 43, -74);

				CustomEntityRegistrar.addCustomEntity(StationaryVillager.class, "Villager", EntityType.VILLAGER);
				StationaryVillager tut = (StationaryVillager) CustomEntityRegistrar.spawnCustomEntity(
						StationaryVillager.class, new Location(dw(), -66.5, 50, -91.5, 0, 30));
				tut.setCustomName("Tutorial Villager");
				tut.setCustomNameVisible(true);
				tut.setProfession(Profession.FARMER.getId());
			}
		}, 200l);

		regSigns();
	}

	public void regSigns() {

		// Clan join/info
		new CustomSign(border, "§eAir Clan", "Click for info", border, "[Air]", "info", null, null)
				.onClick(new SignRunnable() {
					public void run(Player p, Sign s, PlayerInteractEvent e) {
						Clan.Air.sendInfo(p);
					}
				});
		new CustomSign(border, "§eAir Clan", "Click to join", border, "[Air]", "join", null, null)
				.onClick(new SignRunnable() {
					public void run(Player p, Sign s, PlayerInteractEvent e) {
						Clan.Air.addPlayer(p);
					}
				});
		new CustomSign(border, "§4Fire Clan", "Click for info", border, "[Fire]", "info", null, null)
				.onClick(new SignRunnable() {
					public void run(Player p, Sign s, PlayerInteractEvent e) {
						Clan.Fire.sendInfo(p);
					}
				});
		new CustomSign(border, "§4Fire Clan", "Click to join", border, "[Fire]", "join", null, null)
				.onClick(new SignRunnable() {
					public void run(Player p, Sign s, PlayerInteractEvent e) {
						Clan.Fire.addPlayer(p);
					}
				});
		new CustomSign(border, "§aEarth Clan", "Click for info", border, "[Earth]", "info", null, null)
				.onClick(new SignRunnable() {
					public void run(Player p, Sign s, PlayerInteractEvent e) {
						Clan.Earth.sendInfo(p);
					}
				});
		new CustomSign(border, "§aEarth Clan", "Click to join", border, "[Earth]", "join", null, null)
				.onClick(new SignRunnable() {
					public void run(Player p, Sign s, PlayerInteractEvent e) {
						Clan.Earth.addPlayer(p);
					}
				});
		new CustomSign(border, "§bWater Clan", "Click for info", border, "[Water]", "info", null, null)
				.onClick(new SignRunnable() {
					public void run(Player p, Sign s, PlayerInteractEvent e) {
						Clan.Water.sendInfo(p);
					}
				});
		new CustomSign(border, "§bWater Clan", "Click to join", border, "[Water]", "join", null, null)
				.onClick(new SignRunnable() {
					public void run(Player p, Sign s, PlayerInteractEvent e) {
						Clan.Water.addPlayer(p);
					}
				});

		// Tutorial
		new CustomSign("§8Click here to", "§8redo tutorial", "", "(3 mins)", "[Tutorial]", null, null, null)
				.onClick(new SignRunnable() {
					public void run(Player p, Sign s, PlayerInteractEvent e) {
						if (inTut.contains(p)) {
							return;
						}
						new TutorialClass().getTutorial(p).execute();
					}
				});
	}

	public void onDisable() {
		for (Entity e : dw().getEntities()) {
			if (e instanceof Villager) {
				e.remove();
			}
		}
	}

	public static World				w		= null;

	public static ArrayList<Player>	inTut	= new ArrayList<>();

	public static Location			shiai	= new Location(dw(), 0.5, 42, -6.5);

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("dojo") && sender instanceof Player) {
			Player p = (Player) sender;

			if (args[0].equalsIgnoreCase("tutorial")) {
				new TutorialClass().getTutorial(p).execute();
			}

			if (args[0].equalsIgnoreCase("setclan")) {
				Clan c = Clan.valueOf(WordUtils.capitalizeFully(args[1]));
				c.addPlayer(p);
			}

			if (args[0].equalsIgnoreCase("power")) {
				final ArrayList<Location> circle = LocUtils.circle(p, p.getLocation(), 5, 1, true, false, 30);
				final Location loc = p.getLocation();

				SchedulerUtils util = SchedulerUtils.getNew();
				util.add(p);

				for (int i = 0; i < 30; i++) {
					final int j = i;
					util.add(new Runnable() {
						public void run() {
							for (Location l : circle) {
								Location m = l.clone().add(0, -j, 0);
								
								PacketUtils.playBlockDustParticles(Material.FIRE, 0, m, 0, 0.1f);
							}
						}
					});
					util.add(2);
				}
				util.add(2);
				
				util.add(new Runnable() {
					public void run() {
						loc.getWorld().strikeLightningEffect(loc);
					}
				});

				util.execute();
			}
		}

		return false;
	}

	public static World dw() {
		World dw = Bukkit.getWorld("Dojo");
		if (w == null) {
			w = dw;
		}
		return dw;
	}

	public static boolean canDamage(Player d, Player p) {
		Scoreboard s = Bukkit.getScoreboardManager().getMainScoreboard();

		if (s.getPlayerTeam(d) == s.getPlayerTeam(p) && s.getPlayerTeam(d) != null && !Mat.isInMat(p)
				&& !Mat.isInMat(d)) {
			return false;
		}
		if (Mat.isInMat(p) && Mat.getMat(p).timeGrace > System.currentTimeMillis()) {
			return false;
		}
		if (inTut.contains(p) || inTut.contains(d)) {
			return false;
		}
		for (Zone z : Zone.values()) {
			if (!z.pvpEnabled && z.inZone.contains(p)) {
				return false;
			}
		}
		return true;
	}

	public static void milkPlayer(Player p) {
		for (PotionEffect pe : p.getActivePotionEffects()) {
			p.removePotionEffect(pe.getType());
		}
	}

	public static void resetPlayer(Player p) {
		milkPlayer(p);
		p.getInventory().clear();
		p.getInventory().setArmorContents(null);
		for (Clan c : Clan.values()) {
			c.inClan.remove(p);
			c.team.removePlayer(p);
		}
	}

}
