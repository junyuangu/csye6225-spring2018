package neu.csye6225.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by callouswander on 2018/2/27.
 */
@Entity  // This tells Hibernate to make a table out of this class
@Table( name = "picture" )
public class Picture {
    @Id
    //@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    //@Column(name = "username")
    private String username;
    //@Column(name = "picpath")
    private String picpath;

    public Picture() {}

    public Picture( String username, String picpath ) {
        Date date = new Date();
        int id = (int)date.getTime();
        id = id + (int)(Math.random()*65536);
        this.id = Math.abs(id);
        this.username = username;
        this.picpath = picpath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getPicpath() {
        return picpath;
    }

    public void setPicpath(String picpath) {
        this.picpath = picpath;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String toString() {
        return "id: " + id + ", user: " + username +
                ", picpath: " + picpath;
    }

}


