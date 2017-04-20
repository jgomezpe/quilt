package quilt;

import quilt.gui.Drawer;

public class Quilt implements Remnant{
	protected MinRemnant[][] remnant;
	public Quilt( Remnant left, Remnant right ){
		int c = left.columns();
		remnant = new MinRemnant[left.rows()][c+right.columns()];
		for( int i=0; i<rows(); i++ ){
			for( int j=0; j<left.columns(); j++ ) remnant[i][j] = left.get(i, j);
			for( int j=0; j<right.columns(); j++ ) remnant[i][j+c] = right.get(i, j);
		}
	}
	
	public Quilt( MinRemnant[][] remnant ){
		this.remnant = remnant;
	}
	
	public MinRemnant get( int r, int c ){
		if( 0<=r && r<rows() && 0<=c && c<columns()){
			return remnant[r][c];
		}
		return null;
	}

	@Override
	public int rows() {
		return remnant.length;
	}

	@Override
	public int columns() {
		return remnant[0].length;
	}

	@Override
	public void draw(Drawer g, int column, int row) {
		for( int i=0; i<rows(); i++ )
			for( int j=0; j<columns(); j++ ) remnant[i][j].draw(g, column+j, row+i);
	}	

	public Remnant[] unstitch(){
		int c = columns();
		if( c>1 ){
			int r = rows();
			c--;
			MinRemnant[][] left_m = new MinRemnant[r][c];
			MinRemnant[][] right_m = new MinRemnant[r][1];
			for( int i=0; i<r; i++ ){
				for( int j=0; j<c; j++){
					left_m[i][j] = remnant[i][j];
				}
				right_m[i][0] = remnant[i][c];
			}
			if( r==1 ){
				if(c==1) return new Remnant[]{left_m[0][0],right_m[0][0]};
				else return new Remnant[]{new Quilt(left_m),right_m[0][0]};
			}else{
				return new Remnant[]{new Quilt(left_m),new Quilt(right_m)};
			}
		}
		return null;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		for( int i=0; i<rows(); i++ ){
			for( int j=0; j<columns(); j++ ){
				sb.append(get(i,j));
			}
			sb.append('\n');
		}
		return sb.toString();
	}	
}