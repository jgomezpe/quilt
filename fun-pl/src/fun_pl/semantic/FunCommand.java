package fun_pl.semantic;

import fun_pl.syntax.FunEncoder;
import fun_pl.util.FunConstants;
import unalcol.types.collection.iterator.Position2DTrack;
import unalcol.language.LanguageException;

public abstract class FunCommand extends FunObject{
	protected FunMachine machine;
	
	public FunCommand( Position2DTrack pos, FunMachine machine ){ 
		super(pos);
		this.machine = machine;
	}
	
	public void setMachine(FunMachine machine){ this.machine = machine; }
	public FunMachine machine(){ return machine; }

	protected LanguageException exception( String code, Object... args){
		Object[] nargs = new Object[3+args.length]; 
		if( args.length>0 ) System.arraycopy(args, 0, nargs, 3, args.length);
		nargs[0] = name();
		nargs[1] = pos.row()+1;
		nargs[2] = pos.column()+1;
		return new LanguageException(pos, code, nargs);
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