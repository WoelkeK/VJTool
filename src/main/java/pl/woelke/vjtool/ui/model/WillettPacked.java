package pl.woelke.vjtool.ui.model;

import java.util.logging.Logger;

public class WillettPacked {

    private static final Logger LOGGER = Logger.getLogger(WillettPacked.class.getName());
    private static final String STX = "\u0002";
    private static final String ETX = "\u0003";

    private Character type;
    private String data;


    StringBuilder stringBuilder = new StringBuilder();

    public WillettPacked() {
    }

    public WillettPacked(Character type, String data) {
        this.type = type;
        this.data = data;
    }

    public Character getType() {
        return type;
    }

    public void setType(Character type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WillettPacked{" +
                "type=" + type +
                ", data='" + data + '\'' +
                '}';
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
