import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;

public class AccessUpdateStaffTable extends Application {
	
	private PreparedStatement preparedStatementinsert;
	private PreparedStatement preparedStatementUpdate;
	private PreparedStatement statement;
	private TextField tfID = new TextField();
	private TextField tfLastName = new TextField();
	private TextField tfFirstName = new TextField();
	private TextField tfMI = new TextField();
	private TextField tfAddress = new TextField();
	private TextField tfCity = new TextField();
	private TextField tfState = new TextField();
	private TextField tfTelephone = new TextField();
	private TextField tfEmail = new TextField();
	
	
	@Override
	public void start(Stage primaryStage) {
		// Initialize database connection and create a Statement object
		initializeDB();
		
		// Create 4 buttons
		Button viewBtn = new Button("View");
		Button insertBtn = new Button("Insert");
		Button updateBtn = new Button("Update");
		Button clearBtn = new Button("Clear");
		
		// Create HBoxes and populate them
		HBox row1 = new HBox(5);
		HBox row2 = new HBox(5);
		HBox row3 = new HBox(5);
		HBox row4 = new HBox(5);
		HBox row5 = new HBox(5);
		HBox row6 = new HBox(5);
		
		// Add rows and TextFields to rows
		tfMI.setPrefWidth(50);
		row1.getChildren().addAll(new Label("ID"), tfID);
		row2.getChildren().addAll(new Label("Last Name"), tfLastName, new Label("First Name"), tfFirstName, new Label("MI"), tfMI);
		row3.getChildren().addAll(new Label("Address"), tfAddress);
		row4.getChildren().addAll(new Label("City"), tfCity, new Label("State"), tfState);
		row5.getChildren().addAll(new Label("Telephone"), tfTelephone, new Label("Email"), tfEmail);
		row6.getChildren().addAll((viewBtn), (insertBtn), (updateBtn), (clearBtn));
		row6.setAlignment(Pos.CENTER);
		
		// add rows to VBox
		VBox vBox = new VBox(15);
		vBox.getChildren().addAll(row1, row2, row3, row4, row5, row6);
		vBox.setPadding(new Insets(15, 12, 15, 12));
		
		// Create events on buttons
		viewBtn.setOnAction(e -> showStaff());
		insertBtn.setOnAction(e -> insertStaff());
		updateBtn.setOnAction(e -> updateStaff());
		clearBtn.setOnAction(e -> clearStaff());
		
		// Create scene and place it in the stage
		Scene scene = new Scene(vBox, 650, 300);
		primaryStage.setTitle("Staff");
		primaryStage.setScene(scene);
		primaryStage.show();

	
	}
	
	public static void main(String[] args) {
		Application.launch(args);

	}
	
	private void initializeDB() {
		try {
			// Load the JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver loaded");
			
			// Establish a connection
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/javabook", "username", "password");
			connection.setAutoCommit(false);
			System.out.println("Database connected");
			
			
			String queryString = "select * from staff where id = ?";
			statement = connection.prepareStatement(queryString);
			
			
			
			// SQL insert query string and store in preparedStatmentinsert to use in insertStaff()
			String queryStringInsert = ("insert into staff (id, lastName, firstName, mi, address, city, state, telephone, email) " +
					"values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
			preparedStatementinsert = connection.prepareStatement(queryStringInsert);
			
			
			// SQL update query and store in preparedStatementUpdate to use in updateStaff()
			String queryStringUpdate = ("update staff set lastName = ?, firstName = ?, mi = ?, address = ?, city = ?, state = ?,"
					+ " telephone = ?, email = ?"
					+ " where id = ?");
			preparedStatementUpdate = connection.prepareStatement(queryStringUpdate);
			
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	
	private void showStaff() {
		String ID = tfID.getText();
		
		try {
			statement.setString(1, ID);
			
			ResultSet rset = statement.executeQuery();
			
			if (rset.next()) {
				String lastName = rset.getString(2);
				String firstName = rset.getString(3);
				String MI = rset.getString(4);
				String address = rset.getString(5);
				String city = rset.getString(6);
				String state = rset.getString(7);
				String telephone = rset.getString(8);
				String email = rset.getString(9);
				
				tfLastName.setText(lastName);
				tfFirstName.setText(firstName);
				tfMI.setText(MI);
				tfAddress.setText(address);
				tfCity.setText(city);
				tfState.setText(state);
				tfTelephone.setText(telephone);
				tfEmail.setText(email);
			}
			else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText("Error");
				alert.setContentText("Sorry that record was not found");
				alert.showAndWait();
			}
			
		} 
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void insertStaff() {
		String ID = tfID.getText();
		String lastName = tfLastName.getText();
		String firstName = tfFirstName.getText();
		String MI = tfMI.getText();
		String address = tfAddress.getText();
		String city = tfCity.getText();
		String state = tfState.getText();
		String telephone = tfTelephone.getText();
		String email = tfEmail.getText();
		
		
		try {
		
		
		preparedStatementinsert.setString(1, ID);
		preparedStatementinsert.setString(2, lastName);
		preparedStatementinsert.setString(3, firstName);
		preparedStatementinsert.setString(4, MI);
		preparedStatementinsert.setString(5, address);
		preparedStatementinsert.setString(6, city);
		preparedStatementinsert.setString(7, state);
		preparedStatementinsert.setString(8, telephone);
		preparedStatementinsert.setString(9, email);
		preparedStatementinsert.executeUpdate();
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information Dialog");
		alert.setHeaderText("Information");
		alert.setContentText("Record stored");
		alert.showAndWait();
		
		tfID.setText("");
		tfFirstName.setText("");
		tfLastName.setText("");
		tfMI.setText("");
		tfAddress.setText("");
		tfCity.setText("");
		tfState.setText("");
		tfTelephone.setText("");
		tfEmail.setText("");
		
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	private void updateStaff() {
		String id = tfID.getText();
		String lastName = tfLastName.getText();
		String firstName = tfFirstName.getText();
		String MI = tfMI.getText();
		String address = tfAddress.getText();
		String city = tfCity.getText();
		String state = tfState.getText();
		String telephone = tfTelephone.getText();
		String email = tfEmail.getText();
		
		try {
			
			
			preparedStatementUpdate.setString(1, lastName);
			preparedStatementUpdate.setString(2, firstName);
			preparedStatementUpdate.setString(3, MI);
			preparedStatementUpdate.setString(4, address);
			preparedStatementUpdate.setString(5, city);
			preparedStatementUpdate.setString(6, state);
			preparedStatementUpdate.setString(7, telephone);
			preparedStatementUpdate.setString(8, email);
			preparedStatementUpdate.setString(9, id);
			preparedStatementUpdate.executeUpdate();
		
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText("Information");
			alert.setContentText("Record Updated");
			alert.showAndWait();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}
	
	private void clearStaff() {
		tfID.setText("");
		tfLastName.setText("");
		tfFirstName.setText("");
		tfMI.setText("");
		tfAddress.setText("");
		tfCity.setText("");
		tfState.setText("");
		tfTelephone.setText("");
		tfEmail.setText("");
	}

}
