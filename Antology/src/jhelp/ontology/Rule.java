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

package jhelp.ontology;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.Objects;
import jhelp.util.io.ByteArray;
import jhelp.util.list.ArrayObject;

/**
 * Describe a {@link Graph} rule to apply.<br>
 * When a rule is applied, it list all triplet match the first and second triplet,
 * filter these two lists to match the constraints, and create new triplet on apply result rule.
 */
public final class Rule
{
    /**
     * Parse byte array to have a Rule
     *
     * @param byteArray Byte array to parse
     * @return Rule parsed
     * @throws Exception If byte array not contains data for Rule
     */
    public static Rule parse(ByteArray byteArray) throws Exception
    {
        Node       firstSubject      = Node.parse(byteArray);
        Node       firstPredicate    = Node.parse(byteArray);
        Node       firstInformation  = Node.parse(byteArray);
        Node       secondSubject     = Node.parse(byteArray);
        Node       secondPredicate   = Node.parse(byteArray);
        Node       secondInformation = Node.parse(byteArray);
        RuleResult resultSubject     = RuleResult.parse(byteArray);
        RuleResult resultPredicate   = RuleResult.parse(byteArray);
        RuleResult resultInformation = RuleResult.parse(byteArray);
        final Rule rule = new Rule(firstSubject, firstPredicate, firstInformation,
                                   secondSubject, secondPredicate, secondInformation,
                                   resultSubject, resultPredicate, resultInformation);
        final int size = byteArray.readInteger();

        for (int i = 0; i < size; i++)
        {
            rule.ruleConstraints.add(byteArray.readEnum(RuleConstraint.class));
        }

        return rule;
    }

    /**
     * First information
     */
    private final Node                        firstInformation;
    /**
     * First predicate
     */
    private final Node                        firstPredicate;
    /**
     * First subject
     */
    private final Node                        firstSubject;
    /**
     * Indicates if rule can be changed
     */
    private       boolean                     mutable;
    /**
     * Result information
     */
    private final RuleResult                  resultInformation;
    /**
     * Result predicate
     */
    private final RuleResult                  resultPredicate;
    /**
     * Result subject
     */
    private final RuleResult                  resultSubject;
    /**
     * Rule constraints
     */
    private final ArrayObject<RuleConstraint> ruleConstraints;
    /**
     * Second information
     */
    private final Node                        secondInformation;
    /**
     * Second predicate
     */
    private final Node                        secondPredicate;
    /**
     * Second subject
     */
    private final Node                        secondSubject;

    /**
     * Create a rule.
     *
     * @param firstSubject      First triple subject
     * @param firstPredicate    First triplet predicate
     * @param firstInformation  First triplet information
     * @param secondSubject     Second triple subject
     * @param secondPredicate   Second triplet predicate
     * @param secondInformation Second triplet information
     * @param resultSubject     How create result subject
     * @param resultPredicate   How create result predicate
     * @param resultInformation How create result information
     */
    public Rule(
            @NotNull final Node firstSubject,
            @NotNull final Node firstPredicate,
            @NotNull final Node firstInformation,
            @NotNull final Node secondSubject,
            @NotNull final Node secondPredicate,
            @NotNull final Node secondInformation,
            @NotNull final RuleResult resultSubject,
            @NotNull final RuleResult resultPredicate,
            @NotNull final RuleResult resultInformation)
    {
        Objects.requireNonNull(firstSubject, "firstSubject MUST NOT be null!");
        Objects.requireNonNull(firstPredicate, "firstPredicate MUST NOT be null!");
        Objects.requireNonNull(firstInformation, "firstInformation MUST NOT be null!");
        Objects.requireNonNull(secondSubject, "secondSubject MUST NOT be null!");
        Objects.requireNonNull(secondPredicate, "secondPredicate MUST NOT be null!");
        Objects.requireNonNull(secondInformation, "secondInformation MUST NOT be null!");
        Objects.requireNonNull(resultSubject, "resultSubject MUST NOT be null!");
        Objects.requireNonNull(resultPredicate, "resultPredicate MUST NOT be null!");
        Objects.requireNonNull(resultInformation, "resultInformation MUST NOT be null!");
        this.firstSubject = firstSubject;
        this.firstPredicate = firstPredicate;
        this.firstInformation = firstInformation;
        this.secondSubject = secondSubject;
        this.secondPredicate = secondPredicate;
        this.secondInformation = secondInformation;
        this.resultSubject = resultSubject;
        this.resultPredicate = resultPredicate;
        this.resultInformation = resultInformation;
        this.mutable = true;
        this.ruleConstraints = new ArrayObject<>();
    }

