package classes.shells;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable {
    private ArrayList<String> data = new ArrayList<>();

    public Response(){}

    public Response(String data){
        setData(data);
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(String data) {
        this.data.add(data);
    }

    public void showData(){
    for (String response: data){
        System.out.println(response);
    }
    }
}
