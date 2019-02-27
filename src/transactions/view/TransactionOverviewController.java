package transactions.view;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.text.DateFormatter;

import database.SQLiteJDBC;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import misc.CustomDate;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableRow;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import transactions.MainApp;
import transactions.model.DataModel;
import transactions.model.Transaction;
import transactions.model.Transaction.TransactionStatus;
import transactions.model.Transaction.TransactionType;

public class TransactionOverviewController extends GeneralController {
	@FXML
	private TreeTableView<Transaction> transactionTable;
	@FXML
	private TreeTableColumn<Transaction, LocalDate> col_transactionDate;
	@FXML
	private TreeTableColumn<Transaction, String> col_transactionType;
	@FXML
	private TreeTableColumn<Transaction, String> col_usageText;
	@FXML
	private TreeTableColumn<Transaction, Double> col_value;
	@FXML
	private TreeTableColumn<Transaction, String> col_status;
	@FXML
	private TreeTableColumn<Transaction, String> col_category;

	@Override
	public void initModel(DataModel model) {
		super.initModel(model);

		// transactionTable.setItems(model.getTransactionList());
		
		TreeItem<Transaction> hiddenRootItem = new TreeItem<Transaction>(null);
		transactionTable.setRoot(hiddenRootItem);

		// Convert Transactions to treeItems and add to root item
		Iterator<Transaction> iter = model.getTransactionList().iterator();
		while (iter.hasNext()) {
			TreeItem<Transaction> item = new TreeItem<Transaction>(iter.next());
			hiddenRootItem.getChildren().add(item);
		}

		// Set current transaction in model, after clicking on an item
		transactionTable.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldItem, newItem) -> model.setCurrentTransaction(newItem.getValue()));

		// React to external changes of the current transaction
		model.currentTransactionProperty().addListener((obs, oldTransaction, newTransaction) -> {
			if (newTransaction == null) {
				transactionTable.getSelectionModel().clearSelection();
			} else {
				// transactionTable.getSelectionModel().select(newTransaction);

				newTransaction.categoryProperty().addListener((obs2, oldId, newId) -> {
					if (newId == null) {
					} else {
						transactionTable.refresh();
					}
				});
			}
		});

	}

	/**
	 * The constructor. The constructor is called before the initialize()
	 * method.
	 */
	public TransactionOverviewController() {
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

		// col_transactionDate.setCellValueFactory(cellData ->
		// cellData.getValue().getValue().transactionDateProperty());
		col_transactionDate.setCellValueFactory(
				new Callback<CellDataFeatures<Transaction, LocalDate>, ObservableValue<LocalDate>>() {
					public ObservableValue<LocalDate> call(CellDataFeatures<Transaction, LocalDate> p) {
						if (p.getValue().getValue() == null) {
							return null;
						} else {
							return p.getValue().getValue().transactionDateProperty();
						}
					}
				});
		col_transactionDate.setCellFactory((TreeTableColumn<Transaction, LocalDate> column) -> {
			return new TreeTableCell<Transaction, LocalDate>() {
				@Override
				protected void updateItem(LocalDate item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
					} else {
						setText(item.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
					}
				}
			};
		});
		// col_transactionType.setCellValueFactory(cellData ->
		// cellData.getValue().getValue().transactionTypeProperty().asString());
		col_transactionType
				.setCellValueFactory(new Callback<CellDataFeatures<Transaction, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Transaction, String> p) {
						if (p.getValue().getValue() == null) {
							return null;
						} else {
							return p.getValue().getValue().transactionTypeProperty().asString();
						}
					}
				});
		// col_usageText.setCellValueFactory(cellData ->
		// cellData.getValue().getValue().usageTextProperty());
		col_usageText
				.setCellValueFactory(new Callback<CellDataFeatures<Transaction, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Transaction, String> p) {
						if (p.getValue().getValue() == null) {
							return null;
						} else {
							return p.getValue().getValue().usageTextProperty();
						}
					}
				});
		// col_value.setCellValueFactory(cellData ->
		// cellData.getValue().getValue().valueProperty().asObject());
		col_value.setCellValueFactory(new Callback<CellDataFeatures<Transaction, Double>, ObservableValue<Double>>() {
			public ObservableValue<Double> call(CellDataFeatures<Transaction, Double> p) {
				if (p.getValue().getValue() == null) {
					return null;
				} else {
					return p.getValue().getValue().valueProperty().asObject();
				}
			}
		});
		// col_status.setCellValueFactory(cellData ->
		// cellData.getValue().getValue().statusProperty().asString());
		col_status.setCellValueFactory(new Callback<CellDataFeatures<Transaction, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(CellDataFeatures<Transaction, String> p) {
				if (p.getValue().getValue() == null) {
					return null;
				} else {
					return p.getValue().getValue().statusProperty().asString();
				}
			}
		});
		// col_category.setCellValueFactory(cellData ->
		// model.getCategoryById(cellData.getValue().getValue().categoryIdProperty().get()).categoryNameProperty());
		col_category
				.setCellValueFactory(new Callback<CellDataFeatures<Transaction, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Transaction, String> p) {
						if (p.getValue().getValue() == null) {
							return null;
						} else {
//							return model.getCategoryById(p.getValue().getValue().categoryIdProperty().get())
//									.categoryNameProperty();
							return p.getValue().getValue().categoryProperty().get().categoryNameProperty();
						}
					}
				});

		
		transactionTable.setRowFactory(new Callback<TreeTableView<Transaction>, TreeTableRow<Transaction>>() {
			@Override
			public TreeTableRow<Transaction> call(TreeTableView<Transaction> tableView) {
				final TreeTableRow<Transaction> row = new TreeTableRow<>();

				// Set context menu factory
				final ContextMenu rowMenu = new ContextMenu();

				MenuItem splitItem = new MenuItem("Split");
				splitItem.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						showSplitDialog(row.getItem());
					}
				});

				rowMenu.getItems().addAll(splitItem);

				// only display context menu for non-null items:
				row.contextMenuProperty().bind(Bindings.when(Bindings.isNotNull(row.itemProperty())).then(rowMenu)
						.otherwise((ContextMenu) null));
				return row;
			}
		});
	}

	private void showSplitDialog(Transaction t) {
		if (!t.transactionTypeProperty().get().equals(TransactionType.EIGENEKREDITKARTENABRECHN)) {
			// TODO: Dialog: "Only credit card positions can be split"
			return;
		}
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/SplitTransactionDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Split Transaction");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			// dialogStage.initOwner(stage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			SplitTransactionDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(mainApp);
			controller.initModel(model);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();


		} catch (IOException e) {
			e.printStackTrace();
			// return false;
		}
	}
}
