package solver;

import grid.Square;
import grid.Bar;
import grid.Line;
import grid.Grid;
import grid.SquareFillEventListener;

import java.util.List;
import java.util.ArrayList;

public class Solver
    implements SegmentUpdateEventListener, SquareFillEventListener
{
    private final List<LineSegment> queue_;
    private       int               nSquaresChanged_; // Error tracking (if changed > total, something went wrong)
    private       int               nSquaresTotal_;
    
    public Solver(Grid grid)
    {
        queue_           = new ArrayList<LineSegment>();
        nSquaresChanged_ = 0;
        nSquaresTotal_   = 0;
        
        int    width   = grid.getWidth();
        int    height  = grid.getHeight();
        Line[] columns = grid.getColumns();
        Line[] rows    = grid.getRows();
        
        for (int i = 0; i < width; i++) {
            Line     column  = columns[i];
            Square[] squares = new Square[height];
            for (int j = 0; j < height; j++) {
                Square square = grid.get(i, j);
                squares[j]    = square;
                square.addListener(this);
                nSquaresTotal_++;
            }
            
            List<Bar>   bars    = new ArrayList<Bar>(column.getBars());
            /* Remove comment to see the bars get removed when printing.
            List<Bar>   bars    = column.getBars();*/
            LineSegment segment = new LineSegment(bars, squares);
            segment.addListener(this);
            queue_.add(segment);
        }
        
        for (int y = 0; y < height; y++) {
            Line     row     = rows[y];
            Square[] squares = new Square[width];
            for (int x = 0; x < width; x++) {
                Square square = grid.get(x, y);
                squares[x]    = square;
            }
            
            List<Bar>   bars    = new ArrayList<Bar>(row.getBars());
            /* Remove comment to see the bars get removed when printing.
            List<Bar>   bars    = row.getBars();*/
            LineSegment segment = new LineSegment(bars, squares);
            segment.addListener(this);
            queue_.add(segment);
        }
    }
    
    public void solve()
    {
        int counter = 0;
        while (queue_.size() > 0) {
            LineSegment segment = queue_.get(0);
            queue_.remove(segment);
            segment.solve();
            counter++;
            System.out.println("End of iteration.");
            if (counter % 10 == 0) {
                System.out.println("Iteration = " + counter + ", Queue size = " + queue_.size() + ", Squares Changed = " + nSquaresChanged_ + "/" + nSquaresTotal_);
            }
        }
        System.out.println("Finished after " + counter + " iterations");
    }
    
    // Implementation of SegmentUpdateEventListener
    // Used by a source to update the listener
    public void onNotification(LineSegment segment, SegmentUpdateEvent event)
    {
        switch (event) {
        case ADD_TO_QUEUE:
            if (!(queue_.contains(segment)))
                queue_.add(segment);
            break;
        default:
            
            break;
        }
    }
    
    // Implementation of SquareFillEventListener
    // Used by a source to update the listener
    public void onNotification(Square square)
    {
        nSquaresChanged_++;
    }
}