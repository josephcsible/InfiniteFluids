package josephcsible.infinitefluids;

import java.util.Map;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;

@MCVersion("1.7.10")
public class InfiniteFluidsLoadingPlugin implements IFMLLoadingPlugin {

	// XXX this feels hacky. Is this really the best way to keep track of this?
	public static boolean runtimeDeobfuscationEnabled;

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{InfiniteFluidsClassTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		return InfiniteFluidsModContainer.class.getName();
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		runtimeDeobfuscationEnabled = (Boolean) data.get("runtimeDeobfuscationEnabled");
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
