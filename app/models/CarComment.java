package models;
import java.util.*;
import play.db.jpa.*;
import play.data.validation.*;
import javax.persistence.*;
import play.data.binding.*;

/**汽车点评类
  */
@Entity
@Table(name="CarComment")
public class CarComment extends Model {
    
    @Required
    public String commentType;
    
	@Required @As("yyyy-MM-dd")
	public Date carCommentTime;

	@Required
    @ManyToOne
    public CarSeries seriesName;

	@Required
    @ManyToOne
    public User user;


    public CarComment(String commentType ) {
	this.commentType=commentType;
	this.carCommentTime=carCommentTime;

    }

    public String toString()  {
        return commentType ;
    }
}
