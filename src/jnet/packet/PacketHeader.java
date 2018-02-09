package jnet.packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import jnet.serialization.ByteSerializable;
import jnet.serialization.Serializable;
import jnet.serialization.SerializationException;

@Serializable
public class PacketHeader implements ByteSerializable
{
	public static final long HEADER_LENGTH = Long.BYTES + Integer.BYTES + Integer.BYTES;
	
	public static final int NO_FLAG = 0x00;
	public static final int NULL_CONTENT = 0x01;
	
	public PacketHeader()
	{
		this(-1, -1, NO_FLAG);
	}
	
	public PacketHeader(int type, long contentLength, int flags)
	{
		this.type = type;
		this.packetLength = HEADER_LENGTH + contentLength;
		this.flags = flags;
	}
	
	public int getPacketType()
	{
		return type;
	}
	
	public long getPacketLength()
	{
		return packetLength;
	}
	
	public long getContentLength()
	{
		return packetLength - HEADER_LENGTH;
	}

	public boolean hasFlag(int flag)
	{
		return (flags & flag) == 1;
	}
	
	public void enableFlag(int flag)
	{
		flags |= flag;
	}

	public void disableFlag(int flag)
	{
		flags &= ~flag;
	}
	
	private int type;
	private long packetLength;
	private int flags;

	@Override
	public byte[] serialize() throws SerializationException
	{
		try (ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
			 ObjectOutputStream outObj = new ObjectOutputStream(outBytes))
		{
			outObj.writeInt(type);
			outObj.writeLong(packetLength);
			outObj.writeInt(flags);
			
			return outBytes.toByteArray();
		}
		
		catch (IOException e)
		{
			throw new SerializationException(e);
		}
	}

	@Override
	public void deserialize(byte[] buf) throws SerializationException {
		// TODO Auto-generated method stub
		
	}	
}
