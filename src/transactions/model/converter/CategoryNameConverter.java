package transactions.model.converter;

import javafx.util.StringConverter;
import transactions.model.Category;
import transactions.model.DataModel;

public class CategoryNameConverter extends StringConverter<Integer> {

	DataModel model;

	public CategoryNameConverter(DataModel model) {
		this.model = model;
	}

	@Override
	public String toString(Integer categoryId) {
		if (categoryId == null) {
			return null;
		} else {
			return model.getCategoryById(categoryId).categoryNameProperty().get();
		}
	}

	@Override
    public Integer fromString(String categoryString) {
        Category cat = model.getCategoryByName(categoryString) ;
        if(cat != null){
        	return cat.categoryIdProperty().get();
        }
    	return null;
    }

}
