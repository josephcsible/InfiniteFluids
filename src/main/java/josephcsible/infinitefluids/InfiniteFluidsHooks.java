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

import java.util.Random;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;

public class InfiniteFluidsHooks {
	public static boolean fluidIsInfinite(Block block, World world) {
		if(world.provider.isHellWorld) {
			return InfiniteFluidsModContainer.fluidsInsideNether.contains(GameData.getBlockRegistry().getNameForObject(block)) ^ InfiniteFluidsModContainer.invertInsideNether;
		} else {
			return InfiniteFluidsModContainer.fluidsOutsideNether.contains(GameData.getBlockRegistry().getNameForObject(block)) ^ InfiniteFluidsModContainer.invertOutsideNether;
		}
	}

	public static void maybeCreateSourceBlock(BlockFluidClassic block, World world, int x, int y, int z) {
		if(!block.isSourceBlock(world, x, y, z) && fluidIsInfinite(block, world)) {
			int adjacentSourceBlocks =
					(block.isSourceBlock(world, x - 1, y, z) ? 1 : 0) +
					(block.isSourceBlock(world, x + 1, y, z) ? 1 : 0) +
					(block.isSourceBlock(world, x, y, z - 1) ? 1 : 0) +
					(block.isSourceBlock(world, x, y, z + 1) ? 1 : 0);
			if(adjacentSourceBlocks >= 2 && (world.getBlock(x, y - 1, z).getMaterial().isSolid() || block.isSourceBlock(world, x, y - 1, z))) {
				world.setBlockMetadataWithNotify(x, y, z, 0, 3); // 0: source block. 3: block update and notify clients
			}
		}
	}
}
