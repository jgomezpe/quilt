package funpl.semantic;

public interface FunValueInterpreter {
    Object get( String value );
    boolean valid( String value );
    String description();
    String regex();
}
