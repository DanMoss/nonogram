package solver;

import grid.Bar;
import grid.Square;
import grid.SquareFill;

import java.util.List;
import java.util.TreeSet;

class Trimmer
{
    private final LineSegment  segment_;
    private final List<Square> squaresToFill_;
    // In future implementations make this class static
    public Trimmer(LineSegment segment)
    {
        segment_       = segment;
        squaresToFill_ = segment.getSquaresToFill();
    }
    
    // Attempts to trim squares from the line to simplify the solution.
    // Note that this should only be called when {@code startingIndex} is 
    // either the first or last index of {@code squaresToFill_}.
    public boolean trim(int startingIndex, boolean moveForward)
    {
        int streakLength = segment_.findStreakLength(startingIndex,
                                                     moveForward);
        int direction    = moveForward ? 1 : -1;
        int increment    = moveForward ? 0 : -1;
        int nToTrim      = prepareTrim(startingIndex, streakLength, direction);
        
        for (int i = 0; i < nToTrim; i++) {
            int    index  = i * increment + startingIndex;
            Square square = squaresToFill_.get(index);
            square.removeListener(segment_);
            squaresToFill_.remove(square);
        }
        
        boolean hasTrimmed = nToTrim > 0;
        if (hasTrimmed)
            segment_.notifyListeners(segment_, SegmentUpdateEvent.ADD_TO_QUEUE);
        
        return hasTrimmed;
    }
    
    // Figures out how much to trim. Some extra implementation necessities are
    // taken care of if attemptiong to trim filled squares.
    private int prepareTrim(int startingIndex, int streakLength, int direction)
    {
        SquareFill fill = squaresToFill_.get(startingIndex).getFill();
        int        nToTrim;
        
        switch (fill) {
            case EMPTY:
                nToTrim = streakLength;
                break;
            
            case FILLED:
                nToTrim = prepareTrimFilled(startingIndex, streakLength,
                                                direction);
                break;
            
            default:
                nToTrim = 0;
        }
        
        return nToTrim;
    }
    
    // Checks if a streak of filled squares of length {@code streakLength}
    // starting at {@code startingIndex} in direction {@code direction} can be
    // trimmed. If it can be trimmed, then the next square after the streak
    // is marked empty and {@code bars} is modified appropriately.
    // Note {@code direction} should have value either +1 or -1.
    private int prepareTrimFilled(int startingIndex, int streakLength,
                                  int direction)
    {
        List<Bar> bars      = segment_.getBars();
        boolean   atStart   = startingIndex == 0;
        int       barIndex  = atStart ? 0 : bars.size() - 1;
        int       barLength = bars.get(barIndex).getLength();
        int       nToTrim;
        
        if (barLength == streakLength) {
            nToTrim = barLength;
            bars.remove(barIndex);
            
            // Fixing streak stuff...
            TreeSet<Streak> streaks = segment_.getStreaks();
            Streak  streak;
            if (barIndex == 0)
                streak = streaks.pollFirst();
            else
                streak = streaks.pollLast();
                
            int     shift = streak.getLength() * -direction;
            TreeSet<Streak> temp  = new TreeSet<Streak>(streaks);
            for (Streak s : temp) {
                streaks.remove(s);
                streaks.add(s.shiftStart(shift));
            }
            
            // Marking the next square empty if it exists
            int nextIndex = startingIndex + streakLength * direction;
            if (nextIndex >= 0 && nextIndex < squaresToFill_.size())
                segment_.markSquares(nextIndex, 1, SquareFill.EMPTY);
        }
        else {
            nToTrim = 0;
        }
        
        return nToTrim;
    }
}