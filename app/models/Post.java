package models;

import java.util.*;
import javax.persistence.*;

import play.data.binding.*;
import play.data.validation.*;
import play.db.jpa.Model;

@Entity
public class Post extends Model {
	
	@Required @As("yyyy-MM-dd")
	public Date postedAt;
	
	@Lob 
	@Required
	@MaxSize(1000)
	public String content;
	
	@Required
    @ManyToOne
    public User author;
    
    @OneToMany(mappedBy="post", cascade=CascadeType.ALL)
    public List<Comment> comments;
    
    public Post(User author, String content) { 
        this.comments = new ArrayList<Comment>(); 
        this.author = author;
        this.content = content;
        this.postedAt = new Date();
    }
    
    public Post previous() {
        return Post.find("postedAt < ?1 order by postedAt desc", postedAt).first();
    }

    public Post next() {
        return Post.find("postedAt > ?1 order by postedAt asc", postedAt).first();
    }
        
    public String toString() {
        return content;
    }
    
}