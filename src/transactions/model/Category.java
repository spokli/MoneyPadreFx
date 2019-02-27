package transactions.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Category {

	private IntegerProperty categoryId;
	private StringProperty categoryName;
	private BooleanProperty categoryDefault;

	public Category(Integer id, String name, boolean category_default) {
		this.categoryId = new SimpleIntegerProperty(id);
		this.categoryName = new SimpleStringProperty(name);
		this.categoryDefault = new SimpleBooleanProperty(category_default);
	}


	public IntegerProperty categoryIdProperty() {
		return this.categoryId;
	}

	public StringProperty categoryNameProperty() {
		return this.categoryName;
	}
	
	public BooleanProperty categoryDefaultProperty(){
		return this.categoryDefault;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Category) {
			Category c = (Category) o;
			return c.categoryIdProperty().get() == this.categoryIdProperty().get();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
	    return categoryName.hashCode();
	}
	
	@Override
	public String toString(){
		return this.categoryNameProperty().get();
	}
	
	public String asString(){
		return toString();
	}
}
