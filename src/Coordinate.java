/**
 * Created by Helgi on 18.3.2017.
 */
public class Coordinate
{
    private int cellPixels;
    private int yGridSize;

    public Coordinate(int cellPixels , int yGridSize)
    {
        this.cellPixels = cellPixels;
        this.yGridSize = yGridSize;
    }

    public int ChangeYCellNumberToYPixelCoordinate(int yCell)
    {
        //Figure 7.2 in the AI book has coordinate 0,0 in the lower left corner while the JFrame has
        //0,0 in the upper left corner
        int result = (yGridSize * cellPixels) - ((yCell) * cellPixels);
        return result;
    }
}
