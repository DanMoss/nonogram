package grid;

import java.util.List;
import java.util.ArrayList;

public class Square
    implements SquareFillEventSource
{
    private SquareFill                    fill_;
    private List<SquareFillEventListener> listeners_;
    private List<SquareFillEventListener> toRemove_;
    
    // Constructor
    public Square()
    {
        fill_      = SquareFill.UNKNOWN;
        listeners_ = new ArrayList<SquareFillEventListener>();
        toRemove_  = new ArrayList<SquareFillEventListener>();
    }
    
    // Accessor
    public SquareFill getFill()
    {
        return fill_;
    }
    
    // Mutator
    public void setFill(SquareFill fill)
    {
        fill_ = fill;
        notifyListeners(this);
    }
    
    // Implementation of SquareFillEventSource
    // Adds {@code listener} to the list of listeners
    public void addListener(SquareFillEventListener listener)
    {
        listeners_.add(listener);
    }
    
    // Removes {@code listener} from the list of listeners
    public void removeListener(SquareFillEventListener listener)
    {
        toRemove_.add(listener); // Revisit, could cause memory leak?
    }
    
    // Notifies any listeners of an event
    public void notifyListeners(Square square)
    {
        for (SquareFillEventListener listener : listeners_)
            listener.onNotification(square);
        for (SquareFillEventListener listener : toRemove_)
            listeners_.remove(listener);
    }
}