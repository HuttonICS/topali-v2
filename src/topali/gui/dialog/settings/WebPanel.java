// (C) 2003-2007 Biomathematics & Statistics Scotland
//
// This package may be distributed under the
// terms of the GNU General Public License (GPL)

package topali.gui.dialog.settings;

import java.awt.event.*;

import javax.swing.*;

import topali.gui.Prefs;
import scri.commons.gui.MsgBox;

class WebPanel extends javax.swing.JPanel implements ActionListener
{
	private SpinnerNumberModel secModel, portModel;

	public WebPanel()
	{
		initComponents();
		createControls();
	}

	private void createControls()
	{
		ButtonGroup grp = new ButtonGroup();
		grp.add(radioDirect);
		grp.add(radioBroker);

		radioDirect.addActionListener(this);
		radioBroker.addActionListener(this);

		secModel = new SpinnerNumberModel(Prefs.web_check_secs, 10, 300, 10);
		secSpin.setModel(secModel);
		portModel = new SpinnerNumberModel(Prefs.web_proxy_port, 0, 65535, 1);
		portSpin.setModel(portModel);

		useProxy.addActionListener(this);
		proxyName.setText(Prefs.web_proxy_server);
		userText.setText(Prefs.web_proxy_username);
		passText.setText(Prefs.web_proxy_password);

		setDefaults(false);
	}

	void setDefaults(boolean reset)
	{
		if (reset)
			Prefs.setWebDefaults();

		directURL.setText(Prefs.web_direct_url);
		brokerURL.setText(Prefs.web_broker_url);
		radioBroker.setSelected(Prefs.web_use_rbroker);
		radioDirect.setSelected(!Prefs.web_use_rbroker);
		secSpin.setValue(Prefs.web_check_secs);

		checkUpdates.setSelected(Prefs.web_check_startup);
		useProxy.setSelected(Prefs.web_proxy_enable);

		checkStates(useProxy.isSelected());
		setRadioStates();
	}

	boolean isOK()
	{
		if (radioDirect.isSelected() && directURL.getText().length() == 0)
		{
			MsgBox.msg(
					"Please ensure a URL is entered for direct connections.",
					MsgBox.ERR);
			return false;
		}

		if (radioBroker.isSelected() && brokerURL.getText().length() == 0)
		{
			MsgBox.msg(
					"Please ensure a URL is entered for the resource broker.",
					MsgBox.ERR);
			return false;
		}

		if (useProxy.isSelected() && proxyName.getText().length() == 0)
		{
			MsgBox.msg("Please ensure a proxy server name has been entered.",
					MsgBox.ERR);
			return false;
		}

		Prefs.web_direct_url = directURL.getText();
		Prefs.web_broker_url = brokerURL.getText();
		Prefs.web_use_rbroker = radioBroker.isSelected();
		Prefs.web_check_secs = secModel.getNumber().intValue();

		Prefs.web_check_startup = checkUpdates.isSelected();
		Prefs.web_proxy_enable = useProxy.isSelected();
		Prefs.web_proxy_server = proxyName.getText();
		Prefs.web_proxy_port = portModel.getNumber().intValue();
		Prefs.web_proxy_username = userText.getText();
		Prefs.web_proxy_password = new String(passText.getPassword());

		return true;
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == useProxy)
		{
			boolean state = useProxy.isSelected();

			checkStates(state);
			if (state)
				proxyName.requestFocus();
		}

