package imaing.expsys;

public interface Product {
	/**
	 * @param charaId ID of characteristic.
	 * @param fcls Fuzzy class for which we need membership value.
	 * @return Membership value for characteristic specified by ID and fizzy class.
	 */
	double charaMemberVal(String cht, int fcls);
	String describe();
}
