package nanodegree.regi.spotifystreamer;

/**
 * Created by Regardt on 2015-06-06.
 */
public class Artist {

    private String name;
    private String imgURL;
    private String id;

    public Artist(){}

    public Artist(String name,String imgURL,String id){
        this.name = name;
        this.imgURL = imgURL;
        this.id = id;
    }

    public String getName() {return name;}
    public String getImgURL() {return imgURL;}
    public String getId() {return id;}

    public void setName(String name) {this.name = name;}
    public void setImgURL(String imgURL) {this.imgURL = imgURL;}
    public void setId(String id) {this.id = id;}
}
