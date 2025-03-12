package com.oneblog.auth.service;

@FunctionalInterface
public interface CodeGenerator {

	String generateSixDigits();

}
