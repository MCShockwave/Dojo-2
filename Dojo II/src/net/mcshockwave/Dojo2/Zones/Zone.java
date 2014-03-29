package net.mcshockwave.Dojo2.Zones;

import net.mcshockwave.Dojo2.Dojo;
import net.mcshockwave.MCS.Utils.LocUtils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public enum Zone {

	Tutorial(
		l(-67, 43, -75),
		20,
		false,
		false,
		false),
	Shiai(
		l(0, 41, 0),
		40,
		false,
		true,
		true),
	Chisana_1(
		l(-200.5, 40, -29.5),
		8,
		true,
		true,
		false);

	public ArrayList<Player>	inZone	= new ArrayList<>();

	public String				name;

	public boolean				cube, pvpEnabled, bcJoin, bcPVP;
	public Location				c1, c2;
	public int					r;
	public Location				center;

	Zone(Location c1, Location c2, boolean pvp, boolean broadJoin, boolean broadPVP) {
		this.cube = true;
		this.c1 = c1.add(0.5, 0.5, 0.5);
		this.c2 = c2.add(0.5, 0.5, 0.5);
		this.center = null;
		this.r = 0;
		this.pvpEnabled = pvp;
		this.bcJoin = broadJoin;
		this.bcPVP = broadPVP;

		name = name().replace('_', ' ');
	}

	Zone(Location center, int radius, boolean pvp, boolean broadJoin, boolean broadPVP) {
		this.cube = false;
		this.c1 = null;
		this.c2 = null;
		this.center = center.add(0.5, 0.5, 0.5);
		this.r = radius;
		this.pvpEnabled = pvp;
		this.bcJoin = broadJoin;
		this.bcPVP = broadPVP;

		name = name().replace('_', ' ');
	}

	private static Location l(double x, double y, double z) {
		return new Location(Dojo.dw(), x, y, z);
	}

	public boolean isInZone(Location l) {
		if (cube) {
			return LocUtils.isInCuboid(l, c1, c2);
		} else {
			return l.getWorld() == center.getWorld() && l.distance(center) <= r;
		}
	}

}
