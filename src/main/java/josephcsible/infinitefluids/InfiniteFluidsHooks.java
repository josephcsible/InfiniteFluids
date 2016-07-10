/*
InfiniteFluids Minecraft Mod
Copyright (C) 2016 Joseph C. Sible

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
*/

package josephcsible.infinitefluids;

import java.util.Random;

import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InfiniteFluidsHooks {
	public static boolean shouldCreateSourceBlock(BlockDynamicLiquid liquid, World worldIn, BlockPos pos, IBlockState state, Random rand) {
		@SuppressWarnings("deprecation") // I know getMaterial is deprecated, but blockMaterial is private, and IMO this is better than reflection or an access transformer.
		Material material = liquid.getMaterial(state);
		if(material == Material.WATER) return true;
		if(material == Material.LAVA && worldIn.provider.doesWaterVaporize()) return true;
		return false;
	}
}
