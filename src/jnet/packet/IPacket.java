package jnet.packet;

import io.netty.buffer.ByteBuf;
import jnet.packet.exception.PacketSerializationException;
import jnet.serialization.ByteSerializable;

public interface IPacket<T extends ByteSerializable>
{	
	public void serialize(ByteBuf buf) throws PacketSerializationException;
	public void deserialize(ByteBuf buf) throws PacketSerializationException;
	
	public PacketHeader getHeader();
	public T getContent();
}
