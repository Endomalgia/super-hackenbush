import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/*

	I had plans with the octagon but I didnt really finish it. So its just a sort of
	random visual thing. You should probably just ignore it tho
	Nothing in it is relevent to the lab problems. Same with the fancy BoxButton.
	GN! hope ur having a good weekend.

	~ Autumn

*/

public class Main {

	public static void main(String[] args) {

		// Create the root frame
		JFrame f_root = new JFrame();
		f_root.setTitle("Temperature Converter");
		f_root.setSize(new Dimension(500,240));
		f_root.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create the main panel with a fancy gradient
		MainPanel p_main = new MainPanel();
		p_main.setLayout(new BorderLayout());
		p_main.setBorder(new EmptyBorder(5,10,5,10));

		// Create a wrapper for the temperature input and wheel
		JPanel input_wrapper = new JPanel();
		input_wrapper.setLayout(new BoxLayout(input_wrapper, BoxLayout.X_AXIS));
		input_wrapper.setOpaque(false);

		// Create a celsius sub pannel
		JPanel celsius_wrap = new JPanel();
		celsius_wrap.setOpaque(false);
		input_wrapper.add(celsius_wrap);

		// Create a text area for C input
		JTextArea ta_celsius = new JTextArea("");
		ta_celsius.setColumns(6);
		ta_celsius.setRows(1);
		ta_celsius.setEditable(true);
		celsius_wrap.add(ta_celsius);

		// Label the temp type
		JTextArea ta_celsius_label = new JTextArea("°C");
		ta_celsius_label.setOpaque(false);
		ta_celsius_label.setEditable(false);
		celsius_wrap.add(ta_celsius_label);

		// Create *the wheel*
		CaptainsWheel cw_wheel = new CaptainsWheel(10, new Rectangle(50,50));
		input_wrapper.add(cw_wheel);
		cw_wheel.setOpaque(false);

		// Create a fahrenheit sub pannel
		JPanel fahrenheit_wrap = new JPanel();
		fahrenheit_wrap.setOpaque(false);
		input_wrapper.add(fahrenheit_wrap);

		// Create a text area for C input
		JTextArea ta_fahrenheit = new JTextArea("");
		ta_fahrenheit.setColumns(6);
		ta_fahrenheit.setRows(1);
		ta_fahrenheit.setEditable(true);
		fahrenheit_wrap.add(ta_fahrenheit);

		// Label the temp type
		JTextArea ta_fahrenheit_label = new JTextArea("°F");
		ta_fahrenheit_label.setOpaque(false);
		ta_fahrenheit_label.setEditable(false);
		fahrenheit_wrap.add(ta_fahrenheit_label);

		// Add the input wrapper to the main panel
		p_main.add(input_wrapper);

		// Create another panel for the button with the default bgr color
		JPanel p_inner = new JPanel();
		p_main.add(p_inner, BorderLayout.SOUTH);

		// Create some text areas to show some fancy arrows
		JTextArea ta_left = new JTextArea("-->");
		ta_left.setOpaque(false);
		ta_left.setEditable(false);

		JTextArea ta_right = new JTextArea("<--");
		ta_right.setOpaque(false);
		ta_right.setEditable(false);

		// Create a "Convert" box button with black text
		BoxButton j_convert = new BoxButton("Convert");
		j_convert.setForeground(Color.BLACK);

		j_convert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				// Return decorative UI elements back to normal
				j_convert.setForeground(Color.BLACK);
				ta_left.setText("-->");
				ta_right.setText("<--");

				// Get text from f and c textboxes
				String ft =  ta_fahrenheit.getText();
				String ct =  ta_celsius.getText();

				// Only go forwards if ONE box is full
				if ((ft.equals("")) ^ (ct.equals(""))) {
					System.out.println("HI!" + ft + ct);

					try {

						// If f is filled, parse and convert to C to fill the other text box
						JTextArea toAlter = ta_celsius;
						String toRead = ft;
						if (ft.equals("")) {
							Double convParse = CtoF(Double.parseDouble(ct));
							ta_fahrenheit.setText(convParse.toString());
						} else { // vice versa
							Double convParse = FtoC(Double.parseDouble(ft));
							ta_celsius.setText(convParse.toString());
						}

					// If the user did not enter a number fill both text boxes with INCORRECT FORMAT
					} catch (NumberFormatException nfe) {
						ta_left.setText("INCORRECT");
						ta_right.setText("FORMAT");
						j_convert.setForeground(Color.RED);
					}
					return;
				}

				// Red = bad = you shouldnt have done that
				ta_left.setText("FILL A");
				ta_right.setText("SINGLE BOX");
				j_convert.setForeground(Color.RED);
			}
		});

		// Adds everything in the inner panel to that panel in order
		p_inner.add(ta_left);
		p_inner.add(j_convert);
		p_inner.add(ta_right);

		// Add the main panel to the root frame
		f_root.add(p_main);

		// Make the window visible after everything is added properly
		f_root.setVisible(true);
		cw_wheel.animate();
	}

	// Convert F to C
	public static double FtoC(double F) {
		return (F - 32.0) / 1.8;
	}

	// vice versa
	public static double CtoF(double C) {
		return (C * 1.8) + 32.0;
	}

}

//
//255 width
//70	height of rubber bit
//16	radius
