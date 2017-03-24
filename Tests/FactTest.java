import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Helgi on 23.3.2017.
 */
class FactTest {
    @Test
    void isSameAs()
    {
        Fact fact1 = new Fact(0, 0, "Glitter", true);
        Fact fact2 = new Fact(0, 0, "Glitter", true);
        boolean result = fact1.isSameAs(fact2);
        assert(result == true);
    }

    @Test
    void isNotSameAs()
    {
        Fact fact1 = new Fact(0, 0, "Glitter", true);
        Fact fact2 = new Fact(1, 0, "Glitter", true);
        boolean result = fact1.isSameAs(fact2);
        assert(result == false);
    }

}