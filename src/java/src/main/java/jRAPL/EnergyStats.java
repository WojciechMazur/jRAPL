package jRAPL;

import java.time.Instant;

/** High-level representation of jrapl's energy stats. */
public final class EnergyStats extends EnergySample
{
	private Instant timestamp;
	
	public EnergyStats(double[] primitiveSample, Instant ts) {
		super(primitiveSample);
		timestamp = ts;
	}

	public EnergyStats(double[] primitiveSample) {
		super(primitiveSample);
		timestamp = Instant.now();
	}

	public EnergyStats(EnergyStats other) {
		super(other);
		this.timestamp = other.timestamp;
	}

	public static String csvHeader() {
		return EnergySample.csvHeader() + "timestamp";
	}

	@Override
	public String csv() {
		return super.csv() + (
			(timestamp == null) ? "null" : Utils.timestampToUsec(timestamp)
		);
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant ts) {
		// if (this.timestamp == null) return;
		this.timestamp = ts;
	}

}