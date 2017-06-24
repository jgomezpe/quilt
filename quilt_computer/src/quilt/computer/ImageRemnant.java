package quilt.computer;

import java.awt.Image;

import quilt.MinRemnant;
import quilt.Remnant;
import quilt.Rotatable;
import quilt.Rotate;
import quilt.gui.Drawer;

public class ImageRemnant extends MinRemnant implements Rotatable<Remnant>{
	protected Image image;
	protected int rot=0;
	
	public ImageRemnant(Image image) {
		this.image=image;
	}
	
	public ImageRemnant(Image image, int rot) {
		this(image);
		this.rot=rot;
	}
	
	@Override
	public Object clone() {
		return new ImageRemnant(image,rot);
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
		return new ImageRemnant(image, (rot+90)%360);
	}
}