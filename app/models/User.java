package models;
import java.util.*;
import play.db.jpa.*;
import play.data.validation.*;
import javax.persistence.*;
import play.data.binding.*;

@Entity
@Table(name="User")
public class User extends Model {
    
    @Required
    @MaxSize(15)
    @MinSize(2)
    public String username;
    
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

	@OneToMany(mappedBy="user", cascade=CascadeType.ALL)
    public List<MusicComment> musicComments;
   
    public User() {
        this.isAdmin = false;
    }

    public String toString()  {
        return username ;
    }

    
}
