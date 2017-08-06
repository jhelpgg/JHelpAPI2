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

package jhelp.antology;

/**
 * {@link Rule} constraints
 */
public enum RuleConstraint
{
    /**
     * Verify if first and second subjects are equals
     */
    SUBJECTS_EQUALS,
    /**
     * Verify if first and second predicates are equals
     */
    PREDICATES_EQUALS,
    /**
     * Verify if first and second information are equals
     */
    INFORMATION_EQUALS,
    /**
     * Verify if first subject equals second predicate
     */
    FIRST_SUBJECT_EQUALS_SECOND_PREDICATE,
    /**
     * Verify if first subject equals second information
     */
    FIRST_SUBJECT_EQUALS_SECOND_INFORMATION,
    /**
     * Verify if first predicate equals second subject
     */
    FIRST_PREDICATE_EQUALS_SECOND_SUBJECT,
    /**
     * Verify if first predicate equals second information
     */
    FIRST_PREDICATE_EQUALS_SECOND_INFORMATION,
    /**
     * Verify if first information equals second subject
     */
    FIRST_INFORMATION_EQUALS_SECOND_SUBJECT,
    /**
     * Verify if first information equals second predicate
     */
    FIRST_INFORMATION_EQUALS_SECOND_PREDICATE
}
