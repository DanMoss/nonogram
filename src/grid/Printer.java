package grid;

import java.io.Writer;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;

import java.util.List;

class Printer
{
    private final int    width_;
    private final int    height_;
    private final Line[] columns_;
    private final Line[] rows_;
    private final Grid   grid_;
    private       int    maxNBarsInColumns_;
    private       int    maxNBarsInRows_;
    
    public Printer(Grid grid)
    {
        grid_    = grid;
        width_   = grid.getWidth();
        height_  = grid.getHeight();
        columns_ = grid.getColumns();
        rows_    = grid.getRows();
    }
    
    public void printToFile(String fileName)
    {
        findMaxNBars();
        
        try (Writer writer = new BufferedWriter(
                                 new OutputStreamWriter(
                                     new FileOutputStream(fileName), "utf-8")))
        {
            writer.write(createString());
        }
        catch (Exception exception) {
            System.out.println("Error when trying to printToFile");
        }
    }
    
    private String createString()
    {
        int    rowIndent = maxNBarsInRows_ + 2;
        String output    = "";
        
        // Columns
        for (int i = maxNBarsInColumns_; i > 0; i--) {
            String line       = "\r\n"; // New line
            String formatting = String.format("%1$" + rowIndent + "s", "");
            line             += formatting; // Indenting
            
            for (int j = 0; j < width_; j++) {
                List<Bar> bars   = columns_[j].getBars();
                int       nBars  = bars.size();
                
                if (nBars >= i) {
                    int barIndex  = nBars - i; // Correcting for the index of the bar in the list of bars
                    int barLength = bars.get(barIndex).getLength();
                    line         += barLength >= 10 ? "E" : barLength; // Printing E if barLength isn't a single digit
                }
                else {
                    line += " "; // No entry
                }
            }
            
            output += line;
        }
        
        // Space
        output += "\r\n";
        
        // Rows
        for (int m = 0; m < height_; m++) {
            String    line       = "\r\n"; // New line
            String    formatting = "";
            List<Bar> bars       = rows_[m].getBars();
            int       nBars      = bars.size();
            
            for (int k = 0; k < nBars; k++) {
                int barLength = bars.get(k).getLength();
                formatting   += barLength >= 10 ? "E" : barLength; // Printing E if barLength isn't a single digit
            }
            
            formatting = String.format("%1$" + (rowIndent - 1) + "s", formatting);
            line      += formatting + " ";
            
            for (int n = 0; n < width_; n++) {
                line += grid_.get(n, m).getFill().getRepresentation(); // Printing the grid
            }
            
            output += line;
        }
        
        return output;
    }
    
    private void findMaxNBars()
    {
        int maxColumn = 0;
        for (Line column : columns_) {
            int nBars = column.getBars().size();
            maxColumn = maxColumn < nBars ? nBars : maxColumn;
        }
        maxNBarsInColumns_ = maxColumn;
        
        int maxRow = 0;
        for (Line row : rows_) {
            int nBars = row.getBars().size();
            maxRow    = maxRow < nBars ? nBars : maxRow;
        }
        maxNBarsInRows_ = maxRow;
    }
}