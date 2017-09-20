package quilt.syntax;

import unalcol.gui.editor.Position;

public class ParserPos extends Position{
	protected int offset;
	public ParserPos(){
		super();
		offset=0;
	}
	
	public ParserPos( ParserPos pos ){ set(pos); }
	
	public int offset(){ return offset; }
	
	public void set( int offset, int row, int column ){
		this.row = row;
		this.column = column;
		this.offset = offset;
	}

	public void set( ParserPos pos ){
		row = pos.row;
		column = pos.column;
		offset = pos.offset;
	}
	
	public void init(){
		row=0;
		column=0;
		offset=0;
	}	
}