package jrapltesting;
import jrapl.*;

public class GeneralTestDriver
{
	public static void main(String[] args)
	{
		JRAPL.loadLibrary();
		JRAPL.ProfileInit();
		System.out.println(ArchitectureSpecifications.infoString());
		JRAPL.ProfileDealloc();
	}

	private static void threadThing()
	{
		AsyncEnergyMonitorJavaSide aemonj = new AsyncEnergyMonitorJavaSide();
		aemonj.start();
		try { Thread.sleep(5000); }
		catch (Exception e) {}
		aemonj.stop();
		System.out.println(aemonj);
	}


}
