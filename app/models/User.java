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

	public boolean isCarOwner;//是否为车主

	public String authType;

	public String authPictiurePath;//认证的图片路径

	public String idcardPictiurePath;//认证的图片路径

	public boolean isDeal;//是否已经被处理了认证

	public int integration; //积分

	@As("yyyy-MM-dd HH:mm:ss")
	public Date authDate; //认证日期

	@As("yyyy-MM-dd HH:mm:ss")
	public Date applyDate; //申请认证日期

	@OneToMany(mappedBy="user", cascade=CascadeType.ALL)
    public List<MusicComment> musicComments;
   
    public User(String email, String password, String username) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.isAdmin = false;
    }

    public String toString()  {
        return username ;
    }

    
}
