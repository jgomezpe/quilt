package funpl.semantic;

public abstract class FunObject{
	protected int pos;
	protected String src;
	protected FunMachine machine;
	
	public FunObject( int pos, String src, FunMachine machine ){ 
		this.pos = pos;
		this.src = src;
		this.machine = machine;
	}
	
	public void setMachine(FunMachine machine){ this.machine = machine; }
	public FunMachine machine(){ return machine; }

	protected Exception exception(String code){
	    	int[] pos = machine.pos(this.src, this.pos);
		return new Exception(this.src+"["+(pos[0]+1)+","+(pos[1]+1)+"]: "+code);
	}
	
	public FunObject( int pos ){ this.pos = pos; }
	
	public int pos(){ return pos; }
	
	public void setPos( int pos ){ this.pos = pos; }
	
	public abstract String name();
}