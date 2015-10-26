package solver;

public interface SegmentUpdateEventListener
{
    // Used by a source to update the listener
    public void onNotification(LineSegment segment, SegmentUpdateEvent event);
}