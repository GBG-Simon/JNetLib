package jnet.serialization;

public interface ByteSerializable
{
	/**
	 * Serialize all the data of this object in a byte array
	 * @return serialized data
	 */
	public byte[] serialize() throws SerializationException;
	
	/**
	 * Deserialize this object from a byte array 
	 */
	public void deserialize(byte[] buf) throws SerializationException;
}
