package pl.woelke.vjtool.ui.model;

import java.util.logging.Logger;

public class WillettPacked {

    private static final Logger LOGGER = Logger.getLogger(WillettPacked.class.getName());
    private static final String STX = "\u0002";
    private static final String ETX = "\u0003";
    private static final Character STATUS = 'H';
    private String dataCommand = "";

    StringBuilder stringBuilder = new StringBuilder();

    public WillettPacked() {
    }

    public String getPrinterStatus() {

        stringBuilder.append(STX);
        stringBuilder.append(STATUS);
        stringBuilder.append(ETX);
        LOGGER.info("getPrinterStatus()"+stringBuilder.toString());
        return stringBuilder.toString();
    }


    public String sendMessgae(Character type, String data) {
        LOGGER.info("sendMessage: " + type + " " + data);
        stringBuilder.append(STX);
        stringBuilder.append(type);
        stringBuilder.append(data);
        stringBuilder.append(ETX);
        return stringBuilder.toString();

    }
}
