
import java.util.*;

class HackenbushLibrary extends DeltaLibrary<HackenbushNode,EdgeTint> {

    /***
     * No-arg constructor for a new empty HackenbushLibrary
     */
	public HackenbushLibrary() {
		super();
	}

	/***
     * Generate a HackenbushLibrary from a DeltaLibrary<HackenbushNode,EdgeTint> because basic casting is too hard for java i guess
     * 
     * @param library
     */
	public HackenbushLibrary(DeltaLibrary<HackenbushNode,EdgeTint> library) {
		super(library.getConnectionLibrary(), library.getTintLibrary());
	}

	/***
     * Constructor for a new HackenbushLibrary
     *
     * @param clib   ArrayList of HackenbushNode pairs to initialize the library with (in origin -> insertion order)
     * @param tlib   ArrayList of tints corresponding to the TintWeb<T>HackenbushNode pairs in the clib
     */
	public HackenbushLibrary(ArrayList<HackenbushNode> clib, ArrayList<EdgeTint> tlib) {
		super(clib, tlib);
	}
}