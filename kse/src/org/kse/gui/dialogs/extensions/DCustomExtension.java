/*
 * Copyright 2004 - 2013 Wayne Grant
 *           2013 - 2019 Kai Kramer
 *
 * This file is part of KeyStore Explorer.
 *
 * KeyStore Explorer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * KeyStore Explorer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with KeyStore Explorer.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.kse.gui.dialogs.extensions;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.security.PublicKey;
import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.AccessDescription;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.util.encoders.Hex;
import org.kse.gui.PlatformUtil;
import org.kse.gui.crypto.JKeyIdentifier;
import org.kse.gui.crypto.generalname.JGeneralName;
import org.kse.gui.error.DError;
import org.kse.gui.oid.JObjectId;


/**
 * Dialog used to add or edit a Subject Key Identifier extension.
 *
 */
public class DCustomExtension extends DExtension {
	private static final long serialVersionUID = 1L;

	private static ResourceBundle res = ResourceBundle
			.getBundle("org/kse/gui/dialogs/extensions/resources");

	private static final String CANCEL_KEY = "CANCEL_KEY";

	private JPanel jpCustomExtension;
	private JLabel jlCustomExtensionId;
	private JObjectId joiCustomExtensionId;
	
	private JLabel jlCustomExtensionValue;
	private JTextField jtfCustomExtensionValue;
	
	
	private JPanel jpButtons;
	private JButton jbOK;
	private JButton jbCancel;

	private String oid;
	private byte[] value;

	/**
	 * Creates a new DCustomExtension dialog.
	 *
	 * @param parent
	 *            The parent dialog
	 * @param subjectPublicKey
	 *            Subject public key
	 */
	public DCustomExtension(JDialog parent, PublicKey subjectPublicKey) {
		super(parent);
		setTitle(res.getString("DCustomExtension.Title"));
		initComponents();
	}

	/**
	 * Creates a new DCustomExtension dialog.
	 *
	 * @param parent
	 *            The parent dialog
	 * @param value
	 *            Subject Key Identifier DER-encoded
	 * @throws IOException
	 *             If value could not be decoded
	 */
	public DCustomExtension(JDialog parent, byte[] value) throws IOException {
		super(parent);
		setTitle(res.getString("DCustomExtension.Title"));
		initComponents();
	}

	private void initComponents() {
		jlCustomExtensionId = new JLabel(res.getString("DCustomExtension.jlCustomExtensionId.text"));

		GridBagConstraints gbc_jlCustomExtensionId = new GridBagConstraints();
		gbc_jlCustomExtensionId.gridx = 0;
		gbc_jlCustomExtensionId.gridy = 0;
		gbc_jlCustomExtensionId.gridwidth = 1;
		gbc_jlCustomExtensionId.gridheight = 1;
		gbc_jlCustomExtensionId.insets = new Insets(5, 5, 5, 5);
		gbc_jlCustomExtensionId.anchor = GridBagConstraints.EAST;

		joiCustomExtensionId = new JObjectId(res.getString("DCustomExtension.joiCustomExtensionId.Text"));

		GridBagConstraints gbc_joiCustomExtensionId = new GridBagConstraints();
		gbc_joiCustomExtensionId.gridx = 1;
		gbc_joiCustomExtensionId.gridy = 0;
		gbc_joiCustomExtensionId.gridwidth = 1;
		gbc_joiCustomExtensionId.gridheight = 1;
		gbc_joiCustomExtensionId.insets = new Insets(5, 5, 5, 5);
		gbc_joiCustomExtensionId.anchor = GridBagConstraints.WEST;

		jlCustomExtensionValue = new JLabel(res.getString("DCustomExtension.jlCustomExtensionValue.text"));

		GridBagConstraints gbc_jlCustomExtensionValue = new GridBagConstraints();
		gbc_jlCustomExtensionValue.gridx = 0;
		gbc_jlCustomExtensionValue.gridy = 1;
		gbc_jlCustomExtensionValue.gridwidth = 1;
		gbc_jlCustomExtensionValue.gridheight = 1;
		gbc_jlCustomExtensionValue.insets = new Insets(5, 5, 5, 5);
		gbc_jlCustomExtensionValue.anchor = GridBagConstraints.EAST;

		jtfCustomExtensionValue = new JTextField(25);

		GridBagConstraints gbc_jtfCustomExtensionValue = new GridBagConstraints();
		gbc_jtfCustomExtensionValue.gridx = 1;
		gbc_jtfCustomExtensionValue.gridy = 1;
		gbc_jtfCustomExtensionValue.gridwidth = 1;
		gbc_jtfCustomExtensionValue.gridheight = 1;
		gbc_jtfCustomExtensionValue.insets = new Insets(5, 5, 5, 5);
		gbc_jtfCustomExtensionValue.anchor = GridBagConstraints.WEST;

		jpCustomExtension = new JPanel(new GridBagLayout());

		jpCustomExtension.setBorder(new CompoundBorder(new EmptyBorder(5, 5, 5, 5), new CompoundBorder(
				new EtchedBorder(), new EmptyBorder(5, 5, 5, 5))));

		jpCustomExtension.add(jlCustomExtensionId, gbc_jlCustomExtensionId);
		jpCustomExtension.add(joiCustomExtensionId, gbc_joiCustomExtensionId);
		jpCustomExtension.add(jlCustomExtensionValue, gbc_jlCustomExtensionValue);
		jpCustomExtension.add(jtfCustomExtensionValue, gbc_jtfCustomExtensionValue);

		
		jbOK = new JButton(res.getString("DCustomExtension.jbOK.text"));
		jbOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				okPressed();
			}
		});

		jbCancel = new JButton(res.getString("DCustomExtension.jbCancel.text"));
		jbCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				cancelPressed();
			}
		});
		jbCancel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				CANCEL_KEY);
		jbCancel.getActionMap().put(CANCEL_KEY, new AbstractAction() {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent evt) {
				cancelPressed();
			}
		});

		jpButtons = PlatformUtil.createDialogButtonPanel(jbOK, jbCancel);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(jpCustomExtension, BorderLayout.CENTER);
		getContentPane().add(jpButtons, BorderLayout.SOUTH);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent evt) {
				closeDialog();
			}
		});

		setResizable(false);

		getRootPane().setDefaultButton(jbOK);

		pack();
	}

	private void okPressed() {


		if (joiCustomExtensionId.getObjectId().getId() == null) {
			JOptionPane.showMessageDialog(this,
					res.getString("DCustomExtension.CustomExtensionIdReq.message"), getTitle(),
					JOptionPane.WARNING_MESSAGE);
			return;
		} else {
			oid = joiCustomExtensionId.getObjectId().getId();
		}

		String customExtensionValueStr = jtfCustomExtensionValue.getText();

		if (customExtensionValueStr == null) {
			JOptionPane.showMessageDialog(this,
					res.getString("DCustomExtension.CustomExtensionValueReq.message"), getTitle(),
					JOptionPane.WARNING_MESSAGE);
			return;
		} else {
			value = Hex.decode(customExtensionValueStr);	
		}

		closeDialog();
	}

	/**
	 * Get extension value DER-encoded.
	 *
	 * @return Extension value
	 */
	public String getOid() {
		return oid;
	}

	/**
	 * Get extension value DER-encoded.
	 *
	 * @return Extension value
	 */
	@Override
	public byte[] getValue() {
		return value;
	}

	private void cancelPressed() {
		closeDialog();
	}

	private void closeDialog() {
		setVisible(false);
		dispose();
	}
}
