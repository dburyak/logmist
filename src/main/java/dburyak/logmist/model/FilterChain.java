package dburyak.logmist.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import dburyak.jtools.InstanceBuilder;
import dburyak.jtools.Validators;
import net.jcip.annotations.Immutable;
import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;


/**
 * Project : logmist.<br/>
 * Filter chain (composite filter). <br/>
 * <b>Created on:</b> <i>2:28:23 AM Jul 23, 2015</i>
 *
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
@Immutable
@ThreadSafe
@javax.annotation.concurrent.Immutable
@javax.annotation.concurrent.ThreadSafe
public final class FilterChain implements IFilter {

    /**
     * Serial version ID. <br/>
     * <b>Created on:</b> <i>2:30:21 AM Jul 23, 2015</i>
     */
    private static final long serialVersionUID = 1L;


    /**
     * Project : logmist.<br/>
     * Chain link types. <br/>
     * <b>Created on:</b> <i>2:36:19 AM Jul 23, 2015</i>
     *
     * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
     * @version 0.1
     */
    public static enum LinkType {

        /**
         * "AND" chain link type. <br/>
         * <b>Created on:</b> <i>2:38:41 AM Jul 23, 2015</i>
         */
        AND,

            /**
             * "OR" chain link type. <br/>
             * <b>Created on:</b> <i>2:38:44 AM Jul 23, 2015</i>
             */
        OR
    }

    /**
     * Project : logmist.<br/>
     * Represents one joint (connection, link) of filter chain. Contains filter and type of connection.<br/>
     * <b>Created on:</b> <i>2:49:14 AM Jul 23, 2015</i>
     *
     * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
     * @version 0.1
     */
    @Immutable
    @ThreadSafe
    @javax.annotation.concurrent.Immutable
    @javax.annotation.concurrent.ThreadSafe
    public static final class FilterJoint implements Serializable {

        /**
         * Serial version ID. <br/>
         * <b>Created on:</b> <i>2:48:58 AM Jul 23, 2015</i>
         */
        private static final long serialVersionUID = 1L;

        /**
         * Filter of this joint. <br/>
         * <b>Created on:</b> <i>3:10:29 AM Jul 23, 2015</i>
         */
        private final IFilter filter;

        /**
         * Link type of this joint. <br/>
         * <b>Created on:</b> <i>3:10:43 AM Jul 23, 2015</i>
         */
        private final LinkType linkType;


        /**
         * Validator for filter. <br/>
         * <b>PRE-conditions:</b> NONE <br/>
         * <b>POST-conditions:</b> NONE <br/>
         * <b>Side-effects:</b> NONE <br/>
         * <b>Created on:</b> <i>3:11:00 AM Jul 23, 2015</i>
         *
         * @param filter
         *            filter to be validated
         * @return true if filter is valid
         * @throws IllegalArgumentException
         *             if filter is not valid
         */
        private static final boolean validateFilter(final IFilter filter) {
            return Validators.nonNull(filter);
        }

        /**
         * Validator for linkType. <br/>
         * <b>PRE-conditions:</b> NONE <br/>
         * <b>POST-conditions:</b> NONE <br/>
         * <b>Side-effects:</b> NONE <br/>
         * <b>Created on:</b> <i>3:13:21 AM Jul 23, 2015</i>
         *
         * @param linkType
         *            linkType to be validated
         * @return true if provided linkType is valid
         * @throws IllegalArgumentException
         *             if link type is invalid
         */
        private static final boolean validateLinkType(final LinkType linkType) {
            return Validators.nonNull(linkType);
        }

        /**
         * Constructor for class : [logmist] dburyak.logmist.model.FilterJoint.<br/>
         * <br/>
         * <b>PRE-conditions:</b> non-null args <br/>
         * <b>POST-conditions:</b> NONE <br/>
         * <b>Side-effects:</b> NONE <br/>
         * <b>Created on:</b> <i>3:14:38 AM Jul 23, 2015</i>
         *
         * @param filter
         *            filter of this joint
         * @param linkType
         *            link type of this joint
         */
        public FilterJoint(final IFilter filter, final LinkType linkType) {
            validateFilter(filter);
            validateLinkType(linkType);

            this.filter = filter;
            this.linkType = linkType;
        }


        /**
         * Getter method.<br/>
         * <br/>
         * <b>POST-conditions:</b> non-null result <br/>
         * <b>Side-effects:</b> NONE <br/>
         * <b>Created on:</b> <i>3:05:36 AM Jul 23, 2015</i>
         *
         * @return the filter
         */
        public final IFilter getFilter() {
            return filter;
        }


        /**
         * Getter method.<br/>
         * <br/>
         * <b>POST-conditions:</b> non-null result <br/>
         * <b>Side-effects:</b> NONE <br/>
         * <b>Created on:</b> <i>3:05:36 AM Jul 23, 2015</i>
         *
         * @return the linkType
         */
        public final LinkType getLinkType() {
            return linkType;
        }


    }

    /**
     * Project : logmist.<br/>
     * Instance builder for FilterChain class. <br/>
     * <b>Created on:</b> <i>12:42:10 AM Jul 29, 2015</i>
     * 
     * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
     * @version 0.1
     */
    @NotThreadSafe
    @javax.annotation.concurrent.NotThreadSafe
    public static final class FilterChainBuilder implements InstanceBuilder<FilterChain> {

        /**
         * Chain of filter joints ( {@link FilterJoint} ).
         * <br/><b>Created on:</b> <i>12:56:28 AM Jul 29, 2015</i>
         */
        private final Collection<FilterJoint> chain;

        /**
         * Name of the {@link FilterChain} instance to be built.
         * <br/><b>Created on:</b> <i>10:46:51 AM Jul 29, 2015</i>
         */
        private final String name;


        /**
         * Validator for joint {@link FilterJoint} that internal chain is build of. <br/>
         * <b>PRE-conditions:</b> NONE <br/>
         * <b>POST-conditions:</b> NONE <br/>
         * <b>Side-effects:</b> NONE <br/>
         * <b>Created on:</b> <i>12:46:14 AM Jul 29, 2015</i>
         * 
         * @param joint
         *            joint to be validated
         * @return true if joint is valid
         * @throws IllegalArgumentException
         *             if joint is invalid
         */
        private static final boolean validateJoint(final FilterJoint joint) {
            return Validators.nonNull(joint);
        }

        /**
         * Validator for chain private parameter of {@link FilterChainBuilder}. Used for state invariant validation.
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>1:15:20 AM Jul 29, 2015</i>
         * 
         * @param chain
         *            filter chain to be validated
         * @return true if chain is valid
         * @throws IllegalArgumentException
         *             if chain is invalid
         */
        private static final boolean validateChain(final Collection<FilterJoint> chain) {
            // FIXME : wrong logic
            boolean result = Validators.nonNull(chain);
            final List<IFilter> traversed = new LinkedList<>();
            for (final FilterJoint joint : chain) {
                if (!traversed.contains(joint.getFilter())) { // not traversed yet
                    traversed.add(joint.getFilter());
                } else { // cirle detected - same filter in chain
                    // FIXME : false alarms on same filters in chain (non-circles, just re-use)
                    result = false;
                    break;
                }
            }
            if (!result) {
                throw new IllegalArgumentException();
            }
            return result;
        }

        /**
         * Validator for name of the result {@link FilterChain}.
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>10:48:05 AM Jul 29, 2015</i>
         * 
         * @param name
         *            name of the result filter chain to be validated
         * @return true if name is valid
         * @throws IllegalArgumentException
         *             if name is invalid
         */
        private static final boolean validateName(final String name) {
            return Validators.nonEmpty(name);
        }

        /**
         * Constructor for class : [logmist] dburyak.logmist.model.FilterChainBuilder.<br/>
         * Creates a new builder for {@link FilterChain} with specified {@link FilterJoint} as first filter in chain.
         * <br/><b>PRE-conditions:</b> non-null arg
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>12:44:14 AM Jul 29, 2015</i>
         * 
         * @param name
         *            name of the result {@link FilterChain} instance
         * @param firstFilter
         *            first filter in this chain
         * @param firstLinkType
         *            link type of first filter
         */
        public FilterChainBuilder(final String name, final IFilter firstFilter, final LinkType firstLinkType) {
            final FilterJoint first = new FilterJoint(firstFilter, firstLinkType);
            validateJoint(first);
            validateName(name);

            chain = new LinkedList<>();
            chain.add(first);
            this.name = name;
        }

        /**
         * Add next filter to this chain. Add SEQUENTIALLY.
         * <br/><b>PRE-conditions:</b> non-null arg
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> filter chain state is changed
         * <br/><b>Created on:</b> <i>1:01:16 AM Jul 29, 2015</i>
         * 
         * @param filter
         *            filter to be appended to chain
         * @return this reference (for call chaining)
         */
        public final FilterChainBuilder and(final IFilter filter) {
            final FilterJoint newAND = new FilterJoint(filter, LinkType.AND);
            validateJoint(newAND);
            chain.add(newAND);
            return this;
        }

        /**
         * Add next filter to this chain. Add IN PARALLEL.
         * <br/><b>PRE-conditions:</b> non-null arg
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> filter chain state is changed
         * <br/><b>Created on:</b> <i>1:03:38 AM Jul 29, 2015</i>
         * 
         * @param filter
         *            filter to be appended to chain
         * @return this reference (for call chaining)
         */
        public final FilterChainBuilder or(final IFilter filter) {
            final FilterJoint newOR = new FilterJoint(filter, LinkType.OR);
            validateJoint(newOR);
            chain.add(newOR);
            return this;
        }

        /**
         * Build a new FilterChain based on current state of this builder.
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b>NONE
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>1:23:45 AM Jul 29, 2015</i>
         * 
         * @return new {@link FilterChain}
         * @throws IllegalStateException
         *             if internal state if chain is invalid
         */
        @SuppressWarnings("synthetic-access")
        @Override
        public final FilterChain build() throws IllegalStateException {
            try {
                validateName(name);
                validateChain(chain);
                return new FilterChain(name, chain);
            } catch (final IllegalArgumentException ex) {
                throw new IllegalStateException(ex);
            }

        }

        /**
         * Check if current state of builder is valid and a valid instance can be built within next call of build()
         * method.
         * <br/><b>PRE-conditions:</b> NONE
         * <br/><b>POST-conditions:</b> NONE
         * <br/><b>Side-effects:</b> NONE
         * <br/><b>Created on:</b> <i>1:19:43 AM Jul 29, 2015</i>
         * 
         * @return true if current state of builder is valid, false otherwise
         */
        @Override
        public final boolean isValid() {
            boolean result;
            try {
                result = validateName(name) && validateChain(chain);
            } catch (@SuppressWarnings("unused") final IllegalArgumentException e) {
                result = false;
            }
            return result;
        }

    }


    /**
     * Underlying collection of filter joints for this filter chain.
     * <br/><b>Created on:</b> <i>10:43:57 AM Jul 29, 2015</i>
     */
    private final Collection<FilterJoint> chain;

    /**
     * Name of this filter.
     * <br/><b>Created on:</b> <i>10:55:28 AM Jul 29, 2015</i>
     */
    private final String name;


    /**
     * Constructor for class : [logmist] dburyak.logmist.model.FilterChain.<br/>
     * Supposed to be called only by builder. <br/>
     * <b>PRE-conditions:</b> NONE <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> NONE <br/>
     * <b>Created on:</b> <i>3:16:51 AM Jul 23, 2015</i>
     * 
     * @param name
     *            name for this filter
     * @param chain
     *            collection of filter joint that make up a chain
     */
    private FilterChain(final String name, final Collection<FilterJoint> chain) {
        this.chain = new ArrayList<>(chain);
        this.name = name;
    }

    /**
     * Test method for this filter. Given message is tested through all the chain of filter joints of this chain filter.
     * <br/><b>PRE-conditions:</b> non-null arg
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:28:23 AM Jul 23, 2015</i>
     *
     * @see dburyak.logmist.model.IFilter#accept(dburyak.logmist.model.LogEntry)
     * @param log
     *            log entry to be tested by this filter chain
     * @return true if given log entry passes all the chain
     */
    @Override
    public final boolean accept(final LogEntry log) {
        // heart of filter chain logic goes here
        LinkType prevLinkType = null;
        boolean acceptedAND = false;
        boolean acceptedOR = false;
        for (final FilterJoint joint : chain) {
            if (joint.getLinkType() == LinkType.AND) {
                if ((prevLinkType == LinkType.OR) && !acceptedOR) {
                    return false;
                }
                acceptedAND = joint.getFilter().accept(log);
                prevLinkType = LinkType.AND;
                acceptedOR = false;
            } else { // OR
                acceptedOR = (acceptedOR || joint.getFilter().accept(log));
                prevLinkType = LinkType.OR;
                acceptedAND = false;
                continue;
            }
            if (!acceptedAND && !acceptedOR) {
                return false;
            }
        }
        return (acceptedAND || acceptedOR);
    }

    /**
     * Get name of this filter.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>2:28:23 AM Jul 23, 2015</i>
     *
     * @see dburyak.logmist.model.IFilter#getName()
     * @return name of this filter
     */
    @Override
    public final String getName() {
        return name;
    }

    /**
     * Check if given filter joint is complex (has sub-elements).
     * <br/><b>PRE-conditions:</b> non-null arg
     * <br/><b>POST-conditions:</b> NONE
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>10:56:05 AM Jul 30, 2015</i>
     * 
     * @param joint
     *            filter joint to be tested
     * @return true if given joint contains sub-elements
     */
    private static final boolean isJointComplex(final FilterJoint joint) {
        assert(Validators.nonNull(joint));

        return (joint.getFilter() instanceof FilterChain);
    }

    /**
     * Get number of sub-joint of complex joint.
     * <br/><b>PRE-conditions:</b> non-null arg, joint contains {@link FilterChain}
     * <br/><b>POST-conditions:</b> ressult >= 1
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>11:08:56 AM Jul 30, 2015</i>
     * 
     * @param joint
     *            complex joint to get number of sub-joints from
     * @return number of sub-joints from given complex joint
     */
    private static final int getNumJointsFromComplex(final FilterJoint joint) {
        assert(Validators.nonNull(joint));
        assert(joint.getFilter() instanceof FilterChain);

        // FIXME : bad code, decide how to improve it
        return ((FilterChain) joint.getFilter()).getNumOfJoints();
    }

    /**
     * Validator for number of joints.
     * <br/><b>Created on:</b> <i>2:04:59 AM Aug 3, 2015</i>
     * 
     * @param numOfJoints
     *            number of joints to be validated
     * @return true if number is valid
     * @throws IllegalArgumentException
     *             if number of joints is invalid
     */
    private static final boolean validateNumOfJoints(final int numOfJoints) {
        if (numOfJoints < 1) {
            throw new IllegalArgumentException();
        }
        return true;

    }

    /**
     * Get number of joints in filter chain.
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> result >= 1
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>10:56:59 AM Jul 30, 2015</i>
     * 
     * @return number of joints in this filter chain
     */
    private final int getNumOfJoints() {
        int result = 0;
        for (final FilterJoint joint : chain) {
            if (isJointComplex(joint)) {
                result += getNumJointsFromComplex(joint);
            } else {
                result++;
            }
        }
        assert(validateNumOfJoints(result)); // post-condition

        return result;
    }

    /**
     * Prints this filter chain to string. Example:
     * 
     * <pre>
     * {name=[super-chain-filter],numOfJoints=[6]}
     * </pre>
     * 
     * <br/><b>PRE-conditions:</b> NONE
     * <br/><b>POST-conditions:</b> non-null result
     * <br/><b>Side-effects:</b> NONE
     * <br/><b>Created on:</b> <i>10:50:19 AM Jul 30, 2015</i>
     * 
     * @see java.lang.Object#toString()
     * @return string representation of this filter
     */
    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder("{name=[").append(name); //$NON-NLS-1$
        sb.append("],numOfJoints=[").append(getNumOfJoints()).append("]}");  //$NON-NLS-1$//$NON-NLS-2$
        return sb.toString();
    }

}
