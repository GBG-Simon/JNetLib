package jnet.util;

public interface IUndoToolkit
{
	void undo() throws ToolkitOperationException;
	void redo() throws ToolkitOperationException;
	void rewind(int count) throws ToolkitOperationException;
}
