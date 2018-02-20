package jnet.serialization.serializer;

import io.netty.buffer.ByteBuf;
import jnet.serialization.SerializationOptions;
import jnet.serialization.SerializationToolkit;

public class PrimitiveTypeSerializers
{
	@TypeSerializer(typeId = "byte")
	public static class ByteSerializer
	{
		@Serializer
		public void serialize(byte data, SerializationOptions options, SerializationToolkit toolkit)
		{
			toolkit.writeByte(data);
		}
		
		@Deserializer
		public byte deserialize(DataStorage storage, SerializationOptions options, DeserializationToolkit toolkit)
		{
			storage.store(toolkit.readByte());
		}
	}
	
	@TypeSerializer(typeId = "bool|boolean")
	public static class BooleanSerializer
	{
		
	}
	
	@TypeSerializer(typeId = "char|character")
	public static class CharSerializer
	{
		
	}
	
	
	
}
