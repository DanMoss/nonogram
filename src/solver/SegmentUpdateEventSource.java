package solver;

public interface SegmentUpdateEventSource
{
    // Adds {@code listener} to the list of listeners
    public void addListener(SegmentUpdateEventListener listener);
    
    // Removes {@code listener} from the list of listeners
    public void removeListener(SegmentUpdateEventListener listener);
    
    // Notifies any listeners of an event
    public void notifyListeners(LineSegment segment, SegmentUpdateEvent event);
}