package br.com.bernardorufino.android.universitario.model.base;

import br.com.bernardorufino.android.universitario.ext.observing.AbstractObservable;

public abstract class AbstractModelProvider extends AbstractObservable {

    /* For visibility issues */
    @Override
    protected void notifyObservers() {
        super.notifyObservers();
    }
}