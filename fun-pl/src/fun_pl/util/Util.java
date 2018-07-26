package fun_pl.util;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.imageio.ImageIO;

public class Util {
	public static final String CFG = "cfg/";
	public static final String I18N = ".i18n";
	public static final String resources="resources/";
	public static final String images="imgs/";
	public static final String i18n="I18N/";
	
	
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
	
	public static String plain_file_read( InputStream is, boolean write_eol ) throws Exception{
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		String line = reader.readLine();
		while( line != null ){ 
			sb.append(line);
			line = reader.readLine();
			if( line != null && write_eol) sb.append('\n');
		}
		return sb.toString();
	}
	
	protected static boolean set_i18n( String language, InputStream is ){
		try{ 
			String config = plain_file_read(is,true);
			unalcol.i18n.I18N.add(language, unalcol.i18n.I18N.load(config));
			unalcol.i18n.I18N.use(language);
			return true;
		}catch( Exception e ){}
		return false;
	}
	
	public static boolean i18n(String language){
		try{ return set_i18n(language, new FileInputStream(language)); }catch(Exception e){}
		try{ return set_i18n(language, new FileInputStream(resources+i18n+language+I18N)); }catch(Exception e){}
		try{ return set_i18n(language, Util.class.getResourceAsStream("/"+i18n+language+I18N)); }catch(Exception e){}
		return false;
	}
		
	public static String config(String file){
		
		try{ return plain_file_read(new FileInputStream(file),false); }catch( Exception e ){}
		try{ return plain_file_read(new FileInputStream(resources+CFG+file),false); }catch( Exception e ){}
		try{ return plain_file_read(Util.class.getResourceAsStream("/"+CFG+file),false); }catch( Exception e ){}
		return null;
	}	
}
