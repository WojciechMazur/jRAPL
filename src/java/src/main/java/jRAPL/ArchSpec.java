package jRAPL;

public final class ArchSpec {

	public static final int NUM_SOCKETS;	
	public static final int NUM_STATS_PER_SOCKET;
	public static final double RAPL_WRAPAROUND;
	public static final int MICRO_ARCHITECTURE;
	public static final String MICRO_ARCHITECTURE_NAME;

	public native static int getSocketNum();
	public native static double getWraparoundEnergy();
	public native static String getMicroArchitectureName();
	public native static int getMicroArchitecture();
	public native static String energyStatsStringFormat();

	// the indexes of where power domains are in the returned array of energy stats
	// I wonder if this is the best class to calculate and store these indices.
	//  it's definitely safe and ok enough for now and possibly permanently.
	//  but just keep it in the back of your mind that you might wanna relocate
	//  to something more specific, maybe classes only relevant to energy monitoring,
	//  since where else would you even use these values?
	public static final int DRAM_IDX;
	public static final int GPU_IDX;
	public static final int CORE_IDX;
	public static final int PKG_IDX;
	//@TODO -- there's a 5th possible power domain, right? like full motherboard energy or something

	public static final boolean DRAM_SUPPORTED;
	public static final boolean GPU_SUPPORTED;
	public static final boolean CORE_SUPPORTED;
	public static final boolean PKG_SUPPORTED;

	static {
		EnergyManager m = new EnergyManager();	// to make sure that native access has been set up at this point.
		m.activate();							// is dealloc'd by the end of this static block since it has no other use
		
		MICRO_ARCHITECTURE = getMicroArchitecture();
		MICRO_ARCHITECTURE_NAME = getMicroArchitectureName();

		NUM_SOCKETS = getSocketNum();
		RAPL_WRAPAROUND = getWraparoundEnergy();

		int dramIndex = -1,
			gpuIndex = -1,
			coreIndex = -1,
			pkgIndex = -1;

		int idx = 0;
		for ( String part : EnergySample.csvHeader().split(",") ) {
			switch (part.split("_")[0]) {
				case "dram":
					dramIndex = idx++;
					break;
				case "gpu":
					gpuIndex = idx++;
					break;
				case "core":
					coreIndex = idx++;
					break;
				case "pkg":
					pkgIndex = idx++;
					break;
				case "timestamp":
					continue;
				default:
					System.err.println (
						"unexpected part found in csv header: "
						+ part
					);
					System.exit(0);
			}
		}

		DRAM_IDX = dramIndex;
		GPU_IDX = gpuIndex;
		CORE_IDX = coreIndex;
		PKG_IDX = pkgIndex;

		DRAM_SUPPORTED = (DRAM_IDX != -1);
		GPU_SUPPORTED  = (GPU_IDX  != -1);
		CORE_SUPPORTED = (CORE_IDX != -1);
		PKG_SUPPORTED  = (PKG_IDX  != -1);

		int n = 0;
		boolean[] supported = {
			DRAM_SUPPORTED,
			GPU_SUPPORTED,
			CORE_SUPPORTED,
			PKG_SUPPORTED
		};

		for ( boolean sup : supported ) {
			if (sup) n++;
		} NUM_STATS_PER_SOCKET = n;

		m.deactivate();
	}
	
	public static void init() {} // do-nothing function to trigger the static block...probably a better way of doing this

	public static String infoString() {
		return String.join(
			"\n",
			"MICRO_ARCHITECTURE: " + Integer.toHexString(MICRO_ARCHITECTURE),
			"MICRO_ARCHITECTURE_NAME: " + MICRO_ARCHITECTURE_NAME,
			"",
			"NUM_SOCKETS: " + NUM_SOCKETS,
			"NUM_STATS_PER_SOCKET: " + NUM_STATS_PER_SOCKET,
			"RAPL_WRAPAROUND: " + RAPL_WRAPAROUND,
			"",
			"DRAM_IDX: " + DRAM_IDX,
			"GPU_IDX: " + GPU_IDX,
			"CORE_IDX: " + CORE_IDX,
			"PKG_IDX: " + PKG_IDX,
			"",
			"DRAM_SUPPORTED: " + DRAM_SUPPORTED,
			"GPU_SUPPORTED: " + GPU_SUPPORTED,
			"CORE_SUPPORTED: " + CORE_SUPPORTED,
			"PKG_SUPPORTED: " + PKG_SUPPORTED
		);
	}

}