    /**
     * Compute node corresponding a node result
     *
     * @param ruleResult        How create result
     * @param firstSubject      First triple subject
     * @param firstPredicate    First triplet predicate
     * @param firstInformation  First triplet information
     * @param secondSubject     Second triple subject
     * @param secondPredicate   Second triplet predicate
     * @param secondInformation Second triplet information
     * @return Result node
     */
    private Node computeNode(
            RuleResult ruleResult,
            @NotNull final Node firstSubject,
            @NotNull final Node firstPredicate,
            @NotNull final Node firstInformation,
            @NotNull final Node secondSubject,
            @NotNull final Node secondPredicate,
            @NotNull final Node secondInformation)
    {
        final Node node = ruleResult.fixNode();

        if (node != null)
        {
            return node;
        }

        switch (ruleResult.result())
        {
            case FIRST_SUBJECT:
                return firstSubject;
            case FIRST_PREDICATE:
                return firstPredicate;
            case FIRST_INFORMATION:
                return firstInformation;
            case SECOND_SUBJECT:
                return secondSubject;
            case SECOND_PREDICATE:
                return secondPredicate;
            case SECOND_INFORMATION:
                return secondInformation;
            default:
                throw new RuntimeException("Should never goes here! Unknown result type: " + ruleResult.result());
        }
    }

    /**
     * Make the rule immutable
     */
    void makeImmutable()
    {
        this.mutable = false;
    }

    /**
     * Add a rule constraints
     *
     * @param ruleConstraint Constraints to add
     * @throws IllegalStateException If rule is immutable. See {@link #mutable()}
     */
    public void addConstraint(RuleConstraint ruleConstraint)
    {
        if (!this.mutable)
        {
            throw new IllegalStateException("Rule is immutable");
        }

        if (ruleConstraint != null)
        {
            synchronized (this.ruleConstraints)
            {
                if (!this.ruleConstraints.contains(ruleConstraint))
                {
                    this.ruleConstraints.add(ruleConstraint);
                }
            }
        }
    }

    /**
     * Apply, if possible, the rule to given tripplets
     *
     * @param firstSubject      First triple subject
     * @param firstPredicate    First triplet predicate
     * @param firstInformation  First triplet information
     * @param secondSubject     Second triple subject
     * @param secondPredicate   Second triplet predicate
     * @param secondInformation Second triplet information
     * @return Node result. {@code null} if rule not apply to given triplets
     */
    public @Nullable Triplet apply(
            @NotNull final Node firstSubject,
            @NotNull final Node firstPredicate,
            @NotNull final Node firstInformation,
            @NotNull final Node secondSubject,
            @NotNull final Node secondPredicate,
            @NotNull final Node secondInformation)
    {
        if (this.firstSubject != Node.WILDCARD && !this.firstSubject.equals(firstSubject))
        {
            return null;
        }

        if (this.firstPredicate != Node.WILDCARD && !this.firstPredicate.equals(firstPredicate))
        {
            return null;
        }

        if (this.firstInformation != Node.WILDCARD && !this.firstInformation.equals(firstInformation))
        {
            return null;
        }

        if (this.secondSubject != Node.WILDCARD && !this.secondSubject.equals(secondSubject))
        {
            return null;
        }

        if (this.secondPredicate != Node.WILDCARD && !this.secondPredicate.equals(secondPredicate))
        {
            return null;
        }

        if (this.secondInformation != Node.WILDCARD && !this.secondInformation.equals(secondInformation))
        {
            return null;
        }

        for (RuleConstraint ruleConstraint : this.ruleConstraints)
        {
            switch (ruleConstraint)
            {
                case SUBJECTS_EQUALS:
                    if (!firstSubject.equals(secondSubject))
                    {
                        return null;
                    }

                    break;
                case PREDICATES_EQUALS:
                    if (!firstPredicate.equals(secondPredicate))
                    {
                        return null;
                    }

                    break;
                case INFORMATION_EQUALS:
                    if (!firstInformation.equals(secondInformation))
                    {
                        return null;
                    }

                    break;
                case FIRST_INFORMATION_EQUALS_SECOND_PREDICATE:
                    if (!firstInformation.equals(secondPredicate))
                    {
                        return null;
                    }

                    break;
                case FIRST_INFORMATION_EQUALS_SECOND_SUBJECT:
                    if (!firstInformation.equals(secondSubject))
                    {
                        return null;
                    }

                    break;
                case FIRST_PREDICATE_EQUALS_SECOND_INFORMATION:
                    if (!firstPredicate.equals(secondInformation))
                    {
                        return null;
                    }

                    break;
                case FIRST_PREDICATE_EQUALS_SECOND_SUBJECT:
                    if (!firstPredicate.equals(secondSubject))
                    {
                        return null;
                    }

                    break;
                case FIRST_SUBJECT_EQUALS_SECOND_INFORMATION:
                    if (!firstSubject.equals(secondInformation))
                    {
                        return null;
                    }

                    break;
                case FIRST_SUBJECT_EQUALS_SECOND_PREDICATE:
                    if (!firstSubject.equals(secondPredicate))
                    {
                        return null;
                    }

                    break;
            }
        }

        final Node subject = this.computeNode(this.resultSubject,
                                              firstSubject, firstPredicate, firstInformation,
                                              secondSubject, secondPredicate, secondInformation);
        final Node predicate = this.computeNode(this.resultPredicate,
                                                firstSubject, firstPredicate, firstInformation,
                                                secondSubject, secondPredicate, secondInformation);
        final Node information = this.computeNode(this.resultInformation,
                                                  firstSubject, firstPredicate, firstInformation,
                                                  secondSubject, secondPredicate, secondInformation);

        return new Triplet(subject, predicate, information);
    }

