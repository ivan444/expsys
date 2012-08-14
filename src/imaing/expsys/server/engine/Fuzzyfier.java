package imaing.expsys.server.engine;

import java.util.HashMap;
import java.util.Map;

public class Fuzzyfier {
	private String[] procs = new String[]{"i7", "i5", "i3", "CoreDuo", "P4"};
	private double[][] procsMVal = new double[][]{
			{1.0, 0.6, 0.0, 0.0, 0.0},
			{0.0, 0.5, 1.0, 0.3, 0.0},
			{0.0, 0.0, 0.0, 0.3, 1.0},
	};
	
	private String[] monitors = new String[]{"17in", "15.4in", "15in", "13.3in", "10in"};
	private double[][] monMVal = new double[][]{
			{0.8, 0.5, 0.5, 0.0, 0.0},
			{0.0, 0.6, 0.6, 0.3, 0.0},
			{0.0, 0.0, 0.0, 0.5, 1.0},
	};
	
	private String[] hdds = new String[]{"1000GB", "750GB", "500GB", "250GB", "100GB", "80GB"};
	private double[][] hddMVal = new double[][]{
			{1.0, 0.7, 0.5, 0.0, 0.0, 0.0},
			{0.0, 0.1, 0.5, 1.0, 0.3, 0.0},
			{0.0, 0.0, 0.0, 0.0, 0.7, 1.0},
	};
	
	private Map<String, double[][]> mvals;
	private Map<String, String[]> fClasses;
	
	private Fuzzyfier() {
		mvals = new HashMap<String, double[][]>();
		mvals.put("proc", procsMVal);
		mvals.put("mon", monMVal);
		mvals.put("hdd", hddMVal);
		
		fClasses = new HashMap<String, String[]>();
		fClasses.put("proc", procs);
		fClasses.put("mon", monitors);
		fClasses.put("hdd", hdds);
	}

	/**
	 * SingletonHolder is loaded on the first execution of
	 * Singleton.getInstance() or the first access to SingletonHolder.INSTANCE,
	 * not before.
	 */
	private static class SingletonHolder {
		public static final Fuzzyfier INSTANCE = new Fuzzyfier();
	}

	public static Fuzzyfier get() {
		return SingletonHolder.INSTANCE;
	}

	public double fuzz(String cht, String cid, int fcls) {
		double[][] mvs = mvals.get(cht);
		String[] chts = fClasses.get(cht);
		
		int idx = -1;
		for (int i = 0; i < chts.length; i++) {
			if (chts[i].equals(cid)) {
				idx = i;
				break;
			}
		}
		
		return mvs[fcls][idx];
	}

}
