package jrapl;

public final class ArchitectureSpecifications {

	public static final boolean readingDRAM;
	public static final boolean readingGPU;
	public static final int NUM_SOCKETS;	
	public static final int ENERGY_WRAP_AROUND;
	public static final int cpuModel;
	public static final String cpuModelName;


	public native static int PowerDomainsSupported();
	public native static int GetSocketNum();
	public native static int GetWraparoundEnergy();
	public native static String GetCpuModelName();
	public native static int GetCpuModel();

	public native static String EnergyStatsStringFormat();

	static {
		JRAPL.loadLibrary();

		cpuModel = GetCpuModel();
		cpuModelName = GetCpuModelName();

		int rd = PowerDomainsSupported();
		if ( rd == 3 ) {
			readingDRAM = true;
			readingGPU  = true;
		} else if ( rd == 1 ) {
			readingDRAM = true;
			readingGPU  = false;
		} else if ( rd == 2 ) {
			readingDRAM = false;
			readingGPU  = true;
		} else {
			readingDRAM = false;
			readingGPU  = false;
		}

		NUM_SOCKETS = GetSocketNum();
		ENERGY_WRAP_AROUND = GetWraparoundEnergy();
	}

	public static String infoString() {
		String s = new String();
		s += "readingDRAM: " + readingDRAM + "\n";
		s += "readingGPU: " + readingGPU + "\n";
		s += "NUM_SOCKETS: " + NUM_SOCKETS + "\n";
		s += "ENERGY_WRAP_AROUND: " + ENERGY_WRAP_AROUND + "\n";
		s += "cpuModel: " + Integer.toHexString(cpuModel) + "\n";
		s += "cpuModelName: " + cpuModelName;
		return s;
	}

	public static void main(String[] args) {
		JRAPL.loadLibrary();
		//JRAPL.ProfileInit();
		System.out.println(EnergyStatsStringFormat());
		//JRAPL.ProfileDealloc();
	}

}




