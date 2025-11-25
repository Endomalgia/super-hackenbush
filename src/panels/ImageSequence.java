
import java.util.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.util.*;
import java.awt.*;
import javax.swing.*;

class ImageSequence {

	String directoryPath;
	ArrayList<Image> frames;
	int numberFrames;

	public ImageSequence(String directory, int numberFrames) {
		this.directoryPath = directory;
		this.numberFrames = numberFrames;
		frames = new ArrayList<Image>();
		for (int i=0; i<numberFrames; i++) {
			frames.add(null);
		}

		Thread imageLoader = new Thread(new FrameLoader());
		imageLoader.start();
	}

	private class FrameLoader implements Runnable {
		public void run() {
			for (int i=0; i<numberFrames; i++) {
				String filepath = directoryPath + "/" + String.format("%04d",i+1)+".png";
				try {
            		frames.set(i, ImageIO.read(new File(filepath)));
      			} catch (IOException ioe) { 
            		System.out.println("ERROR : Image not found : " + filepath);
            		System.exit(-1);
      			}
			}
		}
	}

	public void drawFrame(Graphics2D gfx, int x, int y, int frame) {
		frame = Math.max(Math.min(frame, numberFrames),0); // Clamp frame to the max and min possible
		Image toDraw = frames.get(frame);
		while(toDraw == null) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException ie) {continue;}
			toDraw = frames.get(frame);
		}
		gfx.drawImage(frames.get(frame), x, y, null);
	}

}