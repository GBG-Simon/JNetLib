package jnet.serialization;

import java.util.Deque;

import io.netty.buffer.ByteBuf;

public class SNode
{
	/* public static final int NO_HEADER = 0x00;
	
	public SNode(int header)
	{
		this.header = header;
		this.bEditable = true;
	}
	
	public int getHeader()
	{
		return header;
	}
	
	public SNode addContent(Object data)
	{
		if (data == null)
		{
			throw new IllegalArgumentException("Data must not be null!");
		}
		
		if (!bEditable)
		{
			throw new SerializationException("");
		}
		
		if (data.getClass().isPrimitive())
		{
			contents.offer(data);
		}
		
		else
		{
			throw new IllegalArgumentException("The type of data must be a primitive type!");
		}
		
		return this;
	}
	
	public boolean isEditable()
	{
		return bEditable;
	}
	
	public void close()
	{
		bEditable = false;
	}
	
	public void writeTo(ByteBuf buf)
	{
		contents.forEach(o -> writeInternal(buf, o));
	}
	
	private void writeInternal(ByteBuf buf, Object data)
	{
		
	}
	
	private boolean bEditable;
	
	private int header;
	private Deque<Object> contents; */
}
