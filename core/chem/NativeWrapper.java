package organicchem.core.chem;

public class NativeWrapper {
	public native static long smilesToMolecule(String smiles);
	public native static String moleculeToSmiles(long molecule);
	public native static int[] drawMolecule(long molecule);
	public native static long[] processReaction(String reaction, long[] reactant);
	public native static void releaseMolecule(long molecule);
	
	static {
		System.loadLibrary("OrganicChemNative");
	}
}
