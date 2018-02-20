package jnet.serialization;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SerializationManager
{
	public static Class<?> getSerializerClass(Class<?> type)
	{
		return serializerMap.get()
	}
	
	public static Object newSerializer(Class<?> serializedType)
	{
		
	}
	
	public static void registerSerializerClass(String typeId, Class<?> serializerClass)
	{
		
	}
	
	public static void registerSerializerClass(Class<?> type, Class<?> serializerClass)
	{
		
	}
	
	public static boolean serializerExists(String typeId)
	{
		
	}
	
	public static boolean serializerExists(Class<?> type)
	{
		
	}
	
	public static void unregisterSerializerClass(String typeId)
	{
		
	}
	
	public static void unregisterSerializerClass(Class<?> type)
	{
		
	}
	
	private static Map<String, Class<?>> serializerMap;
	
	static
	{
		serializerMap = new ConcurrentHashMap<>();
	}
	
}
