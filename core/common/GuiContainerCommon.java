package organicchem.core.common;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class GuiContainerCommon extends GuiContainer {
	private static final String PATH_PREFIX = "/mods/" + RegistryHelper.TEXTUREPATH + "/textures/gui/";
	private static final String PATH_SUFFIX = ".png";
	private boolean preventExchange = false;
	private boolean handleButtonEvent = false;
	protected TileEntity tileEntity;
	protected String BACKGROUND;

	public GuiContainerCommon(ContainerCommon par1Container, World world, EntityPlayer player, int x, int y, int z) {
		super(par1Container);
		tileEntity = (TileEntity) world.getBlockTileEntity(x, y, z);
	}
	
	protected void setBackground(String value) {
		BACKGROUND = value;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
		this.mc.renderEngine.bindTexture(makeTexturePath(BACKGROUND));
		this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2) {
		GuiButtonCommon i;
		for (Object button : buttonList) {
			i = (GuiButtonCommon) button;
			if (i.item != null)
				drawItem(i.item, i.iconLeft - guiLeft, i.iconTop - guiTop);
		}
	}
	
	@Override
	protected boolean checkHotbarKeys(int par1) {
		if (preventExchange) {
			return false;
		} else {
			return super.checkHotbarKeys(par1);
		}
	}
	
	@Override
	protected void actionPerformed(GuiButton par1GuiButton) {
		if (!handleButtonEvent) return;
		((ContainerCommon) inventorySlots).sendGuiEvent(par1GuiButton.id);
	}

	private void drawItem(ItemStack item, int x, int y) {
		itemRenderer.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, item, x, y);
	}
	
	protected void preventExchange() {
		preventExchange = true;
	}
	
	protected void handleButtonEvent() {
		handleButtonEvent = true;
	}
	
	protected String makeTexturePath(String name) {
		return PATH_PREFIX + name + PATH_SUFFIX;
	}
	
}
