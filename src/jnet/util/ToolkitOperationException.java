package jnet.util;

public class ToolkitOperationException extends Exception
{
	private static final long serialVersionUID = 3418391197169156882L;
	
	public ToolkitOperationException()
	{
		super();
	}
	
	public ToolkitOperationException(String message)
	{
		super(message);
	}
	
	public ToolkitOperationException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public ToolkitOperationException(Throwable cause)
	{
		super(cause);
	}	
}
