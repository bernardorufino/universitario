package br.com.bernardorufino.android.universitario.ext.loader;

import java.util.HashSet;
import java.util.Set;

public class ComposedDynamicView<K> {

    private final Set<K> mSubViewsCanceled = new HashSet<>();

    public void reset() {
        mSubViewsCanceled.clear();
    }

    public void cancelSubViewUpdate(K key) {
        mSubViewsCanceled.add(key);
    }

    public boolean shouldUpdateSubView(K key) {
        return !mSubViewsCanceled.contains(key);
    }
}

