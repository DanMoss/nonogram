package grid;

public interface SquareFillEventSource
{
    // Adds {@code listener} to the list of listeners
    public void addListener(SquareFillEventListener listener);
    
    // Removes {@code listener} from the list of listeners
    public void removeListener(SquareFillEventListener listener);
    
    // Notifies any listeners of an event
    public void notifyListeners(Square square);
}