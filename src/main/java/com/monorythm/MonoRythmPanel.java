package com.monorythm;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import net.runelite.client.util.Filepath;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

@Slf4j
public class MonoRythmPanel extends PluginPanel implements ActionListener {

    private boolean playing = false;

    private JFileChooser fileChooser = new JFileChooser();

    private JPanel songPanel;
    private JLabel titleDisplayLabel;
    private JPanel chooseButtonsPanel;
    private JButton fileChooserButton;
    private JButton urlChooserButton;
    private JPanel bpmInputPanel;
    private JLabel bpmInputLabel;
    private JSpinner bpmInputSpinner;
    private JButton adaptBPMButton;
    private JButton togglePlayButton;

    @Inject
    private EventBus eventBus;

    void init() {
        setLayout(new BorderLayout());
        setBackground(ColorScheme.DARK_GRAY_COLOR);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        songPanel = new JPanel(new GridLayout(6,1, 0, 10));

        titleDisplayLabel = new JLabel("- (No song selected)");

        chooseButtonsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        fileChooserButton = new JButton("Choose from file");
        urlChooserButton = new JButton("Choose from URL");

        bpmInputPanel = new JPanel(new GridLayout(1,2,10,10));
        bpmInputLabel = new JLabel("Song BPM:");
        bpmInputSpinner = new JSpinner(new SpinnerNumberModel(100, 1, 500, 1));

        adaptBPMButton = new JButton("Adapt BPM");
        togglePlayButton = new JButton("Play");

        titleDisplayLabel.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        titleDisplayLabel.setOpaque(true);
        titleDisplayLabel.setHorizontalAlignment(JLabel.CENTER);

        fileChooserButton.addActionListener(this);
        urlChooserButton.addActionListener(this);
        adaptBPMButton.addActionListener(this);
        togglePlayButton.addActionListener(this);

        togglePlayButton.setEnabled(false);

        chooseButtonsPanel.add(fileChooserButton);
        chooseButtonsPanel.add(urlChooserButton);

        bpmInputLabel.setHorizontalAlignment(JLabel.CENTER);

        bpmInputPanel.add(bpmInputLabel);
        bpmInputPanel.add(bpmInputSpinner);

        songPanel.add(titleDisplayLabel);
        songPanel.add(chooseButtonsPanel);
        songPanel.add(bpmInputPanel);
        songPanel.add(adaptBPMButton);
        songPanel.add(togglePlayButton);

        add(songPanel, BorderLayout.NORTH);

        eventBus.register(this);
    }

    void deinit() {
            eventBus.unregister(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.fileChooserButton) {
            int returnVal = fileChooser.showDialog(this, "Choose a song file");
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                titleDisplayLabel.setText(file.getName());
                log.debug("Opened " + file.getName());
                togglePlayButton.setEnabled(true);
            }
        }
        else if (e.getSource() == this.urlChooserButton) {
            log.debug("Clicked url chooser");
        }
        else if (e.getSource() == this.adaptBPMButton) {
            this.bpmInputSpinner.setValue(100);
        }
        else if (e.getSource() == this.togglePlayButton) {
            if (playing) {
                this.togglePlayButton.setText("Play");
            } else {
                this.togglePlayButton.setText("Pause");
            }
            playing = !playing;
        }
    }
}
