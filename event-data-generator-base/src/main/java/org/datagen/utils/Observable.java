package org.datagen.utils;

import javax.annotation.Nonnull;

public interface Observable<O extends Observable<O, E>, E> {

	void addObserver(@Nonnull Observer<O, E> observer);

	void removeObserver(@Nonnull Observer<O, E> observer);

	boolean isObserved();
}
