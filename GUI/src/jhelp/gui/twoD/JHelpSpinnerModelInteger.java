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
package jhelp.gui.twoD;

/**
 * Spinner model based on integer
 *
 * @author JHelp
 */
public class JHelpSpinnerModelInteger
        implements JHelpSpinnerModel<Integer>
{
    /**
     * Actual value
     */
    private       int    actual;
    /**
     * Biggest text
     */
    private       String bigest;
    /**
     * Maximum vlaue
     */
    private final int    maximum;
    /**
     * Minimum value
     */
    private final int    minimum;
    /**
     * Step to use between changes
     */
    private final int    step;

    /**
     * Create a new instance of JHelpSpinnerModelInteger with 1 as step and minimum as start value
     *
     * @param minimum Minimum value
     * @param maximum Maximum value
     */
    public JHelpSpinnerModelInteger(final int minimum, final int maximum)
    {
        this(minimum, maximum, minimum);
    }

    /**
     * Create a new instance of JHelpSpinnerModelInteger with 1 step
     *
     * @param minimum Minimum
     * @param maximum Maximum
     * @param actual  Start value
     */
    public JHelpSpinnerModelInteger(final int minimum, final int maximum, final int actual)
    {
        this(minimum, maximum, actual, 1);
    }

    /**
     * Create a new instance of JHelpSpinnerModelInteger
     *
     * @param minimum Minimum
     * @param maximum Maximum
     * @param actual  Start value
     * @param step    Step to use
     */
    public JHelpSpinnerModelInteger(final int minimum, final int maximum, final int actual, final int step)
    {
        this.minimum = Math.min(minimum, maximum);
        this.maximum = Math.max(minimum, maximum);
        this.actual = Math.max(this.minimum, Math.min(this.maximum, actual));
        this.step = Math.max(1, Math.abs(step));
    }

    /**
     * Actual value text representation <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Actual value text representation
     * @see JHelpSpinnerModel#actualText()
     */
    @Override
    public String actualText()
    {
        return String.valueOf(this.actual);
    }

    /**
     * Actual value <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Actual value
     * @see JHelpSpinnerModel#actualValue()
     */
    @Override
    public Integer actualValue()
    {
        return this.actual;
    }

    /**
     * Change current value.<br>
     * Value is automatic put between minimum and maximum <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @param value New value
     * @see JHelpSpinnerModel#actualValue(Object)
     */
    @Override
    public void actualValue(final Integer value)
    {
        this.actual = Math.max(this.minimum, Math.min(this.maximum, value));
    }

    /**
     * Model biggest text <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return Model biggest text
     * @see JHelpSpinnerModel#biggestText()
     */
    @Override
    public String biggestText()
    {
        if (this.bigest == null)
        {
            final String min = String.valueOf(this.minimum);
            final String max = String.valueOf(this.maximum);

            if (min.length() <= max.length())
            {
                this.bigest = max;
            }
            else
            {
                this.bigest = min;
            }
        }

        return this.bigest;
    }

    /**
     * Indicates if theire are a next value <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return {@code true} if theire are a next value
     * @see JHelpSpinnerModel#hasNextValue()
     */
    @Override
    public boolean hasNextValue()
    {
        return this.actual < this.maximum;
    }

    /**
     * Indicates if theire are a previous value <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @return {@code true} if theire are a previous value
     * @see JHelpSpinnerModel#hasPreviousValue()
     */
    @Override
    public boolean hasPreviousValue()
    {
        return this.actual > this.minimum;
    }

    /**
     * Go to next value <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see JHelpSpinnerModel#nextValue()
     */
    @Override
    public void nextValue()
    {
        this.actual = Math.min(this.maximum, this.actual + this.step);
    }

    /**
     * Go to previous value <br>
     * <br>
     * <b>Parent documentation:</b><br>
     * {@inheritDoc}
     *
     * @see JHelpSpinnerModel#previousValue()
     */
    @Override
    public void previousValue()
    {
        this.actual = Math.max(this.minimum, this.actual - this.step);
    }
}