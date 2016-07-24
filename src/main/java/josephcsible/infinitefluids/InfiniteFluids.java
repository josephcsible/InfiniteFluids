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

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.world.BlockEvent.CreateFluidSourceEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = InfiniteFluids.MODID, version = InfiniteFluids.VERSION, dependencies = "required-after:Forge@[12.18.1.2023,)", guiFactory = "josephcsible.infinitefluids.InfiniteFluidsGuiFactory")
public class InfiniteFluids
{
	// XXX duplication with mcmod.info and build.gradle
	public static final String MODID = "infinitefluids";
	public static final String VERSION = "1.1.0";

	public static Configuration config;
	protected static boolean invertInsideNether, invertOutsideNether;
	protected static Set<String> fluidsInsideNether, fluidsOutsideNether;
	protected static final String[] INSIDE_NETHER_DEFAULT = {}, OUTSIDE_NETHER_DEFAULT = {"minecraft:water"};

	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		syncConfig();
	}

	@EventHandler
	public static void init(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(InfiniteFluids.class);
	}

	@SubscribeEvent
	public static void onConfigChanged(OnConfigChangedEvent eventArgs) {
		if(eventArgs.getModID().equals(MODID))
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
		config.setCategoryComment(Configuration.CATEGORY_GENERAL, "Use block names (like with /setblock) here, such as minecraft:lava or tconstruct:blueslime.");
		fluidsOutsideNether = new HashSet<String>(Arrays.asList(config.getStringList("fluidsOutsideNether", Configuration.CATEGORY_GENERAL, OUTSIDE_NETHER_DEFAULT, "A list of fluids that will be infinite outside of the Nether (or any mod-added dimensions where water can't be placed)")));
		fixVanillaFlowing(fluidsOutsideNether);
		invertOutsideNether = config.getBoolean("invertOutsideNether", Configuration.CATEGORY_GENERAL, false, "Whether to invert the function of fluidsOutsideNether (i.e., make all fluids infinite except those listed)");
		fluidsInsideNether = new HashSet<String>(Arrays.asList(config.getStringList("fluidsInsideNether", Configuration.CATEGORY_GENERAL, INSIDE_NETHER_DEFAULT, "A list of fluids that will be infinite inside of the Nether (and any mod-added dimensions where water can't be placed)")));
		fixVanillaFlowing(fluidsInsideNether);
		invertInsideNether = config.getBoolean("invertInsideNether", Configuration.CATEGORY_GENERAL, false, "Whether to invert the function of fluidsInsideNether (i.e., make all fluids infinite except those listed)");
		if(config.hasChanged())
			config.save();
	}

	protected static boolean fluidIsInfinite(Block block, World world) {
		if(world.provider.doesWaterVaporize()) {
			return fluidsInsideNether.contains(Block.REGISTRY.getNameForObject(block).toString()) ^ invertInsideNether;
		} else {
			return fluidsOutsideNether.contains(Block.REGISTRY.getNameForObject(block).toString()) ^ invertOutsideNether;
		}
	}

	@SubscribeEvent
	public static void onCreateFluidSource(CreateFluidSourceEvent event) {
		Block block = event.getState().getBlock();
		// Be careful not to change the result if it's the default anyway, so we don't unnecessarily interfere with other mods.
		if(fluidIsInfinite(block, event.getWorld())) {
			if(block != Blocks.FLOWING_WATER) {
				event.setResult(Result.ALLOW);
			}
		} else if(block == Blocks.FLOWING_WATER) {
			event.setResult(Result.DENY);
		}
	}
}
