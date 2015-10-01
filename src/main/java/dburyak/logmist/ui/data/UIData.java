package dburyak.logmist.ui.data;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.logmist.ui.Resources;
import dburyak.logmist.ui.Resources.ConfigID;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;


// TODO : code style
@ThreadSafe
@javax.annotation.concurrent.ThreadSafe
public abstract class UIData<T> {
    private static final Logger LOG = LogManager.getFormatterLogger(UIData.class);

    @GuardedBy("rwLock")
    private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
    private final long RW_LOCK_TIMEOUT_MS;
    
    public UIData() {
        final Resources res = Resources.getInstance();
        RW_LOCK_TIMEOUT_MS = Long.parseLong(res.getConfigProp(ConfigID.CORE_UIDATA_RWLOCK_TIMEOUT_MS));
    }
    
    public final void updateData(final T newValue) {
        boolean acquired = false;
        try {
            do {
                acquired = rwLock.writeLock().tryLock(RW_LOCK_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                if (acquired) {
                    doUpdateData(newValue);
                } else {
                    LOG.warn("write lock acquisition timed out : timout = [%d]", RW_LOCK_TIMEOUT_MS);
                }
            } while (!acquired);
        } catch (final InterruptedException e) {
            LOG.catching(Level.TRACE, e);
            LOG.error("unexpected interruption", e);
            throw LOG.throwing(Level.TRACE, new AssertionError());
        } finally {
            if (acquired) {
                rwLock.writeLock().unlock();
            }
        }
    }
    
    protected abstract void doUpdateData(final T newValue);
    
    public final T getData() {
        T result = null;
        boolean acquired = false;
        try {
            do {
                acquired = rwLock.readLock().tryLock(RW_LOCK_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                if (acquired) {
                    result = doGetData();
                } else {
                    LOG.warn("read lock acquisition timed out : timeout = [%d]", RW_LOCK_TIMEOUT_MS);
                }
            } while (!acquired);
        } catch (final InterruptedException e) {
            LOG.catching(Level.TRACE, e);
            LOG.error("unexpected interruption", e);
            throw LOG.throwing(Level.TRACE, new AssertionError());
        } finally {
            if (acquired) {
                rwLock.readLock().unlock();
            }
        }
        return result;
    }
    
    protected abstract T doGetData();
    
}
