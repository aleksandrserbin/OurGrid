/*
 * Copyright (C) 2008 Universidade Federal de Campina Grande
 *  
 * This file is part of OurGrid. 
 *
 * OurGrid is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free 
 * Software Foundation, either version 3 of the License, or (at your option) 
 * any later version. 
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details. 
 * 
 * You should have received a copy of the GNU Lesser General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package org.ourgrid.peer.ui.async.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;

import org.ourgrid.common.ui.InputFieldsUI;
import org.ourgrid.peer.PeerConfiguration;
import org.ourgrid.peer.ui.async.model.PeerAsyncUIModel;

/**
 * Represents a panel where it is possible to define the file containing
 * tags mapping for workers attributes.
 */
public class TagsPeerConfigurationPanel extends javax.swing.JPanel implements InputFieldsUI, ItemListener {
   
	private static final String CHOOSE_TAGS_PROPERTY_FILE_PATH = "Select file";

	private static final long serialVersionUID = 1L;

	private PeerAsyncUIModel model;

	private JCheckBox useCustomisedTagsCheckBox;
	private JButton chooseFileLoadButton;
	 // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel tagsFilePathLabel;
    private javax.swing.JTextField tagsFilePathTextField;
    // End of variables declaration//GEN-END:variables

	private boolean fileSelected;

	/** Creates new form TagsPeerConfigurationPanel 
     * @param model */
    public TagsPeerConfigurationPanel(PeerAsyncUIModel model) {
    	this.model = model;
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tagsFilePathLabel = new javax.swing.JLabel();
        tagsFilePathTextField = new javax.swing.JTextField();

        tagsFilePathLabel.setText("Tags File Path:");
        
        useCustomisedTagsCheckBox = new javax.swing.JCheckBox();
        useCustomisedTagsCheckBox.setText("Publish customised tags");
        
        useCustomisedTagsCheckBox.addItemListener(this);
        
        chooseFileLoadButton = new javax.swing.JButton();
        
        chooseFileLoadButton.setText("Load file");
        
        chooseFileLoadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadChooseTagsFileButtonActionPerformed(e);
			}
        });
        
        fileSelected = false;
        
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                	.add(useCustomisedTagsCheckBox)
                    .add(layout.createSequentialGroup()
                        .add(tagsFilePathLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(tagsFilePathTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(chooseFileLoadButton)
                .addContainerGap(89, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createSequentialGroup()
            .add(layout.createSequentialGroup()
                    .addContainerGap()
               		.add(useCustomisedTagsCheckBox))
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
               		.add(useCustomisedTagsCheckBox)
                    .add(chooseFileLoadButton)
                	.add(tagsFilePathLabel)
                    .add(tagsFilePathTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        initTextFields();
    }// </editor-fold>//GEN-END:initComponents
    
    
    public void itemStateChanged(ItemEvent e) {

        if(e.getStateChange() == ItemEvent.DESELECTED){
        	disableFieldEdition();
        }else{
        	enableFieldEdition();
        	if(!fileSelected){
        		tagsFilePathTextField.setText("");
        	}
        }
    }

    protected void loadChooseTagsFileButtonActionPerformed(ActionEvent e) {
    	
    	
    	String lastOpenedFile = model.getProperty(PeerConfiguration.PROP_TAGS_FILE_PATH);
    	OurGridFileChooser fileChooser = new OurGridFileChooser(lastOpenedFile == null?null:new File(lastOpenedFile));
    	fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

    	File propertiesFile = fileChooser.getFile();
    	
    	if (propertiesFile != null) {
    		tagsFilePathTextField.setText(propertiesFile.getPath());
    		fileSelected = true;
    	}
	}
    
    /**
     * Disable the editable components of this panel.
     */
    public void disableFieldEdition() {
		tagsFilePathTextField.setEnabled(false);
    	chooseFileLoadButton.setEnabled(false);
	}
    
	/**
	 * Enable the editable components of this panel.
	 */
    public void enableFieldEdition() {
		tagsFilePathTextField.setEnabled(true);
    	chooseFileLoadButton.setEnabled(true);
	}
    
	/**
	 * Initializes the textFields of this panel using the default values.
	 */
    private void initTextFields() {
    	String property = model.getProperty(PeerConfiguration.PROP_TAGS_FILE_PATH);
    	if(property == null || (property.length() == 0)){
    		property = CHOOSE_TAGS_PROPERTY_FILE_PATH;
    		useCustomisedTagsCheckBox.setSelected(false);
    		disableFieldEdition();
    	}else{
    		useCustomisedTagsCheckBox.setSelected(true);
    		enableFieldEdition();
    		fileSelected = true;
    	}
    	tagsFilePathTextField.setText(property);
    	
	}
    
	/**
	 * Save the basic properties of the peer.
	 */
    public void saveProperties() {
    	String fileName = "";
    	if(useCustomisedTagsCheckBox.isSelected()){
    		fileName = tagsFilePathTextField.getText();
    	}
    	model.setProperty(PeerConfiguration.PROP_TAGS_FILE_PATH, fileName);
	}

	/**
	 * Initializes the textFields.
	 */
	public void initFields() throws IOException {
		initTextFields();
	}

	/**
	 * Save the values of the field inputs.
	 */
	public void saveFieldInputs() throws IOException {
		saveProperties();
	}

	/**
	 * Disable the editable components of this panel.
	 */
	public void disableInput() {
		disableFieldEdition();
		
	}

	/**
	 * Enable the editable components of this panel.
	 */
	public void enableInput() {
		enableFieldEdition();
		
	}
}
