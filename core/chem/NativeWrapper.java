package organicchem.core.chem;

public class NativeWrapper {
	public native static long smilesToMolecule(String smiles);
	public native static String moleculeToSmiles(long molecule);
	public native static int[] drawMolecule(long molecule);
	
	public native static long initReaction(String reaction);
	public native static long[] processReaction(long reaction, long[] reactant);//products each time, data11..data1n,...datamn
	
	public native static void releaseMolecule(long molecule);
	public native static void releaseReaction(long reaction);
	
	static {
		System.loadLibrary("OrganicChemNative");
	}
}
