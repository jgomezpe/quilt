package quilt.computer;

import java.awt.Image;

import quilt.MinRemnant;
import quilt.Remnant;
import quilt.gui.Drawer;
import quilt.operation.Rotatable;
import quilt.operation.Rotate;

public class ImageRemnant extends MinRemnant implements Rotatable<Remnant>{
	protected String name;
	
	protected Image image;
	protected int rot=0;
	
	public ImageRemnant(String name, Image image, int rot){
		this.name = name;
		this.image = image;
		this.rot = rot;
	}
	
	public ImageRemnant(String name, Image image) {
		this(name, image, 0 );
	}
	
	@Override
	public Object clone() {
		return new ImageRemnant(name,image,rot);
	}

	@Override
	public void draw( Drawer g, int column, int row ){
		super.draw(g,column,row);
		column = units(column);
		row = units(row);
		g.drawImage(column, row, unit(), unit(), rot, image);
	}

	@Override
	public Remnant rotate(Rotate command) {
		return new ImageRemnant(name, image, (rot+270)%360);
	}
}