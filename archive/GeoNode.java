
class GeoNode extends ArrayList<GeoNode> {

  private Short sign; // +1 -1 *(||0)
  private Boolean isGrounded;
  private Boolean isLooped; // Is this a junction with a loop (does it use a,b,etc... in the parser)

  // Optional information for a mesh drawing
  /*

  private Vector<Integer> coordinates;

  */

  // Constructors

  public GeoNode() {
    GeoNode(0);
  }

  public GeoNode(Short sign, ArrayList<GeoNode> nodes) {
    setSign(sign);
    this.addAll(nodes);
  }

  public GeoNode(Short sign, GeoNode node) {
    setSign(sign);
    this.add(node);
  }

  // Get/Set

  public void setSign(Short sign) {this.sign = sign;}
  public void setGrounded(Boolean isGrounded) {this.isGrounded = isGrounded;}

  public Short getSign() {return this.sign;}
  public Boolean getGrounded() {return this.isGrounded;}

}
