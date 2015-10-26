package grid;

public enum SquareFill
{
    UNKNOWN("."), EMPTY("-"), FILLED("X");
    
    private final String fill_;
    
    private SquareFill(String fill)
    {
        fill_ = fill;
    }
    
    public String getRepresentation()
    {
        return fill_;
    }
}