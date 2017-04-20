package quilt;

public abstract class MinRemnant implements Remnant{
	public int rows(){ return 1; }

	public int columns(){ return 1; }

	@Override
	public MinRemnant get(int r, int c) {
		if( 0<=r && r<rows() && 0<=c && c<columns()) return this;
		return null;
	}
}