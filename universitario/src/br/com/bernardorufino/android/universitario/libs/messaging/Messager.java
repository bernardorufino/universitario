package br.com.bernardorufino.android.universitario.libs.messaging;

public interface Messager<T> {

    public void showMessage(T message);

    public void hideMessage();
}
