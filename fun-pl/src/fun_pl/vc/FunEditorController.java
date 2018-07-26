package fun_pl.vc;

import fun_pl.semantic.FunCommandCall;
import fun_pl.semantic.FunProgram;
import unalcol.gui.editor.EditorController;
import unalcol.gui.editor.EditorView;
import unalcol.gui.log.Log;
import unalcol.gui.paint.Drawable;
import unalcol.gui.paint.Render;
import unalcol.io.Position2D;
import unalcol.language.LanguageException;
import unalcol.types.collection.keymap.HTKeyMap;

public class FunEditorController  extends FunController implements EditorController{
	protected boolean isprogram;
	protected EditorView program = null;
	protected EditorView command = null;

	public FunEditorController(String id){ 
		super(id);
		isprogram = isprogram();
	}
	
	protected boolean isprogram(){ return id().equals(FunVCModel.PROGRAM); }

	@Override
	public void position(int row, int column) {}

	protected EditorView editor( boolean program ){ 
		if(program){
			if( this.program == null ) this.program = (EditorView)front().component(FunVCModel.PROGRAM);
			return this.program;
		}else{
			if( this.command == null ) this.command = (EditorView)front().component(FunVCModel.COMMAND);
			return this.command;
		} 
	}
	
	protected void ok(){
		Log theLog = log();
		if( theLog != null ){
			theLog.out(i18n(GUIFunConstants.NO_ERRORS));
			theLog.error("");
			theLog.display(true);
		}	
	}
	
	protected void error( LanguageException e ){
		if( e.position() instanceof Position2D ){
			Position2D pos = (Position2D)e.position();
			EditorView editor = editor(pos.src()==0);
			editor.locate(pos.row(), pos.column());
		}	

		Log theLog = log();
		if( theLog != null ){
			theLog.out(i18n(GUIFunConstants.ERROR));
			theLog.error(e.getMessage());
			theLog.display(false);
		}	
	}
	
	public void compile( String program ){
		try{
			machine.clear();
			FunProgram prog = (FunProgram)quiltLang.process( program, true );
			machine.setProgram(prog);
			ok();
		}catch(LanguageException e){ error(e); }
	}
	
	public void command( String program ){
		FunCommandCall command=null;
		try{ command = (FunCommandCall)quiltLang.process( program, false); }catch(LanguageException e){ error(e); }
		if( command != null ){
			try{
				Drawable r = (Drawable)command.execute( new HTKeyMap<String, Object>() );
				Render ren = render(); 
				if( ren != null ) ren.render(r);
				ok();
			}catch(LanguageException e){ error(e); }
		}	
	}
	
	@Override
	public void text(String code){ if( isprogram ) compile(code); else command(code); }
}
