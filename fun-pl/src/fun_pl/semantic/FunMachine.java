package fun_pl.semantic;

public interface FunMachine{
	public String left_link();
	public String right_link();
	
	public String[] values();
	public FunCommand[] primitives();
	
	public default int primitive_index( String command ){
		FunCommand[] prims = primitives();
		int i=0;
		while( i<prims.length && !prims[i].name().equals(command) ) i++;
		return (i<prims.length)?i:-1;
	}
	
	public default FunCommand primitive( String command ){
		FunCommand[] prims = primitives();
		int i=primitive_index(command);
		return (i<prims.length)?prims[i]:null;
	}
	
	public default boolean is_primitive( String command ){
		return primitive_index(command)<primitives().length;
	}
}