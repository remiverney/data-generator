package org.datagen.utils;

import java.util.HashSet;
import java.util.Set;

public abstract class ObservableBase<O extends Observable<O, E>, E> implements
		Observable<O, E> {

	private final Set<Observer<O, E>> observers = new HashSet<>();

	protected ObservableBase() {
	}

	@Override
	public void addObserver(Observer<O, E> observer) {
		this.observers.add(observer);
	}

	@Override
	public void removeObserver(Observer<O, E> observer) {
		this.observers.remove(observer);
	}

	@Override
	public boolean isObserved() {
		return !this.observers.isEmpty();
	}

	protected void removeAllObservers() {
		this.observers.clear();
	}

	protected void notify(E event) {
		for (Observer<O, E> observer : this.observers) {
			observer.notify(this, event);
		}
	}

}
