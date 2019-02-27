package transactions.view;

import java.time.LocalDate;
import java.util.HashMap;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import transactions.model.Category;
import transactions.model.DataModel;

public class StatisticsDialogController extends GeneralController {

	@FXML
	DatePicker dt_from;
	@FXML
	DatePicker dt_to;
	@FXML
	TableView<Category> tbl_data;
	@FXML
	TableColumn<Category, String> col_category;
	@FXML
	TableColumn<Category, Double> col_value;

	HashMap<Category, Double> statistics = new HashMap<Category, Double>();

	@Override
	public void initModel(DataModel model) {
		super.initModel(model);

		tbl_data.setItems(model.getCategoryList());

		refreshData();
		// tbl_data.getSelectionModel().selectedItemProperty()
		// .addListener((obs, oldTransaction, newTransaction) ->
		// model.setCurrentTransaction(newTransaction));
	}

	@FXML
	private void initialize() {
		// HashMap<Category, Double> statistics =
		// model.getCategoryBalances(dt_from.getValue(), dt_to.getValue());

		col_category.setCellValueFactory(cellData -> cellData.getValue().categoryNameProperty());

		col_value.setCellValueFactory(new Callback<CellDataFeatures<Category, Double>, ObservableValue<Double>>() {
			public ObservableValue<Double> call(CellDataFeatures<Category, Double> p) {
				Double value = statistics.get(p.getValue());
				if (value == null) {
					value = 0.0;
				}
				return new ReadOnlyObjectWrapper<Double>(value);
			};
		});

		dt_from.setValue(LocalDate.now());
		dt_to.setValue(LocalDate.now());

	}

	@FXML
	private void onFromDateChanged() {
		refreshData();
	}

	@FXML
	private void onToDateChanged() {
		refreshData();
	}

	private void refreshData() {
		statistics = model.getCategoryBalances(dt_from.getValue(), dt_to.getValue());
		tbl_data.refresh();
	}

}
