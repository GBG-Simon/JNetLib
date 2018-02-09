package jnet.packet.exception;

import jnet.serialization.SerializationException;

public class PacketSerializationException extends SerializationException
{
	private static final long serialVersionUID = -2015171314221484641L;
	
	public PacketSerializationException()
	{
		super();
	}
	
	public PacketSerializationException(String message)
	{
		super(message);
	}
	
	public PacketSerializationException(String message, Throwable cause)
	{
		super(message, cause);
	}
	
	public PacketSerializationException(Throwable cause)
	{
		super(cause);
	}	
}
