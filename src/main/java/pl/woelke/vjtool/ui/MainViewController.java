package pl.woelke.vjtool.ui;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import pl.woelke.vjtool.ui.model.PrinterType;
import pl.woelke.vjtool.ui.model.ProtocolType;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainViewController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(MainViewController.class.getName());
    private SerialPort port = null;

    @FXML
    private ComboBox<PrinterType> printerName;
    @FXML
    private ComboBox<ProtocolType> interfaceType;
    @FXML
    public Button clearConsoleBtn;
    @FXML
    public ToggleGroup group1;
    @FXML
    public RadioButton asciiRadioBtn;
    @FXML
    public RadioButton hexRadioBtn;
    @FXML
    public RadioButton binaryRadioBtn;
    @FXML
    public CheckBox CRCheckBox;
    @FXML
    public CheckBox LFCheckBox;
    @FXML
    public CheckBox echoCheckBox;
    @FXML
    public TextField dataTextField;
    @FXML
    public Button sendBtn;
    @FXML
    public ComboBox<Integer> baudRateComboBox;
    @FXML
    public CheckBox spaceCheckBox;
    @FXML
    ComboBox<String> portsComboBox;
    @FXML
    Button refreshPortsBtn;
    @FXML
    Button connectBtn;
    @FXML
    Button disconnectBtn;
    @FXML
    TextArea receiveConsole;
    @FXML
    TextArea sendConsole;
    @FXML
    TextArea statusConsole;
    @FXML
    Label connectedLabel;
    @FXML
    Button statusBtn;

    @FXML
    protected void onCloseButtonClick() {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    public void clearLog() {
        receiveConsole.setText("[FX Serial Console]\n");
    }

    @FXML
    public void sendSerialData() {
        if (echoCheckBox.isSelected()) sendConsole.appendText("\n" + dataTextField.getText() + "\n");
        StringBuilder rawData = new StringBuilder();
        rawData.append(dataTextField.getText());
        if (CRCheckBox.isSelected()) rawData.append("\r");
        if (LFCheckBox.isSelected()) rawData.append("\n");
        port.writeBytes(rawData.toString().getBytes(), rawData.toString().length());
        dataTextField.clear();
    }

    @FXML
    public void checkStatus() {
        if (echoCheckBox.isSelected()) statusConsole.appendText("\n" + dataTextField.getText() + "\n");
        StringBuilder rawData = new StringBuilder();
        String protocol = interfaceType.getSelectionModel().getSelectedItem().getProtocolType();
        if (CRCheckBox.isSelected()) rawData.append("\r");
        if (LFCheckBox.isSelected()) rawData.append("\n");
        LOGGER.info("Protokoł: " + protocol);
        switch (protocol) {
            case "wsi":
                rawData.append("wsi");
                port.writeBytes(rawData.toString().getBytes(), rawData.toString().length());
                break;
            case "esi":
                rawData.append("esi");
                port.writeBytes(rawData.toString().getBytes(), rawData.toString().length());
                break;
            case "zipher":
                rawData.append("zipher");
                port.writeBytes(rawData.toString().getBytes(), rawData.toString().length());
                break;
            default:
                rawData.append("Nieprawidłowy typu protokołu! ");
                port.writeBytes(rawData.toString().getBytes(), rawData.toString().length());
                break;
        }
        if (echoCheckBox.isSelected()) statusConsole.appendText("\n" + rawData + "\n");
    }


    //set the baud rate of selected port.
    public void setBaudRate() {
        if (port != null) {
            int baudRate = (baudRateComboBox.getSelectionModel().getSelectedItem());
            port.setBaudRate(baudRate);
            receiveConsole.appendText("\nBaudRate is set to " + baudRate + " Bauds/s");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        InitPrinterSetupView();
        refreshPorts();
        selectBaudRate();
    }

    public void InitPrinterSetupView() {


        printerName.getItems().setAll(PrinterType.values());
        interfaceType.getItems().setAll(ProtocolType.values());

//        setSerialPort.getItems().setAll((communicator.detectPort()));
//        ObservableList<String> printerPorts = FXCollections.observableArrayList();
//        printerPorts.addAll(communicator.detectPort());
//        setSerialPort.setItems(printerPorts);
    }

    public void selectBaudRate() {
        int[] baudRates = {
                110, 300, 600, 1200, 2400, 4800,
                9600, 14400, 19200, 38400, 57600, 74880, 115200,
                128000, 230400, 250000, 500000, 1000000, 2000000};
        for (int baudRate : baudRates
        ) {
            baudRateComboBox.getItems().add(baudRate);
        }
    }

    private void processReceivedData(byte[] newData) {
        StringBuilder builder = new StringBuilder();
        if (hexRadioBtn.isSelected()) {
            for (byte b : newData) {
                builder.append(String.format("%02X", b));
                if (spaceCheckBox.isSelected()) builder.append(" ");
            }
        } else if (asciiRadioBtn.isSelected()) {
            for (byte b : newData) {
                builder.append((char) b);
            }
        } else if (binaryRadioBtn.isSelected()) {
            builder = new StringBuilder();
            for (byte b : newData) {
                builder.append(Integer.toBinaryString(b & 0xFF).replace(' ', '0'));
                if (spaceCheckBox.isSelected()) builder.append(" ");
            }
        }
        receiveConsole.appendText(builder.toString());
    }

    public void connectPort() {
        if (portsComboBox.getSelectionModel().getSelectedIndex() >= 0) {
            port = runtimeVariables.getInstance().getPortLists()
                    [portsComboBox.getSelectionModel().getSelectedIndex()];
            if (!port.isOpen()) {
                port.openPort();

                if (baudRateComboBox.getSelectionModel().getSelectedIndex() < 0)
                    baudRateComboBox.getSelectionModel().select(6);
                int baudRate = baudRateComboBox.getSelectionModel().getSelectedItem();
                port.setBaudRate(baudRate);
                connectedLabel.setText("Połączono ");
                connectedLabel.setTextFill(Color.GREEN);
                receiveConsole.appendText("\nConnected to ");
                receiveConsole.appendText(pl.woelke.vjtool.ui.runtimeVariables.getInstance().getPortLists()
                        [portsComboBox.getSelectionModel().getSelectedIndex()].getDescriptivePortName());
                receiveConsole.appendText(" at baud rate of " + baudRate + " Bauds/s");

                try {
                    port.addDataListener(new SerialPortDataListener() {
                        @Override
                        public int getListeningEvents() {
                            return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
                        }

                        @Override
                        public void serialEvent(SerialPortEvent event) {
                            if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                                return;
                            byte[] newData = new byte[port.bytesAvailable()];
                            port.readBytes(newData, newData.length);
                            Platform.runLater(() -> processReceivedData(newData));
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                statusBtn.setDisable(false);
                connectBtn.setDisable(true);
                disconnectBtn.setDisable(false);
                portsComboBox.setDisable(true);
                refreshPortsBtn.setDisable(true);
                dataTextField.setDisable(false);
                sendBtn.setDisable(false);
            } else {
                runtimeVariables.getInstance().showAlert(Alert.AlertType.ERROR, portsComboBox.getScene().getWindow(),
                        "Port Open Error", "Could not open the port",
                        "The Port you are trying to open could not be opened");
            }
        } else {
            runtimeVariables.getInstance().showAlert(Alert.AlertType.ERROR, portsComboBox.getScene().getWindow(),
                    "Error", "Port Not Selected",
                    "Please select the port to open");
        }
    }

    public void refreshPorts() {
        //clear the current ports combo box and search again for new ports.
        connectedLabel.setText("Rozłączono");
        connectedLabel.setTextFill(Color.RED);

        portsComboBox.getItems().clear();
        receiveConsole.appendText("\nOdświeżanie portów");
        SerialPort[] ports = SerialPort.getCommPorts();
        receiveConsole.appendText("\nDostępne porty:");
        for (SerialPort p : ports) {
            receiveConsole.appendText("\n->\t" + p.getSystemPortName() + "\t" + p.getDescriptivePortName());
            portsComboBox.getItems().add(p.getSystemPortName() + "\t" + p.getDescriptivePortName());
        }
        runtimeVariables.getInstance().setPortLists(ports);
        if (ports.length > 0) {
            connectBtn.setDisable(false);
            portsComboBox.getSelectionModel().select(0);
        } else
            receiveConsole.appendText("\nNo żaden port szeregowy nie jest dospępny");
    }

    public void disconnectPort() {
        statusBtn.setDisable(true);
        connectBtn.setDisable(false);
        disconnectBtn.setDisable(true);
        portsComboBox.setDisable(false);
        refreshPortsBtn.setDisable(false);
        dataTextField.setDisable(true);
        sendBtn.setDisable(true);
        if (port.isOpen()) {
            port.closePort();
            receiveConsole.appendText("\n" + port.getDescriptivePortName());
            receiveConsole.appendText(" Rozłączono");
            connectedLabel.setText("Rołączony");
        } else {
            runtimeVariables.getInstance().showAlert(Alert.AlertType.ERROR, portsComboBox.getScene().getWindow(),
                    "Port Close Error", "Could not close the port",
                    "The Port you are trying to close is not accessible");
        }
    }
}
