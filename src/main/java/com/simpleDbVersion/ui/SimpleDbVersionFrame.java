package com.simpleDbVersion.ui;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.simpleDbVersion.commandLine.CommandLineAppMain;
import com.simpleDbVersion.domain.SimpleDbVersion;
import com.simpleDbVersion.domain.Version;

public class SimpleDbVersionFrame extends JFrame {

	private static final long serialVersionUID = 2235151289954265614L;
	
	private final SimpleDbVersion simpleDbVersion;
	private JButton btnInstall;
	private JButton btnCurrentVersion;

	public SimpleDbVersionFrame(SimpleDbVersion simpleDbVersion) {
		super("Simple DB Version");
		this.simpleDbVersion = simpleDbVersion;
		createAndShowGUI();
	}

	private void createAndShowGUI() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addComponents(getContentPane());
		this.setResizable(false);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	private void addComponents(final Container container) {
		FlowLayout layout = new FlowLayout();
		JPanel panel = new JPanel();
		panel.setLayout(layout);
		layout.setAlignment(FlowLayout.TRAILING);
		
		createAndBindButtons();

		panel.add(btnInstall);
		panel.add(btnCurrentVersion);
		panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

		container.add(panel, BorderLayout.CENTER);
	}

	private void createAndBindButtons() {
		btnInstall = new JButton("Install");
		btnCurrentVersion = new JButton("Current Version");
		
		btnInstall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				simpleDbVersion.install();
				System.out.println("INSTALOU " + simpleDbVersion.currentVersion());
			}
		});

		btnCurrentVersion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Version currentVersion = simpleDbVersion.currentVersion();
				JOptionPane.showMessageDialog(null, currentVersion, "Current Version", JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}
	
	public static void main(String[] args) {
		SimpleDbVersion simpleDbVersion = CommandLineAppMain.getSimpleDbVersion(args);
		new SimpleDbVersionFrame(simpleDbVersion);
	}

}
