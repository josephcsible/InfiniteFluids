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

import com.google.common.eventbus.EventBus;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;

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
