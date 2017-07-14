package quilt.computer;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.imageio.ImageIO;

import unalcol.gui.editor.ErrorManager;

public class Util {
	protected static final String QMS = ".qms";
	protected static final String CFG = "cfg/";
	protected static final String I18N = ".i18n";
	protected static final String resources="resources/";
	protected static final String images="imgs/";
	protected static final String i18n="I18N/";
	
	public static String resource( String name, boolean asResource ){
		return asResource?"/"+images+name:resources+images+name;
	}
	
	public static Image image(String name){
		try{ if( name.startsWith("http://") || name.startsWith("https://")) return ImageIO.read(new URL(name)); }catch(Exception ex){}				
		try{ return ImageIO.read(new File(name)); }catch(Exception ex){};
		try{ return ImageIO.read(Util.class.getResource("/"+images+name)); }catch(Exception ex){}
		try{ return ImageIO.read(new File(resources+images+name)); }catch(Exception ex){};
		return null;
	}
	
	public static ErrorManager i18n(String language){
		try{ return new ErrorManager(new FileInputStream(language)); }catch( Exception e ){}
		try{ return new ErrorManager(new FileInputStream(resources+i18n+language+I18N)); }catch( Exception e ){}
		try{ return new ErrorManager(Util.class.getResourceAsStream("/"+i18n+language+I18N)); }catch( Exception e ){}
		return null;
	}
	
	protected static String config( InputStream is ) throws Exception{
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = reader.readLine();
		reader.close();
		return line;
	}
	
	public static String config(String file){
		try{ return config(new FileInputStream(file)); }catch( Exception e ){}
		try{ return config(new FileInputStream(resources+CFG+file)); }catch( Exception e ){}
		try{ return config(Util.class.getResourceAsStream("/"+CFG+file)); }catch( Exception e ){}
		return null;
	}		
}