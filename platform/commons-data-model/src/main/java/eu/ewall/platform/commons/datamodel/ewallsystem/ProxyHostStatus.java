/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.ewallsystem;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Enum ProxyStatus.
 * 
 * @author emirmos
 *
 */

@XmlRootElement
public class ProxyHostStatus implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3134008293350677290L;

	public ProxyHostStatus() {
	}

	// CPU
	float cpuUsage;

	// RAM
	long totalRAM;
	long usedRAM;
	long freeRAM;
	double usedRAMPercent;

	// SWAP
	long totalSwap;
	long usedSwap;
	long freeSwap;
	double usedSwapPercent;

	// root partition (c: or /)
	long totalRootSpace;
	long usedRootSpace;
	long freeRootSpace;
	double usedRootSpacePercent;

	// JSONArray partitions;
	// @XmlElement(name="partition")
	// @XmlElementWrapper(name="partition")
	// List<DiskPartition> partitions;

	public ProxyHostStatus(float cpuUsage, long totalRAM, long usedRAM,
			long freeRAM, double usedRAMPercent, long totalSwap, long usedSwap,
			long freeSwap, double usedSwapPercent, long totalRootSpace,
			long usedRootSpace, long freeRootSpace, double usedRootSpacePercent) {
		super();
		this.cpuUsage = cpuUsage;
		this.totalRAM = totalRAM;
		this.usedRAM = usedRAM;
		this.freeRAM = freeRAM;
		this.usedRAMPercent = usedRAMPercent;
		this.totalSwap = totalSwap;
		this.usedSwap = usedSwap;
		this.freeSwap = freeSwap;
		this.usedSwapPercent = usedSwapPercent;
		this.totalRootSpace = totalRootSpace;
		this.usedRootSpace = usedRootSpace;
		this.freeRootSpace = freeRootSpace;
		this.usedRootSpacePercent = usedRootSpacePercent;
	}

	/**
	 * @return the cpuUsage
	 */
	public float getCpuUsage() {
		return cpuUsage;
	}

	/**
	 * @param cpuUsage
	 *            the cpuUsage to set
	 */
	public void setCpuUsage(float cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	/**
	 * @return the totalRAM
	 */
	public long getTotalRAM() {
		return totalRAM;
	}

	/**
	 * @param totalRAM
	 *            the totalRAM to set
	 */
	public void setTotalRAM(long totalRAM) {
		this.totalRAM = totalRAM;
	}

	/**
	 * @return the usedRAM
	 */
	public long getUsedRAM() {
		return usedRAM;
	}

	/**
	 * @param usedRAM
	 *            the usedRAM to set
	 */
	public void setUsedRAM(long usedRAM) {
		this.usedRAM = usedRAM;
	}

	/**
	 * @return the freeRAM
	 */
	public long getFreeRAM() {
		return freeRAM;
	}

	/**
	 * @param freeRAM
	 *            the freeRAM to set
	 */
	public void setFreeRAM(long freeRAM) {
		this.freeRAM = freeRAM;
	}

	/**
	 * @return the totalSwap
	 */
	public long getTotalSwap() {
		return totalSwap;
	}

	/**
	 * @param totalSwap
	 *            the totalSwap to set
	 */
	public void setTotalSwap(long totalSwap) {
		this.totalSwap = totalSwap;
	}

	/**
	 * @return the usedSwap
	 */
	public long getUsedSwap() {
		return usedSwap;
	}

	/**
	 * @param usedSwap
	 *            the usedSwap to set
	 */
	public void setUsedSwap(long usedSwap) {
		this.usedSwap = usedSwap;
	}

	/**
	 * @return the freeSwap
	 */
	public long getFreeSwap() {
		return freeSwap;
	}

	/**
	 * @param freeSwap
	 *            the freeSwap to set
	 */
	public void setFreeSwap(long freeSwap) {
		this.freeSwap = freeSwap;
	}

	/**
	 * @return the usedRAMPercent
	 */
	public double getUsedRAMPercent() {
		return usedRAMPercent;
	}

	/**
	 * @param usedRAMPercent
	 *            the usedRAMPercent to set
	 */
	public void setUsedRAMPercent(double usedRAMPercent) {
		this.usedRAMPercent = usedRAMPercent;
	}

	/**
	 * @return the usedSwapPercent
	 */
	public double getUsedSwapPercent() {
		return usedSwapPercent;
	}

	/**
	 * @param usedSwapPercent
	 *            the usedSwapPercent to set
	 */
	public void setUsedSwapPercent(double usedSwapPercent) {
		this.usedSwapPercent = usedSwapPercent;
	}

	/**
	 * @return the totalRootSpace
	 */
	public long getTotalRootSpace() {
		return totalRootSpace;
	}

	/**
	 * @param totalRootSpace
	 *            the totalRootSpace to set
	 */
	public void setTotalRootSpace(long totalRootSpace) {
		this.totalRootSpace = totalRootSpace;
	}

	/**
	 * @return the usedRootSpace
	 */
	public long getUsedRootSpace() {
		return usedRootSpace;
	}

	/**
	 * @param usedRootSpace
	 *            the usedRootSpace to set
	 */
	public void setUsedRootSpace(long usedRootSpace) {
		this.usedRootSpace = usedRootSpace;
	}

	/**
	 * @return the freeRootSpace
	 */
	public long getFreeRootSpace() {
		return freeRootSpace;
	}

	/**
	 * @param freeRootSpace
	 *            the freeRootSpace to set
	 */
	public void setFreeRootSpace(long freeRootSpace) {
		this.freeRootSpace = freeRootSpace;
	}

	/**
	 * @return the usedRootSpacePercent
	 */
	public double getUsedRootSpacePercent() {
		return usedRootSpacePercent;
	}

	/**
	 * @param usedRootSpacePercent
	 *            the usedRootSpacePercent to set
	 */
	public void setUsedRootSpacePercent(double usedRootSpacePercent) {
		this.usedRootSpacePercent = usedRootSpacePercent;
	}

	// public static class DiskPartition {
	// String path;
	// long totalSpace;
	// long usedSpace;
	// long freeSpace;
	// double usedSpacePercent;
	//
	// public DiskPartition() {
	// }
	//
	// public DiskPartition(String path, long totalSpace, long usedSpace,
	// long freeSpace, double usedSpacePercent) {
	// super();
	// this.path = path;
	// this.totalSpace = totalSpace;
	// this.usedSpace = usedSpace;
	// this.freeSpace = freeSpace;
	// this.usedSpacePercent = usedSpacePercent;
	// }
	//
	// /**
	// * @return the path
	// */
	// public String getPath() {
	// return path;
	// }
	//
	// /**
	// * @param path
	// * the path to set
	// */
	// public void setPath(String path) {
	// this.path = path;
	// }
	//
	// /**
	// * @return the totalSpace
	// */
	// public long getTotalSpace() {
	// return totalSpace;
	// }
	//
	// /**
	// * @param totalSpace
	// * the totalSpace to set
	// */
	// public void setTotalSpace(long totalSpace) {
	// this.totalSpace = totalSpace;
	// }
	//
	// /**
	// * @return the usedSpace
	// */
	// public long getUsedSpace() {
	// return usedSpace;
	// }
	//
	// /**
	// * @param usedSpace
	// * the usedSpace to set
	// */
	// public void setUsedSpace(long usedSpace) {
	// this.usedSpace = usedSpace;
	// }
	//
	// /**
	// * @return the freeSpace
	// */
	// public long getFreeSpace() {
	// return freeSpace;
	// }
	//
	// /**
	// * @param freeSpace
	// * the freeSpace to set
	// */
	// public void setFreeSpace(long freeSpace) {
	// this.freeSpace = freeSpace;
	// }
	//
	// /**
	// * @return the freeSpacePercent
	// */
	// public double getUsedSpacePercent() {
	// return usedSpacePercent;
	// }
	//
	// /**
	// * @param freeSpacePercent
	// * the freeSpacePercent to set
	// */
	// public void setUsedSpacePercent(double usedSpacePercent) {
	// this.usedSpacePercent = usedSpacePercent;
	// }
	//
	// }

}