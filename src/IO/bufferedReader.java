package IO;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class bufferedReader {
    public static void newBufferedReader_path(Path path){
        try(BufferedReader bufferedReader = Files.newBufferedReader(path)) {
            String line;
            while ((line = bufferedReader.readLine()) != null){
                System.out.println(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void newBufferedReader_utf8(Path path) throws NoSuchFileException {
        if (!Files.exists(path)) throw new NoSuchFileException("文件不存在");
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)){
            String line;
            while (true){
                line = reader.readLine();
                if (line == null){
                    break;
                }
                System.out.println(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public static void main(String[] args) throws NoSuchFileException {
        Path path = Paths.get("example.txt");
        newBufferedReader_utf8(path);
    }
}