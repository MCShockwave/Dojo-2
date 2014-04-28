package net.mcshockwave.Dojo2;

import net.minecraft.server.v1_7_R2.EntityVillager;
import net.minecraft.server.v1_7_R2.World;

public class StationaryVillager extends EntityVillager {

	public StationaryVillager(World w) {
		super(w);
	}
	
	@Override
	public void move(double x, double y, double z) {
		this.locX = lastX;
		this.locY = lastY;
		this.locZ = lastZ;
	}

}
