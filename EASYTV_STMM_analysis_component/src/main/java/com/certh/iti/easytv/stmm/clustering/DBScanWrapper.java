package com.certh.iti.easytv.stmm.clustering;

import org.apache.commons.math3.ml.clustering.Clusterer;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;

import com.certh.iti.easytv.stmm.user.profile.UserProfile;

public class DBScanWrapper implements iCluster{
	
	private double eps = 155;
	private int minPts = 4;
	private String[] compareMode;
	
	
	public DBScanWrapper() {
	}
	
	public  Clusterer<UserProfile> Clone() {
		
		System.out.print("Initiating Apache DBScan with values eps= "+eps+" minPts= "+minPts+" compareMode= ");
		for(int i = 0; i < compareMode.length - 1; i++)
			System.out.print(compareMode[i]+",");
		System.out.println(compareMode[compareMode.length - 1]);
				
		return new DBSCANClusterer<UserProfile>(eps, minPts, new UserProfileDistanceMeasure(compareMode));
	}

	public String get_Name() {
		return "DBScan";
	}

}
