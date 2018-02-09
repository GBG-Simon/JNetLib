package jnet.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

public class ReflectionUtils
{	
	public static void getMemberFields(List<Field> outFields,
									   Class<?> inClass,
									   boolean bSetAccessible,
									   Predicate<? super Field> filter) throws ReflectionException
	{
		getMemberFieldsInternal(outFields, inClass, false, bSetAccessible, filter);
	}
	
	public static List<Field> getMemberFields(Class<?> inClass,
											  boolean bSetAccessible,
											  Predicate<? super Field> filter) throws ReflectionException
	{
		List<Field> result = new LinkedList<>();
		getMemberFields(result, inClass, bSetAccessible, filter);
		
		return result;
	}
	
	public static void getMemberFieldsInherited(List<Field> outFields,
			   									Class<?> inClass,
			   									boolean bSetAccessible,
			   									Predicate<? super Field> filter) throws ReflectionException
	{
		getMemberFieldsInternal(outFields, inClass, true, bSetAccessible, filter);
	}
	
	public static List<Field> getMemberFieldsInherited(Class<?> inClass,
													   boolean bSetAccessible,
													   Predicate<? super Field> filter) throws ReflectionException
	{
		List<Field> result = new LinkedList<>();
		getMemberFieldsInherited(result, inClass, bSetAccessible, filter);
		
		return result;
	}

	private static void getMemberFieldsInternal(List<Field> outFields,
												Class<?> inClass,
												boolean bIncludeInherited,
												boolean bSetAccessible,
												Predicate<? super Field> filter) throws ReflectionException
	{
		if (outFields == null)
		{
			throw new ReflectionException("Invalid outgoing storage");
		}
		
		Class<?> clazz = inClass;
		
		if (bIncludeInherited)
		{
			while (clazz != null)
			{
				outFields.addAll(Arrays.asList(clazz.getDeclaredFields()));			
				clazz = clazz.getSuperclass();
			}
		}
		
		else
		{
			outFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		}
		
		outFields.removeIf(filter);
		
		if (bSetAccessible)
		{
			outFields.stream()
						.filter(f -> Modifier.isPrivate(f.getModifiers())/* || Modifier.isProtected(f.getModifiers()) */)
							.forEach(f -> f.setAccessible(true));
		}
	}
	
	public static void safeGetMemberFields(List<Field> outFields,
										   Class<?> inClass,
										   boolean bIncludeInherited,
										   boolean bSetAccessible,
										   Predicate<? super Field> filter)
	{
		if (outFields == null)
		{
			return;
		}

		Class<?> clazz = inClass;

		if (bIncludeInherited)
		{
			while (clazz != null)
			{
				outFields.addAll(Arrays.asList(clazz.getDeclaredFields()));			
				clazz = clazz.getSuperclass();
			}
		}

		else
		{
			outFields.addAll(Arrays.asList(clazz.getDeclaredFields()));
		}

		outFields.removeIf(filter);

		if (bSetAccessible)
		{
			outFields.stream()
						.filter(f -> Modifier.isPrivate(f.getModifiers())/* || Modifier.isProtected(f.getModifiers()) */)
							.forEach(f -> f.setAccessible(true));
		}
	}
	
	public static void getMemberMethods(List<Method> outMethods,
										Class<?> inClass,
										boolean bSetAccessible,
										Predicate<? super Method> filter) throws ReflectionException
	{
		getMemberMethodsInternal(outMethods, inClass, false, bSetAccessible, filter);
	}

	public static List<Method> getMemberMethods(Class<?> inClass,
												boolean bSetAccessible,
												Predicate<? super Method> filter) throws ReflectionException
	{
		List<Method> result = new LinkedList<>();
		getMemberMethods(result, inClass, bSetAccessible, filter);

		return result;
	}

	public static void getMemberMethodsInherited(List<Method> outMethods,
												 Class<?> inClass,
												 boolean bSetAccessible,
												 Predicate<? super Method> filter) throws ReflectionException
	{
		getMemberMethodsInternal(outMethods, inClass, true, bSetAccessible, filter);
	}

	public static List<Method> getMemberMethodsInherited(Class<?> inClass,
														 boolean bSetAccessible,
														 Predicate<? super Method> filter) throws ReflectionException
	{
		List<Method> result = new LinkedList<>();
		getMemberMethodsInherited(result, inClass, bSetAccessible, filter);

		return result;
	}
	
	public static Method getMemberMethod(Class<?> inClass,
										 String name,
										 boolean bIncludeInherited,
										 boolean bSetAccessible,
										 Predicate<? super Method> filter,
										 Class<?>...parameterTypes) throws ReflectionException
	{
		Class<?> clazz = inClass;
		Method found = null;
		
		if (bIncludeInherited)
		{
			while (clazz != null)
			{
				try
				{
					found = clazz.getDeclaredMethod(name, parameterTypes);
					if (found != null)
					{
						if (filter.test(found))
						{
							break;
						}
					}
				}

				catch (NoSuchMethodException e1)
				{
					;
				}
				catch (Exception e2)
				{
					throw new ReflectionException(e2);
				}
				
				clazz = clazz.getSuperclass();
			}
		}
		
		else
		{
			try
			{
				found = clazz.getDeclaredMethod(name, parameterTypes);
				if (found != null && !filter.test(found))
				{
					found = null;
				}
			}

			catch (NoSuchMethodException e1)
			{
				;
			}
			catch (Exception e2)
			{
				throw new ReflectionException(e2);
			}
		}
		
		if (found != null)
		{
			if (bSetAccessible)
			{
				found.setAccessible(true);
			}
		}
			
		return found;
	}
	
	public static void getMemberMethodsInternal(List<Method> outMethods,
												Class<?> inClass,
												boolean bIncludeInherited,
												boolean bSetAccessible,
												Predicate<? super Method> filter) throws ReflectionException
	{
		if (outMethods == null)
		{
			throw new ReflectionException("Invalid outgoing storage");
		}
		
		Class<?> clazz = inClass;
		
		if (bIncludeInherited)
		{
			while (clazz != null)
			{
				outMethods.addAll(Arrays.asList(clazz.getDeclaredMethods()));			
				clazz = clazz.getSuperclass();
			}
		}

		else
		{
			outMethods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
		}

		outMethods.removeIf(filter);

		if (bSetAccessible)
		{
			outMethods.stream()
						.filter(f -> Modifier.isPrivate(f.getModifiers())/* || Modifier.isProtected(f.getModifiers()) */)
							.forEach(f -> f.setAccessible(true));
		}
	}
	
}
