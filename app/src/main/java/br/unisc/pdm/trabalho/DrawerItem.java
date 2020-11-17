package br.unisc.pdm.trabalho;

public class DrawerItem {

    String ItemName;
    int imgResID;
    String title;
    boolean isImage;

    public DrawerItem(String itemName, int imgResID) {
        ItemName = itemName;
        this.imgResID = imgResID;
    }

    public DrawerItem(boolean isImage) {
        this(null, 0);
        this.isImage = isImage;
    }

    public String getItemName() {
        return ItemName;
    }

    public int getImgResID() {
        return imgResID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isImage(){
        return  isImage;
    }

}
