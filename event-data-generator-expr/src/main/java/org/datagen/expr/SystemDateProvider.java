package org.datagen.expr;

import java.util.Date;

public class SystemDateProvider implements DateProvider {

	public SystemDateProvider() {
	}

	@Override
	public Date getDate() {
		return new Date();
	}
}
