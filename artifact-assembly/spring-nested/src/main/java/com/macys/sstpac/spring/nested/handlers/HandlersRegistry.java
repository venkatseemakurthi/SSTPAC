package com.macys.sstpac.spring.nested.handlers;


public interface HandlersRegistry {
	
	Void registerByName(String name, Object handler);

	Void registerByAnnotation(Object handler);
}
