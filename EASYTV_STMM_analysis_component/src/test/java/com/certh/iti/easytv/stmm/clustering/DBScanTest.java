package com.certh.iti.easytv.stmm.clustering;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.testng.annotations.Test;

public class DBScanTest {
	
	private final int[][] clusters = 
		   {{196, 197, 202, 199, 204, 206, 201, 205, 198, 203, 200, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195},
		    {105, 136, 135, 134, 133, 132, 131, 130, 129, 128, 127, 126, 125, 124, 123, 122, 121, 120, 119, 118, 117, 116, 115, 114, 113, 112, 111, 110, 109, 108, 107, 106, 102, 103, 104},
		    {139, 137, 138, 140, 141, 142, 143, 145, 146, 148, 150, 151, 152, 154, 155, 156, 157, 158, 160, 161, 163, 165, 166, 167, 168, 169, 170, 171, 172, 174, 175, 177, 178, 173},
		    {164, 162, 159, 153, 149, 147, 144, 176},
		    {69, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 99, 98, 97, 96, 95, 94, 93, 92, 91, 90, 89, 88, 87, 86, 85, 84, 83, 82, 81, 80, 79, 78, 77, 76, 75, 74, 73, 72, 71, 70, 101, 100},
		    {21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 4, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 35, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20}};

	
	private final double[] entries = {
	         1, 68.126832, 161.675204
			,2, 44.914873, 75.058858
			,3, 106.193472, 142.420859
			,4, 162.241872, 120.278874
			,5, 161.206290, 119.607037
			,6, 161.661166, 116.450596
			,7, 160.818656, 120.437720
			,8, 161.492717, 118.919258
			,9, 164.758598, 121.334594
			,10, 163.531752, 122.374783
			,11, 159.588864, 118.221162
			,12, 163.030781, 116.184662
			,13, 157.205464, 121.188570
			,14, 156.162017, 118.281344
			,15, 157.909387, 115.499035
			,16, 160.581008, 119.657215
			,17, 158.342905, 118.014772
			,18, 159.446917, 120.551848
			,19, 160.958824, 122.248301
			,20, 160.369631, 119.300436
			,21, 161.848713, 117.764515
			,22, 162.086683, 118.594427
			,23, 159.303070, 122.232205
			,24, 162.719104, 118.537729
			,25, 163.034790, 116.388094
			,26, 164.500658, 113.191340
			,27, 165.761164, 117.091459
			,28, 162.845648, 114.791801
			,29, 161.010578, 113.325711
			,30, 166.881613, 117.241913
			,31, 164.045529, 115.535974
			,32, 159.461122, 118.319830
			,33, 158.867641, 115.152556
			,34, 162.232244, 118.705234
			,35, 161.658154, 120.682534
			,36, 156.857170, 120.329744
			,37, 157.919330, 118.536114
			,38, 157.491453, 117.221274
			,39, 164.878233, 121.304445
			,40, 168.031702, 122.029140
			,41, 166.375299, 120.163541
			,42, 166.781347, 120.624125
			,43, 165.256740, 122.750409
			,44, 165.383266, 124.162531
			,45, 167.128253, 124.130890
			,46, 161.827320, 122.486828
			,47, 159.962375, 120.550572
			,48, 163.165274, 122.250401
			,49, 165.418238, 122.488616
			,50, 160.286517, 122.111889
			,51, 161.396894, 119.673113
			,52, 161.243613, 123.228029
			,53, 126.007835, 50.534654
			,54, 124.316341, 49.218356
			,55, 123.518813, 51.142150
			,56, 126.895494, 49.766373
			,57, 122.621227, 52.090849
			,58, 123.579528, 50.551896
			,59, 128.177434, 49.355930
			,60, 126.275781, 47.990019
			,61, 126.709203, 50.008349
			,62, 125.608278, 48.926234
			,63, 123.269267, 47.786289
			,64, 126.065612, 44.527704
			,65, 126.912468, 47.794335
			,66, 123.560024, 50.545589
			,67, 126.415743, 50.558593
			,68, 124.721902, 54.091864
			,69, 123.567337, 49.646545
			,70, 124.209045, 52.167648
			,71, 126.275919, 50.103332
			,72, 127.598314, 50.534224
			,73, 123.840446, 48.290478
			,74, 127.653861, 53.096737
			,75, 125.600124, 53.253728
			,76, 125.380811, 55.524964
			,77, 128.246354, 53.349921
			,78, 124.030215, 53.288179
			,79, 128.453011, 56.217118
			,80, 127.591424, 55.537472
			,81, 122.889180, 52.788589
			,82, 121.487604, 54.092280
			,83, 125.497182, 52.754161
			,84, 121.782152, 49.080246
			,85, 122.393345, 50.811030
			,86, 121.343474, 53.172284
			,87, 124.729307, 51.514492
			,88, 128.009491, 50.594617
			,89, 126.112039, 53.033723
			,90, 128.645567, 50.517869
			,91, 130.744827, 51.928902
			,92, 128.288660, 52.438919
			,93, 131.120363, 50.338411
			,94, 127.164090, 54.254214
			,95, 123.948100, 49.819776
			,96, 120.782783, 51.071843
			,97, 121.749638, 46.927542
			,98, 124.166818, 53.335236
			,99, 121.266338, 49.703394
			,100, 125.704751, 49.822736
			,101, 123.774850, 53.424711
			,102, 22.721578, 97.565350
			,103, 22.806353, 101.232148
			,104, 19.396051, 98.678087
			,105, 24.403000, 95.899359
			,106, 25.097265, 98.469112
			,107, 26.599621, 97.583408
			,108, 22.394508, 98.990386
			,109, 18.864660, 97.142583
			,110, 20.516992, 93.524771
			,111, 18.369024, 101.101128
			,112, 17.199364, 99.524787
			,113, 16.694061, 94.186722
			,114, 17.222099, 96.661179
			,115, 16.628548, 98.312193
			,116, 22.087595, 101.235524
			,117, 19.195065, 103.899707
			,118, 22.592969, 104.034536
			,119, 25.514367, 101.757656
			,120, 23.526714, 104.134264
			,121, 22.745766, 97.719963
			,122, 24.610853, 99.556797
			,123, 24.532421, 100.933941
			,124, 21.307016, 98.729857
			,125, 23.043003, 97.268605
			,126, 26.625977, 103.940767
			,127, 24.669360, 97.670911
			,128, 27.570427, 102.195821
			,129, 26.722129, 98.966353
			,130, 22.158403, 97.850218
			,131, 25.545163, 97.154995
			,132, 22.573424, 99.008537
			,133, 22.806964, 99.907524
			,134, 22.315922, 99.928263
			,135, 18.881151, 98.191091
			,136, 21.179082, 94.568263
			,137, 85.934670, 185.045050
			,138, 83.475073, 184.538853
			,139, 87.436254, 181.913110
			,140, 85.560245, 185.976553
			,141, 88.635638, 186.720060
			,142, 83.796145, 184.550370
			,143, 82.710381, 184.206276
			,144, 83.177531, 187.861421
			,145, 84.998018, 186.872186
			,146, 85.424367, 187.110578
			,147, 82.527975, 187.876343
			,148, 85.223912, 186.531869
			,149, 82.605613, 187.684103
			,150, 85.056478, 186.694068
			,151, 84.322086, 184.472096
			,152, 82.097359, 186.678366
			,153, 83.625092, 187.678955
			,154, 87.540070, 184.654662
			,155, 81.870670, 181.451174
			,156, 80.625865, 185.698431
			,157, 87.901090, 184.329471
			,158, 85.205760, 186.296662
			,159, 85.689514, 189.302247
			,160, 85.533105, 186.283638
			,161, 84.827937, 183.200395
			,162, 83.860170, 189.914597
			,163, 86.356875, 183.910541
			,164, 83.564751, 189.862115
			,165, 88.090700, 183.742217
			,166, 86.955175, 183.220172
			,167, 89.831945, 185.939377
			,168, 89.997025, 180.865734
			,169, 86.233783, 187.042431
			,170, 88.673180, 183.302488
			,171, 85.930273, 185.601531
			,172, 82.266948, 186.636663
			,173, 85.626914, 186.698037
			,174, 81.762501, 184.641893
			,175, 80.223986, 186.120085
			,176, 80.124897, 189.396209
			,177, 78.409872, 186.571130
			,178, 81.605662, 183.830743
			,179, 145.383748, 28.923892
			,180, 147.190065, 30.666447
			,181, 147.618628, 31.084393
			,182, 142.051036, 30.007616
			,183, 144.198609, 29.641970
			,184, 146.994621, 27.112720
			,185, 148.352615, 29.364390
			,186, 147.717480, 31.397304
			,187, 146.737094, 33.027149
			,188, 144.420324, 32.663851
			,189, 149.916918, 30.514418
			,190, 149.597637, 32.204248
			,191, 147.326996, 32.511551
			,192, 146.934798, 30.895860
			,193, 147.399365, 29.576559
			,194, 146.459856, 27.490413
			,195, 147.980633, 30.694296
			,196, 149.946448, 29.942541
			,197, 145.425171, 29.964518
			,198, 148.625455, 31.321747
			,199, 147.816705, 30.186715
			,200, 141.984727, 29.768851
			,201, 143.050234, 29.983765
			,202, 141.173867, 27.644984
			,203, 140.931483, 30.586059
			,204, 144.942085, 27.399866
			,205, 144.474944, 32.566729
			,206, 142.717603, 29.122460};


	
	public class Profile implements Clusterable {
		private int id;
		private double x;
		private double y;
		
