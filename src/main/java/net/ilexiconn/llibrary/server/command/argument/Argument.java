package net.ilexiconn.llibrary.server.command.argument;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public class Argument {
    private String name;
    private String value;
    private IArgumentParser<?> argumentParser;

    public Argument(String name, String value, IArgumentParser<?> argumentParser) {
        this.name = name;
        this.value = value;
        this.argumentParser = argumentParser;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public IArgumentParser<?> getArgumentParser() {
        return argumentParser;
    }
}
