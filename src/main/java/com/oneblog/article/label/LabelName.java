package com.oneblog.article.label;

public enum LabelName {
	Programming_languages("Programming Languages"),
	Java("Java"),
	CHash("C#"),
	C("C"),
	CPlusPlus("C++"),
	Python("Python"),
	JavaScript("JavaScript"),
	TypeScript("TypeScript"),
	Sql("SQL"),
	Go("GO"),
	Kotlin("Kotlin"),
	Rust("Rust"),
	Php("PHP"),
	Pascal("Pascal"),
	Ruby("Ruby"),
	Basic("Basic"),
	Assembler("Assembler");

	private final String value;

	LabelName(String value) {
		this.value = value;
	}

	public String toString() {
		return this.value;
	}
}
