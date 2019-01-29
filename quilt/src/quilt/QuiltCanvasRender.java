package quilt;

import unalcol.gui.paint.CanvasRender;
import unalcol.gui.paint.Drawable;

public interface QuiltCanvasRender extends CanvasRender{
	@Override
	default void add( Drawable obj ){
		init();
		objects().add(obj);
	}
}