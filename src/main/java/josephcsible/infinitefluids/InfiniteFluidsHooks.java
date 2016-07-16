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

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;

public class InfiniteFluidsHooks {
	public static boolean fluidIsInfinite(Block block, World world) {
		if(world.provider.doesWaterVaporize()) {
			return InfiniteFluidsModContainer.fluidsInsideNether.contains(Block.REGISTRY.getNameForObject(block).toString()) ^ InfiniteFluidsModContainer.invertInsideNether;
		} else {
			return InfiniteFluidsModContainer.fluidsOutsideNether.contains(Block.REGISTRY.getNameForObject(block).toString()) ^ InfiniteFluidsModContainer.invertOutsideNether;
		}
	}

	public static void maybeCreateSourceBlock(BlockFluidClassic block, World world, BlockPos pos, IBlockState state) {
		if(!block.isSourceBlock(world, pos) && fluidIsInfinite(block, world)) {
			int adjacentSourceBlocks =
					(block.isSourceBlock(world, pos.north()) ? 1 : 0) +
					(block.isSourceBlock(world, pos.south()) ? 1 : 0) +
					(block.isSourceBlock(world, pos.east()) ? 1 : 0) +
					(block.isSourceBlock(world, pos.west()) ? 1 : 0);
			int densityDir = block.getDensity(world, pos) > 0 ? -1 : 1;
			if(adjacentSourceBlocks >= 2 && (world.getBlockState(pos.up(densityDir)).getMaterial().isSolid() || block.isSourceBlock(world, pos.up(densityDir)))) {
				world.setBlockState(pos, state.withProperty(BlockFluidClassic.LEVEL, 0));
			}
		}
	}
}
