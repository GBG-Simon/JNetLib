package jnet.util;

public abstract class Operation<E, P>
{
	public Operation(E type)
	{
		this.type = type;
	}
	
	public abstract void undo(P param);
	
	public E getType()
	{
		return type;
	}
	
	private E type;
}
