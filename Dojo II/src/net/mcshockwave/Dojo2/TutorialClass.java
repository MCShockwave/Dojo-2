package net.mcshockwave.Dojo2;

import net.mcshockwave.MCS.Utils.SchedulerUtils;
import net.mcshockwave.MCS.Utils.SchedulerUtils.SoundEffect;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class TutorialClass {

	public static final Location	tutMid	= new Location(Dojo.dw(), -66.5, 43, -74.5, 180, 0);

	public static final String		tutPre	= "§eTutorial Villager§f: ";

	public SchedulerUtils			u;

	public TutorialClass() {
		this.u = SchedulerUtils.getNew();
	}

	public SchedulerUtils getTutorial(final Player p) {
		World w = Dojo.dw();

		u.add(p);
		u.add(new Runnable() {
			public void run() {
				Dojo.inTut.add(p);
				p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 0));
			}
		});
		w(5);
		u.add(tutMid);

		// start tutorial here

		s("Greetings, young warrior! I am your Tutorial Sensei and will be helping you learn the ropes of the Dojo.");
		w(5);
		s("Our kingdom is split between 4 rival Clans; Fire, Water, Earth, and Air.");
		w(5);
		s("All of the warriors in each Clan have been fighting for centuries and have never stopped.");
		w(4);
		s("Each Clan offers different attack and defense abilities to assist you in battle. As you earn new belts, you gain access to new abilities.");
		w(8);
		u.add(new Location(w, -69.5, 43, -71.5, 45, 0));
		s("This is the Fire Clan. These fierce warriors show no mercy when it comes to burning opponents and performing brutal attacks, but lack defense.");
		w(8);
		u.add(new Location(w, -63.5, 43, -71.5, 315, 0));
		s("This is the Water Clan. These peaceful warriors believe being humble and well-balanced are qualities of life."
				+ " Warriors in the Water Clan are fantastic defenders, but lack offensive strength.");
		w(10);
		u.add(new Location(w, -63.5, 43, -77.5, 225, 0));
		s("This is the Earth Clan. These proud warriors offer the most diverse range of attacks and defense, and are a large well-rounded Clan.");
		w(8);
		u.add(new Location(w, -69.5, 43, -77.5, 135, 0));
		s("And finally, we have the Air Clan. These lightweight warriors are very quick to their feet, as well as quick in the head."
				+ " Warriors in the Air Clan are clever and cunning and get upper hand in particular fields because of that.");
		w(8);
		u.add(tutMid);
		s("So there you have it! The 4 Clans. But there's no rush to choose a side just yet! Let's take a tour.");
		w(5);
		u.add(Dojo.shiai);
		s("This is the Shiai, the center of the map and the only place where all 4 Clans can settle peacefully."
				+ " Those mats on the ground are for organized battling,"
				+ " where Warriors fight one another in a controlled environment to gain progress to the next belt.");
		w(13);
		s("Winning battles using mats will award you new belts quicker. The higher your belt, the more abilities will be unlocked for your use."
				+ " You can view your progress to your next belt and a whole lot of other neat features by opening your \"Hon (Book)\", which you will receive later.");
		w(15);
		s("As you unlock more than one ability, you will have to choose one you'd like to bind to your special abilities (shift) key,"
				+ " so you can only use one unlocked ability at a time in battle. These binds can be changed at any time.");
		w(11);
		s("Whenever you use an ability, a portion of your Stamina (XP Bar) will be drained, but will slowly recharge over time.");
		w(7);
		s("The belts go in a specific color order, and sometimes having different degrees of that color, such as brown belt 1st degree.");
		w(5);
		s("Once you advance past 10th degree black belt, you will have the option to start fresh in a new Clan of your choosing.");
		w(5);
		u.add(new Location(w, 200, 41, 200, 270, 0));
		s("Each Clan has their own main dojo and their own Sensei. This is Earth Clan's dojo."
				+ " Dojos are mainly for training and will not help you progress to the next belt.");
		w(10);
		u.add(new Location(w, 0, 41, 200, 180, 0));
		s("You can walk paths to get to the Shiai or another Clan's dojo. Combat is enabled here, so be careful!");
		w(5);
		u.add(new Location(w, 0, 41, 200));
		u.add("§7(Will set location when we add one)"); // TODO
		s("This is a Chisana. You will see them throughout the map on paths."
				+ " Capture them to improve your Clan's Stamina by making it recharge faster and use less!");
		w(9);
		u.add(new Location(w, 0, 41, 200));
		u.add("§7(Will set location when we add one)"); // TODO
		s("Your Clan's Sensei will stand outside of the Clan's walls, leaving him open to attack.");
		w(5);
		s("If your Clan's Sensei is killed all of your currently captured Chisanas will be reset!"
				+ " Your Clan will need an hour to find a replacement Sensei,"
				+ " giving your Clan some time to recapture Chisanas without having them being reset too soon."
				+ " Defend the sensei to keep your stamina in check!");
		w(13);
		u.add(tutMid);
		s("And that concludes our tour, thank you for being patient. I see lots of potential in you, young Warrior.");
		w(7);
		s("The time has come for you to choose your side. Good luck, and farewell!");
		w(5);
		u.add("§7§oImmobilization has been lifted");

		// end of tutorial

		u.add(new Runnable() {
			public void run() {
				Dojo.inTut.remove(p);
				p.removePotionEffect(PotionEffectType.INVISIBILITY);
			}
		});
		
		return u;
	}

	public void s(String chat) {
		u.add(tutPre + chat);
		u.add(new SoundEffect(Sound.VILLAGER_IDLE, 1, 1));
	}

	public void w(double secs) {
		u.add((int) (secs * 20));
	}

}
