class HackenbushMove {

  	private HackenbushPicture source;
  	private HackenbushNode origin;
  	private HackenbushNode insert;
  	private Fraction value;

  	public HackenbushMove(HackenbushNode origin, HackenbushNode insert, Fraction value, HackenbushPicture source) {
  		this.origin = origin;
  		this.insert = insert;
  		this.value = value;
  		this.source = source;
  	}

    public void setSource(HackenbushPicture source) {this.source = source;}
    public void setOrigin(HackenbushNode origin) {this.origin = origin;}
    public void setInsert(HackenbushNode insert) {this.insert = insert;}
    public void setValue(Fraction value) {this.value = value;}

    public HackenbushPicture getSource() {return this.source;}
    public HackenbushNode getOrigin() {return this.origin;}
    public HackenbushNode getInsert() {return this.insert;}
    public Fraction getValue() {return this.value;}
}
