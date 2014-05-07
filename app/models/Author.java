package models;
import java.util.*;
import play.db.jpa.*;
import play.data.validation.*;
import javax.persistence.*;
import play.data.binding.*;

@Entity
@Table(name="Author")
public class Author extends Model {
    
    @Required
    @MaxSize(15)
    @MinSize(2)
    public String authorName;
    
    @Required
    @MaxSize(15)
    @MinSize(2)
    public String password;
    
    @Required
    @MaxSize(100)
	@Email
    public String email;
    
    public boolean isAdmin;

	public int integration; //积分

	@As("yyyy-MM-dd HH:mm:ss")
	public Date registerDate; //注册日期

	@OneToMany(mappedBy="Author", cascade=CascadeType.ALL)
    public List<MusicComment> musicComments;
   
    public Author() {
        this.isAdmin = false;
    }

    public String toString()  {
        return authorName ;
    }

    
}
