package jnet.serialization;

public class SerializationOption
{
	SerializationOption(String name, Object value)
	{
		this.name = name;
		this.value = value;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof SerializationOption)
		{
			SerializationOption other = (SerializationOption) obj;
			
			return name.equals(other.name);
		}
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return name.hashCode();
	}
	
	@Override
	public String toString()
	{
		return String.format("%s=%s", name, value);
	}
	
	public String name()
	{
		return name;
	}
	
	public Object value()
	{
		return value;
	}
	
	private String name;
	private Object value;
}
