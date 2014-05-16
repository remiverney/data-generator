package org.datagen.utils;

public interface Observer<O extends Observable<O, E>, E> {

	void notify(Observable<O, E> observable, E event);
}
