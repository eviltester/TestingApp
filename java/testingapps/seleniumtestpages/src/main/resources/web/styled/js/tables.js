function TableCreator(){

    this.caption="";
    this.headings = [];
    this.data = [];

    this.setCaption = function(name){
        this.caption = name;
    }

    this.setData = function(anArrayOfObjects){
        this.setHeadingsFrom(anArrayOfObjects);
        this.data = anArrayOfObjects;
    }

    this.setHeadingsFrom = function(anArrayOfObjects){
        for(anObject in anArrayOfObjects){
            var keys = Object.keys(anArrayOfObjects[anObject]);
            for(aHeading in keys){
                this.headings[keys[aHeading]] = keys[aHeading];
            }
        }
    }

    this.getTable = function(attributesArray){

        var table = document.createElement("table");
        for(attribute in attributesArray){
            table.setAttribute(attribute, attributesArray[attribute]);
        }
        if(this.caption.length>0) {
            var caption = document.createElement("caption");
            caption.innerText = this.caption;
            table.appendChild(caption);
        }

        var headingRow = document.createElement("tr");
        for(heading in this.headings){
            var th = document.createElement("th");
            th.innerText = this.headings[heading];
            headingRow.appendChild(th);
        }
        table.appendChild(headingRow);

        for(row in this.data){
            var rowData = document.createElement("tr");
            for(heading in this.headings){
                var td = document.createElement("td");
                var value = this.data[row][this.headings[heading]];
                td.innerText = value ? value : "";
                rowData.appendChild(td);
            }
            table.appendChild(rowData);
        }

        return table;
    }

}