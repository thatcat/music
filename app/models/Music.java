package models;
import java.util.*;
import play.db.jpa.*;
import play.data.validation.*;
import javax.persistence.*;
import play.data.binding.*;

@Entity
@Table(name="Music")
public class Music extends Model {
    
    @Required
    @MaxSize(15)
    @MinSize(1)
	public String musicName="未知歌曲";

    @MaxSize(30)
    public String player="未知歌手";
    
    @MaxSize(8)
    public int type=1;//所属类别：国语歌为1、粤语歌为2、英文歌为3、韩文歌为4

	@MaxSize(15)
	public String albums="未知专辑";//所属专辑
    
    public boolean isDelete=false;

	public int playCount=0;//播放次数

	public int parseCount=0;//点赞数

	public String path="defaultPath";//上传的路径

	@As("yyyy-MM-dd HH:mm:ss")
	public Date publicDate; //出版日期

	@As("yyyy-MM-dd HH:mm:ss")
	public Date uploadDate; //上传日期

	@OneToMany(mappedBy="music", cascade=CascadeType.ALL)
    public List<MusicComment> musicComments;
   
    public Music() {

    }

    public String toString()  {
        return musicName ;
    }

    
}