		public Profile(int id, double x, double y) {
			this.setX(x);
			this.y = y;
			this.setId(id);
		}

		public double getX() {
			return x;
		}

		public void setX(double x) {
			this.x = x;
		}
		
		public double gety() {
			return x;
		}

		public void sety(double y) {
			this.y = y;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
		
		@Override
		public String toString() {
			return String.valueOf(id);
		}
		
		@Override
		public boolean equals(Object other) {
			return id == ((Profile) other).id;
		}

		public double distanceTo(Clusterable other) {
			Profile profile2 = (Profile) other; 
			//return Math.sqrt(Math.pow(x - profile2.x, 2) + Math.pow(y - profile2.y, 2));
			return Math.abs(x - profile2.x) + Math.abs(y - profile2.y);
		}

		public double[] getPoint() {
			double[] points = {x, y};
			return points;
		}
	}
	
	public class ProfileComparable implements Comparator<Clusterable> {

		public int compare(Clusterable o1, Clusterable o2) {
			return ((Profile)o1).id - ((Profile)o2).id;
		}	
	}
	
	@Test
	public void test_apache_DBScan() {
		DBSCANClusterer<Profile> dbscan = new DBSCANClusterer<Profile>(1, 4);
		HashSet<HashSet<Clusterable> > expectedClustersSet = new HashSet<HashSet<Clusterable>>();
		
		//load profiles
		Collection<Profile> profiles = new ArrayList<Profile>();
		for(int i = 0 ; i < entries.length; i+=3) {
			profiles.add(new Profile((int) entries[i], entries[i+1], entries[i+2]));
		}

		List<Cluster<Profile>> actualClusters = dbscan.cluster(profiles);
		for(int i = 0 ; i < clusters.length; i ++) {
			HashSet<Clusterable> tmp = new HashSet<Clusterable>();
			for(int j = 0 ; j <clusters[i].length; j++) {
				tmp.add(new Profile(clusters[i][j], 0, 0));
			}
			expectedClustersSet.add(tmp);
		}
		
		printContent(actualClusters);
		printPoints(actualClusters);
	}
	
