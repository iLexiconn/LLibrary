package net.ilexiconn.llibrary.server.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashMap;
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
        return delegate.containsKey(new IdentityWeakReference((K) key));
    }

    @Override
    public boolean containsValue(Object value) {
        reap();
        return delegate.containsValue(value);
    }

    @Override
    public V get(Object key) {
        reap();
        return delegate.get(new IdentityWeakReference((K) key));
    }

    @Override
    public V put(K key, V value) {
        return delegate.put(new IdentityWeakReference(key), value);
    }

    @Override
    public V remove(Object key) {
        reap();
        return delegate.remove(new IdentityWeakReference((K) key));
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
        return new AbstractSet<K>() {
            @Override
            public int size() {
                reap();
                return delegate.size();
            }

            @Override
            public Iterator<K> iterator() {
                reap();
                Iterator<IdentityWeakReference> iter = delegate.keySet().iterator();
                return new Iterator<K>() {
                    @Override
                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    @Override
                    public K next() {
                        return iter.next().get();
                    }

                    @Override
                    public void remove() {
                        iter.remove();
                    }
                };
            }

            @Override
            public void clear() {
                delegate.clear();
                reap();
            }
        };
    }

    @Override
    public Collection<V> values() {
        return new AbstractCollection<V>() {
            @Override
            public int size() {
                reap();
                return delegate.size();
            }

            @Override
            public boolean contains(Object o) {
                reap();
                return containsValue(o);
            }

            @Override
            public Iterator<V> iterator() {
                reap();
                return delegate.values().iterator();
            }

            @Override
            public void clear() {
                delegate.clear();
                reap();
            }
        };
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        reap();
        return new AbstractSet<Entry<K, V>>() {
            @Override
            public int size() {
                reap();
                return delegate.size();
            }

            @Override
            public Iterator<Entry<K, V>> iterator() {
                reap();
                Iterator<Entry<IdentityWeakReference, V>> iter = delegate.entrySet().iterator();
                return new Iterator<Entry<K, V>>() {
                    @Override
                    public boolean hasNext() {
                        return iter.hasNext();
                    }

                    @Override
                    public Entry<K, V> next() {
                        Entry<IdentityWeakReference, V> entry = iter.next();
                        return new Entry<K, V>() {
                            @Override
                            public K getKey() {
                                return entry.getKey().get();
                            }

                            @Override
                            public V getValue() {
                                return entry.getValue();
                            }

                            @Override
                            public V setValue(V value) {
                                return delegate.put(entry.getKey(), value);
                            }
                        };
                    }

                    @Override
                    public void remove() {
                        iter.remove();
                    }
                };
            }

            @Override
            public void clear() {
                delegate.clear();
                reap();
            }
        };
    }

    @Override
    public String toString() {
        reap();
        return delegate.toString();
    }

    private synchronized void reap() {
        Reference<? extends K> zombie = queue.poll();
        while (zombie != null) {
            IdentityWeakReference victim = (IdentityWeakReference) zombie;
            delegate.remove(victim);
            zombie = queue.poll();
        }
    }

    private class IdentityWeakReference extends WeakReference<K> {
        private final int hash;

        public IdentityWeakReference(K obj) {
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

        @Override
        public String toString() {
            return String.valueOf(get());
        }
    }
}
