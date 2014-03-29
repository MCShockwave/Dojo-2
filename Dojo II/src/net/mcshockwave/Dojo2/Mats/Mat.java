package net.mcshockwave.Dojo2.Mats;

import net.mcshockwave.Dojo2.Dojo;
import net.mcshockwave.MCS.Utils.LocUtils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public enum Mat {

	Shiai_6p(
		l(3, 41, -2),
		l(-3, 41, 2),
		n(-2, 41, 1, 80, 47, 98),
		n(-2, 41, -1, 91, 47, 91),
		n(0, 41, 1, 92, 47, 73),
		n(0, 41, -1, 79, 47, 72),
		n(2, 41, 1, 67, 47, 79),
		n(2, 41, -1, 69, 47, 96)),
	Shiai_4p1(
		l(-16, 41, -15),
		l(-12, 41, -11),
		n(-15, 41, -14, 87, 43, -65),
		n(-15, 41, -12, 100, 43, -76),
		n(-13, 41, -14, 111, 43, -65),
		n(-13, 41, -12, 98, 43, -54)),
	Shiai_4p2(
		l(12, 41, -15),
		l(16, 41, -11),
		n(13, 41, -14, -93, 42, 79),
		n(13, 41, -12, -93, 42, 79),
		n(15, 41, -14, -93, 42, 79),
		n(15, 41, -12, -93, 42, 79)),
	Shiai_2p1(
		l(3, 41, -15),
		l(5, 41, -11),
		n(4, 41, -14, 37, 44, -99),
		n(4, 41, -12, 51, 44, -99)),
	Shiai_2p2(
		l(-5, 41, -15),
		l(-3, 41, -11),
		n(-4, 41, -14, 100, 45, -40),
		n(-4, 41, -12, 100, 45, -40));

	public static final byte	openSlot	= 5, takenSlot = 1, inGameSlot = 14;

	public boolean				shiai, started = false;
	Location					c1, c2;
	public LocLink[]			spawns;

	public ArrayList<Player>	ps			= new ArrayList<>();

	public long					timeGrace	= 0;

	Mat(Location c1, Location c2, LocLink... spawns) {
		this.c1 = c1.add(0.5, 1.5, 0.5);
		this.c2 = c2.add(0.5, -0.5, 0.5);

		this.spawns = spawns;

		this.shiai = name().startsWith("Shiai");
	}

	public boolean isInArea(Location l) {
		return LocUtils.isInCuboid(l, c1, c2);
	}

	public static class LocLink {
		public Location	l1, l2;

		public LocLink(Location l1, Location l2) {
			this.l1 = l1.add(0.5, 0, 0.5);
			this.l2 = l2.add(0.5, 0, 0.5);
		}
	}

	public static LocLink n(int l1x, int l1y, int l1z, int l2x, int l2y, int l2z) {
		return new LocLink(new Location(Dojo.dw(), l1x, l1y, l1z), new Location(Dojo.dw(), l2x, l2y, l2z));
	}

	private static Location l(double x, double y, double z) {
		return new Location(Dojo.dw(), x, y, z);
	}
	
	public static Mat getMat(Player p) {
		for (Mat m : Mat.values()) {
			if (m.ps.contains(p)) {
				return m;
			}
		}
		return null;
	}

	public static boolean isInMat(Player p) {
		return getMat(p) != null;
	}

	@SuppressWarnings("deprecation")
	public void setBlockFromID(int id, byte data) {
		spawns[id].l1.getBlock().setData(data);
	}

	public void teleportPlayerToID(Player p, int id) {
		p.teleport(spawns[id].l1);
	}

	public int getIDOfPlayer(Player p) {
		return ps.indexOf(p);
	}

	public void updateSlots() {
		for (int i = 0; i < spawns.length; i++) {
			if (ps.size() > i) {
				teleportPlayerToID(ps.get(i), i);
				setBlockFromID(i, takenSlot);
			} else {
				setBlockFromID(i, openSlot);
			}
		}
	}

	public void addPlayer(Player p) {
		if (started)
			return;

		if (!ps.contains(p)) {
			ps.add(p);
			updateSlots();

			if (ps.size() >= spawns.length) {
				startGame();
			}
		} else {
			ps.remove(p);
			updateSlots();
			p.teleport(spawns[0].l1);
		}
	}

	public void startGame() {
		started = true;

		for (int i = 0; i < spawns.length; i++) {
			setBlockFromID(i, inGameSlot);
		}

		for (Player p : ps) {
			p.teleport(spawns[ps.indexOf(p)].l2);
			p.setHealth(20);
		}

		timeGrace = System.currentTimeMillis() + 5000;
	}

	public void endGame() {
		started = false;

		for (Player p : ps.toArray(new Player[0])) {
			ps.remove(p);
			p.teleport(spawns[0].l1);
			p.setHealth(20);
		}

		updateSlots();
	}

}
