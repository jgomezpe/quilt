package qm.remnant;

public class Immutable extends Remnant{

	public Immutable(String id) { super(id); }

	@Override
	public void rotate() {}

	@Override
	public void undo_rotate() {}

	@Override
	public Object clone() { return new Immutable(this.id); }
}