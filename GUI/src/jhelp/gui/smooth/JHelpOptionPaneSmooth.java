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
package jhelp.gui.smooth;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import jhelp.gui.action.GenericAction;
import jhelp.gui.smooth.event.JHelpOptionPaneSmoothListener;
import jhelp.gui.smooth.layout.JHelpBorderConstraintsSmooth;
import jhelp.gui.smooth.layout.JHelpBorderLayoutSmooth;
import jhelp.gui.smooth.layout.JHelpTableConstraintsSmooth;
import jhelp.gui.smooth.layout.JHelpTableLayoutSmooth;
import jhelp.gui.smooth.resources.JHelpResourcesSmooth;
import jhelp.gui.smooth.shape.ShadowLevel;
import jhelp.gui.smooth.shape.SmoothSausage;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.JHelpPaint;

/**
 * Represents an option pane
 *
 * @author JHelp
 */
public class JHelpOptionPaneSmooth
        implements JHelpConstantsSmooth
{
    /**
     * Action played by a button
     *
     * @author JHelp
     */
    class ActionButton
            extends GenericAction
    {
        /**
         * JHelpOptionPaneSmooth.java [long]
         */
        private static final long serialVersionUID = 5949991512082338594L;
        /**
         * Button type
         */
        private final OptionPaneButton optionPaneButton;

        /**
         * Create a new instance of ActionButton
         *
         * @param optionPaneButton Button type
         */
        public ActionButton(final OptionPaneButton optionPaneButton)
        {
            super(optionPaneButton.keyText(), JHelpResourcesSmooth.RESOURCE_TEXT);
            this.optionPaneButton = optionPaneButton;
        }

        /**
         * Does the button's action <br>
         * <br>
         * <b>Parent documentation:</b><br>
         * {@inheritDoc}
         *
         * @param actionEvent Event description
         * @see GenericAction#doActionPerformed(ActionEvent)
         */
        @Override
        protected void doActionPerformed(final ActionEvent actionEvent)
        {
            JHelpOptionPaneSmooth.this.buttonClicked(this.optionPaneButton);
        }
    }

    /**
     * Back ground color
     */
    private final int                                 background;
    /**
     * Frame smooth parent
     */
    private final JHelpFrameSmooth                    frameSmooth;
    /**
     * Option pane ID
     */
    private final int                                 id;
    /**
     * Option pane listeners
     */
    private final List<JHelpOptionPaneSmoothListener> listeners;
    /**
     * Panel where option pane is draw
     */
    private final JHelpPanelSmooth                    mainPanel;
    /**
     * Background paint
     */
    private final JHelpPaint                          paintBackground;
    /**
     * Shadow level
     */
    private final int                                 shadow;
    /**
     * Texture background
     */
    private final JHelpImage                          textureBackground;

    /**
     * Create a new instance of JHelpOptionPaneSmooth
     *
     * @param id             Option pane ID
     * @param frameSmooth    Frame parent
     * @param icon           Option pane icon, use {@code null} for no icon
     * @param component      Main component
     * @param background     Background color (Used if paint and texture are both {@code null})
     * @param paint          Background paint (Used if not {@code null} and texture is {@code null})
     * @param texture        Background texture (Used if not {@code null} and paint is {@code null})
     * @param shadow         Shadow level
     * @param optionPaneType Option pane type
     */
    private JHelpOptionPaneSmooth(
            final int id, final JHelpFrameSmooth frameSmooth, final JHelpImage icon,
            final JHelpComponentSmooth component,
            final int background, final JHelpPaint paint, final JHelpImage texture, final int shadow,
            final OptionPaneType optionPaneType)
    {
        this.id = id;
        this.frameSmooth = frameSmooth;
        this.background = background;
        this.paintBackground = paint;
        this.textureBackground = texture;
        this.shadow = shadow;
        this.listeners = new ArrayList<JHelpOptionPaneSmoothListener>();

        this.mainPanel = new JHelpPanelSmooth(new JHelpBorderLayoutSmooth());

        if (icon != null)
        {
            final JHelpLabelImageSmooth imageSmooth = new JHelpLabelImageSmooth(icon);
            imageSmooth.shadowLevel(ShadowLevel.NO_SHADOW);
            this.mainPanel.addComponent(imageSmooth, JHelpBorderConstraintsSmooth.LEFT);
        }

        this.mainPanel.addComponent(component, JHelpBorderConstraintsSmooth.CENTER);
        component.shadowLevel(ShadowLevel.NO_SHADOW);

        final JHelpPanelSmooth panelButtons = new JHelpPanelSmooth(new JHelpTableLayoutSmooth());

        switch (optionPaneType)
        {
            case CANCEL_YES_NO:
                panelButtons.addComponent(this.createButton(OptionPaneButton.CANCEL),
                                          new JHelpTableConstraintsSmooth(0, 0));
                panelButtons.addComponent(this.createButton(OptionPaneButton.YES),
                                          new JHelpTableConstraintsSmooth(3, 0));
                panelButtons.addComponent(this.createButton(OptionPaneButton.NO),
                                          new JHelpTableConstraintsSmooth(4, 0));
                break;
            case OK:
                panelButtons.addComponent(this.createButton(OptionPaneButton.OK),
                                          new JHelpTableConstraintsSmooth(0, 0));
                break;
            case YES_NO:
                panelButtons.addComponent(this.createButton(OptionPaneButton.YES),
                                          new JHelpTableConstraintsSmooth(0, 0));
                panelButtons.addComponent(this.createButton(OptionPaneButton.NO),
                                          new JHelpTableConstraintsSmooth(1, 0));
                break;
        }

        this.mainPanel.addComponent(panelButtons, JHelpBorderConstraintsSmooth.DOWN_EXPAND);
    }

    /**
     * Create a new instance of JHelpOptionPaneSmooth with unified background color
     *
     * @param id             Option option pane ID
     * @param frameSmooth    Frame parent
     * @param component      Component to insert inside the option pane
     * @param background     Background color
     * @param shadow         Shadow level
     * @param optionPaneType Option pane type
     */
    public JHelpOptionPaneSmooth(
            final int id, final JHelpFrameSmooth frameSmooth, final JHelpComponentSmooth component,
            final int background, final int shadow,
            final OptionPaneType optionPaneType)
    {
        this(id, frameSmooth, null, component, background, shadow, optionPaneType);
    }

    /**
     * Create a new instance of JHelpOptionPaneSmooth with texture background
     *
     * @param id             Option pane ID
     * @param frameSmooth    Frame parent
     * @param component      Component to insert inside the option pane
     * @param background     Background texture
     * @param shadow         Shadow level
     * @param optionPaneType Option pane type
     */
    public JHelpOptionPaneSmooth(
            final int id, final JHelpFrameSmooth frameSmooth, final JHelpComponentSmooth component,
            final JHelpImage background,
            final int shadow, final OptionPaneType optionPaneType)
    {
        this(id, frameSmooth, null, component, background, shadow, optionPaneType);
    }

    /**
     * Create a new instance of JHelpOptionPaneSmooth with paint background
     *
     * @param id             Option pane ID
     * @param frameSmooth    Frame parent
     * @param component      Component to insert inside the option pane
     * @param background     Background paint
     * @param shadow         Shadow level
     * @param optionPaneType Option pane type
     */
    public JHelpOptionPaneSmooth(
            final int id, final JHelpFrameSmooth frameSmooth, final JHelpComponentSmooth component,
            final JHelpPaint background,
            final int shadow, final OptionPaneType optionPaneType)
    {
        this(id, frameSmooth, null, component, background, shadow, optionPaneType);
    }

    /**
     * Create a new instance of JHelpOptionPaneSmooth with unified background color
     *
     * @param id             Option pane ID
     * @param frameSmooth    Frame parent
     * @param icon           Icon to use
     * @param component      Component to insert inside the option pane
     * @param background     Background color
     * @param shadow         Shadow level
     * @param optionPaneType Option pane type
     */
    public JHelpOptionPaneSmooth(
            final int id, final JHelpFrameSmooth frameSmooth, final JHelpImage icon,
            final JHelpComponentSmooth component,
            final int background, final int shadow, final OptionPaneType optionPaneType)
    {
        this(id, frameSmooth, icon, component, background, null, null, shadow, optionPaneType);
    }

    /**
     * Create a new instance of JHelpOptionPaneSmooth with texture background
     *
     * @param id             Option pane ID
     * @param frameSmooth    Frame parent
     * @param icon           Icon to use
     * @param component      Component to insert inside the option pane
     * @param background     Background texture
     * @param shadow         Shadow level
     * @param optionPaneType Option pane type
     */
    public JHelpOptionPaneSmooth(
            final int id, final JHelpFrameSmooth frameSmooth, final JHelpImage icon,
            final JHelpComponentSmooth component,
            final JHelpImage background, final int shadow, final OptionPaneType optionPaneType)
    {
        this(id, frameSmooth, icon, component, JHelpConstantsSmooth.COLOR_CYAN_0500, null, background, shadow,
             optionPaneType);
    }

    /**
     * Create a new instance of JHelpOptionPaneSmooth with paint background
     *
     * @param id             Option pane ID
     * @param frameSmooth    Frame parent
     * @param icon           Icon to use
     * @param component      Component to insert inside the option pane
     * @param background     Background texture
     * @param shadow         Shdow level
     * @param optionPaneType Option pane type
     */
    public JHelpOptionPaneSmooth(
            final int id, final JHelpFrameSmooth frameSmooth, final JHelpImage icon,
            final JHelpComponentSmooth component,
            final JHelpPaint background, final int shadow, final OptionPaneType optionPaneType)
    {
        this(id, frameSmooth, icon, component, JHelpConstantsSmooth.COLOR_CYAN_0500, background, null, shadow,
             optionPaneType);
    }

    /**
     * Create an option pane button
     *
     * @param optionPaneButton Button, type
     * @return Created button
     */
    private JHelpButtonSmooth createButton(final OptionPaneButton optionPaneButton)
    {
        final JHelpButtonSmooth buttonSmooth = new JHelpButtonSmooth(new ActionButton(optionPaneButton));
        buttonSmooth.shape(SmoothSausage.SAUSAGE);
        return buttonSmooth;
    }

    /**
     * Called when one option pane button clicked
     *
     * @param optionPaneButton Option pane button clicked
     */
    void buttonClicked(final OptionPaneButton optionPaneButton)
    {
        this.frameSmooth.hideDialog(this.id);

        for (final JHelpOptionPaneSmoothListener listener : this.listeners)
        {
            listener.optionPaneButtonClicked(this, optionPaneButton);
        }
    }

    /**
     * Create dialog description for show the option pane.<br>
     * Use it with {@link JHelpFrameSmooth#createDialog(int)}
     *
     * @return Dialog description
     */
    public DialogDescription createDialogDescriDecsription()
    {
        final Dimension size   = this.mainPanel.preferredSize();
        final Dimension screen = this.frameSmooth.frameSize();
        final int       x      = (screen.width - size.width) >> 1;
        final int       y      = (screen.height - size.height) >> 1;

        if (this.paintBackground != null)
        {
            return new DialogDescription(x, y, this.mainPanel, this.paintBackground, this.shadow, ShadowLevel.FAR);
        }

        if (this.textureBackground != null)
        {
            return new DialogDescription(x, y, this.mainPanel, this.textureBackground, this.shadow, ShadowLevel.FAR);
        }

        return new DialogDescription(x, y, this.mainPanel, this.background, this.shadow, ShadowLevel.FAR);
    }

    /**
     * Option pane ID
     *
     * @return Option pane ID
     */
    public int id()
    {
        return this.id;
    }

    /**
     * Register listener of option pane events
     *
     * @param listener Listener to register
     */
    public void registerJHelpOptionPaneSmoothListener(final JHelpOptionPaneSmoothListener listener)
    {
        if ((listener != null) && (!this.listeners.contains(listener)))
        {
            this.listeners.add(listener);
        }
    }

    /**
     * Unregister listener of option pane events
     *
     * @param listener Listener to unregister
     */
    public void unregisterJHelpOptionPaneSmoothListener(final JHelpOptionPaneSmoothListener listener)
    {
        this.listeners.remove(listener);
    }
}