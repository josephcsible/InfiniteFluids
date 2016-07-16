/*
InfiniteFluids Minecraft Mod
Copyright (C) 2016 Joseph C. Sible

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License along
with this program; if not, write to the Free Software Foundation, Inc.,
51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/

package josephcsible.infinitefluids;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.minecraftforge.common.config.Configuration;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class InfiniteFluidsModContainer extends DummyModContainer {
	public static Configuration config;
	public static boolean invertInsideNether, invertOutsideNether;
	public static Set<String> fluidsInsideNether, fluidsOutsideNether;
	public static final String[] INSIDE_NETHER_DEFAULT = {}, OUTSIDE_NETHER_DEFAULT = {"minecraft:water"};

	// XXX duplication with mcmod.info and build.gradle
	public static final String MODID = "infinitefluids";
	public static final String VERSION = "1.0.0";

	public InfiniteFluidsModContainer() {
		super(new ModMetadata());
		ModMetadata metadata = getMetadata();
		metadata.modId = MODID;
		metadata.version = VERSION;
		metadata.name = "InfiniteFluids";
		metadata.description = "Allows fluids other than water to be infinite (i.e., turn non-source blocks next to source blocks into source blocks).";
		metadata.url = "http://minecraft.curseforge.com/projects/infinitefluids";
		metadata.authorList.add("Joseph C. Sible");
	}

	@Override
	public String getGuiClassName() {
		return InfiniteFluidsGuiFactory.class.getName();
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
	}

	@Subscribe
	public void preInit(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		syncConfig();
	}

	@Subscribe
	public void init(FMLInitializationEvent event) {
		FMLCommonHandler.instance().bus().register(this);
	}

	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent eventArgs) {
		if(eventArgs.modID.equals(MODID))
			syncConfig();
	}

	protected static void fixVanillaFlowing(Set<String> s) {
		if(s.contains("minecraft:water")) {
			s.remove("minecraft:water");
			s.add("minecraft:flowing_water");
		}
		if(s.contains("minecraft:lava")) {
			s.remove("minecraft:lava");
			s.add("minecraft:flowing_lava");
		}
	}

	protected static void syncConfig() {
		config.setCategoryComment(Configuration.CATEGORY_GENERAL, "Use block names (like with /setblock) here, such as minecraft:lava or TConstruct:liquid.slime.");
		fluidsOutsideNether = new HashSet(Arrays.asList(config.getStringList("fluidsOutsideNether", Configuration.CATEGORY_GENERAL, OUTSIDE_NETHER_DEFAULT, "A list of fluids that will be infinite outside of the Nether (or any mod-added dimensions where water can't be placed)")));
		fixVanillaFlowing(fluidsOutsideNether);
		invertOutsideNether = config.getBoolean("invertOutsideNether", Configuration.CATEGORY_GENERAL, false, "Whether to invert the function of fluidsOutsideNether (i.e., make all fluids infinite except those listed)");
		fluidsInsideNether = new HashSet(Arrays.asList(config.getStringList("fluidsInsideNether", Configuration.CATEGORY_GENERAL, INSIDE_NETHER_DEFAULT, "A list of fluids that will be infinite inside of the Nether (and any mod-added dimensions where water can't be placed)")));
		fixVanillaFlowing(fluidsInsideNether);
		invertInsideNether = config.getBoolean("invertInsideNether", Configuration.CATEGORY_GENERAL, false, "Whether to invert the function of fluidsInsideNether (i.e., make all fluids infinite except those listed)");
		if(config.hasChanged())
			config.save();
	}
}
