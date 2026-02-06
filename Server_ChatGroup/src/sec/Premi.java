package sec;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTextField;

public class Premi implements ActionListener{
	JTextField name;
	JTextField cod;
	
	public Premi(JTextField name, JTextField cod) {
		this.name = name;
		this.cod =cod;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String nome = name.getText();
		
		Sicurezza sec = new Sicurezza(nome);
		String c = sec.getOTP();
		cod.setText(c);
	}
}
