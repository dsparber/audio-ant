package io.logger;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class EventLoggerGui extends JFrame implements Observer {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private JProgressBar progressBar;

	public EventLoggerGui() {
		setType(Type.UTILITY);

		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 64, 240);
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		progressBar = new JProgressBar();
		progressBar.setOrientation(SwingConstants.VERTICAL);
		progressBar.setBorder(null);
		progressBar.setForeground(Color.LIGHT_GRAY);
		contentPane.add(progressBar, BorderLayout.CENTER);

		setVisible(true);
	}

	@Override
	public void update(Observable o, Object arg) {

		double percent = (double) arg;

		progressBar.setValue((int) (percent * 100));

	}

}
