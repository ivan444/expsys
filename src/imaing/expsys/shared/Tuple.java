package imaing.expsys.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Tuple<T1, T2> implements IsSerializable {
	public final T1 fst;
	public final T2 snd;
	
	public Tuple(T1 fst, T2 snd) {
		this.fst = fst;
		this.snd = snd;
	}

	@Override
	public String toString() {
		return "(" + fst + ", " + snd + ")";
	}
}
