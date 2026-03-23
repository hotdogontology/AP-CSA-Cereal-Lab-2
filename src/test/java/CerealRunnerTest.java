import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.*;
import java.util.*;

public class CerealRunnerTest {

    private static final String FILE_PATH = "src/data/cerealSubset.csv";

    @Test
    void runner_creates_same_number_of_records_as_csv() throws Exception {

        assertTrue(Files.exists(Path.of(FILE_PATH)),
                "missing " + FILE_PATH);

        int expected = countDataLines(FILE_PATH);

        CerealRunner cr = new CerealRunner(FILE_PATH);
        List<Cereal> cereals = extractCerealsList(cr);

        assertNotNull(cereals, "cereals list should not be null");
        assertEquals(expected, cereals.size(),
                "runner should create one Cereal per data line in the csv");
    }

    @Test
    void runner_parses_values_correctly_for_a_few_rows() throws Exception {

        assertTrue(Files.exists(Path.of(FILE_PATH)),
                "missing " + FILE_PATH);

        // build expected cereals by parsing the csv in the test
        List<Cereal> expected = parseCsvToCereals(FILE_PATH);

        CerealRunner cr = new CerealRunner(FILE_PATH);
        List<Cereal> actual = extractCerealsList(cr);

        assertEquals(expected.size(), actual.size(), "size mismatch");

        int[] idxs = pickIndices(expected.size());
        for (int i : idxs) {
            Cereal e = expected.get(i);
            Cereal a = actual.get(i);

            assertEquals(e.getName(), a.getName(), "name mismatch at row " + i);
            assertEquals(e.getCalories(), a.getCalories(), "calories mismatch at row " + i);
            assertEquals(e.getFiber(), a.getFiber(), 1e-9, "fiber mismatch at row " + i);
            assertEquals(e.getCarbs(), a.getCarbs(), 1e-9, "carbs mismatch at row " + i);
            assertEquals(e.getCups(), a.getCups(), 1e-9, "cups mismatch at row " + i);
        }
    }

    // ---------- helpers ----------

    @SuppressWarnings("unchecked")
    private static List<Cereal> extractCerealsList(CerealRunner cr) throws Exception {
        Field f = CerealRunner.class.getDeclaredField("cereals");
        f.setAccessible(true);
        Object v = f.get(cr);
        assertInstanceOf(List.class, v, "cereals should be a List/ArrayList");
        return (List<Cereal>) v;
    }

    private static int countDataLines(String fileName) throws IOException {
        int count = 0;
        try (BufferedReader br = Files.newBufferedReader(Path.of(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (looksLikeHeader(line)) continue;
                count++;
            }
        }
        return count;
    }

    private static boolean looksLikeHeader(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length < 5) return true;
        try {
            Integer.parseInt(parts[1].trim());
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private static List<Cereal> parseCsvToCereals(String fileName) throws IOException {
        List<Cereal> out = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Path.of(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (looksLikeHeader(line)) continue;

                String[] p = line.split(",", -1);
                assertTrue(p.length >= 5, "bad csv row: " + line);

                String name = p[0].trim();
                int calories = Integer.parseInt(p[1].trim());
                double fiber = Double.parseDouble(p[2].trim());
                double carbs = Double.parseDouble(p[3].trim());
                double cups = Double.parseDouble(p[4].trim());

                out.add(new Cereal(name, calories, fiber, carbs, cups));
            }
        }
        return out;
    }

    private static int[] pickIndices(int n) {
        if (n <= 3) {
            int[] all = new int[n];
            for (int i = 0; i < n; i++) all[i] = i;
            return all;
        }
        return new int[] {0, n / 2, n - 1};
    }
}
