import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class Minitest2 {

    public static Map<Path, List<String>> fichiersParRacine(List<String> chemins){
        Map<Path, List<String>> resultat = new HashMap<>();

        for(Path p : FileSystems.getDefault().getRootDirectories()){
            resultat.put(p, new ArrayList<>());
        }

        for(String chemin : chemins) {
            Path p = Paths.get(chemin);
            if(!p.isAbsolute()) continue;

            /**********************************************************************************************************/
            /* Dans la vraie vie, on voudrait une validation dans ce genre. Pour le minitest, je ne l'exigeais pas.   */
            if(!resultat.containsKey(p.getRoot()))
                resultat.put(p.getRoot(), new ArrayList<>());
            /**********************************************************************************************************/
            resultat.get(p.getRoot()).add(p.toString());
        }

        return resultat;
    }

    public static int nbExtension(Path repertoireBase, String ext) throws IOException {
        int compteur = 0;

        // isDirectory retourne false si repertoireBase n'existe pas. Un seul test est nécessaire.
        if(!Files.isDirectory(repertoireBase)) throw new IOException("Chemin n'est pas un répertoire");

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(repertoireBase)){
            for(Path f : stream) {
                /* J'assume que l'extension est fournie sans le ".". Donc pour s'assurer que c'est bien l'extension
                * au fichier, on l'ajoute au moment de vérifier la fin de son nom.
                * *   e.g. :
                * *     nbExtension(path, "txt")
                * *         toto.txt -> est compté
                * *         mes_notes_txt -> n'est pas compté
                * */
                if(Files.isRegularFile(f) &&  f.getFileName().toString().endsWith("." + ext)) {
                    compteur++;
                }
            }
        }

        return compteur;
    }

    public static Path pwd(){
        return Paths.get("").toAbsolutePath();
    }

    public static void copieVers(Path src, Path dst) throws IOException{
        if(!Files.isDirectory(src)) throw new RuntimeException();
        if(Files.exists(dst)) throw new RuntimeException();

        Files.createDirectories(dst);

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(src, (Path p) -> Files.isRegularFile(p))){
            for(Path f : stream) {
                Path target = dst.resolve(f.getFileName());
                Files.copy(f, target);
            }
        }
    }

    /* Solution à la reprise */

    public static Map<Path, Path> cheminPlusLongParRacines(){
        Map<Path, Path> resultat = new HashMap<>();

        for(Path root : FileSystems.getDefault().getRootDirectories()) {
            resultat.put(root, cheminPlusLongParBase(root));
        }

        return resultat;
    }

    public static Path cheminPlusLongParBase(Path base) {
        Path retour = base;

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(base)) {
            for ( Path p : stream ) {
                if(Files.isDirectory(p)) {
                    Path deepFile = cheminPlusLongParBase(p);
                    if(deepFile.getNameCount() > retour.getNameCount()) retour = deepFile;
                }
            }

        } catch (IOException ex) {}

        return retour;
    }

    public static List<Path> avecExtension(Path base, List<String> exts) throws IOException{
        List<Path> resultat = new ArrayList<>();

        if(!Files.isDirectory(base)) throw new IOException("Chemin n'est pas un répertoire");

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(base)){
            for(Path f : stream) {
                for(String ext : exts) {
                    if (Files.isRegularFile(f) && f.getFileName().toString().endsWith("." + ext)) {
                        resultat.add(f);
                        break;
                    }
                }
            }
        }

        return resultat;
    }

    public static void supprimeDe(Path src, Path dst) throws IOException{
        if(!Files.isDirectory(src)) throw new RuntimeException();
        if(!Files.isDirectory(dst)) throw new RuntimeException();

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(src, (Path p) -> Files.isRegularFile(p))){
            for(Path f : stream) {
                Path target = dst.resolve(f.getFileName());
                Files.deleteIfExists(target);
            }
        }
    }
}
