package maryam.storage;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Stream;

@Service
public class FileStorageService implements FileStorageServiceInterface {

    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
        try{
            Files.createDirectory(root);
        }catch (IOException e){
            throw new RuntimeException("Cloud not initialize folder for upload!");
        }
    }

    @Override
    public String save(MultipartFile file) {
        Random random = new Random();
        String extension = file.getOriginalFilename().split("\\.")[1];
        String new_name = String.format("%s%s",System.currentTimeMillis(),random.nextInt(100000)+"."+extension);
        try{
            Files.copy(file.getInputStream(),this.root.resolve(new_name));
            return new_name;
        }catch (Exception e){
            throw new RuntimeException("Cloud not store the file.Error:"+ e.getMessage());
        }

    }

    @Override
    public Resource load(String filename) {
        try{
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()){
                return resource;
            }  else {
                throw new RuntimeException("Cloud not read the file!");
            }
        } catch (MalformedURLException e){
            throw new RuntimeException("Error:"+e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
    }

    @Override
    public Stream<Path> loadAll() {
        try{
            return Files.walk(this.root,1).filter(path->!path.equals(this.root)).map(this.root::relativize);
        }catch(IOException e){
            throw new RuntimeException("Cloud not load the files");
        }
    }
}
