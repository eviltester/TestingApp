package com.javafortesters.pulp.reader;

public class CsvReader {
    private final String resourcePath;
    private String data;
    private String lines[];

    public CsvReader(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public void read() {
        this.data = new ResourceReader().asString(resourcePath);
        String splitter = "\n";
        if(data.contains("\r\n")){
            splitter = "\r\n";
        }
        this.lines = data.split(splitter);
    }

    public int numberOfLines() {
        return lines.length;
    }

    public String getLines(int lineNumber) {
        return lines[lineNumber];
    }

    public String getLineField(int line, int field) {
        String fields[]=this.getLines(line).split("\",\"");

        if(field==0){
            // get rid of first "
            return fields[0].substring(1);
        }

        if(field==fields.length-1){
            // remove last "
            return fields[field].substring(0,fields[field].length()-1);
        }

        return fields[field];
    }
}
