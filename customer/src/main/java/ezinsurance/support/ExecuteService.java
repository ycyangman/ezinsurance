package ezinsurance.support;

import java.util.HashMap;

public abstract class ExecuteService<T extends DefaultDTO> implements Executable<T>{
    
    public abstract T execute(HashMap<?,?> param);

}
