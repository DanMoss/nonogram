package solver;

import java.util.List;

import grid.Bar;
import grid.Square;
import grid.SquareFill;

class EdgeFiller
{
    private final LineSegment   segment_;
    private final List<Square>  squaresToFill_;
    
    public EdgeFiller(LineSegment segment)
    {
        segment_       = segment;
        squaresToFill_ = segment.getSquaresToFill();
    }
    
    public void fill()
    {
        List<Bar> bars = segment_.getBars();
        
        int        firstBarLength = bars.get(0).getLength();
        SquareFill firstBarFill   = findFill(0, 1, firstBarLength);
        
        if (firstBarFill != SquareFill.UNKNOWN)
            fillEdge(0, 1, firstBarLength, firstBarFill);
        
        int        lastBarStart  = squaresToFill_.size() - 1;
        int        lastBarIndex  = bars.size() - 1;
        int        lastBarLength = bars.get(lastBarIndex).getLength();
        SquareFill lastBarFill   = findFill(lastBarStart, -1, lastBarLength);
        
        if (lastBarFill != SquareFill.UNKNOWN)
            fillEdge(lastBarStart, -1, lastBarLength, lastBarFill);
    }
    
    private void fillEdge(int barStartingIndex, int barDirection,
                             int barLength, SquareFill fill)
    {
        int fillStartingIndex;
        int fillDirection;
        if (fill == SquareFill.FILLED) {
            fillStartingIndex = barStartingIndex +
                               (barLength - 1) * barDirection;
            fillDirection     = -barDirection;
        }
        else {
            fillStartingIndex = barStartingIndex;
            fillDirection     = barDirection;
        }
        
        int nSquaresToMark = 0;
        for (int i = 0; i < barLength; i++) {
            int searchIndex = fillStartingIndex + i * fillDirection;
            if (squaresToFill_.get(searchIndex).getFill() == fill)
                nSquaresToMark = i + 1;
        }
        
        for (int j = 0; j < nSquaresToMark; j++) {
            int fillIndex = fillStartingIndex + j * fillDirection;
            segment_.markSquares(fillIndex, 1, fill);
        }
    }
    
    private SquareFill findFill(int barStartingIndex, int barDirection,
                                int barLength)
    {
        int        searchStartingIndex = barStartingIndex +
                                         (barLength - 1) * barDirection;
        int        searchDirection     = -barDirection;
        SquareFill barFill             = SquareFill.UNKNOWN;
        
        for (int i = 0; i < barLength; i++) {
            int        index = searchStartingIndex + i * searchDirection;
            SquareFill fill  = squaresToFill_.get(index).getFill();
            if (fill != SquareFill.UNKNOWN)
                barFill = fill;
        }
        
        return barFill;
    }
}
