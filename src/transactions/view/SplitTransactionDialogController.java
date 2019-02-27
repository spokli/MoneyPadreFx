package transactions.view;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import misc.Validator;
import transactions.model.Category;
import transactions.model.DataModel;
import transactions.model.Transaction;
import transactions.model.Transaction.Currency;

public class SplitTransactionDialogController extends GeneralController {

	@FXML
	private TableView<Transaction> splitPositionsTable;
	@FXML
	private TableColumn<Transaction, LocalDate> col_transactionDate;
	@FXML
	private TableColumn<Transaction, LocalDate> col_valueDate;
	@FXML
	private TableColumn<Transaction, String> col_usageText;
	@FXML
	private TableColumn<Transaction, Double> col_value;
	@FXML
	private TableColumn<Transaction, Category> col_category;
	@FXML
	private TableColumn<Transaction, String> col_note;
	@FXML
	private Button btn_add;
	@FXML
	private Button btn_ok;
	@FXML
	private TextField txt_sum;
	@FXML
	private TextField txt_sumGoal;

	Transaction currentTransaction;

	@FXML
	private void initialize() {
		
		txt_sum.setText("0.0");
		txt_sumGoal.setText("0.0");

		splitPositionsTable.setEditable(true);
		splitPositionsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		col_transactionDate.setCellValueFactory(cellData -> cellData.getValue().transactionDateProperty());
		col_transactionDate.setCellFactory(column -> new DateEditingCell());
		col_transactionDate.setMinWidth(100);

		col_valueDate.setCellValueFactory(cellData -> cellData.getValue().valueDateProperty());
		col_valueDate.setCellFactory(column -> new DateEditingCell());
		col_valueDate.setMinWidth(100);

		col_usageText.setCellValueFactory(cellData -> cellData.getValue().usageTextProperty());
		col_usageText.setCellFactory(column -> new StringEditingCell());
		col_usageText.setMinWidth(200);

		col_value.setCellValueFactory(cellData -> cellData.getValue().valueProperty().asObject());
		col_value.setCellFactory(column -> new DoubleEditingCell());
		col_value.setMinWidth(60);

		// After value is given by user, calculate the value difference
		col_value.setOnEditCommit((TableColumn.CellEditEvent<Transaction, Double> t) -> {
			Transaction current = (Transaction) t.getTableView().getItems().get(t.getTablePosition().getRow());

			double oldSum = Double.parseDouble(txt_sum.getText());

			double newSum = oldSum + current.valueProperty().get();
			txt_sum.setText("" + newSum);

			double difference = newSum - oldSum;

			double newGoal = Double.parseDouble(txt_sumGoal.getText()) + difference;
		});

		col_category.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
		col_category.setCellFactory(column -> new ComboBoxEditingCell());
		col_category.setMinWidth(80);

	}

	@Override
	public void initModel(DataModel model) {
		super.initModel(model);

		// Get current transaction to prefill the table
		this.currentTransaction = model.currentTransactionProperty().get();

		txt_sumGoal.setText("" + Math.abs(this.currentTransaction.valueProperty().get()));

		ObservableList<Transaction> splitPositions = FXCollections.observableArrayList();
		splitPositions.add(this.currentTransaction);

		splitPositionsTable.setItems(splitPositions);

		// Create one new split transaction as a default
		// Transaction newSplitPosition = this.currentTransaction.copy();
		// newSplitPosition.parentIdProperty().set(this.currentTransaction.getId());
		//
		//
		//
		//
		// splitPositionsTable.setItems(this.currentTransaction);

	}

	@FXML
	private void handleOk() {
		// Wenn ok gedrückt und Summe korrekt aufgeteilt:
		// Setze currentTransaction.value auf 0
		// Speichere alle SplitPositions und update die currentTransaction
	}

	@FXML
	private void handleAdd() {
		Transaction newSplitPosition = this.currentTransaction.copy();
		newSplitPosition.parentIdProperty().set(this.currentTransaction.getId());

		ObservableList<Transaction> itemList = splitPositionsTable.getItems();
		itemList.add(newSplitPosition);

		// splitPositionsTable.setItems(itemList);
	}

	// ########################################################
	class ComboBoxEditingCell extends TableCell<Transaction, Category> {

		private ComboBox<Category> comboBox;

		private ComboBoxEditingCell() {
		}

		@Override
		public void startEdit() {
			if (!isEmpty()) {
				super.startEdit();
				createComboBox();
				setText(null);
				setGraphic(comboBox);
			}
		}

		@Override
		public void cancelEdit() {
			super.cancelEdit();

			setText(getCategory().categoryNameProperty().get());
			setGraphic(null);
		}

		@Override
		public void updateItem(Category item, boolean empty) {
			super.updateItem(item, empty);

			if (empty) {
				setText(null);
				setGraphic(null);
			} else {
				if (isEditing()) {
					if (comboBox != null) {
						comboBox.setValue(getCategory());
					}
					setText(getCategory().categoryNameProperty().get());
					setGraphic(comboBox);
				} else {
					setText(getCategory().categoryNameProperty().get());
					setGraphic(null);
				}
			}
		}

