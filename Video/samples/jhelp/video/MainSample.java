/*
 * Copyright:
 * License :
 *  The following code is deliver as is.
 *  I take care that code compile and work, but I am not responsible about any  damage it may  cause.
 *  You can use, modify, the code as your need for any usage.
 *  But you can't do any action that avoid me or other person use,  modify this code.
 *  The code is free for usage and modification, you can't change that fact.
 *  @author JHelp
 *
 */

package jhelp.video;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import jhelp.util.debug.Debug;
import jhelp.util.filter.FileFilter;
import jhelp.util.gui.UtilGUI;
import jhelp.util.io.UtilIO;

public class MainSample implements ActionListener, ChangeListener
{
    private static final String LOAD  = "Load";
    private static final String PAUSE = "Pause";
    private static final String PLAY  = "Play";
    private static final String STOP  = "Stop";

    public static void main(String[] arguments)
    {
        UtilGUI.initializeGUI();
        MainSample mainSample = new MainSample();
        mainSample.show();
    }

    private JButton        buttonLoad;
    private JButton        buttonPause;
    private JButton        buttonPlay;
    private JButton        buttonStop;
    private JCheckBox      checkBoxFixSize;
    private JFrame         frame;
    private boolean        load;
    private VideoComponent videoComponent;

    public MainSample()
    {
        this.frame = new JFrame("Video player");
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.videoComponent = new VideoComponent(512, 512);
        this.videoComponent.video().registerVideoListener(this::listenVideo);
        this.frame.setLayout(new BorderLayout());
        this.frame.add(this.videoComponent, BorderLayout.CENTER);
        this.load = true;

        JPanel panel = new JPanel(new FlowLayout());
        this.buttonPlay = this.button(MainSample.PLAY);
        panel.add(this.buttonPlay);
        this.buttonPause = this.button(MainSample.PAUSE);
        panel.add(this.buttonPause);
        this.buttonStop = this.button(MainSample.STOP);
        panel.add(this.buttonStop);
        this.buttonLoad = this.button(MainSample.LOAD);
        panel.add(this.buttonLoad);
        this.checkBoxFixSize = new JCheckBox("Fix", this.videoComponent.fixSize());
        this.checkBoxFixSize.addChangeListener(this);
        panel.add(this.checkBoxFixSize);

        this.frame.add(panel, BorderLayout.SOUTH);

        UtilGUI.packedSize(this.frame);
        UtilGUI.centerOnScreen(this.frame);
    }

    private JButton button(String action)
    {
        JButton button = new JButton(action);
        button.setActionCommand(action);
        button.addActionListener(this);
        button.setEnabled(false);
        return button;
    }

    private void chooseFile()
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(UtilIO.obtainOutsideDirectory());
        fileChooser.setMultiSelectionEnabled(false);
        fileChooser.setFileFilter(FileFilter.createFilterForVideos());
        int result = fileChooser.showOpenDialog(this.videoComponent);

        if (result == JFileChooser.APPROVE_OPTION)
        {
            File videoFile = fileChooser.getSelectedFile();

            if (videoFile != null && videoFile.exists())
            {
                this.videoComponent.video().play(videoFile);
            }
        }
    }

    private void listenVideo(VideoState videoState)
    {
        Debug.information("videoState=", videoState);

        this.buttonLoad.setEnabled(videoState == VideoState.PLAYING || videoState == VideoState.PAUSE);
        this.buttonPause.setEnabled(videoState == VideoState.PLAYING);
        this.buttonPlay.setEnabled(videoState == VideoState.PAUSE);
        this.buttonStop.setEnabled(videoState == VideoState.PLAYING || videoState == VideoState.PAUSE);

        if (videoState == VideoState.STOP && this.load)
        {
            this.chooseFile();
        }

        this.load = true;
    }

    /**
     * Invoked when an action occurs.
     *
     * @param actionEvent Event description
     */
    @Override
    public void actionPerformed(final ActionEvent actionEvent)
    {
        switch (actionEvent.getActionCommand())
        {
            case MainSample.LOAD:
                this.load = true;
                this.videoComponent.video().definitiveStop();
                break;
            case MainSample.PAUSE:
                this.videoComponent.video().pause();
                break;
            case MainSample.PLAY:
                this.videoComponent.video().resumePlay();
                break;
            case MainSample.STOP:
                this.load = false;
                this.videoComponent.video().stop();
                break;
        }
    }

    public void show()
    {
        this.frame.setVisible(true);
        this.chooseFile();
    }

    /**
     * Invoked when the target of the listener has changed its state.
     *
     * @param changeEvent a ChangeEvent object
     */
    @Override
    public void stateChanged(final ChangeEvent changeEvent)
    {
        this.videoComponent.fixSize(this.checkBoxFixSize.isSelected());
    }
}
