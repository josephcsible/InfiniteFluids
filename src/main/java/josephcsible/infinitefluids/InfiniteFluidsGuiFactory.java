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

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;

public class InfiniteFluidsGuiFactory implements IModGuiFactory {

	public static class InfiniteFluidsGuiConfig extends GuiConfig {
		public InfiniteFluidsGuiConfig(GuiScreen parent) {
			super(
				parent,
				new ConfigElement(InfiniteFluidsModContainer.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(),
				InfiniteFluidsModContainer.MODID, false, false, GuiConfig.getAbridgedConfigPath(InfiniteFluidsModContainer.config.toString())
			);
		}
	}

	@Override
	public void initialize(Minecraft minecraftInstance) {
	}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return InfiniteFluidsGuiConfig.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		return null;
	}

}
