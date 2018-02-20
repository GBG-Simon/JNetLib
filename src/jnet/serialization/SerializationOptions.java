package jnet.serialization;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public class SerializationOptions
{	
	public static SerializationOptions empty()
	{
		return EMPTY_INSTANCE;
	}
	
	private static final SerializationOptions EMPTY_INSTANCE;
	
	public static SerializationOptions from(SerializationParam[] rawParams)
	{
		SerializationOptions options = new SerializationOptions();
		
		for (SerializationParam param : rawParams)
		{
			String optionName = param.key().toLowerCase();
			OptionData data = registeredOptions.get(optionName);
			options.add(new SerializationOption(optionName, data == null? null : data.parse(param.value())));
		}
		
		return options;
	}
	
	private SerializationOptions()
	{
		options = new HashMap<>(registeredOptions.size());
	}
	
	private void add(SerializationOption option)
	{
		options.put(option.name(), option);
	}
	
	private void addAll(Iterator<SerializationOption> iter)
	{
		iter.forEachRemaining(op -> options.put(op.name(), op));
	}
	
	public boolean hasOption(String optionName)
	{
		return options.get(optionName.toLowerCase()) != null;
	}
	
	public SerializationOption getOption(String optionName)
	{
		return options.get(optionName.toLowerCase());
	}
	
	public Object getOptionValue(String optionName)
	{
		SerializationOption option = options.get(optionName.toLowerCase());
		if (option != null)
		{
			return option.value();
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getOptionValueAs(String optionName)
	{
		SerializationOption option = options.get(optionName.toLowerCase());
		if (option != null)
		{
			return (T) option.value();
		}
		
		return null;
	}
	
	private Map<String, SerializationOption> options;

	public static boolean registerNewOption(String optionName, Class<?> valueType, Function<String, Object> parser)
	{
		if (optionName == null || valueType == null || parser == null)
		{
			throw new IllegalArgumentException("Arguments of this method must not be null!");
		}
		
		optionName = optionName.toLowerCase();
		
		if (registeredOptions.containsKey(optionName))
		{
			return false;
		}
		
		registeredOptions.put(optionName, new OptionData(valueType, parser));
		return true;
	}
	
	public static boolean unregisterExistingOption(String optionName)
	{
		if (optionName == null)
		{
			throw new IllegalArgumentException("optionName must not be null!");
		}
		
		optionName = optionName.toLowerCase();
		
		if (registeredOptions.containsKey(optionName))
		{
			registeredOptions.remove(optionName);
			return true;
		}
		
		return false;
	}
	
	private static Map<String, OptionData> registeredOptions;
	
	static
	{
		registeredOptions = new ConcurrentHashMap<>();
		
		// @SerializationParam("condition", "isSerializable_{#fieldName}"): the name of the method in the class of the target object
		registerNewOption("condition", String.class, (s) -> s);
		// @SerializationParam("method", "serialize_{#fieldName}"): the name of the method in the class of the target object
		registerNewOption("method", String.class, (s) -> s);
		
		// @SerializationParam("serializer", "{#serializerId}")
		registerNewOption("serializer", String.class, (s) -> s);
		
		// @Unused: serialization process is all up to the individual serializer class; cannot limit the size of the data
		// @SerializationParam("size", "{#maximumSize}"): maximum size of the target object in bytes
		// registerNewOption("size", Integer.class, Integer::parseInt);
		
		// @SerializationParam("pack", "4")
		registerNewOption("pack", Integer.class, Integer::parseInt);
		
		// @SerializationParam("range", "[0, 13)")
		registerNewOption("range", ArraySerializationInfo.Range.class, ArraySerializationInfo.Range::parse);
		
		// @SerializationParam("charset", "UTF-8")
		registerNewOption("charset", String.class, (s) -> s);
		
		
		EMPTY_INSTANCE = new SerializationOptions();
		registeredOptions.forEach((k, v) -> EMPTY_INSTANCE.add(new SerializationOption(k, null)));
	}
	
	private static class OptionData
	{
		OptionData(Class<?> valueType, Function<String, Object> parser)
		{			
			this.valueType = valueType;
			this.parser = parser;
		}
		
		Class<?> valueType()
		{
			return valueType;
		}
		
		@SuppressWarnings("unchecked")
		<T> T parse(String str)
		{
			return (T) parser.apply(str);
		}
		
		private Class<?> valueType;
		private Function<String, Object> parser;
	}
}
