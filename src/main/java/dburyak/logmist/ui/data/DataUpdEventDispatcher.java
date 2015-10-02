package dburyak.logmist.ui.data;


import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dburyak.logmist.ui.Resources;
import dburyak.logmist.ui.Resources.ConfigID;
import net.jcip.annotations.ThreadSafe;


// TODO : code style
@ThreadSafe
@javax.annotation.concurrent.ThreadSafe
public final class DataUpdEventDispatcher {

    private static final Logger LOG = LogManager.getFormatterLogger(DataUpdEventDispatcher.class);


    private static final class InstanceHolder {

        private static final DataUpdEventDispatcher INSTANCE = new DataUpdEventDispatcher();
    }


    private static final int EVENT_QUEUE_SIZE;
    private static final long EVENT_QUEUE_PUT_TIMEOUT_MS;
    private static final boolean EVENT_QUEUE_HANDLER_WAIT;


    static {
        final Resources res = Resources.getInstance();
        EVENT_QUEUE_SIZE = Integer.parseInt(res.getConfigProp(ConfigID.CORE_UIDATA_EVENT_QUEUE_SIZE));
        EVENT_QUEUE_PUT_TIMEOUT_MS = Long.parseLong(res.getConfigProp(ConfigID.CORE_UIDATA_EVENT_QUEUE_PUT_TIMEOUT_MS));
        EVENT_QUEUE_HANDLER_WAIT = Boolean.parseBoolean(res.getConfigProp(
            ConfigID.CORE_UIDATA_EVENT_QUEUE_HANDLER_WAIT));
    }


    private final ConcurrentMap<DataUpdEventType, ConcurrentMap<Integer, DataUpdEventHandler>> handlers =
        new ConcurrentHashMap<>();
    private final BlockingQueue<DataUpdEvent> eventQueue = new ArrayBlockingQueue<>(EVENT_QUEUE_SIZE, true);
    private final Executor handlerThreadPool;
    private final Executor eventPollThreadPool = Executors.newSingleThreadExecutor();


    public static final DataUpdEventDispatcher getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private DataUpdEventDispatcher() {
        Arrays.stream(DataUpdEventType.values()).parallel().forEach(event -> {
            final ConcurrentMap<Integer, DataUpdEventHandler> newSet = new ConcurrentHashMap<>();
            final ConcurrentMap<Integer, DataUpdEventHandler> prevSet = handlers.putIfAbsent(event, newSet);
            assert(prevSet == null);
        });
        final Resources res = Resources.getInstance();
        final int handlersPoolSize = Integer.parseInt(res.getConfigProp(
            ConfigID.CORE_UIDATA_EVENT_QUEUE_HANDLER_THREAD_POOL_SIZE));
        handlerThreadPool = Executors.newFixedThreadPool(handlersPoolSize);
        startEventPollingThread();
    }

    private final void startEventPollingThread() {
        eventPollThreadPool.execute(() -> {
            while (true) {
                try {
                    final DataUpdEvent event = eventQueue.take(); // blocks until is available
                    notifyRegisteredHanlders(event);
                } catch (final InterruptedException e) {
                    LOG.catching(Level.TRACE, e);
                    LOG.error("unexpected ");
                }
            }
        });
    }

    public final void signal(final DataUpdEvent event) {
        LOG.trace("event signalled : event = [%s]", event);
        try {
            boolean wasPut;
            int tryNum = 1;
            do {
                wasPut = eventQueue.offer(event, EVENT_QUEUE_PUT_TIMEOUT_MS, TimeUnit.MILLISECONDS);
                if (!wasPut) {
                    LOG.warn("timed out putting event in queue : event = [%s] ; putTimeout = [%d] ; try = [%d]"
                        + " ; queueSize = [%d]", event, EVENT_QUEUE_PUT_TIMEOUT_MS, tryNum, eventQueue.size());
                    tryNum++;
                }
            } while (!wasPut);
        } catch (final InterruptedException e) {
            LOG.catching(Level.TRACE, e);
            LOG.error("enexpected interruption received", e);
        }
    }

    private final void notifyRegisteredHanlders(final DataUpdEvent event) {
        final ConcurrentMap<Integer, DataUpdEventHandler> handlersForType = handlers.get(event.getType());
        assert(handlersForType != null);
        if (!EVENT_QUEUE_HANDLER_WAIT) {
            handlersForType.values().parallelStream().forEach(handler -> {
                handlerThreadPool.execute(() -> {
                    handler.handle(event);
                });
            });
        } else {
            // TODO : implement waiting strategy (CyclicBarrier)
            throw new AssertionError("not implemented yet");
        }
    }

    public final void register(final DataUpdEventType eventType, final DataUpdEventHandler handler) {
        // assert(handler instanceof Comparable<?>);
        LOG.entry(eventType, handler);
        try {
            final ConcurrentMap<Integer, DataUpdEventHandler> handlersForType = handlers.get(eventType);
            LOG.trace("found handlers for eventType : eventType = [%s] ; numHandlers = [%d]",
                eventType, handlersForType.size());
            assert(handlersForType != null);
            if (handlersForType.put(handler.hashCode(), handler) != null) {
                LOG.warn("repeated registration : eventType = [%s] ; handler = [%s]", eventType, handler);
            } else {
                LOG.debug("handler was added to set : eventType = [%s] ; newSize = [%s] ; handler = [%s]",
                    eventType, handlersForType.size(), handler);
            }
        } catch (final Throwable e) {
            LOG.fatal("caught e : ", e);
        }
        LOG.exit();
    }

    public final boolean unregister(final DataUpdEventType eventType, final DataUpdEventHandler handler) {
        LOG.entry(eventType, handler);
        final ConcurrentMap<Integer, DataUpdEventHandler> handlersForType = handlers.get(eventType);
        assert(handlersForType != null);
        final boolean wasRemoved = handlersForType.remove(handler.hashCode(), handler);
        if (!wasRemoved) {
            LOG.warn("requested unregister of not-registered handler : eventType = [%s] ; handler = [%s]", eventType,
                handler);
        }
        return LOG.exit(wasRemoved);
    }

    public final int unregister(final DataUpdEventHandler handler) {
        LOG.entry(handler);
        assert(handler != null);
        final AtomicInteger resCount = new AtomicInteger(0);
        handlers.values().parallelStream().forEach((handlersForType) -> {
            handlersForType.values().parallelStream().forEach((handlerInSet) -> {
                if (handlerInSet.equals(handler)) {
                    handlersForType.remove(handlerInSet);
                    resCount.incrementAndGet();
                }
            });
        });
        return LOG.exit(resCount.get());
    }

}
