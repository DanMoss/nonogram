package solver;

import grid.Square;
import grid.SquareFill;
import grid.SquareFillEventListener;
import grid.Bar;

import java.util.List;
import java.util.ArrayList;
import java.util.TreeSet;

public class LineSegment
    implements SquareFillEventListener, SegmentUpdateEventSource
{
    private final TreeSet<Streak>                  streaks_;
    private final List<Bar>                        bars_;
    private final List<Square>                     squaresToFill_;
    private final List<SegmentUpdateEventListener> listeners_;
    
    // Constructor
    public LineSegment(List<Bar> bars, Square... squares)
    {
        streaks_ = new TreeSet<Streak>();
        bars_    = bars;
        
        squaresToFill_ = new ArrayList<Square>();
        for (Square square : squares) {
            squaresToFill_.add(square);
            square.addListener(this);
        }
        
        listeners_ = new ArrayList<SegmentUpdateEventListener>();
    }
    
    // Accessors
    public TreeSet<Streak> getStreaks()
    {
        return streaks_;
    }
    
    public List<Bar> getBars()
    {
        return bars_;
    }
    
    public List<Square> getSquaresToFill()
    {
        return squaresToFill_;
    }
    
    // Other methods
    public void solve()
    {
        combineStreaks();
        BarFinder barFinder = new BarFinder(this);
        barFinder.findBars();
        
        SlackFiller slackFiller = new SlackFiller(this);
        slackFiller.fill();
        
        EdgeFiller edgeFiller = new EdgeFiller(this);
        if (bars_.size() > 0)
            edgeFiller.fill();
        
        Trimmer trimmer = new Trimmer(this);
        if (squaresToFill_.size() > 0)
            trimmer.trim(0, true);
        if (squaresToFill_.size() > 0)
            trimmer.trim(squaresToFill_.size() - 1, false);
    }
    
    // Finds the length of a streak of squares of the same fill, starting at
    // {@code startingIndex} and moving either forwards or backwards, depending
    // {@code moveForward}.
    protected int findStreakLength(int startingIndex, boolean moveForward)
    { // Once finished consider adding fill as a parameter
        SquareFill fill          = squaresToFill_.get(startingIndex).getFill();
        int        direction     = moveForward ? 1 : -1;
        int        nextIndex     = startingIndex + direction;
        int        counter       = 1;
        int        iteration     = 1;
        int        nTotalSquares = squaresToFill_.size();
        boolean    endOfBar      = false;
        
        while (iteration < nTotalSquares && !endOfBar) {
            if (squaresToFill_.get(nextIndex).getFill() == fill)
                counter++;
            else
                endOfBar = true;
            nextIndex += direction;
            iteration++;
        }
        
        return counter;
    }
    
    // Marks {@code length} squares to be {@code fill}, starting at
    // {@code startingIndex} and ignoring those previously marked.
    protected void markSquares(int startingIndex, int length, SquareFill fill)
    {
        for (int i = 0; i < length; i++) {
            Square square = squaresToFill_.get(startingIndex + i);
            if (square.getFill() == SquareFill.UNKNOWN)
                square.setFill(fill);
        }
    }
    
    // Scrolls through {@code streaks} in reverse order, combining any
    // overlapping streaks into a single streak.
    private void combineStreaks()
    {
        int    nComparisons   = streaks_.size() - 1;
        Streak previousStreak = new Streak(-1, -1);
        if (nComparisons > 0)
            previousStreak = streaks_.last();
        
        for (int i = 0; i < nComparisons; i++) {
            Streak nextStreak = streaks_.lower(previousStreak);
            
            if (nextStreak.checkOverlap(previousStreak)) {
                streaks_.remove(previousStreak);
                streaks_.remove(nextStreak);
                previousStreak = nextStreak.combine(previousStreak);
                streaks_.add(previousStreak);
            }
            else {
                previousStreak = nextStreak;
            }
        }
    }
    
    // Implementation of SquareFillEventListener
    // Used by a source to update the listener
    public void onNotification(Square square)
    {
        if (square.getFill() == SquareFill.FILLED) {
            Streak streak = new Streak(squaresToFill_.indexOf(square), 1);
            streaks_.add(streak);
        }
        square.removeListener(this);
        notifyListeners(this, SegmentUpdateEvent.ADD_TO_QUEUE);
        // Consider logic depending on where the square is
    }
    
    // Implementation of SegmentUpdateEventSource
    // Adds {@code listener} to the list of listeners
    public void addListener(SegmentUpdateEventListener listener)
    {
        listeners_.add(listener);
    }
    
    // Removes {@code listener} from the list of listeners
    public void removeListener(SegmentUpdateEventListener listener)
    {
        listeners_.remove(listener);
    }
    
    // Notifies any listeners of an event
    public void notifyListeners(LineSegment segment, SegmentUpdateEvent event)
    {
        for (SegmentUpdateEventListener listener : listeners_)
            listener.onNotification(segment, event);
    }
}