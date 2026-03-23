import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CerealTest {

    @Test
    void constructor_and_getters_work() {
        Cereal c = new Cereal("test flakes", 110, 3.5, 22.0, 1.0);

        assertEquals("test flakes", c.getName());
        assertEquals(110, c.getCalories());
        assertEquals(3.5, c.getFiber(), 1e-9);
        assertEquals(22.0, c.getCarbs(), 1e-9);
        assertEquals(1.0, c.getCups(), 1e-9);
    }

    @Test
    void toString_contains_expected_fields() {
        Cereal c = new Cereal("test flakes", 110, 3.5, 22.0, 1.0);
        String s = c.toString();

        assertTrue(s.contains("Cereal:"), "should mention 'Cereal:'");
        assertTrue(s.contains("test flakes"), "should include name");
        assertTrue(s.contains("110"), "should include calories");
        assertTrue(s.contains("1.0"), "should include cups");
        assertTrue(s.contains("3.5"), "should include fiber");
        assertTrue(s.contains("22.0"), "should include carbs");
    }
}
