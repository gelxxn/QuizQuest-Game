package Foundations.PUZZLE;

import java.io.File;
import java.util.ArrayList;

public class PictureImport {
    private ArrayList<File> images = new ArrayList<>();
    private int currentGame = 1;

    protected void loadImages(String Path) {
        File folderPicture = new File(Path);
        File[] image = folderPicture.listFiles((dir,name) -> name.toLowerCase().endsWith(".png") || name.toLowerCase().endsWith(".jpg"));
        
        if(image != null) {
            for(File i : image) {
                images.add(i);
            }
        }
    }

    public PictureImport(int level) {
        String url = System.getProperty("user.dir") + File.separator + "Foundations\\pics\\chopped\\" + level;
        loadImages(url);
    }

    public ArrayList<File> getFolderImages() {
        return images;
    }

    public String getPicture() {
        if(currentGame+1 <= 15)
            currentGame++;
        else currentGame = 0;
        return images.get(currentGame).toString();
    }
}