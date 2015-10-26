package solver;

import java.util.List;
import java.util.SortedSet;

import com.google.common.collect.LinkedHashMultimap;

import grid.Bar;
import grid.Square;
import grid.SquareFill;

class BarFinder
{
    private final LinkedHashMultimap<Streak, Bar> map_;
    private final SortedSet<Streak>               streaks_;
    private final List<Bar>                       bars_;
    private final List<Square>                    squares_;
    private       int                             nSquares_;
    
    // Constructor
    public BarFinder(LineSegment segment)
    {
        map_      = LinkedHashMultimap.create();
        streaks_  = segment.getStreaks();
        bars_     = segment.getBars();
        squares_  = segment.getSquaresToFill();
        nSquares_ = squares_.size();
    }
    
    public void findBars()
    {
        generateInitialOptions();
        filterByRanges();
        
        String line = "";
        for (int i = 0; i < nSquares_; i++)
            line += squares_.get(i).getFill().getRepresentation();
        System.out.println(line);
        String streaks = "";
        
        for (Streak streak : streaks_)
            streaks += streak.getLength() + " ";
        System.out.println("Streaks = " + streaks);
        
        for (Bar bar : bars_) {
            int pairs = 0;
            for (Streak streak : streaks_) {
                if (map_.containsEntry(streak, bar))
                    pairs++;
            }
            System.out.println("This bar (" + bar.getLength() + ") has " + pairs + "/" + streaks_.size() + " options.");
        }
    }
    
    // Runs through {@code streaks_}. For each new streak, the set of possible
    // bar pairings (based on length only) are found and added to {@code map_}.
    private void generateInitialOptions()
    {
        for (Streak streak : streaks_) {
            if (!map_.containsKey(streak)) {
                for (Bar bar : bars_) {
                    if (streak.getLength() <= bar.getLength())
                        map_.put(streak, bar);
                }
            }
        }
    }
    
    // Removes any (streak, bar) pairing where the streak is outside the limits
    // of the bar.
    private void filterByRanges()
    {
        int   nBars         = bars_.size();
        int[] barLowerLimit = findBarRangeLimit(nBars, true);
        int[] barUpperLimit = findBarRangeLimit(nBars, false);
        
        for (int i = 0; i < nBars; i++) {
            Bar bar        = bars_.get(i);
            int lowerLimit = barLowerLimit[i];
            int upperLimit = barUpperLimit[i];
            for (Streak streak : streaks_) {
                if (map_.containsEntry(streak, bar)) {
                    int streakStart = streak.getStart();
                    int streakEnd   = streakStart + streak.getLength() - 1;
                    if (streakStart < lowerLimit || streakEnd > upperLimit)
                        map_.remove(streak, bar);
                }
            }
        }
    }
    
    // Finds a limit for an edge of each bar. Limits are inclusive.
    private int[] findBarRangeLimit(int nBars, boolean lowerEdge)
    {
        int   barsPlaced = 0;
        int[] barLimit   = new int[nBars];
        int   bar        = lowerEdge ? 0 : nBars - 1;
        int   index      = lowerEdge ? 0 : nSquares_ - 1;
        int   direction  = lowerEdge ? 1 : -1;
        
        while (barsPlaced < nBars) {
            int length    = bars_.get(bar).getLength();
            int placement = index;
            int shift;
            // Looping through the line of squares to find a viable placement
            do {
                placement  = findNextClearLength(placement, length, direction);
                shift      = findBarShift(placement, length, direction);
                placement += shift;
            } while (shift != 0);
            
            barLimit[bar] = placement;
            barsPlaced++;
            bar   += direction;
            index  = placement + length * direction; // End of bar
            index += direction; // Gap after bar
        }
        
        return barLimit;
    }
    
    // Finds the next set of squares from {@code start} which can accommodate a
    // bar of length {@code length}.
    private int findNextClearLength(int start, int length, int direction)
    {
        int     index = start;
        int     startOfBarIndex;
        boolean emptyInLength;
        
        do {
            startOfBarIndex = index;
            int barWalker   = 0;
            emptyInLength   = false;
            
            while (barWalker < length && !emptyInLength) {
                if (squares_.get(index).getFill() == SquareFill.EMPTY)
                    emptyInLength = true;
                barWalker++;
                index += direction;
            }
        } while (emptyInLength);
        
        return startOfBarIndex;
    }
    
    // Finds how much to shift the starting index of a bar based on a set of
    // filled squares directly before the bar.
    private int findBarShift(int barStart, int length, int direction)
    {
        int     shift       = 0;
        boolean foundShift  = false;
        int     indexBefore = barStart - direction;
        int     indexAfter  = barStart + length * direction;
        
        do {
            boolean beforeInvalid = checkForSquareFilled(indexBefore);
            boolean afterInvalid  = checkForSquareFilled(indexAfter);
            
            if (beforeInvalid || afterInvalid) {
                shift       += direction;
                indexBefore += direction;
                indexAfter  += direction;
            }
            else {
                foundShift = true;
            }
        } while (!foundShift);
        
        return shift;
    }
    
    // Checks if the given {@code index} is a filled square
    private boolean checkForSquareFilled(int index)
    {
        boolean outOfBounds = index >= nSquares_ || index < 0;
        boolean squareFilled;
        if (outOfBounds) {
            squareFilled = false;
        }
        else {
            SquareFill fill = squares_.get(index).getFill();
            squareFilled    = fill == SquareFill.FILLED;
        }
        return squareFilled;
    }
}