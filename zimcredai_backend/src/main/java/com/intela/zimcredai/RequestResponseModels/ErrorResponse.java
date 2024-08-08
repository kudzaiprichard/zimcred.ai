package com.intela.zimcredai.RequestResponseModels;

public class ErrorResponse {
    private String title;
    private String details;
    private int status;

    public ErrorResponse(String title, String details, int status) {
        this.title = title;
        this.details = details;
        this.status = status;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
