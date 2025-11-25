
import java.util.*;

class DeltaLibrary<N extends TintDelta<? extends T>,T> {

	/*//*/// VARIABLES

	private ArrayList<N> connectionLibrary;
	private ArrayList<T> tintLibrary;
  
  	/*//*/// CONSTRUCTORS

    /***
     * Constructor for a new DeltaLibrary<N,T>
     *
     * @param clib   ArrayList of TintWeb<T> pairs to initialize the library with (in origin -> insertion order)
     * @param tlib   ArrayList of tints corresponding to the TintWeb<T> pairs in the clib
     */
	public DeltaLibrary(ArrayList<N> clib, ArrayList<T> tlib) {
		super();
		setConnectionLibrary(clib);
		setTintLibrary(tlib);
	}

    /***
     * No-arg constructor for a new empty DeltaLibrary<N,T>
     */
	public DeltaLibrary() {
		this(new ArrayList<N>(), new ArrayList<T>());
	}

	/*//*/// METHODS

    /***
     * Append two DeltaLibrary<N,T> objects
     * 
     * @return DeltaLibrary<N,T> to add
     */
	public void append(DeltaLibrary<N,T> deltaLibrary) {
		getConnectionLibrary().addAll(deltaLibrary.getConnectionLibrary());
		getTintLibrary().addAll(deltaLibrary.getTintLibrary());
	}

	/***
     * Remove a connection between two nodes
     * 
     * @param origin 	Origin node.
     * @param insert 	Node connected to from the origin node (insertion node).
     */
	public void removeConnection(N origin, N insert) {
		ArrayList<N> clib = getConnectionLibrary();
		ArrayList<T> tlib = getTintLibrary();
		int len = clib.size();
		for (int i=0; i<len; i += 2) {
			N node = clib.get(i);
			if (node.equals(origin)) {
				N other = clib.get(i+1);
				if (other.equals(insert)) {
					clib.remove(i);
					clib.remove(i);
					tlib.remove(i / 2);
					return; // Node pairs should not be in the connection library multiple times, if debugging replace this line with i-=2 to continue removing connections
				}
			} else if (node.equals(insert)) {
				N other = clib.get(i+1);
				if (other.equals(origin)) {
					clib.remove(i);
					clib.remove(i);
					tlib.remove(i / 2);
					return; // Node pairs should not be in the connection library multiple times, if debugging replace this line with i-=2 to continue removing connections
				}
			}
		}
	}

    /***
     * Check for isolated ground nodes and split into multiple libraries if found
     * 
     * @return ArrayList of DeltaLibrary<N,T> objects split from the current DeltaLibrary<N,T>.
     */
	public ArrayList<DeltaLibrary<N,T>> split() {
		ArrayList<DeltaLibrary<N,T>> libArray = new ArrayList<DeltaLibrary<N,T>>();

		ArrayList<N> sourceClib 			= getConnectionLibrary();
		ArrayList<T> sourceTlib 			= getTintLibrary();
		int len = sourceClib.size();
		for (int i=0; i<len; i+=2) {
			N a = sourceClib.get(i);
			N b = sourceClib.get(i+1);

			ArrayList<DeltaLibrary<N,T>> connectedLibraries = new ArrayList<DeltaLibrary<N,T>>();
			for (DeltaLibrary<N,T> dl : libArray) {
				ArrayList<N> dlclib = dl.getConnectionLibrary();
				if (dlclib.contains(a) || dlclib.contains(b)) connectedLibraries.add(dl);
			}

			if (connectedLibraries.size() == 0) {
				DeltaLibrary<N,T> newLib = new DeltaLibrary<N,T>();
				newLib.getConnectionLibrary().addAll(Arrays.asList(a,b));
				newLib.getTintLibrary().add(sourceTlib.get(i / 2));
				libArray.add(newLib);
				continue;
			}

			DeltaLibrary<N,T> first = connectedLibraries.remove(0);
			ArrayList<N> firstClib = first.getConnectionLibrary();
			ArrayList<T> firstTlib = first.getTintLibrary();
			firstClib.addAll(Arrays.asList(a,b));
			firstTlib.add(sourceTlib.get(i / 2));
			for (DeltaLibrary<N,T> connectedLib : connectedLibraries) {
				firstClib.addAll(connectedLib.getConnectionLibrary());
				firstTlib.addAll(connectedLib.getTintLibrary());
				libArray.remove(connectedLib);
			}
		}
		return libArray;
	}

    /***
     * Check for isolated ground nodes and split into multiple libraries if found
     * 
     * @return Array of ground nodes
     */
  	public ArrayList<N> findGroundNodes() {
    	ArrayList<N> clib = getConnectionLibrary();
    	ArrayList<N> gndNew = new ArrayList<N>();
    	for (N node : clib) {
    	  if (node.isGroundNode() && !gndNew.contains(node)) {
    	    gndNew.add(node);
    	  }
    	}
   	 	return gndNew;
  	}

	/*//*/// GET &* SET

	/** Get the internal library of N pairs @return ArrayList of N pairs*/
	public ArrayList<N> getConnectionLibrary() {return this.connectionLibrary;}
	/** Get the internal library of N connections @return ArrayList of connection tints*/
	public ArrayList<T> getTintLibrary() {return this.tintLibrary;}

	/** Set the internal library of N pairs @param ArrayList of N pairs*/
	public void setConnectionLibrary(ArrayList<N> clib) {this.connectionLibrary = clib;}
	/** Set the internal library of N connections @param ArrayList of connection tints*/
	public void setTintLibrary(ArrayList<T> tlib) {this.tintLibrary = tlib;}

}