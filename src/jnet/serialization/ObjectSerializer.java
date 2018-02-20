package jnet.serialization;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import io.netty.buffer.ByteBuf;
import javafx.util.Pair;
import jnet.packet.PacketHeader;
import jnet.util.ReflectionUtils;

public class ObjectSerializer
{
	public void serialize(Object obj) throws SerializationException
	{
		if (obj == null)
		{
			throw new SerializationException("The serialized object must not be null!");
		}
		
		Class<?> clazz = obj.getClass();
		Serializable serialData_object = clazz.getAnnotation(Serializable.class);
		if (serialData_object == null)
		{
			throw new SerializationException("The object is not serializable!");
		}
		
		SerializationParams params = SerializationParams.parse(serialData_object.value());
		
		List<Field> fields = ReflectionUtils.getMemberFieldsInherited(clazz, true, f -> Modifier.isTransient(f.getModifiers())); //getAllMemberFields(obj);
		for (Field field : fields)
		{
			SerializationParams params_field = null;
			boolean bSerialize = true;
			
			Serializable serialData_field = field.getAnnotation(Serializable.class);
			if (serialData_field != null)
			{
				params_field = SerializationParams.parse(serialData_field.value());
				
				if (params_field.contains("condition"))
				{
					String conditionCheckerName = params_field.getValueAs("condition");
					Method conditionChecker = ReflectionUtils.getMemberMethod(clazz, conditionCheckerName, true, true, m -> true, ObjectSerializer.class);
					
					bSerialize = conditionChecker != null? (boolean) conditionChecker.invoke(obj, this) : false;
				}
			}
			
			if (bSerialize)
			{				
				Class<?> fieldClass = field.getType();
				Method serializationMethod = null;
				
				if (params_field != null)
				{
					if (params_field.contains("method"))
					{
						String serializationMethodName = params_field.getValueAs("method");
						serializationMethod = ReflectionUtils.getMemberMethod(clazz, serializationMethodName, true, true, m -> true, ObjectSerializer.class);
					}
				}
				
				if (serializationMethod == null)
				{
					if (isInternalType(fieldClass))
					{
						serializeInternalType(obj, fieldClass, field, params_field);
					}
					
					else if (fieldClass.isPrimitive())
					{
						serializePrimitive(obj, field, params_field);
					}

					else if (fieldClass.isArray())
					{
						serializeArray();
					}

					else if (Collection.class.isAssignableFrom(fieldClass))
					{
						serializeCollection();
					}

					else
					{
						Serializable serialData_fieldInstance = fieldClass.getAnnotation(Serializable.class);
						if (serialData_fieldInstance == null)
						{
							continue;
						}
						
						new ObjectSerializer().serialize(field.get(obj));
					}
				}

				else
				{
					serializationMethod.invoke(obj, this);
				}
			}
		}
	}
	
	private void serializeInternalType(Object enclosingObject, Class<?> type, Field target, SerializationParams params)
	{
		
	}
	
	private void serializePrimitive(Object enclosingObject, Class<?> type, Field target, SerializationParams params)
	{
		Object value = target.get(enclosingObject);
		
		try (ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
			 DataOutputStream outData = new DataOutputStream(outBytes))
		{
			
		}
	}
	
	private void serializeArray()
	{
		
	}
	
	private void serializeCollection()
	{
		
	}
	
	private void serializeHeader(SerializationHeader header)
	{
		buffer.writeLong(header.toCompactData());
	}
	
	private boolean isInternalType(Class<?> inClass)
	{
		
	}
	
	public byte[] bytes()
	{
		return buffer.array();
	}
	
	public ByteBuf buffer()
	{
		return buffer;
	}
	
	public ByteBuffer nioBuffer()
	{
		return buffer.nioBuffer();
	}
	
	private Field currentTargetField;
	private Object currentTarget;
	private Stack<Object> serializedObjects;
	private ByteBuf buffer;
	
