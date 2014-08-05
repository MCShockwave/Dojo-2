package net.mcshockwave.Dojo2;

import net.mcshockwave.Dojo2.Clans.Clan;
import net.mcshockwave.Dojo2.Mats.Mat;
import net.mcshockwave.Dojo2.Zones.Zone;
import net.mcshockwave.MCS.MCShockwave;
import net.mcshockwave.MCS.Menu.ItemMenu;
import net.mcshockwave.MCS.Menu.ItemMenu.Button;
import net.mcshockwave.MCS.Utils.LocUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.util.Vector;

public class DefaultListener implements Listener {

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();

		if ((Dojo.inTut.contains(p) || Mat.isInMat(p) && !Mat.getMat(p).started)
				&& !LocUtils.isSame(event.getFrom(), event.getTo())) {
			event.setTo(event.getFrom());
		}

		for (Zone z : Zone.values()) {
			if (z.inZone.contains(p)) {
				if (!z.isInZone(p.getLocation())) {
					z.inZone.remove(p);
					if (z.bcJoin && !Dojo.inTut.contains(p)) {
						MCShockwave.send(p, "Now leaving %s", z.name);
						p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
					}
					if (z.bcPVP && !Dojo.inTut.contains(p)) {
						MCShockwave.send(ChatColor.GREEN, p, "PVP is %s Be careful!", "enabled!");
					}
				}
			} else {
				if (z.isInZone(p.getLocation())) {
					z.inZone.add(p);
					if (z.bcJoin && !Dojo.inTut.contains(p)) {
						p.playSound(p.getLocation(), Sound.CHICKEN_EGG_POP, 1, 1);
						MCShockwave.send(p, "Now entering %s", z.name);
					}
					if (z.bcPVP && !Dojo.inTut.contains(p)) {
						if (z.pvpEnabled) {
							MCShockwave.send(ChatColor.GREEN, p, "PVP is %s Be careful!", "enabled!");
						} else {
							MCShockwave.send(ChatColor.RED, p, "PVP is %s You are safe!", "disabled!");
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onPlayerKickPlayer(PlayerInteractEntityEvent event) {
		Player d = event.getPlayer();
		if (event.getRightClicked().getType() == EntityType.PLAYER) {
			Player p = (Player) event.getRightClicked();
			if (!d.isSneaking()) {
				if (!Dojo.canDamage(d, p) || !Dojo.canDamage(p, d)) {
					event.setCancelled(true);
					return;
				}

				Clan c = Clan.getClan(d);
				if (c == null) {
					event.setCancelled(true);
					return;
				}

				// if (c == Clan.Fire) {
				// PacketUtils.playParticleEffect(ParticleEffect.FLAME,
				// p.getEyeLocation(), 0, 0.1f, 10);
				// }
				// if (c == Clan.Water) {
				// PacketUtils.playParticleEffect(ParticleEffect.DRIP_WATER,
				// p.getEyeLocation(), 1, 1, 10);
				// }
				// if (c == Clan.Earth) {
				// PacketUtils.playParticleEffect(ParticleEffect.SLIME,
				// p.getEyeLocation(), 0, 1, 10);
				// }
				// if (c == Clan.Air) {
				// PacketUtils.playParticleEffect(ParticleEffect.CRIT,
				// p.getEyeLocation(), 0, 0.1f, 10);
				// }

				p.damage(1.5, d);
				p.getWorld().playSound(p.getLocation(), Sound.HURT_FLESH, 1, 0.5F);
				if (!p.isSneaking()) {
					p.setVelocity(d.getLocation().getDirection().multiply(1).setY(0.25F));
				}
			}
		}
	}

	@EventHandler
	public void onSignUpdate(SignChangeEvent event) {
		for (int i = 0; i < 4; i++) {
			String line = event.getLine(i);
			event.setLine(i, ChatColor.translateAlternateColorCodes('&', line));
		}
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player p = event.getPlayer();

		if (p.getGameMode() != GameMode.ADVENTURE) {
			p.setGameMode(GameMode.ADVENTURE);
		}

		p.teleport(TutorialClass.tutMid);
		Dojo.resetPlayer(p);
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		onQuit(event.getPlayer());
	}

	@EventHandler
	public void onPlayerKick(PlayerKickEvent event) {
		onQuit(event.getPlayer());
	}

	public void onQuit(Player p) {
		if (Mat.isInMat(p)) {
			onPlayerRespawn(new PlayerRespawnEvent(p, Mat.getMat(p).spawns[0].l1, false));
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		Entity ee = event.getEntity();
		Entity de = event.getDamager();

		if (ee instanceof Player && de instanceof Player) {
			Player p = (Player) ee;
			Player d = (Player) de;

			if (!Dojo.canDamage(d, p) || !Dojo.canDamage(p, d)) {
				event.setCancelled(true);
				return;
			}

			Clan c = Clan.getClan(d);
			if (c == null) {
				event.setCancelled(true);
				return;
			}

			event.setDamage(2);

			// if (c == Clan.Fire) {
			// PacketUtils.playParticleEffect(ParticleEffect.FLAME,
			// p.getEyeLocation(), 0, 0.1f, 10);
			// }
			// if (c == Clan.Water) {
			// PacketUtils.playParticleEffect(ParticleEffect.DRIP_WATER,
			// p.getEyeLocation(), 1, 1, 10);
			// }
			// if (c == Clan.Earth) {
			// PacketUtils.playParticleEffect(ParticleEffect.SLIME,
			// p.getEyeLocation(), 0, 1, 10);
			// }
			// if (c == Clan.Air) {
			// PacketUtils.playParticleEffect(ParticleEffect.SNOWBALL_POOF,
			// p.getEyeLocation(), 0, 0.1f, 10);
			// }
		}
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		event.setFoodLevel(20);
	}

	@EventHandler
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		Player p = event.getPlayer();

		if (!event.isSneaking()) {
			for (Mat m : Mat.values()) {
				if (m.isInArea(p.getLocation())) {
					m.addPlayer(p);
					break;
				}
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();

		// event.setDeathMessage("");
		PlayerRespawnEvent pre = new PlayerRespawnEvent(p, p.getWorld().getSpawnLocation(), false);
		Bukkit.getPluginManager().callEvent(pre);
		p.setHealth(20f);
		p.setNoDamageTicks(40);
		p.setVelocity(new Vector());
		p.teleport(pre.getRespawnLocation());
	}

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player p = event.getPlayer();

		if (Mat.isInMat(p)) {
			Mat m = Mat.getMat(p);

			if (m.started) {
				m.ps.remove(p);
				event.setRespawnLocation(m.spawns[0].l1);

				if (m.ps.size() <= 1) {
					m.endGame();
				}
			}
		} else if (Clan.getClan(p) != null) {
			event.setRespawnLocation(Clan.getClan(p).dojo);
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		Entity ee = event.getEntity();
		if (ee instanceof Player) {
			Player p = (Player) ee;

			if (p.getNoDamageTicks() > 0) {
				event.setCancelled(true);
			}

			if (event.getCause() == DamageCause.VOID) {
				event.setDamage(20f);
			}
		}
	}

	public ItemMenu getBookMenu(Player p) {
		ItemMenu m = new ItemMenu("Hon", 9);

		Button ability = new Button(false, Material.DIAMOND, 1, 0, "Abilities", "Click to select", "",
				"Abilities are activated with §oSHIFT");
		m.addButton(ability, 4);
		
		ItemMenu abs = new ItemMenu("Abilities - " + p.getName(), 9);
		m.addSubMenu(abs, ability, true);
		
		

		return m;
	}

}