	@Test
	public void printclustersPoints() {
		
		//load profiles
		List<Profile> profiles = new ArrayList<Profile>();
		for(int i = 0 ; i < entries.length; i+=3) {
			profiles.add(new Profile((int) entries[i], entries[i+1], entries[i+2]));
		}
		
		for(int i = 0; i < clusters.length; i++) {
			for(int j = 0; j < clusters[i].length; j++) {
				Profile profile = profiles.get(clusters[i][j] - 1);
				System.out.println(profile.x+"\t"+profile.y);
			}
			System.out.println("\n");
		}	
	}

	
	
	/**
	 * Print the content of 
	 */
	private void printPoints(List<Cluster<Profile>> actualClusters) {

		for(int i = 0 ; i < actualClusters.size(); i++) {
			Cluster<Profile> cluster = actualClusters.get(i);
			List<Profile> profiles = cluster.getPoints();
			
			for(int j = 0; j < profiles.size(); j++) {
				Profile profile = profiles.get(j);
				System.out.println(profile.x+"\t"+profile.y);
			}
			System.out.println("\n");
			
		}
	}
	
	/**
	 * Print the content of 
	 */
	private void printContent(List<Cluster<Profile>> actualClusters) {

		for(int i = 0 ; i < actualClusters.size(); i++) {
			Cluster<Profile> cluster = actualClusters.get(i);
			List<Profile> l1 = cluster.getPoints();
			Collections.sort(l1, new ProfileComparable());
			System.out.println(l1.toString());
		}
	}
}