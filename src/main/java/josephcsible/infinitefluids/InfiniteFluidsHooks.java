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
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class InfiniteFluidsHooks {
	public static boolean shouldCreateSourceBlock(BlockDynamicLiquid liquid, World worldIn, int x, int y, int z, Random rand) {
		Material material = liquid.getMaterial();
		if(material == Material.water) return true;
		if(material == Material.lava && worldIn.provider.isHellWorld) return true;
		return false;
	}
}
