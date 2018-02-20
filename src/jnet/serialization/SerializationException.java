package jnet.serialization;

import java.io.IOException;

public class SerializationException extends IOException
{
	private static final long serialVersionUID = -2015171314221484641L;
	
	public SerializationException()
	{
		super();
	}
	
	public SerializationException(String message)
	{
		super(message);
	}
	
	public SerializationException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public SerializationException(Throwable cause)
	{
		super(cause);
	}	
}
