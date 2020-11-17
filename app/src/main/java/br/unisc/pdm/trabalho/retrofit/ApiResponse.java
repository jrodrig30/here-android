package br.unisc.pdm.trabalho.retrofit;

public class ApiResponse {

    private String message;

    private String event_id;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getStatus() {
        return "true".equals(message) || "success".equals(message) || "Success".equals(message);
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }
}
