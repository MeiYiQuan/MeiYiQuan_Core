package com.salon.backstage.core;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

/**
 * 功能：所有持久化对象都要继承这个类
 *
 *         mobile enterprise application platform Version 0.1
 */
@MappedSuperclass
public abstract class PO {
	public abstract Serializable getId();
	
}
