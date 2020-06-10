package jrapl;

/**
*	Functions around getting energy reading of the system
*/
public class EnergyCheckUtils extends JRAPL {

	private EnergyCheckUtils() {} // private constructor -- never initialized
	
	/**	Rapl Energy Conversion unit, fixed with (1/unit) to make a small value to prevent integer overflow.
	 *	I still don't fully understnand the concept behind this or how to use it. No current examples I can find anywhere
	 *	(No current implementation actually initializes or makes use of this variable).
	*/
	public static int wrapAroundValue;
	
	/**	Number of CPU sockets the current system has.
	 *	(No current implementation actually initializes or makes use of this variable).
	*/
	public static int socketNum;

	/** Returns a string with current total energy consumption reported in MSR registers.
	 *	<br>Formatted " 1stSocketInfo @ 2ndSocketInfo @ ... @ NthSocketInfo " with @ delimiters
	 *	<br>Each NthSocketInfo subsection formatted " dram_energy # cpu_energy # package_energy " with # delimieters
	 *	<br>This string gets parsed into an array in getEnergyStats().
	 *	<br>Example string for a 2-socket machine: 9389.21312#239874.987213#97432.2333@12321.3211#987324.1222#1237.213
	 *	@return String containing per socket energy info
	*/
	public native static String EnergyStatCheck();


	/** Parses string generated from the native EnergyStatCheck() method into an array of doubles.
	 *  <br>Array will be size (3 * socketnum). There will be three entries per socket
	 *  <br>The first entry is: Dram/uncore gpu energy (depends on the cpu architecture)
	 *  <br>The second entry is: CPU energy
	 *  <br>The third entry is: Package energy
	 *  <br>General layout of the array:
	 * 	[dram_s1, cpu_s1, pkg_s1, dram_s2, cpu_s2, pkg_s2, ... , dram_sn, cpu_sn, pkg_sn]
	 *	sn means socket number associated with this reading for all n greater than 1
	 * @return an array of current energy information.
	*/
	public static double[] getEnergyStats() {
		int socketNum = ArchSpec.GetSocketNum(); //@TODO -- is this redundant? can we just assume that it was already set during the sstatic block?
		String EnergyInfo = EnergyStatCheck();
		/*One Socket*/
		if(socketNum == 1) {
			double[] stats = new double[3]; // 3 stats per socket
			String[] energy = EnergyInfo.split("#");

			stats[0] = Double.parseDouble(energy[0]);
			stats[1] = Double.parseDouble(energy[1]);
			stats[2] = Double.parseDouble(energy[2]);

			return stats;

		} else {
		/*Multiple sockets*/
			String[] perSockEner = EnergyInfo.split("@");
			double[] stats = new double[3*socketNum]; // 3 stats per socket
			int count = 0;


			for(int i = 0; i < perSockEner.length; i++) {
				String[] energy = perSockEner[i].split("#");
				for(int j = 0; j < energy.length; j++) {
					count = i * 3 + j;	//accumulative count
					stats[count] = Double.parseDouble(energy[j]);
				}
			}
			return stats;
		}
	}

	public static void main(String[] args)
	{



	}

}
