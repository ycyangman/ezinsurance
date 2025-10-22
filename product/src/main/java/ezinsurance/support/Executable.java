package ezinsurance.support;

import java.util.HashMap;


public interface Executable <T> {
	public abstract T execute(HashMap<?,?> param);
}