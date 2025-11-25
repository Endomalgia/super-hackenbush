
enum HackenbushType {
  RED,
  BLUE,
  REDBLUE,
  GREEN,
  MIXED
}

class Hackenbush extends GeoNode {

  private GeoNode ground;
  private HackenbushType type;

  // Constructors

  public Hackenbush() {
    Hackenbush(new GeoNode());
  }

  public Hackenbush(GeoNode ground) {
    setGroundNode(ground);

  }

  // Methods

  /*

    Pictures of all one color (+/-) are the number of nodes
    Pictures of all green follow the standard rules of nim addition

    Stacks of red and green are given by their binary sums

  */

  // How is this possible lmao?
  public Integer evaluateRedBlue( ) {



  }

  private HackenbushType SignToType(Short sign) {
    if (n_type == 0) {
      return HackenbushType.GREEN;
    } else if (n_type < 0) {
      return HackenbushType.RED;
    }
    return HackenbushType.BLUE;
  }

  public HackenbushType calculateType(GeoNode ground) {
    if (ground.size() == 0 || ground.isLooped()) {return SignToType(ground.getSign());}
    HackenbushType ret_type = SignToType(ground.getSign());
    for (GeoNode child : ground) {
      calculateType gn_type = calculateType(gn);

      // If the type is the same keep it
      if (gn_type == gn_type) {
        return gn_type;
      }

      if (gn_type == HackenbushType.GREEN || gn_type == HackenbushType.MIXED) {
        if (ground.getType())
      } else if () {

      }

      // Add the types correctly ie GREEN & RED = MIXED
      switch (ground.getType()) {
        case HackenbushType.RED:
        case HackenbushType.BLUE:
        case HackenbushType.REDBLUE:
          if (gn_type != HackenbushType.GREEN) {
            ret_type = HackenbushType.REDBLUE;
          }
        case HackenbushType.GREEN:
        case HackenbushType.MIXED:
          ret_type = HackenbushType.MIXED;
      }
    }
  }

  // Applies the simplicity rule to a game of value {a|b}
  public Integer simplify(int a, int b) {

  }

  /* SHORTHAND NOTATION
    0       > green
    +       > blue
    -       > red
    [a-z]   > identifier, allows loops & related connections
    ()      > establishes hierarchy

    +a(+(-0+(+a))) = a blue apple with a red stem and green leaf
  */

  public applyPattern(String shorthand) {

  }

  public applyPattern(GeoNode noder, String shorthand) {

  }



  // Get Set

  public void setGroundNode(GeoNode ground) {this.ground = ground;}
  private void setType(HackenbushType type) {this.type = type;}

  public GeoNode getGroundNode() {return this.ground;}
  public HackenbushType getType() {return this.type;}

}
