package models;
import java.util.*;
import play.db.jpa.*;
import play.data.validation.*;
import javax.persistence.*;

/**����Ʒ���࣬һ��Ʒ���ж��ϵ��
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
