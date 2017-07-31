package com.salon.backstage.common.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class Configs {
	private static Config conf = ConfigFactory.load();

	public static Config get() {
		return conf;
	}
}
