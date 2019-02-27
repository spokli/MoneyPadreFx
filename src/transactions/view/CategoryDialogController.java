package transactions.view;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Optional;

import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import transactions.MainApp;
import transactions.model.Category;
import transactions.model.DataModel;

public class CategoryDialogController extends GeneralController {

	@FXML
	private TableView<Category> categoryTable;
	@FXML
	private TableColumn<Category, Integer> col_categoryId;
	@FXML
	private TableColumn<Category, String> col_categoryName;
	@FXML
	private TableColumn<Category, Button> col_delete;
	@FXML
	private TableColumn<Category, Button> col_edit;
	@FXML
	private TableColumn<Category, Boolean> col_default;
	@FXML
	private TextField txt_newCategoryName;
	@FXML
	private Button btn_addNewCategory;

	final ToggleGroup radioButtonGroup = new ToggleGroup();

	private enum ButtonType {
		EDIT, DELETE
	};

	@Override
	public void initModel(DataModel model) {
		super.initModel(model);
		categoryTable.setItems(model.getCategoryList());
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		col_categoryId.setCellValueFactory(cellData -> cellData.getValue().categoryIdProperty().asObject());
		col_categoryName.setCellValueFactory(cellData -> cellData.getValue().categoryNameProperty());

		// Place delete-buttons in each row
		addButtons(ButtonType.EDIT);
		addButtons(ButtonType.DELETE);
		addDefaultButtons();
	}

	@FXML
	private void handleAddNewCategory() {
		String categoryText = txt_newCategoryName.getText();
		Category dummyCat = new Category(-1, categoryText, false);
		if (categoryText.equals("")) {
			// TODO: Dialog "Enter a category name"
			return;
		}

		if(model.getCategoryByName(categoryText) != null){
			// TODO: Dialog "Category exists"
			return;
		}

		model.addCategory(dummyCat);
		txt_newCategoryName.clear();
	}

	@FXML
	private void handleKeyPressed(KeyEvent ke) {
		if (ke.getCode().equals(KeyCode.ENTER)) {
			handleAddNewCategory();
		}
	}

	private void addButtons(ButtonType type) {
		TableColumn<Category, Button> column = null;
		String tmpLabel = "";

		switch (type) {
		case DELETE:
			column = col_delete;
			tmpLabel = "Delete";
			break;
		case EDIT:
			column = col_edit;
			tmpLabel = "Edit";
			break;
		}
		final String buttonLabel = tmpLabel;

		column.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

		Callback<TableColumn<Category, Button>, TableCell<Category, Button>> cellFactory = //
				new Callback<TableColumn<Category, Button>, TableCell<Category, Button>>() {
					@Override
					public TableCell<Category, Button> call(final TableColumn<Category, Button> param) {
						final TableCell<Category, Button> cell = new TableCell<Category, Button>() {

							final Button btn = new Button(buttonLabel);

							@Override
							public void updateItem(Button item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else {

									btn.setOnAction(event -> {
										Category cat = getTableView().getItems().get(getIndex());
										if (type.equals(ButtonType.DELETE)) {
											deleteCategory(cat);
										} else if (type.equals(ButtonType.EDIT)) {
											editCategory(cat);
										}
									});

									setGraphic(btn);
									setText(null);
								}
							}
						};
						return cell;
					}
				};

		column.setCellFactory(cellFactory);
	}

	
	private void addDefaultButtons() {
		col_default.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));

		Callback<TableColumn<Category, Boolean>, TableCell<Category, Boolean>> cellFactory = //
				new Callback<TableColumn<Category, Boolean>, TableCell<Category, Boolean>>() {
					@Override
					public TableCell<Category, Boolean> call(final TableColumn<Category, Boolean> param) {
						final TableCell<Category, Boolean> cell = new TableCell<Category, Boolean>() {

							final RadioButton radioButton = new RadioButton();

							@Override
							protected void updateItem(Boolean item, boolean empty) {
								super.updateItem(item, empty);

								if (empty) {
									setGraphic(null);
									setText(null);
								} else {
									if (radioButton.getToggleGroup() == null) {
										radioButton.setToggleGroup(radioButtonGroup);
									}

									if (this.getTableRow().getItem() != null) {
										Category cat = (Category) this.getTableRow().getItem();
										radioButton.setSelected(cat.categoryDefaultProperty().get());
									}

									radioButton.setOnAction(event -> {
										Category cat = getTableView().getItems().get(getIndex());
										model.setDefaultCategory(cat);
									});
									setGraphic(radioButton);
									setText(null);
								}
							}
						};
						return cell;
					}
				};

		col_default.setCellFactory(cellFactory);

	}

	private void deleteCategory(Category cat) {
		if (cat.categoryDefaultProperty().get()) {
			// TODO: Message: Cannot delete the default category
			return;
		}
		model.deleteCategory(cat);
	}

	private void editCategory(Category cat) {
		// Open textinput for category name
		TextInputDialog dialog = new TextInputDialog(cat.categoryNameProperty().get());
		dialog.setTitle("Edit category");
		dialog.setContentText("New category name:");
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			String newName = result.get();
			if(newName.equals("") || model.getCategoryByName(newName) != null){
				// TODO: Dialog "Category exists"
				return;
			}

			cat.categoryNameProperty().set(result.get());
			model.updateCategory(cat);
		}
		
	}
}
