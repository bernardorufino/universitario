package br.com.bernardorufino.android.universitario.ext.messaging;

public interface Messager<T> {

    public void showMessage(T message);

    public void hideMessage();
}
