package com.dwakenya.techsavanna.newdwa;

public class PDFModel {
    String pdf;
    String name;
    
    public PDFModel() {
    }
    
    public PDFModel(String pdf, String name) {
        this.pdf = pdf;
        this.name = name;
    }
    
    public String getPdf() {
        return pdf;
    }
    
    public void setPdf(String pdf) {
        this.pdf = pdf;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
