import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Minitest2 {

    public static Map<Path, List<String>> fichiersParRacine(List<String> chemins){
        Map<Path, List<String>> resultat = new HashMap<>();

        for(Path p : FileSystems.getDefault().getRootDirectories()){
            resultat.put(p, new LinkedList<>());
        }

        for(String chemin : chemins) {
            Path p = Paths.get(chemin);
            if(p.isAbsolute()) resultat.get(p.getRoot()).add(p.toString());
        }

        return resultat;
    }

    public static int nbExtension(Path repertoireBase, String ext) {
        int compteur = 0;

        if(!Files.isDirectory(repertoireBase)) throw new RuntimeException();

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(repertoireBase)){
            for(Path f : stream) {
                if(Files.isRegularFile(f) &&  f.getFileName().toString().endsWith("." + ext)) {
                    compteur++;
                }
            }
        } catch(IOException ex) {
        }

        return compteur;
    }

    public static Path pwd(){
        return Paths.get("").toAbsolutePath();
    }

    public static void copieVers(Path src, Path dst) throws IOException{
        if(!Files.isDirectory(src)) throw new RuntimeException();
        if(Files.exists(dst)) throw new RuntimeException();

        Files.createDirectory(dst);

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(src, (Path p) -> Files.isRegularFile(p))){
            for(Path f : stream) {
                Path target = dst.resolve(f.getFileName());
                Files.copy(f, target);
            }
        }
    }
}
