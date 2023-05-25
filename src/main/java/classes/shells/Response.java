package classes.shells;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable {
    private boolean isLastResponse = false;
    private ArrayList<String> data = new ArrayList<>();
    private ArrayList<String[]> formattedData = new ArrayList<>();
    private boolean isFormatted = false;
    private String stringFormat;

    public Response() {
    }

    public Response(String data) {
        setData(data);
    }

    public ArrayList<String> getData() {
        return data;
    }


    public void setData(String data) {
        this.data.add(data);
    }

    public void addData(Response response) {
        data.addAll(response.getData());
    }

    public void showData() {
        if (isFormatted) {
            for (String[] response : formattedData) {
                System.out.printf(stringFormat, response[0], response[1]);
            }
        } else {
            for (String response : data) {
                System.out.println(response);
            }
        }
    }

    public void setLastResponse() {
        isLastResponse = true;
    }

    public void setFormat(String format){
        isFormatted = true;
        stringFormat = format;
    }


    public void setFormattedData(String [] data){
        formattedData.add(data);
    }

    public boolean isLastResponse() {
        return isLastResponse;
    }
}
