import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Created by Samuel on 17/6/2014.
 */
public class Parse {
    public static void main(String[] args) throws IOException {
        final Path rootPath = Paths.get("lib\\").toAbsolutePath();
        Files.walkFileTree(rootPath,new FileVisitor<Path>() {

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {

                String jarFile = null;
                File[] files = dir.toFile().listFiles();
                if (files!=null){
                    for (File file: files){
                        if (file.isDirectory() || file.getName().endsWith(".pom")){
                            return FileVisitResult.CONTINUE;
                        }else{
                            if (file.getName().endsWith(".jar")){
                                jarFile=file.toPath().toAbsolutePath().toString();
                            }
                        }
                    }
                }

                if (jarFile == null || jarFile.contains("sources")){
                    return FileVisitResult.CONTINUE;
                }


                String version = dir.getFileName().toString();
                String artifactName = dir.getParent().getFileName().toString();
                String organization = rootPath.relativize(dir.getParent().getParent()).toString().replace('\\', '.');

                System.out.println(String.format("call mvn org.apache.maven.plugins:maven-install-plugin:2.5.1:install-file -Dfile=\"%s\" -DgroupId=%s -DartifactId=%s -Dversion=%s -Dpackaging=jar -DgeneratePom=true", jarFile,organization,artifactName,version));

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