		private void createComboBox() {
			comboBox = new ComboBox<>(model.getCategoryList());
			comboBoxConverter(comboBox);
			comboBox.valueProperty().set(getCategory());
			comboBox.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
			comboBox.setOnAction((e) -> {
				System.out.println("Committed: " + comboBox.getSelectionModel().getSelectedItem());
				commitEdit(comboBox.getSelectionModel().getSelectedItem());
			});
			comboBox.focusedProperty().addListener(
					(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
						if (!newValue) {
							commitEdit(comboBox.getSelectionModel().getSelectedItem());
						}
					});
		}

		private void comboBoxConverter(ComboBox<Category> comboBox) {
			// Define rendering of the list of values in ComboBox drop down.
			comboBox.setCellFactory((c) -> {
				return new ListCell<Category>() {
					@Override
					protected void updateItem(Category item, boolean empty) {
						super.updateItem(item, empty);

						if (item == null || empty) {
							setText(null);
						} else {
							setText(item.categoryNameProperty().get());
						}
					}
				};
			});
		}

		private Category getCategory() {
			return getItem() == null ? new Category(-1, "", false) : getItem();
		}
	}

	// ################################################
	class DateEditingCell extends TableCell<Transaction, LocalDate> {

		private DatePicker datePicker;

		private DateEditingCell() {
		}

		@Override
		public void startEdit() {
			if (!isEmpty()) {
				super.startEdit();
				createDatePicker();
				setText(null);
				setGraphic(datePicker);
			}
		}

		@Override
		public void cancelEdit() {
			super.cancelEdit();

			setText(getDate().toString());
			setGraphic(null);
		}

		@Override
		public void updateItem(LocalDate item, boolean empty) {
			super.updateItem(item, empty);

			if (empty) {
				setText(null);
				setGraphic(null);
			} else {
				if (isEditing()) {
					if (datePicker != null) {
						datePicker.setValue(getDate());
					}
					setText(null);
					setGraphic(datePicker);
				} else {
					setText(getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
					setGraphic(null);
				}
			}
		}

		private void createDatePicker() {
			datePicker = new DatePicker(getDate());
			datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
			datePicker.setOnAction((e) -> {
				System.out.println("Committed: " + datePicker.getValue().toString());
				commitEdit(datePicker.getValue());
			});
			datePicker.focusedProperty().addListener(
					(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
						if (!newValue) {
							commitEdit(datePicker.getValue());
						}
					});
		}

		private LocalDate getDate() {
			return getItem() == null ? LocalDate.now() : getItem();
		}
	}

	// ################################################
	class DoubleEditingCell extends TableCell<Transaction, Double> {

		private TextField valueInput;

		private DoubleEditingCell() {
			super();

			addEventFilter(KeyEvent.KEY_TYPED, new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent event) {
					if (!Validator.validatePositiveDouble(getText())) {
						event.consume();
					}
				}
			});

			textProperty().addListener(new ChangeListener<String>() {
				@Override
				public void changed(ObservableValue<? extends String> observableValue, String oldValue,
						String newValue) {
					if (!Validator.validatePositiveDouble(newValue)) {
						setText(oldValue);
					}
				}
			});
		}

		@Override
		public void startEdit() {
			if (!isEmpty()) {
				super.startEdit();
				createTextField();
				setText(null);
				setGraphic(valueInput);
			}
		}

		@Override
		public void cancelEdit() {
			super.cancelEdit();

			setText(getValue().toString());
			setGraphic(null);
		}

		@Override
		public void updateItem(Double item, boolean empty) {
			super.updateItem(item, empty);

			if (empty) {
				setText(null);
				setGraphic(null);
			} else {
				if (isEditing()) {
					if (valueInput != null) {
						valueInput.setText(getValue().toString());
					}
					setText(null);
					setGraphic(valueInput);
				} else {
					setText(getValue().toString());
					setGraphic(null);
				}
			}
		}

		private void createTextField() {
			valueInput = new TextField(getValue().toString());
			valueInput.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
			valueInput.setOnAction((e) -> {
				System.out.println("Committed: " + valueInput.getText().toString());
				commitEdit(Double.parseDouble(valueInput.getText()));
			});
			valueInput.focusedProperty().addListener(
					(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
						if (!newValue) {
							commitEdit(Double.parseDouble(valueInput.getText()));
						}
					});
		}

		private Double getValue() {
			return getItem() == null ? 0.0 : getItem();
		}
	}

	// ################################################
	class StringEditingCell extends TableCell<Transaction, String> {

		private TextField stringInput;

		private StringEditingCell() {
		}

		@Override
		public void startEdit() {
			if (!isEmpty()) {
				super.startEdit();
				createTextField();
				setText(null);
				setGraphic(stringInput);
			}
		}

		@Override
		public void cancelEdit() {
			super.cancelEdit();

			setText(getValue());
			setGraphic(null);
		}

		@Override
		public void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);

			if (empty) {
				setText(null);
				setGraphic(null);
			} else {
				if (isEditing()) {
					if (stringInput != null) {
						stringInput.setText(getValue());
					}
					setText(null);
					setGraphic(stringInput);
				} else {
					setText(getValue());
					setGraphic(null);
				}
			}
		}

		private void createTextField() {
			stringInput = new TextField(getValue());
			stringInput.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
			stringInput.setOnAction((e) -> {
				System.out.println("Committed: " + stringInput.getText());
				commitEdit(stringInput.getText());
			});
			stringInput.focusedProperty().addListener(
					(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
						if (!newValue) {
							commitEdit(stringInput.getText());
						}
					});
		}

		private String getValue() {
			return getItem() == null ? "" : getItem();
		}
	}

}
