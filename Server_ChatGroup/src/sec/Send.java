package sec;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JTextField;

public class Send implements ActionListener{
	JTextField txt;
	Socket s;
	String msg;
	PrintWriter pw;
	
	public Send(JTextField txt, Socket s, String msg) {
		this.txt = txt;
		this.s = s;
		this.msg = msg;
		
		try {
			pw = new PrintWriter(s.getOutputStream(), true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		msg = txt.getText();		
		pw.println(msg);
		txt.setText("");
	}
}
