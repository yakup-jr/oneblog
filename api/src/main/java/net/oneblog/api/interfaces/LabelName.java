package net.oneblog.api.interfaces;

/**
 * The enum Label name.
 */
public enum LabelName {
    /**
     * The Programming languages.
     */
    PROGRAMMING_LANGUAGES("Programming Languages"),
    /**
     * Java label name.
     */
    JAVA("Java"),
    /**
     * C hash label name.
     */
    C_HASH("C#"),
    /**
     * C label name.
     */
    C("C"),
    /**
     * C plus plus label name.
     */
    C_PLUS_PLUS("C++"),
    /**
     * Python label name.
     */
    PYTHON("Python"),
    /**
     * Java script label name.
     */
    JAVA_SCRIPT("JavaScript"),
    /**
     * Type script label name.
     */
    TYPE_SCRIPT("TypeScript"),
    /**
     * Sql label name.
     */
    SQL("SQL"),
    /**
     * Go label name.
     */
    GO("GO"),
    /**
     * Kotlin label name.
     */
    KOTLIN("Kotlin"),
    /**
     * Rust label name.
     */
    RUST("Rust"),
    /**
     * Php label name.
     */
    PHP("PHP"),
    /**
     * Pascal label name.
     */
    PASCAL("Pascal"),
    /**
     * Ruby label name.
     */
    RUBY("Ruby"),
    /**
     * Basic label name.
     */
    BASIC("Basic"),
    /**
     * Assembler label name.
     */
    ASSEMBLER("Assembler");

    private final String value;

    LabelName(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}