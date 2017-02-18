package io.github.teamfractal.entity;

import io.github.teamfractal.entity.enums.ResourceType;

public class Roboticon {
	private int ID;
	private ResourceType customisation;
	private LandPlot installedLandPlot;
	
	/**
	 * Constructor
	 * @param ID The ID number of the roboticon
	 */
	Roboticon(int ID) {
		this.ID = ID;
		customisation = ResourceType.Unknown;
	}
	
	/**
	 * @return The ID number of this roboticon (useful to distinguish between roboticons)
	 */
	public int getID () {
		return this.ID;
	}

	/**
	 * @return The customisation type of this roboticon (which is of type ResourceType)
	 */
	public ResourceType getCustomisation() {
		return this.customisation;
	}
	
	/**
	 * Set the customisation type of this roboticon which determines what resource
	 * it is capable of producing
	 * @param type
	 */
	void setCustomisation(ResourceType type) {
		this.customisation = type;
	}

	/**
	 * @return true if this roboticon has been installed on some LandPlot and false otherwise
	 */
	public synchronized boolean isInstalled() {
		return installedLandPlot != null;
	}
	/**
	 * 
	 * @param sets land plot which roboticon is installed to
	 * @return true if roboticon is installed, false if not
	 */
	public synchronized boolean setInstalledLandplot(LandPlot landplot) {
		if (!isInstalled()) {
			installedLandPlot = landplot;
			return true;
		}

		System.out.println("Already installed to LandPlot! Cancel.");
		return false;
	}
}
