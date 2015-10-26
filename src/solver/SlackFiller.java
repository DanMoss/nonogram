package solver;

import grid.Bar;
import grid.Square;
import grid.SquareFill;

import java.util.List;

class SlackFiller
{
    private final LineSegment  segment_;
    private final List<Square> squaresToFill_;
    private final List<Bar>    bars_;
    
    public SlackFiller(LineSegment segment)
    {
        segment_       = segment;
        squaresToFill_ = segment_.getSquaresToFill();
        bars_          = segment_.getBars();
    }
    
    public boolean fill()
    {
        int     slack     = findSlack();
        int     index     = 0;
        boolean hasFilled = false;
        
        for (Bar bar : bars_) {
            int     barLength  = bar.getLength();
            int     fillLength = barLength - slack;
            boolean canFillBar = fillLength > 0;
            hasFilled          = hasFilled || canFillBar;
            
            if (canFillBar) {
                index += slack;
                segment_.markSquares(index, fillLength, SquareFill.FILLED);
                index += fillLength;
            }
            else {
                index += barLength;
            }
            index++; // Bars are followed by an empty space
        }
        
        return hasFilled;
    }
    
    private int findSlack()
    {
        int nBars          = bars_.size();
        int nGaps          = nBars > 0 ? (nBars - 1) : 0;
        int totalBarLength = 0;
        
        for (Bar bar : bars_)
            totalBarLength += bar.getLength();
        
        int segmentLength = squaresToFill_.size();
        int slack         = segmentLength - totalBarLength - nGaps;
        
        return slack;
    }
}