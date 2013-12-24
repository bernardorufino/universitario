package br.com.bernardorufino.android.universitario.ext.observing;

public interface Observable {

    public void registerObserver(Observer observer);

    public void unregisterObserver(Observer observer);
}
