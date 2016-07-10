package josephcsible.infinitefluids;

import com.google.common.eventbus.EventBus;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

public class InfiniteFluidsModContainer extends DummyModContainer {
	public InfiniteFluidsModContainer() {
		super(new ModMetadata());
		ModMetadata metadata = getMetadata();
		// XXX almost all this is duplicated between here and mcmod.info
		metadata.modId = "infinitefluids";
		// XXX version is duplicated between here and build.gradle
		metadata.version = "0.1.0";
		metadata.name = "InfiniteFluids";
		metadata.description = "Allows fluids other than water to be infinite (i.e., turn non-source blocks next to source blocks into source blocks).";
		metadata.url = "";
		metadata.authorList.add("Joseph C. Sible");
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		return true; // even if we don't have anything to register for, if we return false, Forge says we're not loaded
	}
}
