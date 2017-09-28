package fun_pl.semantic;

public abstract class FunMachine{
	protected FunProgram program = new FunProgram(this);

	public FunMachine(){}
	public FunMachine(FunProgram program ){
		this.program = program;
		program.machine = this;
	}
	public FunProgram get(){ return program; }
	public void setProgram( FunProgram program ){ this.program = program; }

	public abstract String left_link();
	public abstract String right_link();
	public abstract Object[] left_unlink(Object obj);
	public abstract Object[] right_unlink(Object obj);
	public abstract Object value(String value);
	public abstract String[] values(String value);
	public abstract FunCommand primitive(String command);
	
	public Object execute( String command, Object... args ) throws Exception{
		FunCommand c = primitive(command);
		if( c != null )	return c.execute(args);

		Object obj = value(command);
		if( obj != null ){
			if( args.length > 0 ) throw new Exception("Unnecesary arguments...");
			return obj;
		}
		
		return program.execute(command, args);
	}
}