	private static enum ESerializationType
	{
		INVALID		(0),
		SINGLE		(1),
		ARRAY		(2),
		COLLECTION	(3);
		
		private ESerializationType(final int code)
		{
			this.code = code;
		}
		
		public int getCode()
		{
			return code;
		}
		
		private final int code;
	}
	
	private static class SerializationHeader
	{
		private static final int INVALID_VALUE = Integer.MIN_VALUE;
		
		public static final int ID_PRIMITIVE_VOID    = 0x00;
		public static final int ID_PRIMITIVE_BOOLEAN = 0x01;
		public static final int ID_PRIMITIVE_BYTE    = 0x02;
		public static final int ID_PRIMITIVE_CHAR 	 = 0x03;
		public static final int ID_PRIMITIVE_SHORT   = 0x04;
		public static final int ID_PRIMITIVE_INT     = 0x05;
		public static final int ID_PRIMITIVE_LONG    = 0x06;
		public static final int ID_PRIMITIVE_FLOAT   = 0x07;
		public static final int ID_PRIMITIVE_DOUBLE  = 0x08;
		
		public static final int ID_INTERNAL_STRING 	 = 0x09;
		public static final int ID_INTERNAL_		 = 0x0A;
		
		public static final int ID_COLLECTION_ARRAYLIST 	= 0x0B;
		public static final int ID_COLLECTION_LINKEDLIST 	= 0X0C;
		
		public static final int ID_COLLECTION_HASHSET	= 0x0D;
		
		public static final int ID_COLLECTION_HASHMAP	= 0x0E;
		
		public static final int ID_COLLECTION_ARRAYDEQUE	= 0x0F;
		
		public SerializationHeader()
		{
			this.serializationType = ESerializationType.INVALID;
			this.typeId = INVALID_VALUE;
			this.data0 = INVALID_VALUE;
			this.data1 = INVALID_VALUE;
		}
		
		public SerializationHeader(ESerializationType serializationType, int typeId, int data0, int data1)
		{
			this.serializationType = serializationType;
			this.typeId = typeId & Short.MAX_VALUE;
			this.data0 = data0 & Byte.MAX_VALUE;
			this.data1 = data1;
		}
		
		public ESerializationType getSerializationType()
		{
			return serializationType;
		}
		
		public int getTypeId()
		{
			return typeId;
		}
		
		public int getData0()
		{
			return data0;
		}
		
		public int getData1()
		{
			return data1;
		}
		
		public long toCompactData()
		{
			return serializationType.getCode() << 55 | typeId << 39 | data0 << 31 | data1;
		}
		
		public void fromCompactData(long compactData)
		{
			
		}
		
		public boolean isValid()
		{
			return this.serializationType != ESerializationType.INVALID &&
				   this.typeId != INVALID_VALUE &&
				   this.data0 != INVALID_VALUE &&
				   this.data1 != INVALID_VALUE;
		}
		
		/* Byte */
		private ESerializationType serializationType;
		
		/* Short */
		private int typeId;
		
		/* Byte */
		private int data0;
		
		/* Int */
		private int data1;
		
		private static final Map<Class<?>, Integer> typeIds = new ConcurrentHashMap<>();
		
		static
		{
			typeIds.put(Void.class, ID_PRIMITIVE_VOID);
			typeIds.put(Boolean.class, ID_PRIMITIVE_BOOLEAN);
			typeIds.put(Byte.class, ID_PRIMITIVE_BYTE);
			typeIds.put(Character.class, ID_PRIMITIVE_CHAR);
			typeIds.put(Short.class, ID_PRIMITIVE_SHORT);
			typeIds.put(Integer.class, ID_PRIMITIVE_INT);
			typeIds.put(Long.class, ID_PRIMITIVE_LONG);
			typeIds.put(Float.class, ID_PRIMITIVE_FLOAT);
			typeIds.put(Double.class, ID_PRIMITIVE_DOUBLE);
			
			typeIds.put(String.class, ID_INTERNAL_STRING);
		}
	}

}
