package qm.remnant;

public class CQueue extends Remnant{
	protected int idx;
	public static String[] ids;
	
	public CQueue(String id) {
		super(id);
		idx = 0;
		while(idx<ids.length && !id.equals(ids[idx])) idx++;
		idx = idx%ids.length;
	}
	
	public CQueue(int idx) {
		super(ids[idx]);
		this.idx = idx;
	}

	@Override
	public void rotate() {
		idx = (idx+1)%ids.length;
		id = ids[idx];
	}

	@Override
	public void undo_rotate() {
		idx = (idx+ids.length-1)%ids.length;
		id = ids[idx];
	}

	@Override
	public Object clone() { return new CQueue(idx); }
}