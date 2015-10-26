package solver;

class Streak
    implements Comparable<Streak>
{
    // Immutable fields to keep the same hashCode.
    // This is important for the use of LinkedHashMultiMap in LineSegment.
    private final int startingIndex_;
    private final int length_;
    
    // Constructor
    public Streak(int startingIndex, int length)
    {
        startingIndex_ = startingIndex;
        length_        = length;
    }
    
    // Accessors
    public int getStart()
    {
        return startingIndex_;
    }
    
    public int getLength()
    {
        return length_;
    }
    
    // Other methods
    public Streak shiftStart(int shift)
    {
        return new Streak(getStart() + shift, getLength());
    }
    
    // Combines with {@code streak}.
    public Streak combine(Streak streak)
    {
        int thisStart     = getStart();
        int otherStart    = streak.getStart();
        int thisEnd       = thisStart  + getLength();
        int otherEnd      = otherStart + streak.getLength();
        int startingIndex = thisStart < otherStart ? thisStart : otherStart;
        int endingIndex   = thisEnd   > otherEnd   ? thisEnd   : otherEnd;
        int length        = endingIndex - startingIndex;
        return new Streak(startingIndex, length);
    }
    
    // Checks whether the streak overlaps with {@code streak}.
    public boolean checkOverlap(Streak streak)
    {
        int thisStart  = getStart();
        int otherStart = streak.getStart();
        int firstStartingIndex;
        int firstLength;
        int lastStartingIndex;
        
        if (thisStart < otherStart) {
            firstStartingIndex = thisStart;
            firstLength        = getLength();
            lastStartingIndex  = otherStart;
        }
        else {
            firstStartingIndex = otherStart;
            firstLength        = streak.getLength();
            lastStartingIndex  = thisStart;
        }
        
        return lastStartingIndex <= firstStartingIndex + firstLength;
    }
    
    // Implementation of Comparable
    public int compareTo(Streak streak)
    {
        Integer thisStart  = new Integer(getStart());
        Integer otherStart = new Integer(streak.getStart());
        int result         = thisStart.compareTo(otherStart);
        if (result == 0) {
            Integer thisLength  = new Integer(getLength());
            Integer otherLength = new Integer(streak.getLength());
            result              = thisLength.compareTo(otherLength);
        }
        return result;
    }
    
    // Overriding equals and hashCode for consistency with compareTo. This is
    // to allow for the use of sorted sets in LineSegment.
    @Override
    public boolean equals(Object object)
    {
        if (object == this)
            return true;
        if (!(object instanceof Streak))
            return false;
        
        Streak  streak      = (Streak) object;
        boolean equalStart  = getStart()  == streak.getStart();
        boolean equalLength = getLength() == streak.getLength();
        return equalStart && equalLength;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 1;
        hash = hash * 31 + getStart();
        hash = hash * 23 + getLength();
        return hash;
    }
}