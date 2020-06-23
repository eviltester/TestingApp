package com.seleniumsimplified.seleniumtestpages.spark.app;

import com.seleniumsimplified.seleniumtestpages.ResourceReader;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class InputValidationProcessor {
    private final Request req;
    private final Response res;

    public InputValidationProcessor(final Request req, final Response res) {
        this.req=req;
        this.res=res;
    }


    public String post() {

        // get input - do not attempt to make this too secure, allow 500 errors

        String firstname = req.queryParams("firstname");
        String surname = req.queryParams("surname");
        String age = req.queryParams("age");
        String country = req.queryParams("country");
        String notes = req.queryParams("notes");

        StringBuilder html = new StringBuilder();

        html.append("<h1>Input Validation Response</h1>\n");

        // validate input
        html.append("<h2>Validation Report</h2>\n");

        boolean inError=false;

        // create an output response
        if(firstname!=null){
            if(firstname.length()<=4){
                html.append(getValuePair("Firstname Error", "Firstname too short"));
                inError=true;
            }
            if(firstname.length()>100){
                html.append(getValuePair("Firstname Error", "Firstname too long"));
                inError=true;
            }
        }

        if(surname!=null){
            if(surname.length()>100){
                html.append(getValuePair("Surname Error", "Surname too long"));
                inError=true;
            }
        }

        if(age!=null){
            if(Integer.valueOf(age) <18 || Integer.valueOf(age)> 100){
                html.append(getValuePair("Age Error", "Age out of range"));
                inError=true;
            }
        }

        if(notes!=null){
            if(notes.length()>3000){
                html.append(getValuePair("Notes Error", "Notes too long"));
                inError=true;
            }
        }

        if(country==null){
            html.append(getValuePair("Country Error", "Country is mandatory"));
            inError=true;
        }else{
            if(!isCountryValid(country)){
                html.append(getValuePair("Country Error", "Country is not recognised"));
                inError=true;
            }
        }

        if(!inError){
            html.append(getValuePair("Valid Input", "Your Input passed validation"));
        }

        html.append("<h2>You submitted</h1>\n");
        html.append(getValuePair("Firstname", firstname));
        html.append(getValuePair("Surname", surname));
        html.append(getValuePair("Age", age));
        html.append(getValuePair("Country", country));
        html.append(getValuePair("Notes", notes));

        html.append("<br><br><p><a id='backtoform' href='/styled/validation/input-validation.html'>Back to Input Form</a></p>");

        return prettyPrintedOutput(html.toString());

    }

    private boolean isCountryValid(final String country) {
        String []countries = {
                "Afghanistan",
                "Albania",
                "Algeria",
                "Andorra",
                "Angola",
                "Antigua",
                "Barbuda",
                "Argentina",
                "Armenia",
                "Australia",
                "Austria",
                "Azerbaijan",
                "Bahamas",
                "Bahrain",
                "Bangladesh",
                "Barbados",
                "Belarus",
                "Belgium",
                "Belize",
                "Benin",
                "Bhutan",
                "Bolivia",
                "Bosnia and Herzegovina",
                "Botswana",
                "Brazil",
                "Brunei",
                "Bulgaria",
                "Burkina Faso",
                "Burundi",
                "Cote d Ivoire",
                "Cabo Verde",
                "Cambodia",
                "Cameroon",
                "Canada",
                "Central African Republic",
                "Chad",
                "Chile",
                "China",
                "Colombia",
                "Comoros",
                "Congo",
                "Congo-Brazzaville",
                "Costa Rica",
                "Croatia",
                "Cuba",
                "Cyprus",
                "Czechia",
                "Czech Republic",
                "Democratic Republic of the Congo",
                "Denmark",
                "Djibouti",
                "Dominica",
                "Dominican Republic",
                "Ecuador",
                "Egypt",
                "El Salvador",
                "Equatorial Guinea",
                "Eritrea",
                "Estonia",
                "Eswatini",
                "Swaziland",
                "Ethiopia",
                "Fiji",
                "Finland",
                "France",
                "Gabon",
                "Gambia",
                "Georgia",
                "Germany",
                "Ghana",
                "Greece",
                "Grenada",
                "Guatemala",
                "Guinea",
                "Guinea-Bissau",
                "Guyana",
                "Haiti",
                "Holy See",
                "Honduras",
                "Hungary",
                "Iceland",
                "India",
                "Indonesia",
                "Iran",
                "Iraq",
                "Ireland",
                "Israel",
                "Italy",
                "Jamaica",
                "Japan",
                "Jordan",
                "Kazakhstan",
                "Kenya",
                "Kiribati",
                "Kuwait",
                "Kyrgyzstan",
                "Laos",
                "Latvia",
                "Lebanon",
                "Lesotho",
                "Liberia",
                "Libya",
                "Liechtenstein",
                "Lithuania",
                "Luxembourg",
                "Madagascar",
                "Malawi",
                "Malaysia",
                "Maldives",
                "Mali",
                "Malta",
                "Marshall Islands",
                "Mauritania",
                "Mauritius",
                "Mexico",
                "Micronesia",
                "Moldova",
                "Monaco",
                "Mongolia",
                "Montenegro",
                "Morocco",
                "Mozambique",
                "Myanmar",
                "Burma)",
                "Namibia",
                "Nauru",
                "Nepal",
                "Netherlands",
                "New Zealand",
                "Nicaragua",
                "Niger",
                "Nigeria",
                "North Korea",
                "North Macedonia",
                "Norway",
                "Oman",
                "Pakistan",
                "Palau",
                "Palestine State",
                "Papua New Guinea",
                "Paraguay",
                "Peru",
                "Philippines",
                "Poland",
                "Portugal",
                "Qatar",
                "Romania",
                "Russia",
                "Rwanda",
                "Saint Kitts and Nevis",
                "Saint Lucia",
                "Saint Vincent and the Grenadines",
                "Samoa",
                "San Marino",
                "Sao Tome and Principe",
                "Saudi Arabia",
                "Senegal",
                "Serbia",
                "Seychelles",
                "Sierra Leone",
                "Singapore",
                "Slovakia",
                "Slovenia",
                "Solomon Islands",
                "Somalia",
                "South Africa",
                "South Korea",
                "South Sudan",
                "Spain",
                "Sri Lanka",
                "Sudan",
                "Suriname",
                "Sweden",
                "Switzerland",
                "Syria",
                "Tajikistan",
                "Tanzania",
                "Thailand",
                "Timor-Leste",
                "Togo",
                "Tonga",
                "Trinidad and Tobago",
                "Tunisia",
                "Turkey",
                "Turkmenistan",
                "Tuvalu",
                "Uganda",
                "Ukraine",
                "United Arab Emirates",
                "United Kingdom",
                "United States of America",
                "Uruguay",
                "Uzbekistan",
                "Vanuatu",
                "Venezuela",
                "Vietnam",
                "Yemen",
                "Zambia",
                "Zimbabwe"};

                // Panama

        for(String aKnownCountry : countries){
            if(country.contains(aKnownCountry)){
                return true;
            }
        }

        return false;
    }

    private String getValuePair(final String name, final String value) {
        String ulli = "<ul><li id='%s'>%s</li></ul>";

        String nameAsId =  name.toLowerCase().replace(" ", "-");

        return String.format(ulli,nameAsId,
                name + String.format(ulli,nameAsId+"-value", value));
    }

    private String prettyPrintedOutput(final String bodyContent) {

        String htmlPage = new ResourceReader().asString("/web/styled/template.html");
        htmlPage = htmlPage.replace("<!-- TITLE -->", "Input Validation Page" );

        htmlPage = htmlPage.replace("<!-- BODY CONTENT -->", bodyContent);
        return htmlPage;
    }
}
