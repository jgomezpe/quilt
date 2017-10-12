package fun_pl.semantic;

import fun_pl.syntax.FunEncoder;
import fun_pl.util.FunConstants;
import unalcol.io.Position;
import unalcol.io.Position2D;
import unalcol.language.LanguageException;

public abstract class FunCommand extends FunObject{
	protected FunMachine machine;
	
	public FunCommand( Position pos, FunMachine machine ){ 
		super(pos);
		this.machine = machine;
	}
	
	public FunCommand( FunMachine machine ){ this( new Position2D(), machine); }

	public FunCommand(){ this( null ); }
	
	public void setMachine(FunMachine machine){ this.machine = machine; }
	public FunMachine machine(){ return machine; }

	protected LanguageException exception( String code, Object... args){
		Object[] nargs = new Object[3+args.length]; 
		if( args.length>0 ) System.arraycopy(args, 0, nargs, 3, args.length);
		nargs[0] = name();
		nargs[1] = row()+1;
		nargs[2] = column()+1;
		return new LanguageException(this, code, nargs);
	}
		
	public abstract Object execute( Object... value ) throws LanguageException;
	public abstract int arity();
	
	public String comment(){ return null; }
	public String toString(){
		StringBuilder sb=new StringBuilder();
		String c = comment();
		if( c!=null ) sb.append(c+"\n");
		sb.append(name());
		int n = arity();
		if( n>0 ){
			String var="XYZABCDEIJKNM";
			sb.append(FunEncoder.get_symbol(FunConstants.OPEN));
			sb.append(var.charAt(0));
			for( int i=1; i<n;i++ ){
				sb.append(FunEncoder.get_symbol(FunConstants.COMMA));			
				sb.append(var.charAt(i%var.length())+((i>=var.length())?(""+i/var.length()):""));
			}
			sb.append(FunEncoder.get_symbol(FunConstants.CLOSE));			
		}
		return sb.toString();
	}
	
}