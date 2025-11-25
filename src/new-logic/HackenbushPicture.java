
import java.util.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

class HackenbushPicture {

	/*//*/// CONSTANTS

  	private static final Map<EdgeTint,Color> TINT_MAP = Map.of(
    	EdgeTint.GREEN, new Color(95, 153, 75),   // #5f994b
    	EdgeTint.RED,   new Color(226, 87, 74),   // #e2574a
    	EdgeTint.BLUE,  new Color(63, 131, 240)); // #3f83f0

  	private static final BasicStroke STROKE_LINE			= new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke. JOIN_ROUND);
  	private static final BasicStroke STROKE_DOT 		 	= new BasicStroke(9, BasicStroke.CAP_ROUND, BasicStroke. JOIN_ROUND);
 	private static final BasicStroke STROKE_DOT_INNER		= new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke. JOIN_ROUND);
  	private static final Color 		 COLOR_DOT 				= Color.BLACK;
  	private static final Color 		 COLOR_DOT_INNER		= Color.WHITE;

	/*//*/// VARIABLES

	private HackenbushLibrary hackenbushLibrary;
	private ArrayList<HackenbushNode> groundNodes;
	private Vector<Integer> position;
	private int longestLine;

  	/*//*/// CONSTRUCTORS

   /***
    * Constructor for a new HackenbushPicture
    *
    * @param hackenbushLibrary  Delta library associated with the ground nodes
    * @param groundNodes   Grounded HackenbushNodes in the picture
    */
	public HackenbushPicture(HackenbushLibrary hackenbushLibrary, ArrayList<HackenbushNode> groundNodes, int x, int y) {
		setHackenbushLibrary(hackenbushLibrary);
		setGroundNodes(groundNodes);
		setPosition(new Vector<Integer>(Arrays.asList(x,y)));
	}

   /***
    * Constructor for a new HackenbushPicture
    *
    * @param hackenbushLibrary  Delta library associated with the ground nodes
    * @param groundNodes   Grounded HackenbushNodes in the picture
    */
	public HackenbushPicture(HackenbushLibrary hackenbushLibrary, ArrayList<HackenbushNode> groundNodes) {
		this(hackenbushLibrary,groundNodes,0,0);
	}

   /***
    * No-arg constructor for a new HackenbushPicture
    */
	public HackenbushPicture() {
		this(new HackenbushLibrary(), new ArrayList<HackenbushNode>());
	}

   /***
    * Copy constructor, makes deep copies of HackenbushPictures
    */
	public HackenbushPicture(HackenbushPicture picture) {
		this();
		HackenbushLibrary hlib = getHackenbushLibrary();
		ArrayList<HackenbushNode> clib = hlib.getConnectionLibrary();
		ArrayList<HackenbushNode> gn = getGroundNodes();

		// Map to hold old node : new node pairs so things line up properly
		HashMap<HackenbushNode,HackenbushNode> isomerase = new HashMap<HackenbushNode,HackenbushNode>();

		// Bind the ligand
		HackenbushLibrary dlib = picture.getHackenbushLibrary();
		for (HackenbushNode node : dlib.getConnectionLibrary()) {
			if (isomerase.containsKey(node)) {
				clib.add(isomerase.get(node));
				continue;
			}
			HackenbushNode newNode = new HackenbushNode(node);
			clib.add(newNode);
			if (newNode.isGroundNode()) gn.add(newNode);
			isomerase.put(node, newNode);
		}

		// Catalyze (Link nodes to their copied counterparts instead of the dusty old nodes lolz XP)
		for (HackenbushNode node : clib) {
			ArrayList<TintDelta<EdgeTint>> nodes = node.getNodes();
			int node_len = nodes.size();
			for (int i=0; i<node_len; i++) {
				HackenbushNode next = isomerase.get(nodes.get(i));
				nodes.set(i, (next == null) ? nodes.get(i) : next); // This is a cop out to fix a bug REMOVE ME LATER
			}
			HackenbushNode prev = isomerase.get(node.getPrevious());
			node.setPrevious((prev == null) ? node.getPrevious() : prev);
			// Check over loops as well if present
			if (!node.isLooped()) continue;
			nodes = node.getLoopNodes();
			node_len = nodes.size();
			for (int i=0; i<node_len; i++) {
				HackenbushNode loop = isomerase.get(nodes.get(i));
				nodes.set(i, (loop == null) ? nodes.get(i) : loop);
			}
		}
		hlib.setTintLibrary(new ArrayList<EdgeTint>(dlib.getTintLibrary()));
		setPosition(new Vector<Integer>(picture.getPosition()));
	//	System.out.println("NEW PICTURE AT POSITION : " + getPosition());
	}

	/*//*/// METHODS

   /***
    * Split a contiguous hackenbush picture by removing a single connecion
    *
    * @param origin  	Source node
    * @param insertion  Node being inserted into
    * @return    		ArrayList of grounded hackenbush pictures generated from this removal
    */
	public ArrayList<HackenbushPicture> removeConnection(HackenbushNode origin, HackenbushNode insertion) {
		ArrayList<HackenbushPicture> out = new ArrayList<HackenbushPicture>();

		HackenbushLibrary hlib = getHackenbushLibrary();
		hlib.removeConnection(origin, insertion);
		origin.removeEdge(origin.getNodes().indexOf(insertion));

		// Get the grounded delta libraries made by this new removal
		ArrayList<DeltaLibrary<HackenbushNode,EdgeTint>> newDL = hlib.split();

		// Very simple, just make new ones based on the stuff we have here :D. OOP is so cool!
		int len = newDL.size();
		for (int i=0; i<len; i++) {
			HackenbushLibrary dl = new HackenbushLibrary(newDL.get(i));
			ArrayList<HackenbushNode> dlGnd = dl.findGroundNodes();
			if (dlGnd.size() == 0) {
				// This is a falling leaf, ADD CALLBACK HERE IF WANTED
				continue;
			}
			Vector<Integer> pos = getPosition();
			out.add(new HackenbushPicture(dl,
				                          dlGnd,
				                      	  pos.get(0),
				                      	  pos.get(1)));
		}
		return out;
	}

   /***
    * Split a contiguous hackenbush picture by removing a single connecion
    *
    * @param a  	Some node (origin or insertion)
    * @param b  	Some other node (origin or insertion)
    * @return    	ArrayList of grounded hackenbush pictures generated from this removal
    */
	public ArrayList<HackenbushPicture> removeDisorderedConnection(HackenbushNode a, HackenbushNode b) {
    System.out.println("STARTING THIS");
    ArrayList<TintDelta<EdgeTint>> aNodes = a.getNodes();
    System.out.println("STARTING THIS");
		if (aNodes == null || !aNodes.contains(b)) {
        System.out.println("STARTING THIS");
			return removeConnection(b,a);
		}
    System.out.println("STARTING THIS");
		return removeConnection(a,b);
	}

   /***
   	* Checks the distance between a position and the current hackenbush picture.
   	* Returns the index of the collided line or -1 if no collision occured.
   	* The crown jewel of the HackenbushPicture object.
   	*
   	* @param position          The position to check
   	* @param threshold         The threshold squared distance before a line is considered clicked
   	* @return                  The index of the collided line or -1 if no collision occured
   	*/
  	public Integer collide(int xp, int yp, Integer threshold) {
    	int sqrtThreshold = (int)Math.sqrt(threshold);
    	int distClosest = Integer.MAX_VALUE;
    	int actualClosest = distClosest;
    	int closest = -1;
    	int longest = getLongestLine();
    	ArrayList<HackenbushNode> vlib = getHackenbushLibrary().getConnectionLibrary();
    	for (int i=0; i<vlib.size()-1; i+=2) {
      		Vector<Integer> p1 = vlib.get(i).getPosition();
      		int y1 = p1.get(1);
      		if (Math.abs(y1 - yp) <= longest && Math.abs(p1.get(0) - xp) <= longest) {
        		Vector<Integer> p2 = vlib.get(i+1).getPosition();
        		int y2 = p2.get(1);
        		// NOTE: The way I did this removes the threshold from top and bottom of the line at the end points :P....

        		// Calculate the distance to the line of interest, if the point is also within x1 and x2, its good!
        		int a = squaredDistanceToLine(xp,yp, p1.get(0),y1, p2.get(0),y2);
        		if (a<actualClosest) actualClosest=a;
        		if (y1 < y2) sqrtThreshold = -sqrtThreshold;
        		if (a <= threshold) {
          			if (a < distClosest) {
            			distClosest = a;
            			closest = i;
          			}
        		}
      		}
    	}
    	if (distClosest > threshold) {
      		closest = -1; // Better to just check once down here
    	}
    	return closest;
  	}
  	private int squaredDistanceToLine(int x, int y, int x1, int y1, int x2, int y2) {
    	int a = x - x1;
    	int b = y - y1;
    	int c = x2 - x1;
    	int d = y2 - y1;
    	int e = -d;
    	int f = c;
    	int dot = a*e + b*f;
    	int len_sq = e*e + f*f;
    	return dot * dot / len_sq;
  	}
  	private int distanceToPoint(int x, int y, int xp, int yp) {
    	int a = x-xp;
    	int b = y-yp;
    	return (int)Math.sqrt(a*a + b*b);
  	}

   /***
   	* Calculate the internal length of the longest line in the hackenbush picture
   	*/
  	public void calculateLongestLine() {
    	int longest = 0;
    	ArrayList<HackenbushNode> clib = getHackenbushLibrary().getConnectionLibrary();
    	for (int i=0; i<clib.size()-1; i+=2) {
      		Vector<Integer> p1 = clib.get(i).getPosition();
      		Vector<Integer> p2 = clib.get(i+1).getPosition();
      		int next = distanceToPoint(p1.get(0),p1.get(1), p2.get(0),p2.get(1));
      		if (next > longest) {
        		longest = next;
      		}
    	}
    	setLongestLine(longest);
  	}

   /***
   	* Draws a hackenbush tree on a Graphics2D object.
   	*
   	* @param gfx          The graphics object to draw to
   	*/
   	public Fraction evaluateRedBlue() {return evaluateRedBlue(0);}
  	public Fraction evaluateRedBlue(int depth) {

  		Fraction bLargest = new Fraction(Integer.MIN_VALUE,1);
  		Fraction rSmallest = new Fraction(Integer.MAX_VALUE,1);
  		Fraction val = new Fraction(0,1);

  		//System.out.println("//////////////// STARTING ON NEW PICTURE ////////////////\n"+this);

      /*
  		System.out.println("\t".repeat(depth) + "Removal -->");
  		String redSums = "";
  		String blueSums = "";
      */

  		ArrayList<EdgeTint> tlib = getHackenbushLibrary().getTintLibrary();
  		int len = tlib.size();
  		for (int i=0; i<len; i++) {
  			HackenbushPicture recurse = new HackenbushPicture(this);
  			ArrayList<HackenbushNode> clib = recurse.getHackenbushLibrary().getConnectionLibrary();

  			HackenbushNode origin = clib.get(i << 1);
  			HackenbushNode insert = clib.get((i << 1) + 1);

  			//System.out.println("//////////////// CHECKING SUB PICTURE ////////////////\n"+recurse);
			//System.out.println("\torigin "+origin+"\n\tinsert "+insert);

  			Fraction eval = removeAndEval(origin, insert, recurse,depth);
  			switch (tlib.get(i)) {
  				case BLUE:
  					//blueSums += eval + " ,";
  					if (eval.compareTo(bLargest) > 0) {
  						bLargest = eval;
  					}
  					break;
  				case RED:
  					//redSums += eval + " ,";
  					if (eval.compareTo(rSmallest) < 0) {
  						rSmallest = eval;
  					}
  					break;
  				default:
  					System.out.println("ERROR : EVALUATING NON RED/BLUE HACKENBUSH TREE!");
  					System.exit(-1);
  			}
  		}
  		//System.out.println("//////////////// RETURNING PICTURE ////////////////");
  		//System.out.println("\t".repeat(depth) + "RESULT {"+blueSums+"= "+bLargest+"|"+redSums+"= "+rSmallest+"}");
  		//System.out.println("//////////////// CONTINUING RETURN ////////////////");

  		Boolean b_min= (bLargest.getNumerator() == Integer.MIN_VALUE);
  		Boolean r_max= (rSmallest.getNumerator() == Integer.MAX_VALUE);
  		if (b_min && r_max) return new Fraction(0,1);
  		if (b_min) {
  			rSmallest.add(new Fraction(-1,1));
  			return rSmallest; // For a game of form {|n} the value is n-1
  		}
  		if (r_max) {
  			bLargest.add(new Fraction(1,1));
  			return bLargest;  // For a game of form {n|} the value is n+1
  		}

  		Fraction.reduceWithDiadic(bLargest, rSmallest);
  		return simplify(bLargest, rSmallest);
  	}
  	private static Fraction removeAndEval(HackenbushNode origin, HackenbushNode insert, HackenbushPicture pic, int depth) {
  		// If the origin is actually the insert, swap the nodes. THIS CAN BE AVOIDED BY JUST ORDERING THE NODES IN THE CONNECTION LIBRARY PROPERLY XP
  		HackenbushNode prev = (HackenbushNode)(origin.getPrevious());
  		if ((prev != null && origin.getPrevious().equals(insert)) || (origin.isLooped() && origin.getLoopNodes().contains(insert))) {
  			HackenbushNode xchg = origin;
  			origin = insert;
  			insert = xchg;
  		}

  		Fraction out = new Fraction(0,1);
  		for (HackenbushPicture p : pic.removeConnection(origin, insert)) {
  			HackenbushLibrary phlib = p.getHackenbushLibrary();
  			ArrayList<HackenbushNode> pclib = phlib.getConnectionLibrary();
  			if (pclib.size() == 0) continue;
  			if (pclib.size() <= 2) {
  				switch (phlib.getTintLibrary().get(0)) {
  					case BLUE:
  						out.add(new Fraction(1,1));
  						break;
  					case RED:
  						out.add(new Fraction(-1,1));
  						break;
  					default:
  				}
  				continue;
  			}
  		out.add(p.evaluateRedBlue(depth+1));
  		}
  		return out;
  	}
  	private Fraction simplify(Fraction b, Fraction r) {
  		/* Requires a set of diadic fractions with the same denominator */

  		int u = b.getNumerator();
  		int v = r.getNumerator();
  		int d = b.getDenominator();
  		if (u > v) {
  			System.out.println("ERROR : INVALID HACKENBUSH POSITION {"+b+"|"+r+"}");
  			System.exit(-1);
  		}
  		if (v - u == 1) {
  			d *= 2;
  			Fraction o = new Fraction((2 * u) + 1, d);
  			//System.out.println("{"+ b+"|" + r+"} = "+o);
			return o;
  		}
  		if (d == 1) {
  			return new Fraction(u+1,1);
  		}
  		if (u == -1 && v == 1) return new Fraction(0,1);
  		d /= 2;
  		if (u % 2 == 0) {
  			u = (u/2);
  		} else {
  			u = (u-1) / 2;
  		}
  		if (v % 2 == 0) {
  			v = (v/2);
  		} else {
  			v = (v+1) / 2;
  		}
  		Fraction out = simplify(new Fraction(u,d), new Fraction(v,d));
  		//System.out.println("{"+ b+"|" + r+"} = "+out);
  		return out;
  	}

   /***
   	*/
  	public Vector<HackenbushNode> getBestRedMove() {
  		Fraction rSmallest = new Fraction(Integer.MAX_VALUE,1);
  		Fraction val = new Fraction(0,1);

  		Vector<HackenbushNode> outMove = new Vector<HackenbushNode>(Arrays.asList(null,null));

  		ArrayList<EdgeTint> tlib = getHackenbushLibrary().getTintLibrary();
  		int len = tlib.size();
  		for (int i=0; i<len; i++) {
  			HackenbushPicture recurse = new HackenbushPicture(this);
  			ArrayList<HackenbushNode> clib = recurse.getHackenbushLibrary().getConnectionLibrary();

  			HackenbushNode origin = clib.get(i << 1);
  			HackenbushNode insert = clib.get((i << 1) + 1);

  			Fraction eval = removeAndEval(origin, insert, recurse,0);
  			if (tlib.get(i) != EdgeTint.RED) continue;

  			if (eval.compareTo(rSmallest) < 0) {
  				rSmallest = eval;
  				outMove.set(0,origin);
  				outMove.set(1,insert);
  			}
  		}

  		if (rSmallest.getNumerator() == Integer.MAX_VALUE) return null;
  		return outMove;
  	}

   /***
   	*/
  	public static HackenbushMove getBestRedMoveAccross(ArrayList<HackenbushPicture> pics) {
  		Fraction rSmallest = new Fraction(Integer.MAX_VALUE,1);

  		HackenbushMove outMove = new HackenbushMove(null,null,null,null);

  		for (HackenbushPicture pic : pics) {
  			HackenbushLibrary hlib = pic.getHackenbushLibrary();
  			ArrayList<HackenbushNode> topClib = hlib.getConnectionLibrary();
  			ArrayList<EdgeTint> tlib = hlib.getTintLibrary();
        System.out.println("STARTING WITH TINT : " + tlib);
  			int len = tlib.size();
  			for (int i=0; i<len; i++) {
          if (tlib.get(i) != EdgeTint.RED) continue;
  				HackenbushPicture recurse = new HackenbushPicture(pic);
  				ArrayList<HackenbushNode> clib = recurse.getHackenbushLibrary().getConnectionLibrary();

  				HackenbushNode origin = clib.get(i << 1);
  				HackenbushNode insert = clib.get((i << 1) + 1);

  				Fraction eval = removeAndEval(origin, insert, recurse, 0);

          System.out.println("\tGIVING EVAL : " + eval);
          System.out.println("\tCOMPARING TO ("+eval.compareTo(rSmallest)+") : " + rSmallest);
  				if (eval.compareTo(rSmallest) < 0) {
            System.out.println("\tCOMPARISON IS TRUE!");
  					rSmallest = eval;

            outMove.setOrigin(topClib.get(i << 1));
            outMove.setInsert(topClib.get((i << 1) + 1));
            outMove.setSource(pic);
  				}
  			}
  		}
      System.out.println("SMALLEST = " + rSmallest);

      outMove.setValue(rSmallest);
  		if (rSmallest.getNumerator() == Integer.MAX_VALUE) return null;
  		return outMove;
  	}

    /***
    	*/
   	public static HackenbushMove getBestBlueMoveAccross(ArrayList<HackenbushPicture> pics) {
   		Fraction bLargest = new Fraction(Integer.MIN_VALUE,1);

   		HackenbushMove outMove = new HackenbushMove(null,null,null,null);

   		for (HackenbushPicture pic : pics) {
   			HackenbushLibrary hlib = pic.getHackenbushLibrary();
   			ArrayList<HackenbushNode> topClib = hlib.getConnectionLibrary();
   			ArrayList<EdgeTint> tlib = hlib.getTintLibrary();
         System.out.println("STARTING WITH TINT : " + tlib);
   			int len = tlib.size();
   			for (int i=0; i<len; i++) {
           if (tlib.get(i) != EdgeTint.RED) continue;
   				HackenbushPicture recurse = new HackenbushPicture(pic);
   				ArrayList<HackenbushNode> clib = recurse.getHackenbushLibrary().getConnectionLibrary();

   				HackenbushNode origin = clib.get(i << 1);
   				HackenbushNode insert = clib.get((i << 1) + 1);

   				Fraction eval = removeAndEval(origin, insert, recurse, 0);
   				if (eval.compareTo(bLargest) > 0) {
             System.out.println("\tCOMPARISON IS TRUE!");
   					bLargest = eval;

             outMove.setOrigin(topClib.get(i << 1));
             outMove.setInsert(topClib.get((i << 1) + 1));
             outMove.setSource(pic);
   				}
   			}
   		}

      outMove.setValue(bLargest);
   		if (bLargest.getNumerator() == Integer.MIN_VALUE) return null; // Unnecessary
   		return outMove;
   	}



   /***
   	* Draws a hackenbush tree on a Graphics2D object.
   	*
   	* @param gfx          The graphics object to draw to
   	*/
  	public void draw(Graphics2D gfx) {
  		HackenbushLibrary dl = getHackenbushLibrary();
   		ArrayList<HackenbushNode> clib = dl.getConnectionLibrary();
    	ArrayList<EdgeTint> tlib = dl.getTintLibrary();

    	Vector<Integer> pos = getPosition();
    	AffineTransform mat_transform = gfx.getTransform();
    	AffineTransform mat_def = new AffineTransform(mat_transform);
    	mat_transform.translate(pos.get(0), pos.get(1));
    	gfx.setTransform(mat_transform);

    	for (int i=0; i<clib.size(); i+=2) {
    		Vector<Integer> p1 = clib.get(i).getPosition();
      		Vector<Integer> p2 = clib.get(i+1).getPosition();

     	 	// Draw the line with the appropriate color
     	 	gfx.setStroke(STROKE_LINE);
      		gfx.setColor(TINT_MAP.get(tlib.get(i / 2)));
      		gfx.drawLine(p1.get(0),p1.get(1),p2.get(0),p2.get(1));

      		// Draw the endpoints of the line
      		gfx.setStroke(STROKE_DOT);
      		gfx.setColor(COLOR_DOT);
      		gfx.drawLine(p1.get(0),p1.get(1),p1.get(0),p1.get(1));
      		gfx.drawLine(p2.get(0),p2.get(1),p2.get(0),p2.get(1));

      		// Draw the inner portion of each line
      		gfx.setStroke(STROKE_DOT_INNER);
      		gfx.setColor(COLOR_DOT_INNER);
      		gfx.drawLine(p1.get(0),p1.get(1),p1.get(0),p1.get(1));
      		gfx.drawLine(p2.get(0),p2.get(1),p2.get(0),p2.get(1));
    	}
    	gfx.setTransform(mat_def);
  	}

   /***
   	* Set the position of a HackenbushPicture without passing in a new vector
	*
   	* @return			String representation of the picture
   	*/
  	public String toString() {
  		HackenbushLibrary hlib = getHackenbushLibrary();
    	return String.format("0x%08X",hashCode())+"\n\tclib "+hlib.getConnectionLibrary()+"\n\ttlib "+hlib.getTintLibrary()+"\n\tgnd "+getGroundNodes();
  	}

   /***
   	* Set the position of a HackenbushPicture without passing in a new vector
   	*
   	* @param x          relative x-axis position
   	* @param y          relative y-axis position
   	*/
  	public void setPosition(Integer x, Integer y) {
    	position.set(0, x);
    	position.set(1, y);
  	}

	/*//*/// GET &* SET

	/** Get the internal hackenbush library of the picture @return the delta library*/
	public HackenbushLibrary getHackenbushLibrary() {return this.hackenbushLibrary;}
	/** Get the ground nodes @return ArrayList of ground nodes*/
	public ArrayList<HackenbushNode> getGroundNodes() {return this.groundNodes;}
	/** Get the size of the longest line in the picture @return The longest line*/
  	public Integer getLongestLine() {return longestLine;}
  	/** Get the position vector of the picture @return The position vector*/
  	public Vector<Integer> getPosition() {return this.position;};

	/** Set the internal hackenbush library of the picture @param the new delta library*/
	public void setHackenbushLibrary(HackenbushLibrary hlib) {this.hackenbushLibrary = hlib;}
	/** Set the ground nodes @param ArrayList of ground nodes*/
	public void setGroundNodes(ArrayList<HackenbushNode> groundNodes) {this.groundNodes = groundNodes;}

	private void setLongestLine(Integer length) {this.longestLine = length;}
	private void setPosition(Vector<Integer> position) {this.position = position;}

}
