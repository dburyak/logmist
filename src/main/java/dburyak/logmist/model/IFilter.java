package dburyak.logmist.model;


import java.io.Serializable;


/**
 * Project : logmist.<br/>
 * Represents log filter, which can accept or not provided {@link LogEntry} objects. <br/>
 * <b>Created on:</b> <i>10:27:55 PM Jul 21, 2015</i>
 *
 * @author <i>Dmytro Buryak &ltdmytro.buryak@gmail.com&gt</i>
 * @version 0.1
 */
public interface IFilter extends Serializable {

    /**
     * Test if the specified log entry is acceptable for this filter. <br/>
     * <b>PRE-conditions:</b> non-null argument <br/>
     * <b>POST-conditions:</b> NONE <br/>
     * <b>Side-effects:</b> UNDEFINED <br/>
     * <b>Created on:</b> <i>10:40:36 PM Jul 21, 2015</i>
     *
     * @param log
     *            log entry to be tested
     * @return true is given log entry satisfies this filter criteria, false otherwise
     * @throws NullPointerException
     *             if log argument is null
     */
    public boolean accept(final LogEntry log);

    /**
     * Filter is supposed to have a name. <br/>
     * <b>PRE-conditions:</b> NONE<br/>
     * <b>POST-conditions:</b> non-null result <br/>
     * <b>Side-effects:</b> UNDEFINED <br/>
     * <b>Created on:</b> <i>1:21:07 AM Jul 22, 2015</i>
     *
     * @return name of the filter
     */
    public String getName();

}
