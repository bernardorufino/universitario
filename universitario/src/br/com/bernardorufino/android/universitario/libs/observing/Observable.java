package br.com.bernardorufino.android.universitario.libs.observing;

public interface Observable {

    public void registerObserver(Observer observer);

    public void unregisterObserver(Observer observer);
}
