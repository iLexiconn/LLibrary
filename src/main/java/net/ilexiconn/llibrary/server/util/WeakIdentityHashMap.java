package net.ilexiconn.llibrary.server.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * Generic implementation of com.sun.jna.WeakIdentityHashMap
 *
 * @author pau101
 */
public class WeakIdentityHashMap<K, V> implements Map<K, V> {
    private final ReferenceQueue<K> queue = new ReferenceQueue();

    private final Map<IdentityWeakReference, V> delegate;

    public WeakIdentityHashMap(int expectedMaxSize, float loadFactor) {
        delegate = new HashMap<>(expectedMaxSize, loadFactor);
    }

    public WeakIdentityHashMap(int expectedMaxSize) {
        delegate = new HashMap<>(expectedMaxSize);
    }

    public WeakIdentityHashMap() {
        delegate = new HashMap<>();
    }

    @Override
    public int size() {
        reap();
        return delegate.size();
    }

    @Override
    public boolean isEmpty() {
        reap();
        return delegate.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        reap();
        return delegate.containsKey(new IdentityWeakReference(key));
    }

    @Override
    public boolean containsValue(Object value) {
        reap();
        return delegate.containsValue(value);
    }

    @Override
    public V get(Object key) {
        reap();
        return delegate.get(new IdentityWeakReference(key));
    }

    @Override
    public V put(K key, V value) {
        return delegate.put(new IdentityWeakReference(key), value);
    }

    @Override
    public V remove(Object key) {
        reap();
        return delegate.remove(new IdentityWeakReference(key));
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public void clear() {
        delegate.clear();
        reap();
    }

    @Override
    public Set<K> keySet() {
        reap();
        Set ret = new HashSet();
        for (Iterator i = delegate.keySet().iterator(); i.hasNext();) {
            IdentityWeakReference ref = (IdentityWeakReference) i.next();
            ret.add(ref.get());
        }
        return Collections.unmodifiableSet(ret);
    }

    @Override
    public Collection<V> values() {
        reap();
        return delegate.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        reap();
        Set<Entry<K, V>> ret = new HashSet();
        for (Iterator<Entry<IdentityWeakReference, V>> i = delegate.entrySet().iterator(); i.hasNext();) {
            Entry<IdentityWeakReference, V> ref = i.next();
            final K key = (K) ref.getKey().get();
            final V value = ref.getValue();
            Entry<K, V> entry = new Entry<K, V>() {
                @Override
                public K getKey() {
                    return key;
                }

                @Override
                public V getValue() {
                    return value;
                }

                @Override
                public V setValue(V value) {
                    throw new UnsupportedOperationException();
                }
            };
            ret.add(entry);
        }
        return Collections.unmodifiableSet(ret);
    }

    private synchronized void reap() {
        Reference<? extends K> zombie = queue.poll();
        while (zombie != null) {
            IdentityWeakReference victim = (IdentityWeakReference) zombie;
            delegate.remove(victim);
            zombie = queue.poll();
        }
    }

    private class IdentityWeakReference extends WeakReference {
        private final int hash;

        public IdentityWeakReference(Object obj) {
            super(obj, queue);
            hash = System.identityHashCode(obj);
        }

        @Override
        public int hashCode() {
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || ((IdentityWeakReference) obj).get() == get();
        }
    }
}