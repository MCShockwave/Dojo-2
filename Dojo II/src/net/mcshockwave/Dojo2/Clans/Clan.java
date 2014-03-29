package net.mcshockwave.Dojo2.Clans;

import net.mcshockwave.Dojo2.Dojo;
import net.mcshockwave.MCS.MCShockwave;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;

public enum Clan {

	Air(
		new Location(Dojo.dw(), -238, 43, -230),
		ChatColor.YELLOW,
		"The Air Clan is a very §eclever§7 clan.",
		"They cannot dish out or take much damage, but when it comes to strategizing and being quick thinking they are a very balanced nation.",
		"Here, everything seems to move at a faster pace.",
		"You will earn new belts quicker, capture Chisanas faster, sprint faster, and so on. "),
	Water(
		new Location(Dojo.dw(), -176.5, 42, 161.5),
		ChatColor.AQUA,
		"The Water Clan is a very §bdefensive§7 clan.",
		"They can learn to take less and sometimes completely absorb damage,"
				+ " but are not very offensive and will not do as big of a margin of damage.",
		"Warriors here have better of a chance of holding out in battles longer and picking off weak players."),
	Fire(
		new Location(Dojo.dw(), 219.5, 41, -191),
		ChatColor.RED,
		"The Fire Clan is a very §caggressive§7 clan.",
		"They are capable of doing lots of damage, but lack proper defensive skills.",
		"Because of this, matches go by faster for these warriors."),
	Earth(
		new Location(Dojo.dw(), 225, 42, 218),
		ChatColor.GREEN,
		"The Earth Clan is overall a §abalanced§7 clan.",
		"They offer a diverse amount of abilities and don’t particularly specialize in any one fighting strategy.",
		"Warriors here are open-minded and tend to go all over the map when it comes to battles.");

	public ChatColor			color;
	public String[]				info;
	public Location				dojo;

	public ArrayList<Player>	inClan;
	public Team					team;

	Clan(Location dojo, final ChatColor color, String... info) {
		this.dojo = dojo;
		this.color = color;
		this.info = new String[info.length];
		for (int i = 0; i < info.length; i++) {
			this.info[i] = ChatColor.GRAY + info[i];
		}
		inClan = new ArrayList<>();

		final Scoreboard s = Bukkit.getScoreboardManager().getMainScoreboard();
		if (s.getTeam(name()) != null) {
			Team t = s.getTeam(name());
			for (OfflinePlayer op : t.getPlayers().toArray(new OfflinePlayer[0])) {
				t.removePlayer(op);
			}
			t.unregister();
		}

		Bukkit.getScheduler().runTaskLater(Dojo.ins, new Runnable() {
			public void run() {
				team = s.registerNewTeam(name());
				team.setCanSeeFriendlyInvisibles(true);
				team.setAllowFriendlyFire(true);
				team.setPrefix(color.toString());
			}
		}, 1l);
	}

	public static Clan getClan(Player p) {
		for (Clan c : values()) {
			if (c.inClan.contains(p)) {
				return c;
			}
		}
		return null;
	}

	public void sendInfo(Player p) {
		p.sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
		MCShockwave.send(color, p, "The %s Clan", name());
		MCShockwave.sendAll(p, info);
	}

	public void addPlayer(Player p) {
		MCShockwave.send(color, p, "Joined the %s Clan!", name());
		for (Clan c : Clan.values()) {
			try {
				c.inClan.remove(p);
				c.team.removePlayer(p);
			} catch (Exception e) {
			}
		}
		inClan.add(p);
		team.addPlayer(p);
		p.teleport(dojo);
	}

}
