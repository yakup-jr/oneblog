package net.oneblog.api.interfaces;

/**
 * The enum Label name.
 */
public enum LabelName {
    /**
     * The Programming languages.
     */
    Programming_languages("Programming Languages"),
    /**
     * Java label name.
     */
    Java("Java"),
    /**
     * C hash label name.
     */
    CHash("C#"),
    /**
     * C label name.
     */
    C("C"),
    /**
     * C plus plus label name.
     */
    CPlusPlus("C++"),
    /**
     * Python label name.
     */
    Python("Python"),
    /**
     * Java script label name.
     */
    JavaScript("JavaScript"),
    /**
     * Type script label name.
     */
    TypeScript("TypeScript"),
    /**
     * Sql label name.
     */
    Sql("SQL"),
    /**
     * Go label name.
     */
    Go("GO"),
    /**
     * Kotlin label name.
     */
    Kotlin("Kotlin"),
    /**
     * Rust label name.
     */
    Rust("Rust"),
    /**
     * Php label name.
     */
    Php("PHP"),
    /**
     * Pascal label name.
     */
    Pascal("Pascal"),
    /**
     * Ruby label name.
     */
    Ruby("Ruby"),
    /**
     * Basic label name.
     */
    Basic("Basic"),
    /**
     * Assembler label name.
     */
    Assembler("Assembler");

    private final String value;

    LabelName(String value) {
        this.value = value;
    }

    public String toString() {
        return this.value;
    }
}