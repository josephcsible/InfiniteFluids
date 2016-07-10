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
