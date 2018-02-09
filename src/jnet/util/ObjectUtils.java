package jnet.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectUtils
{
	public static byte[] convertToBytes(Object obj) throws IOException
	{
		try (ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
				ObjectOutputStream outObj = new ObjectOutputStream(outBytes))
		{
			outObj.writeObject(obj);
			outObj.flush();

			return outBytes.toByteArray();
		}

		catch (IOException e)
		{
			throw e;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T convertFromBytes(byte[] buf) throws IOException, ClassNotFoundException
	{
		try (ObjectInputStream inObj = new ObjectInputStream(new ByteArrayInputStream(buf)))
		{
			return (T) inObj.readObject();
		}
		
		catch (Exception e)
		{
			throw e;
		}
	}	
}
