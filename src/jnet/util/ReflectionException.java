package jnet.util;


public class ReflectionException extends Exception
{
	private static final long serialVersionUID = 8586650435540694237L;

	public ReflectionException()
	{
		super();
	}
	
	public ReflectionException(String message)
	{
		super(message);
	}
	
	public ReflectionException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public ReflectionException(Throwable cause)
	{
		super(cause);
	}	
}
