package palletPacker.model;

public class Move{
	Package pkg;
	Tuple<Carrier, Integer> from;
	Tuple<Carrier, Integer> to;
	
	public Package getPallet() {
		return pkg;
	}
	public Tuple<Carrier, Integer> getFrom() {
		return from;
	}
	public Tuple<Carrier, Integer> getTo() {
		return to;
	}
	public void setTo(Tuple<Carrier, Integer> to){
		this.to = to;
	}
	public Move(Package pkg, Tuple<Carrier, Integer> from) {
		super();
		this.pkg = pkg;
		this.from = from;
	}
}
