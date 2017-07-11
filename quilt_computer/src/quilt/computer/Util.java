package quilt.computer;

import java.awt.Image;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;

public class Util {
	protected static final String resources="resources/";
	protected static final String images="imgs/";
	protected static final String i18n="I18N/";
	
	public static String resource( String name, boolean asResource ){
		return asResource?"/"+images+name:resources+images+name;
	}
	
	public static Image image(String name, boolean asResource){
		try {
			if( name.startsWith("http://") || name.startsWith("https://")) return ImageIO.read(new URL(name));
			else if( asResource ) return ImageIO.read(Util.class.getResource(name));
			else return ImageIO.read(new File(name));
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			return null; 
		}				
	}
	
	public static String i18n_file_name(String language, boolean asResource){
		if( asResource ) return "/"+i18n+language+".txt";
		else return resources+i18n+language+".txt";
	}
}