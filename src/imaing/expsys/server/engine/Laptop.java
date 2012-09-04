package imaing.expsys.server.engine;

import java.util.HashMap;
import java.util.Map;

public class Laptop implements IProduct {
	private Map<String, String> chts;
	private String desc;
	
	public Laptop(String desc, String proc, String mon, String hdd) {
		chts = new HashMap<String, String>();
		chts.put("proc", proc);
		chts.put("mon", mon);
		chts.put("hdd", hdd);
		
		this.desc = desc;
	}
	
	@Override
	public double charaMemberVal(String cht, int fcls) {
		String charaId = chts.get(cht);
		return Fuzzyfier.get().fuzz(cht, charaId, fcls);
	}

	@Override
	public String describe() {
		return desc;
	}

	@Override
	public String toString() {
		return describe();
	}

}
