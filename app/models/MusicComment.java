package models;
import java.util.*;
import play.db.jpa.*;
import play.data.validation.*;
import javax.persistence.*;
import play.data.binding.*;

/**歌曲点评类
  */
@Entity
@Table(name="MusicComment")
public class MusicComment extends Model {
    
    @Required
    public String commentContent;
    
	@Required @As("yyyy-MM-dd")
	public Date commentTime;

	@Required
    @ManyToOne
    public Music music;

	@Required
    @ManyToOne
    public Author author;


    public MusicComment() {

    }

    public String toString()  {
        return commentContent ;
    }
}