    /**
     * Indicates if given object equals to this Rule
     *
     * @param object Object to compare with
     * @return {@code true} if given object equals to this Rule
     */
    @Override
    public boolean equals(final Object object)
    {
        if (this == object)
        {
            return true;
        }

        if (null == object)
        {
            return false;
        }

        if (!Rule.class.equals(object.getClass()))
        {
            return false;
        }

        Rule rule = (Rule) object;

        if (!this.firstSubject.equals(rule.firstSubject) ||
            !this.firstPredicate.equals(rule.firstPredicate) ||
            !this.firstInformation.equals(rule.firstInformation) ||
            !this.secondSubject.equals(rule.secondSubject) ||
            !this.secondPredicate.equals(rule.secondPredicate) ||
            !this.secondInformation.equals(rule.secondInformation) ||
            !this.resultSubject.equals(rule.resultSubject) ||
            !this.resultPredicate.equals(rule.resultPredicate) ||
            !this.resultInformation.equals(rule.resultInformation))
        {
            return false;
        }

        return this.ruleConstraints.containsAll(rule.ruleConstraints) &&
               rule.ruleConstraints.containsAll(this.ruleConstraints);
    }

    /**
     * First triplet information
     *
     * @return First triplet information
     */
    public @NotNull Node firstInformation()
    {
        return this.firstInformation;
    }

    /**
     * First triplet predicate
     *
     * @return First triplet predicate
     */
    public @NotNull Node firstPredicate()
    {
        return this.firstPredicate;
    }

    /**
     * First triplet subject
     *
     * @return First triplet subject
     */
    public @NotNull Node firstSubject()
    {
        return this.firstSubject;
    }

    /**
     * Indicates if rule is mutable
     *
     * @return {@code true} if rule is mutable
     */
    public boolean mutable()
    {
        return this.mutable;
    }

    /**
     * How create result information
     *
     * @return How create result information
     */
    public @NotNull RuleResult resultInformation()
    {
        return this.resultInformation;
    }

    /**
     * How create result predicate
     *
     * @return How create result predicate
     */
    public @NotNull RuleResult resultPredicate()
    {
        return this.resultPredicate;
    }

    /**
     * How create result subject
     *
     * @return How create result subject
     */
    public @NotNull RuleResult resultSubject()
    {
        return this.resultSubject;
    }

    /**
     * Second triplet information
     *
     * @return Second triplet information
     */
    public @NotNull Node secondInformation()
    {
        return this.secondInformation;
    }

    /**
     * Second triplet predicate
     *
     * @return Second triplet predicate
     */
    public @NotNull Node secondPredicate()
    {
        return this.secondPredicate;
    }

    /**
     * Second triplet subject
     *
     * @return Second triplet subject
     */
    public @NotNull Node secondSubject()
    {
        return this.secondSubject;
    }

    /**
     * Serialize Rule in byte array
     *
     * @param byteArray Byte array where write
     */
    public void serialize(ByteArray byteArray)
    {
        this.firstSubject.serialize(byteArray);
        this.firstPredicate.serialize(byteArray);
        this.firstInformation.serialize(byteArray);
        this.secondSubject.serialize(byteArray);
        this.secondPredicate.serialize(byteArray);
        this.secondInformation.serialize(byteArray);
        this.resultSubject.serialize(byteArray);
        this.resultPredicate.serialize(byteArray);
        this.resultInformation.serialize(byteArray);
        byteArray.writeInteger(this.ruleConstraints.size());
        this.ruleConstraints.consume(byteArray::writeEnum);
    }
}
