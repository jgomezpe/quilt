package fun_pl.vc;

import fun_pl.semantic.FunCommandCall;
import fun_pl.semantic.FunProgram;
import unalcol.gui.editor.EditorController;
import unalcol.gui.editor.EditorView;
import unalcol.gui.log.Log;
import unalcol.gui.render.Render;
import unalcol.iterator.Position2DTrack;
import unalcol.language.LanguageException;
import unalcol.collection.keymap.HashMap;

public class FunEditorController  extends FunController implements EditorController{
	protected boolean isprogram;
	protected EditorView program = null;
	protected EditorView command = null;

	public FunEditorController(String id){ 
		super(id);
		isprogram = id.equals(FunVCModel.PROGRAM);
	}
	
	protected boolean isprogram(){ return isprogram; }

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
		System.out.println("[FunEditorController.ok]"+theLog);
		if( theLog != null ){
			theLog.out(i18n(GUIFunConstants.NO_ERRORS));
		}	
	}
	
	protected void error( LanguageException e ){
		if( e.position() instanceof Position2DTrack ){
			Position2DTrack pos = (Position2DTrack)e.position();
			EditorView editor = editor(pos.src()==1);
			editor.locate(pos.row(), pos.column());
		}	

		Log theLog = log();
		System.out.println("[FunEditorController.err]"+e.getMessage());
		if( theLog != null ){
			theLog.error(e.getMessage());
		}	
	}
	
	public void compile( String program ){
		try{
			machine.clear();
			FunProgram prog = (FunProgram)quiltLang.process( program, true );
			machine.setProgram(prog);
			System.out.println("[FunEditorController]ok");
			ok();
		}catch(LanguageException e){ error(e); }
	}
	
	public void command( String program ){
		FunCommandCall command=null;
		System.out.println("[FunEditorController.command]"+"step0 "+program);
		try{ command = (FunCommandCall)quiltLang.process( program, false); }catch(LanguageException e){ error(e); }
		if( command != null ){
			try{
				System.out.println("[FunEditorController.command]"+"step1");
				Object r = command.execute( new HashMap<String, Object>() );
				System.out.println("[FunEditorController.command]"+"step2");
				Render ren = render(); 
				System.out.println("[FunEditorController.command]"+"step3"+ren);
				if( ren != null ) ren.render(r);
				System.out.println("[FunEditorController.command]"+"step4");
				ok();
			}catch(LanguageException e){ error(e); }
		}	
	}
	
	@Override
	public void text(String code){
		System.out.println("[FunEditorController]"+isprogram+"-->"+code+"<--");
		if( isprogram ) compile(code); else command(code); 
	}
}
