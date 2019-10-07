package dad.javafx.calculadora;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

public class CalculadoraComplejaApp extends Application {

	// modelo

	private Complejo operando1 = new Complejo();
	private Complejo operando2 = new Complejo();
	private Complejo resultado = new Complejo();
	private StringProperty operador = new SimpleStringProperty();

	// elementos de la interfaz

	private TextField real1Text, real2Text, imaginario1Text, imaginario2Text, resultadoRealText,
			resultadoImaginarioText;
	private HBox resultadoBox, operando1Box, operando2Box;
	private VBox operadorBox, operandosBox;
	private ComboBox<String> operadorCombo;
	private Separator separador = new Separator();

	@Override
	public void start(Stage primaryStage) throws Exception {

		real1Text = new TextField();
		real1Text.setPrefColumnCount(4);

		imaginario1Text = new TextField();
		imaginario1Text.setPrefColumnCount(4);

		operando1Box = new HBox(4, real1Text, new Label("+"), imaginario1Text, new Label("i"));
		operando1Box.setAlignment(Pos.CENTER);

		real2Text = new TextField();
		real2Text.setPrefColumnCount(4);

		imaginario2Text = new TextField();
		imaginario2Text.setPrefColumnCount(4);

		operando2Box = new HBox(4, real2Text, new Label("+"), imaginario2Text, new Label("i"));
		operando2Box.setAlignment(Pos.CENTER);

		resultadoRealText = new TextField();
		resultadoRealText.setEditable(false);
		resultadoRealText.setPrefColumnCount(4);

		resultadoImaginarioText = new TextField();
		resultadoImaginarioText.setEditable(false);
		resultadoImaginarioText.setPrefColumnCount(4);

		resultadoBox = new HBox(4, resultadoRealText, new Label("+"), resultadoImaginarioText, new Label("i"));
		resultadoBox.setAlignment(Pos.CENTER);

		operadorCombo = new ComboBox<String>();
		operadorCombo.getItems().addAll("+", "-", "*", "/");
		operadorCombo.setMaxWidth(60);
		;
		operadorCombo.setPromptText("Operador");

		operadorBox = new VBox(5, operadorCombo);
		operadorBox.setAlignment(Pos.CENTER);

		operandosBox = new VBox(5, operando1Box, operando2Box, separador, resultadoBox);
		operandosBox.setAlignment(Pos.CENTER);

		HBox root = new HBox(5, operadorBox, operandosBox);
		root.setAlignment(Pos.CENTER);

		Scene scene = new Scene(root, 320, 200);

		primaryStage.setTitle("Calculadora Compleja");
		primaryStage.setScene(scene);
		primaryStage.show();

		// bindeos

		Bindings.bindBidirectional(real1Text.textProperty(), operando1.realProperty(), new NumberStringConverter());
		Bindings.bindBidirectional(real2Text.textProperty(), operando2.realProperty(), new NumberStringConverter());
		Bindings.bindBidirectional(resultadoRealText.textProperty(), resultado.realProperty(),
				new NumberStringConverter());

		Bindings.bindBidirectional(imaginario1Text.textProperty(), operando1.imaginarioProperty(),
				new NumberStringConverter());
		Bindings.bindBidirectional(imaginario2Text.textProperty(), operando2.imaginarioProperty(),
				new NumberStringConverter());
		Bindings.bindBidirectional(resultadoImaginarioText.textProperty(), resultado.imaginarioProperty(),
				new NumberStringConverter());

		operador.bind(operadorCombo.getSelectionModel().selectedItemProperty());

		// listeners

		operador.addListener((o, ov, nv) -> onOperadorChanged(nv));

		operadorCombo.getSelectionModel().selectFirst();

	}

	private void onOperadorChanged(String nv) {
		switch (nv) {
		case "+":
			resultado.realProperty().bind(operando1.realProperty().add(operando2.realProperty()));
			resultado.imaginarioProperty().bind(operando1.imaginarioProperty().add(operando2.imaginarioProperty()));
			break;
		case "-":
			resultado.realProperty().bind(operando1.realProperty().subtract(operando2.realProperty()));
			resultado.imaginarioProperty()
					.bind(operando1.imaginarioProperty().subtract(operando2.imaginarioProperty()));
			break;
		case "*":
			resultado.realProperty().bind(operando1.realProperty().multiply(operando2.realProperty()
					.subtract(operando1.imaginarioProperty().multiply(operando2.imaginarioProperty()))));
			resultado.imaginarioProperty().bind(operando1.realProperty().multiply(operando2.imaginarioProperty())
					.add(operando1.imaginarioProperty().multiply(operando2.realProperty())));
			break;
		case "/":
			resultado.realProperty()
					.bind((operando1.realProperty()
							.multiply(operando2.realProperty()
									.add(operando1.imaginarioProperty().multiply(operando2.imaginarioProperty())))
							.divide(operando2.realProperty().multiply(operando2.realProperty())
									.add(operando2.imaginarioProperty().multiply(operando2.imaginarioProperty())))));
			resultado.imaginarioProperty()
					.bind((operando1.imaginarioProperty().multiply(operando2.realProperty())
							.subtract(operando1.realProperty().multiply(operando2.imaginarioProperty())))
									.divide(operando2.realProperty().multiply(operando2.realProperty()).add(
											operando2.imaginarioProperty().multiply(operando2.imaginarioProperty()))));
			break;
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
