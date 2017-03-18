import org.junit.jupiter.api.Test;

/**
 * Created by Helgi on 18.3.2017.
 */
class CoordinateTest {
    @Test
    void getY0DrawCoordinate() {
        Coordinate coordinate = new Coordinate(50, 4);
        int y = coordinate.ChangeYCellNumberToYPixelCoordinate(0);
        System.out.println(y);
        assert(y == 200);
    }

    @Test
    void getY1DrawCoordinate() {
        Coordinate coordinate = new Coordinate(50, 4);
        int y = coordinate.ChangeYCellNumberToYPixelCoordinate(1);
        System.out.println(y);
        assert(y == 150);
    }

    @Test
    void getY2DrawCoordinate() {
        Coordinate coordinate = new Coordinate(50, 4);
        int y = coordinate.ChangeYCellNumberToYPixelCoordinate(2);
        System.out.println(y);
        assert(y == 100);
    }

    @Test
    void getY3DrawCoordinate() {
        Coordinate coordinate = new Coordinate(50, 4);
        int y = coordinate.ChangeYCellNumberToYPixelCoordinate(3);
        System.out.println(y);
        assert(y == 50);
    }

}