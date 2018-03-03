package neu.csye6225.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * @author  Junyuan GU
 * @NUid    001825583
 */
@Entity  // This tells Hibernate to make a table out of this class
@Table(name = "description")
public class Description {
    @Id
    //@Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name = "username")
    private String username;
    @Column( name = "aboutme", columnDefinition = "VARCHAR(140)")
    private String aboutMe;

    public Description() {}

    public Description( String username, String aboutMe ) {
        Date date = new Date();
        int id = (int)date.getTime();
        id = id + (int)(Math.random()*65536);
        this.id = Math.abs(id);
        this.username = username;
        this.aboutMe = aboutMe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String toString() {
        return "id: " + id + ", user: " + username +
                ", aboutMe: " + aboutMe;
    }

}
