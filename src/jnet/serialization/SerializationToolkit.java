package jnet.serialization;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import io.netty.buffer.ByteBuf;
import jnet.serialization.serializer.Serializer;
import jnet.util.IUndoToolkit;
import jnet.util.Operation;
import jnet.util.ToolkitOperationException;

public class SerializationToolkit implements IUndoToolkit
{
	public SerializationToolkit(ByteBuf buffer)
	{		
		this(buffer, SerializationOptions.empty());
	}
	
	public SerializationToolkit(ByteBuf buffer, SerializationOptions options)
	{
		if (buffer == null)
		{
			throw new IllegalArgumentException("Failed to initialize SerializationToolkit: buffer should not be null!");
		}
		
		this.buffer = buffer;
		this.options = options;
		this.doStack = new LinkedList<>();
		this.undoStack = new LinkedList<>();
	}
	
	/**
	 * Defers serialization of the incoming object to another serializer
	 * @param obj
	 */
	public void writeObject(Object obj)
	{
		Class<?> serializerClass = SerializationManager.get(obj);
		Object serializer =  serializerClass.createInstance();
		Method serializerMethod = Arrays.stream(serializerClass.getMethods()).filter(m -> m.isAnnotationPresent(Serializer.class)).findFirst().get();
		
		if (serializerMethod == null)
		{
			// throw new SerializationException();
		}
		
		else
		{
			serializerMethod.invoke(serializer, obj, this);
		}
	}
	
	public int writableBytes()
	{
		return buffer.writableBytes();
	}
	
	public void writeByte(int data)
	{
		newWriteOperation();
		
		buffer.writeByte(data);
	}
	
	public void writeBytes(byte[] data)
	{
		newWriteOperation();
		
		buffer.writeBytes(data);
	}
	
	public void writeBytes(byte[] data, int length)
	{
		newWriteOperation();
		
		buffer.writeBytes(data, 0, length);
	}
	
	public void writeBytes(byte[] data, int offset, int length)
	{
		newWriteOperation();
		
		buffer.writeBytes(data, offset, length);
	}
	
	public void writeBoolean(boolean data)
	{
		newWriteOperation();
		
		buffer.writeBoolean(data);
	}
	
	public void writeChar(int data)
	{
		newWriteOperation();
		
		buffer.writeChar(data);
	}
	
	public void writeShort(int data)
	{
		newWriteOperation();
		
		buffer.writeShort(data);
	}
	
	public void writeShortLE(int data)
	{
		newWriteOperation();
		
		buffer.writeShortLE(data);
	}
	
	public void writeMedium(int data)
	{
		newWriteOperation();
		
		buffer.writeMedium(data);
	}
	
	public void writeMediumLE(int data)
	{
		newWriteOperation();
		
		buffer.writeMediumLE(data);
	}
	
	public void writeInt(int data)
	{
		newWriteOperation();
		
		buffer.writeInt(data);
	}
	
	public void writeIntLE(int data)
	{
		newWriteOperation();
		
		buffer.writeIntLE(data);
	}
	
	public void writeNull(int length)
	{
		newWriteOperation();
		
		buffer.writeZero(length);
	}
	
	public void writeLong(long data)
	{
		newWriteOperation();
		
		buffer.writeLong(data);
	}
	
	public void writeLongLE(long data)
	{
		newWriteOperation();
		
		buffer.writeLongLE(data);
	}
	
	public void writeFloat(float data)
	{
		newWriteOperation();
		
		buffer.writeFloat(data);
	}
	
	public void writeFloatLE(float data)
	{
		newWriteOperation();
		
		buffer.writeFloatLE(data);
	}
	
	public void wirteDouble(double data)
	{
		newWriteOperation();
		
		buffer.writeDouble(data);
	}
	
	public void wirteDoubleLE(double data)
	{
		newWriteOperation();
		
		buffer.writeDoubleLE(data);
	}
	
	public void writeCharSequence(CharSequence data)
	{
		writeCharSequence(data, Charset.defaultCharset());
	}
	
	public void writeCharSequence(CharSequence data, Charset charset)
	{
		newWriteOperation();
		
		buffer.writeCharSequence(data, charset);
	}
	
	public SerializationOptions options()
	{
		return options;
	}
	
	private void newWriteOperation()
	{
		if (!undoStack.isEmpty())
		{
			undoStack.clear();
		}
		
		doStack.push(new SerializationOperation(OperationType.WRITE_DATA, buffer.writerIndex()));
	}
	
	private void newRewindOperation()
	{
		undoStack.push(new SerializationOperation(OperationType.REWIND_BUFFER, buffer.writerIndex()));
	}
	
	private SerializationOptions options;
	private ByteBuf buffer;
	private Deque<SerializationOperation> doStack;
	private Deque<SerializationOperation> undoStack;

	@Override
	public void undo() throws ToolkitOperationException
	{
		if (doStack.isEmpty())
		{
			throw new ToolkitOperationException("No previous task exists!");
		}
		
		newRewindOperation();
		
		SerializationOperation op = doStack.pop();
		op.undo(buffer);
	}

	@Override
	public void redo() throws ToolkitOperationException
	{
		if (undoStack.isEmpty())
		{
			throw new ToolkitOperationException("No previous undo task exists!");
		}
		
		SerializationOperation op = undoStack.pop();
		op.undo(buffer);
	}

	@Override
	public void rewind(int count) throws ToolkitOperationException
	{
		for (int i=0; i < count; i++)
		{
			redo();
		}
	}
	
	private static enum OperationType
	{
		WRITE_DATA,
		REWIND_BUFFER
	}
	
	private static class SerializationOperation extends Operation<OperationType, ByteBuf>
	{		
		public SerializationOperation(OperationType type, int index)
		{
			super(type);

			this.prevIndex = index;
		}

		@Override
		public void undo(ByteBuf buf)
		{
			buf.writerIndex(prevIndex);
		}

		private int prevIndex;
	}
}
