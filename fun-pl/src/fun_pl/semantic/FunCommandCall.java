package fun_pl.semantic;

import fun_pl.syntax.FunEncoder;
import unalcol.io.Position;
import unalcol.util.I18N;

public class FunCommandCall extends FunCommand {
	protected String name;
	protected FunCommand[] args=null;

	public FunCommandCall( Position pos, String name ){
		super(pos);
		this.name = name;
	}
	
	public FunCommandCall( Position pos, String name, FunCommand[] args ){
		this(pos, name);
		this.args = args;
	}	

	public String name(){ return name; }
	public FunCommand[] args(){ return args; }

	public abstract Object execute( FunMachine machine, Object[] value ) throws Exception;

	public String toString( FunEncoder encoder ){
		StringBuilder sb = new StringBuilder();
		sb.append(I18N.get(name));
		sb.append(name);
		if( args!=null && args.length>0 ){
			sb.append(encoder.symbol(FunEncoder.OPEN));
			sb.append(args[0]);
			for( int i=1; i<args.length; i++ ){
				sb.append(encoder.symbol(FunEncoder.COMMA));
				sb.append(args[i]);
			}
			sb.append(encoder.symbol(FunEncoder.CLOSE));
		}
		return sb.toString();
	}	

}
