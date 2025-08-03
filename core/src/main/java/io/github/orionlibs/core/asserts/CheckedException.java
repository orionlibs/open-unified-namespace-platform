package io.github.orionlibs.core.asserts;

public class CheckedException extends Exception
{
    private static final String DefaultErrorMessage = "There was an error.";


    public CheckedException(String errorMessage)
    {
        super(errorMessage);
    }


    public CheckedException(String errorMessage, Object... arguments)
    {
        super(String.format(errorMessage, arguments));
    }


    public CheckedException(Throwable cause, String errorMessage, Object... arguments)
    {
        super(String.format(errorMessage, arguments), cause);
    }


    public CheckedException(Throwable cause)
    {
        super(DefaultErrorMessage, cause);
    }
}