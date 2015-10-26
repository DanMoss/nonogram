package grid;

import solver.Solver;

public class Grid
{
    private final int        width_;
    private final int        height_;
    private final Square[][] grid_;
    private final Line[]     columns_;
    private final Line[]     rows_;
    
    // Constructor
    public Grid(int width, int height)
    {
        width_  = width;
        height_ = height;
        
        grid_ = new Square[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++)
                grid_[i][j] = new Square();
        }
        
        columns_ = new Line[width];
        for (int m = 0; m < width; m++)
            columns_[m] = new Line(height);
        
        rows_ = new Line[height];
        for (int n = 0; n < height; n++)
            rows_[n] = new Line(width);
    }
    
    // Accessors (Requires review after finishing solver and printing)
    public int getWidth()
    {
        return width_;
    }
    
    public int getHeight()
    {
        return height_;
    }
    
    public Square get(int column, int row)
    {
        return grid_[column][row];
    }
    
    public Line[] getColumns()
    {
        return columns_;
    }
    
    public Line[] getRows()
    {
        return rows_;
    }
    
    public static void main(String[] args)
    {
        Grid usflag = new Grid(65, 45);
        
        usflag.columns_[0].setBars(4);
        usflag.columns_[1].setBars(45);
        usflag.columns_[2].setBars(1,42);
        usflag.columns_[3].setBars(1,2);
        usflag.columns_[4].setBars(45);
        
        usflag.columns_[5].setBars(4,1,3,6,4);
        usflag.columns_[6].setBars(9,5,4,3);
        usflag.columns_[7].setBars(3,9,7,3,3);
        usflag.columns_[8].setBars(7,9,3,3,3);
        usflag.columns_[9].setBars(1,3,5,5,3,3,3);
        
        usflag.columns_[10].setBars(9,7,3,3,3);
        usflag.columns_[11].setBars(3,9,3,3,3,3);
        usflag.columns_[12].setBars(7,9,3,3,3);
        usflag.columns_[13].setBars(1,3,5,4,3,3,3);
        usflag.columns_[14].setBars(9,6,3,3,3);
        
        usflag.columns_[15].setBars(3,9,2,4,3,3);
        usflag.columns_[16].setBars(7,8,4,3,3);
        usflag.columns_[17].setBars(2,3,5,4,4,3,3);
        usflag.columns_[18].setBars(10,6,4,3,3);
        usflag.columns_[19].setBars(4,9,2,4,3,3);
        
        usflag.columns_[20].setBars(7,9,4,3,3);
        usflag.columns_[21].setBars(1,3,5,4,3,3,3);
        usflag.columns_[22].setBars(9,6,3,4,3);
        usflag.columns_[23].setBars(3,9,2,4,3,4);
        usflag.columns_[24].setBars(7,8,3,3,4);
        
        usflag.columns_[25].setBars(1,3,5,4,3,4,4);
        usflag.columns_[26].setBars(9,6,3,4,3);
        usflag.columns_[27].setBars(13,2,3,3,3);
        usflag.columns_[28].setBars(4,3,7,3,3,3);
        usflag.columns_[29].setBars(4,3,3,3,3,3,3);
        
        usflag.columns_[30].setBars(3,3,3,3,3,3,4);
        usflag.columns_[31].setBars(3,3,3,3,3,3,4);
        usflag.columns_[32].setBars(3,4,3,4,3,3,3);
        usflag.columns_[33].setBars(3,4,3,3,3,3,3);
        usflag.columns_[34].setBars(3,3,3,3,3,3,3);
        
        usflag.columns_[35].setBars(3,3,3,3,3,3,3);
        usflag.columns_[36].setBars(3,3,3,3,4,2,3);
        usflag.columns_[37].setBars(4,3,3,3,4,2,3);
        usflag.columns_[38].setBars(4,3,3,4,3,2,4);
        usflag.columns_[39].setBars(3,3,3,4,3,3,4);
        
        usflag.columns_[40].setBars(3,3,3,3,3,3,3);
        usflag.columns_[41].setBars(3,3,3,3,4,3,3);
        usflag.columns_[42].setBars(3,3,4,3,4,3,3);
        usflag.columns_[43].setBars(3,3,3,3,3,3,3);
        usflag.columns_[44].setBars(3,3,3,3,3,4,3);
        
        usflag.columns_[45].setBars(3,3,3,3,4,3,3);
        usflag.columns_[46].setBars(3,3,3,3,3,3,4);
        usflag.columns_[47].setBars(3,3,3,3,3,4,3);
        usflag.columns_[48].setBars(3,2,2,2,2,3,3);
        usflag.columns_[49].setBars(3,3,3,3,3,3,4);
        
        usflag.columns_[50].setBars(3,3,3,3,4,3,4);
        usflag.columns_[51].setBars(3,3,3,3,3,3,4);
        usflag.columns_[52].setBars(3,3,3,3,3,3,3);
        usflag.columns_[53].setBars(3,3,3,4,3,4,3);
        usflag.columns_[54].setBars(3,3,3,4,3,4,3);
        
        usflag.columns_[55].setBars(3,3,3,3,4,4,4);
        usflag.columns_[56].setBars(3,3,3,3,4,3,4);
        usflag.columns_[57].setBars(3,3,3,3,3,3,3);
        usflag.columns_[58].setBars(3,3,4,3,3,3,3);
        usflag.columns_[59].setBars(3,3,4,4,3,3,3);
        
        usflag.columns_[60].setBars(2,3,4,4,4,3,3);
        usflag.columns_[61].setBars(1,3,3,3,5,3,3);
        usflag.columns_[62].setBars(3,4,3,4,3,3);
        usflag.columns_[63].setBars(4,4,3,3,3);
        usflag.columns_[64].setBars(3,3);
        
        usflag.rows_[0].setBars(4);
        usflag.rows_[1].setBars(2,2);
        usflag.rows_[2].setBars(2,2);
        usflag.rows_[3].setBars(3,2);
        usflag.rows_[4].setBars(6);
        
        usflag.rows_[5].setBars(4,12);
        usflag.rows_[6].setBars(2,1,24);
        usflag.rows_[7].setBars(2,1,34);
        usflag.rows_[8].setBars(2,1,31);
        usflag.rows_[9].setBars(2,17,3,13,13);
        
        usflag.rows_[10].setBars(2,1,3,3,3,12,22);
        usflag.rows_[11].setBars(2,19,4,31);
        usflag.rows_[12].setBars(2,3,3,3,3,8,19,1);
        usflag.rows_[13].setBars(2,17,3,15,12);
        usflag.rows_[14].setBars(2,1,3,3,3,16,22);
        
        usflag.rows_[15].setBars(2,16,3,4,28);
        usflag.rows_[16].setBars(2,4,3,3,11,18,6);
        usflag.rows_[17].setBars(2,18,3,16,6,2);
        usflag.rows_[18].setBars(2,2,3,3,3,17,17);
        usflag.rows_[19].setBars(2,17,3,4,26);
        
        usflag.rows_[20].setBars(2,5,3,3,11,16,12);
        usflag.rows_[21].setBars(2,1,17,3,16,2,6);
        usflag.rows_[22].setBars(2,1,1,3,3,3,20,12,2);
        usflag.rows_[23].setBars(2,1,27,21);
        usflag.rows_[24].setBars(2,1,14,12,13);
        
        usflag.rows_[25].setBars(2,1,6,23,10);
        usflag.rows_[26].setBars(2,1,1,28,7,5);
        usflag.rows_[27].setBars(2,1,31,12,4);
        usflag.rows_[28].setBars(2,1,17,21);
        usflag.rows_[29].setBars(2,1,14,23,11);
        
        usflag.rows_[30].setBars(2,1,1,23,3,10);
        usflag.rows_[31].setBars(2,1,30,7,4);
        usflag.rows_[32].setBars(2,1,21,19);
        usflag.rows_[33].setBars(2,1,17,34);
        usflag.rows_[34].setBars(2,2,24,11);
        
        usflag.rows_[35].setBars(2,36,9);
        usflag.rows_[36].setBars(2,28);
        usflag.rows_[37].setBars(2,22);
        usflag.rows_[38].setBars(2,1);
        usflag.rows_[39].setBars(2,1);
        
        usflag.rows_[40].setBars(2,1);
        usflag.rows_[41].setBars(2,1);
        usflag.rows_[42].setBars(2,1);
        usflag.rows_[43].setBars(2,1);
        usflag.rows_[44].setBars(2,1);
        
        Solver skylineS = new Solver(usflag);
        skylineS.solve();
        Printer skylineP = new Printer(usflag);
        skylineP.printToFile("Test - EdgeFiller (new).txt");
    }
}