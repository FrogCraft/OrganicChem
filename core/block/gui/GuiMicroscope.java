package organicchem.core.block.gui;

import org.lwjgl.opengl.GL11;

import organicchem.core.chem.Graph;
import organicchem.core.chem.NativeWrapper;
import organicchem.core.common.GuiContainerCommon;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiMicroscope extends GuiContainerCommon {
	private static final String BACKGROUND = "Microscope";
	
	private static final int border = 6;
	private static final int molL = 30 + border;
	private static final int molT = 9 + border;
	private static final int molR = 167 - border;
	private static final int molB = 76 - border;
	
	//private Graph graph;
	private static final int[] testData = new int[] {
		5, 100, 4, 0, 0, 689, 104,
		1, 1, 0, 17, 6, 40, 30, 169, 104,
		1, 1, 0, 6, 6, 169, 104, 299, 29,
		1, 1, 0, 6, 6, 292, 42, 422, 117,
		1, 1, 0, 6, 6, 307, 17, 437, 92,
		1, 1, 0, 6, 6, 429, 104, 559, 29,
		1, 1, 0, 6, 6, 559, 29, 689, 104,
		3, 17, 40, 30, 2, 67, 108, 4
	};
	
	public GuiMicroscope(World world, EntityPlayer player, int x, int y, int z) {
		super(new ContainerMicroscope(world, player, x, y, z), world, player, x, y, z);
		setBackground(BACKGROUND);
		//long molecule = NativeWrapper.smilesToMolecule("CCO");
		//int graphData[] = NativeWrapper.drawMolecule(molecule);
		//graph = new Graph(graphData);
		//NativeWrapper.releaseMolecule(molecule);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
		super.drawGuiContainerBackgroundLayer(f, i, j);
		drawMolecule();
	}
	
	private void drawMolecule() {
		ContainerMicroscope container = (ContainerMicroscope) inventorySlots;
		if (container.graph != null) {
			GL11.glColor3f(0, 0, 0);
			GL11.glPushMatrix();
			GL11.glTranslatef(this.guiLeft, this.guiTop, 0);
			GL11.glTranslatef(molL, molT, 0);
			
			//graph.render(molL + this.guiLeft, molT + this.guiTop, molR - molL, molB - molT, fontRenderer);
			container.graph.render(molL + this.guiLeft, molT + this.guiTop, molR - molL, molB - molT, fontRenderer);
		}

	}
}
