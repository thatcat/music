package models;
import java.util.*;
import play.db.jpa.*;
import play.data.validation.*;
import javax.persistence.*;

/**汽车品牌类，一个品牌有多个系列
  */
@Entity
@Table(name="CarBrand")
public class CarBrand extends Model {
    
    @Required
    @MaxSize(15)
    public String brandName;
    
    @Required
    public int parise;

	@OneToMany(mappedBy="brandName", cascade=CascadeType.ALL)
    public List<CarSeries> carSeries;

    public CarBrand(String brandName,int parise ) {
	this.brandName=brandName;
	this.parise=parise;
    }

    public String toString()  {
        return brandName ;
    }
}
