package io.github.orionlibs.core.data;

import io.github.orionlibs.core.asserts.UncheckedException;

public class ResourceNotFoundException extends UncheckedException
{
    private static final String DefaultErrorMessage = "There was an error.";


    public ResourceNotFoundException(String errorMessage)
    {
        super(errorMessage);
    }


    public ResourceNotFoundException(String errorMessage, Object... arguments)
    {
        super(String.format(errorMessage, arguments));
    }


    public ResourceNotFoundException(Throwable cause, String errorMessage, Object... arguments)
    {
        super(String.format(errorMessage, arguments), cause);
    }


    public ResourceNotFoundException(Throwable cause)
    {
        super(DefaultErrorMessage, cause);
    }
}