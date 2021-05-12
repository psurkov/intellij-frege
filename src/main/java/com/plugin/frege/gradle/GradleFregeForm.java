package com.plugin.frege.gradle;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class GradleFregeForm {
    private JPanel panel;

    public GradleFregeForm() {
        javaTarget.setText("11");
        fregeRelease.setText("3.25alpha");
        fregeVersion.setText("3.25.84");
        autoDownloadTheCompilerCheckBox.addItemListener(e -> {
            int state = e.getStateChange();
            switch (state) {
                case ItemEvent.SELECTED:
                    fregeCompilerPathLabel.setVisible(false);
                    fregeCompilerPath.setVisible(false);
                    break;
                case ItemEvent.DESELECTED:
                    fregeCompilerPathLabel.setVisible(true);
                    fregeCompilerPath.setVisible(true);
                    break;
            }
        });
    }

    public JPanel getPanel() {
        return panel;
    }

    public String getJavaTarget() {
        return javaTarget.getText();
    }

    public String getFregeRelease() {
        return fregeRelease.getText();
    }

    public String getFregeVersion() {
        return fregeVersion.getText();
    }

    public boolean isCompilerAutoDownloaded() {
        return autoDownloadTheCompilerCheckBox.isSelected();
    }

    public String getFregeCompilerPath() {
        return fregeCompilerPath.getText();
    }

    public String isMinimalistic() {
        return minimalisticGradleFileCheckBox.getText();
    }

    private JTextField javaTarget;
    private JTextField fregeRelease;
    private JTextField fregeVersion;

    private JCheckBox autoDownloadTheCompilerCheckBox;
    private JLabel fregeCompilerPathLabel;
    private JTextField fregeCompilerPath;
    private JCheckBox minimalisticGradleFileCheckBox;

}
