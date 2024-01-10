import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LibrarysystemTest {

    private String TEST_FILE_PATH = "test_books.bin";

    @Test
    void testAddBook() {
        Librarysystem librarySystem = new Librarysystem(System.in, System.out);

        String testBookName = "Test Book";

        Files.deleteIfExists(Paths.get(TEST_FILE_PATH));

        boolean result = librarySystem.addBook(testBookName, TEST_FILE_PATH);

        assertTrue(result);

        // Dosyadan kitapları oku
        List<Book> books = librarySystem.loadBooks(TEST_FILE_PATH);

        // Kitapların sayısını kontrol et
        assertEquals(1, books.size());

        // Eklenen kitabın özelliklerini kontrol et
        Book addedBook = books.get(0);
        assertEquals(1, addedBook.getId());
        assertEquals(testBookName, addedBook.getName());
        assertFalse(addedBook.isMarked());
        assertFalse(addedBook.isWishlist());
        assertFalse(addedBook.isLoaned());

    }

    // Diğer test metotları buraya eklenmeli...

    // Test bittikten sonra test dosyasını temizle
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void cleanup() {
        try {
            Files.deleteIfExists(Paths.get(TEST_FILE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
