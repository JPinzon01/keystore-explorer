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
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ResourceBundle;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.kse.gui.JEscDialog;
import org.kse.gui.PlatformUtil;
import org.kse.gui.crypto.customextkeyusage.JCustomExtendedKeyUsage;

/**
 * Dialog used to add or edit a Certificate Policies extension.
 *
 */
public class DCustomExtKeyUsage extends JEscDialog {
	private static final long serialVersionUID = 1L;

	private static ResourceBundle res = ResourceBundle
			.getBundle("org/kse/gui/dialogs/extensions/resources");

	private static final String CANCEL_KEY = "CANCEL_KEY";

	private JPanel jpCertificatePolicies;
	private JLabel jlCertificatePolicies;
	private JCustomExtendedKeyUsage jCustomExtendedKeyUsage;
	private JPanel jpButtons;
	private JButton jbOK;
	private JButton jbCancel;

	private Set<ASN1ObjectIdentifier> customExtUsageOids;

	/**
	 * Creates a new DCertificatePolicies dialog.
	 *
	 * @param parent
	 *            The parent dialog
	 */
	public DCustomExtKeyUsage(JDialog parent, Set<ASN1ObjectIdentifier> customExtUsageOids) {
		super(parent, ModalityType.DOCUMENT_MODAL);
		setTitle("Extended Key Usage Extension");
		this.customExtUsageOids = customExtUsageOids;
		initComponents();
		prepopulateWithOidList(customExtUsageOids);
	}

	private void initComponents() {
		jlCertificatePolicies = new JLabel("Extended Key Usage");

		GridBagConstraints gbc_jlCertificatePolicies = new GridBagConstraints();
		gbc_jlCertificatePolicies.gridx = 0;
		gbc_jlCertificatePolicies.gridy = 1;
		gbc_jlCertificatePolicies.gridwidth = 1;
		gbc_jlCertificatePolicies.gridheight = 1;
		gbc_jlCertificatePolicies.insets = new Insets(5, 5, 5, 5);
		gbc_jlCertificatePolicies.anchor = GridBagConstraints.NORTHEAST;

		jCustomExtendedKeyUsage = new JCustomExtendedKeyUsage("Extended Key Usage Extension");
		jCustomExtendedKeyUsage.setPreferredSize(new Dimension(400, 150));

		GridBagConstraints gbc_jpiCertificatePolicies = new GridBagConstraints();
		gbc_jpiCertificatePolicies.gridx = 1;
		gbc_jpiCertificatePolicies.gridy = 1;
		gbc_jpiCertificatePolicies.gridwidth = 1;
		gbc_jpiCertificatePolicies.gridheight = 1;
		gbc_jpiCertificatePolicies.insets = new Insets(5, 5, 5, 5);
		gbc_jpiCertificatePolicies.anchor = GridBagConstraints.WEST;

		jpCertificatePolicies = new JPanel(new GridBagLayout());

		jpCertificatePolicies.setBorder(new CompoundBorder(new EmptyBorder(5, 5, 5, 5), new EtchedBorder()));

		jpCertificatePolicies.add(jlCertificatePolicies, gbc_jlCertificatePolicies);
		jpCertificatePolicies.add(jCustomExtendedKeyUsage, gbc_jpiCertificatePolicies);

		jbOK = new JButton(res.getString("DCertificatePolicies.jbOK.text"));
		jbOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				okPressed();
			}
		});

		jbCancel = new JButton(res.getString("DCertificatePolicies.jbCancel.text"));
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
		getContentPane().add(jpCertificatePolicies, BorderLayout.CENTER);
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

	private void prepopulateWithOidList(Set<ASN1ObjectIdentifier> customExtKeyUsageOids) {
		jCustomExtendedKeyUsage.setCustomExtKeyUsage(customExtKeyUsageOids);
	}

	private void okPressed() {
		Set<ASN1ObjectIdentifier> objectIds = jCustomExtendedKeyUsage.getCustomExtKeyUsage();

		if (objectIds.isEmpty()) {
			JOptionPane.showMessageDialog(this, res.getString("DCertificatePolicies.ValueReq.message"), getTitle(),
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		customExtUsageOids = objectIds;

		closeDialog();
	}

	/**
	 * Get extension value DER-encoded.
	 *
	 * @return Extension value
	 */
	public Set<ASN1ObjectIdentifier> getObjectIds() {
		return customExtUsageOids;
	}

	private void cancelPressed() {
		closeDialog();
	}

	private void closeDialog() {
		setVisible(false);
		dispose();
	}
}
