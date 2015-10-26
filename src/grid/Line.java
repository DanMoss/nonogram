package grid;

import java.util.List;
import java.util.ArrayList;

public class Line
{
    private final List<Bar> bars_;
    private final int       length_;
    
    // Constructor
    public Line(int length, int... barLengths)
    {
        length_ = length;
        bars_   = new ArrayList<Bar>();
        setBars(barLengths);
    }
    
    // Accessor
    public List<Bar> getBars()
    {
        return bars_;
    }
    
    // Mutator
    public void setBars(int... barLengths)
    {
        boolean canSet = validateLengths(barLengths) && canFit(barLengths);
        if (canSet) {
            getBars().clear();
            for (int length : barLengths)
                bars_.add(new Bar(length));
        }
    }
    
    // Validation methods for setting
    // Checks that the selection of bars given will fit on the line
    private boolean canFit(int... barLengths)
    {
        int nGaps         = barLengths.length - 1;
        int spaceRequired = nGaps >= 0 ? nGaps : 0;
        for (int bar : barLengths)
            spaceRequired += bar;
        return spaceRequired <= length_;
    }
    
    // Checks that all given bars have strictly positive lengths
    private boolean validateLengths(int... barLengths)
    {
        boolean validLengths = true;
        for (int bar : barLengths)
            validLengths = validLengths && bar > 0;
        return validLengths;
    }
}