		else if (e.getSource() == radioBroker || e.getSource() == radioDirect)
			setRadioStates();
	}

	private void setRadioStates()
	{
		directURL.setEnabled(radioDirect.isSelected());
		brokerURL.setEnabled(radioBroker.isSelected());
	}

	private void checkStates(boolean state)
	{
		proxyName.setEnabled(state);
		portSpin.setEnabled(state);
		serverLabel.setEnabled(state);
		portLabel.setEnabled(state);
		userLabel.setEnabled(state);
		userText.setEnabled(state);
		passLabel.setEnabled(state);
		passText.setEnabled(state);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed" desc=" Generated Code
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        brokerURL = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        secSpin = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        directURL = new javax.swing.JTextField();
        radioBroker = new javax.swing.JRadioButton();
        radioDirect = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        useProxy = new javax.swing.JCheckBox();
        serverLabel = new javax.swing.JLabel();
        proxyName = new javax.swing.JTextField();
        portLabel = new javax.swing.JLabel();
        portSpin = new javax.swing.JSpinner();
        checkUpdates = new javax.swing.JCheckBox();
        userText = new javax.swing.JTextField();
        userLabel = new javax.swing.JLabel();
        passLabel = new javax.swing.JLabel();
        passText = new javax.swing.JPasswordField();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Web services:"));

        jLabel2.setDisplayedMnemonic('j');
        jLabel2.setLabelFor(secSpin);
        jLabel2.setText("Check job progress every:");

        secSpin.setFont(new java.awt.Font("Dialog", 0, 11));

        jLabel3.setText("seconds");

        radioBroker.setMnemonic('r');
        radioBroker.setText("Use resource broker:");
        radioBroker.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radioBroker.setMargin(new java.awt.Insets(0, 0, 0, 0));

        radioDirect.setMnemonic('d');
        radioDirect.setText("Use direct connection:");
        radioDirect.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        radioDirect.setMargin(new java.awt.Insets(0, 0, 0, 0));


        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel2)
                    .add(radioDirect)
                    .add(radioBroker))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(secSpin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 52, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel3))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, brokerURL, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, directURL, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(radioBroker)
                    .add(brokerURL, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(radioDirect)
                    .add(directURL, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jLabel3)
                    .add(secSpin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Other settings:"));

        useProxy.setMnemonic('u');
        useProxy.setText("Use a proxy server for all web connections (requires restart):");
        useProxy.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        useProxy.setMargin(new java.awt.Insets(0, 0, 0, 0));

        serverLabel.setDisplayedMnemonic('s');
        serverLabel.setLabelFor(proxyName);
        serverLabel.setText("Server name:");

        portLabel.setDisplayedMnemonic('p');
        portLabel.setLabelFor(portSpin);
        portLabel.setText(" Port:");

        portSpin.setFont(new java.awt.Font("Dialog", 0, 11));

        checkUpdates.setMnemonic('c');
        checkUpdates.setText("Check for updated versions of TOPALi at program startup");
        checkUpdates.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        checkUpdates.setMargin(new java.awt.Insets(0, 0, 0, 0));

        userLabel.setDisplayedMnemonic('n');
        userLabel.setLabelFor(userText);
        userLabel.setText("Username:");

        passLabel.setDisplayedMnemonic('w');
        passLabel.setLabelFor(passText);
        passLabel.setText("Password:");

        passText.setFont(new java.awt.Font("DialogInput", 0, 11));
        passText.setText("jPasswordField1");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(checkUpdates)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(14, 14, 14)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(serverLabel)
                            .add(userLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(proxyName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 124, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(userText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(27, 27, 27)
                                .add(portLabel))
                            .add(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(passLabel)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(passText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(portSpin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(useProxy))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(new java.awt.Component[] {passText, portSpin, proxyName, userText}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(checkUpdates)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(useProxy)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(proxyName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(serverLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(userText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(userLabel)))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(portSpin, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(portLabel))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(passLabel)
                            .add(passText, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))
                .add(11, 11, 11))
        );

        jPanel2Layout.linkSize(new java.awt.Component[] {portSpin, proxyName, userText}, org.jdesktop.layout.GroupLayout.VERTICAL);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField brokerURL;
    private javax.swing.JCheckBox checkUpdates;
    private javax.swing.JTextField directURL;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel passLabel;
    private javax.swing.JPasswordField passText;
    private javax.swing.JLabel portLabel;
    private javax.swing.JSpinner portSpin;
    private javax.swing.JTextField proxyName;
    private javax.swing.JRadioButton radioBroker;
    private javax.swing.JRadioButton radioDirect;
    private javax.swing.JSpinner secSpin;
    private javax.swing.JLabel serverLabel;
    private javax.swing.JCheckBox useProxy;
    private javax.swing.JLabel userLabel;
    private javax.swing.JTextField userText;
    // End of variables declaration//GEN-END:variables

}
