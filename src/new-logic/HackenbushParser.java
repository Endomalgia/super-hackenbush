
import java.util.*;
import java.io.*;

class HackenbushParser {

	/***
    * Parse hackenbush picture syntax (hps) files into hackenbush pictures
    * red node 	  : -
    * blue node   : +
    * green node  : 0
    * GROUND node : G
    * hierarchy	  : ()
    * positioning : []
    * loops		  : a-z
    * 
    * Example: +[0,0]a(+[-1,1](-[-0.5,2.5]0[0.5,2.5]+[0,2](+[1,1](+a)))) = a blue apple with a red stem and green leaf
    *
    * @param filepath  	HPS string
    * @return    		The coresponding hackenbush picture
    */
    public static HackenbushPicture parseString(int x, int y, String hackenbushString) {

    	int len = hackenbushString.length();
    	char[] cstHPS = hackenbushString.toCharArray();
    	if (len == 0) return null;

    	int parenDepth = 0;
    	Stack<HackenbushNode> parenStack = new Stack<HackenbushNode>();
    	HashMap<Character, HackenbushNode> loopIndex = new HashMap<Character, HackenbushNode>();

    	ArrayList<HackenbushNode> groundNodes = new ArrayList<HackenbushNode>();
        HackenbushLibrary hlib                = new HackenbushLibrary();
        ArrayList<HackenbushNode> clib        = hlib.getConnectionLibrary();
        ArrayList<EdgeTint> tlib              = hlib.getTintLibrary();

    	Boolean etreGround = false;
    	HackenbushNode currentNode = new HackenbushNode();
    	HackenbushNode lastNode = new HackenbushNode(true); // Create a 0,0 ground node
    	groundNodes.add(lastNode);

    	EdgeTint nextTint = EdgeTint.GREEN; // Default to green for no reason in particular :P
    	for (int i=0; i<len; i++) {
    		Character next = cstHPS[i];

    		switch(next) {
    			case '(':
    				parenDepth++;
    				parenStack.push(lastNode);
    				lastNode = currentNode;
    				break;
    			case ')':
    				if (parenDepth < 1 || parenStack.empty()) {
    					System.out.println("ERROR: INVALID HPS STRING : " + hackenbushString);
    					System.exit(-1);
    				}
    				parenDepth--;
    				currentNode = lastNode;
    				lastNode = parenStack.pop();
    				break;
    			case 'G':
    				etreGround = true;
    				break;
    			case '+':	// Blue
    				nextTint = EdgeTint.BLUE;
    				etreGround = false;
    				break;
    			case '-':	// Red
    				nextTint = EdgeTint.RED;
    				etreGround = false;
    				break;
    			case '0':	// Green
    				nextTint = EdgeTint.GREEN;
    				etreGround = false;
    				break;
    			case '[':	// Get a nodes positional value
    				int fi = i;
    				while (cstHPS[i] != ',') i++;
    				Integer nx = Integer.parseInt(hackenbushString.substring(fi+1, i));

    				fi = i;
    				while (cstHPS[i] != ']') i++;
    				Integer ny = Integer.parseInt(hackenbushString.substring(fi+1, i));
    				
    				currentNode = new HackenbushNode(etreGround, nx, -ny); // Flipping y takes us from 'hackenbush syntax picture space' to local space
    				lastNode.addNode(currentNode, nextTint);
                    
                    // Add to the hackenbush library
                    clib.add(lastNode);
                    clib.add(currentNode);
                    tlib.add(nextTint);

    				if (etreGround) groundNodes.add(currentNode); // May break stuff
    				break;
    			default:	// Loop tokens
    				if (loopIndex.containsKey(next)) {
                        HackenbushNode lnode = loopIndex.get(next);
    					lastNode.linkNode(lnode, nextTint);

                        // Add to the hackenbush library
                        clib.add(lastNode);
                        clib.add(lnode);
                        tlib.add(nextTint);
    					continue;
    				}
    				loopIndex.put(next, currentNode);
    		}
    	}
        HackenbushPicture out = new HackenbushPicture(hlib, groundNodes, x, y);
        out.calculateLongestLine();
    	return out;
    }

   /***
    * Parse hackenbush picture syntax (hps) files into hackenbush pictures.
    *
    * @param filepath  	Path to the file of interest
    * @return    		The coresponding hackenbush picture
    */
    public static HackenbushPicture parseHPS(String filepath) {
    	File rawHPS = new File(filepath);
        int x = 0;
        int y = 0;

    	String strHPS = new String();
    	try (Scanner scnr = new Scanner(rawHPS)) {
            // Get the picture position
            x = scnr.nextInt();
            y = scnr.nextInt();
            scnr.nextLine();

            // Get the hps data
    		while (scnr.hasNextLine()) strHPS += scnr.nextLine();
    	} catch (FileNotFoundException fnfe) {
    		System.out.println("ERROR: UNABLE TO READ HPS FILE : " + filepath);
    		System.exit(-1);
    	}

    	return HackenbushParser.parseString(x, y, strHPS);
    }

    public static ArrayList<HackenbushPicture> parseMultipleHPS(String filepath) {
        ArrayList<HackenbushPicture> out = new ArrayList<HackenbushPicture>();
        File rawHPS = new File(filepath);
        int x = 0;
        int y = 0;

        String strHPS = new String();
        try (Scanner scnr = new Scanner(rawHPS)) {

            while (scnr.hasNextLine()) {
                // Get the picture position
                x = scnr.nextInt();
                y = scnr.nextInt();
                scnr.nextLine();

                out.add(HackenbushParser.parseString(x, y, scnr.nextLine()));
            }
        } catch (FileNotFoundException fnfe) {
            System.out.println("ERROR: UNABLE TO READ HPS FILE : " + filepath);
            System.exit(-1);
        }
        return out;
    }

}