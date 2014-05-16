package org.datagen.utils;

public interface Observable<O extends Observable<O, E>, E> {

	void addObserver(Observer<O, E> observer);

	void removeObserver(Observer<O, E> observer);

	boolean isObserved();
}
