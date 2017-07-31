package com.salon.backstage.common.util;

import java.util.List;
import java.util.Random;

public class ObjectProvider<T> {
	private List<T> objects;

	public ObjectProvider() {
	}

	public ObjectProvider(List<T> objects) {
		this.objects = objects;
	}

	public List<T> getObjects() {
		return objects;
	}

	public void setObjects(List<T> objects) {
		this.objects = objects;
	}

	public T random() {
		return objects.get(new Random().nextInt(objects.size()));
	}
}
