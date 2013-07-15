package organicchem.core.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;
import organicchem.core.common.ItemCommon;
import organicchem.core.common.RegistryHelper;

public class ItemOrganicLiquid extends ItemCommon {

	public ItemOrganicLiquid() {
		super(ItemOrganicLiquid.class, null);
	}
	
	@Override
	public void registerIcons(IconRegister par1IconRegister) {
		super.registerIcons(par1IconRegister);
		/*
		LiquidDictionary.getCanonicalLiquid(new LiquidStack(itemID, 1000))
				.setRenderingIcon(itemIcon)
				.setTextureSheet("/gui/items.png");
		*/
		//TODO reg fluid icon?
	}
}
