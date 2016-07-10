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
