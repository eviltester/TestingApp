HTML5 Validation does not match JavaScript validation

HTML5

- firstname (len > 1)
- surname (none)
- age (18 - 79)
- country (none)
- notes (max 2000 chars)

JavaScript

- firstname (len > 2)
- surname (can be "", or (len > 10 && len < 80))
- age (16 - 100)
- country (must not include ".")
- notes (can be empty, must not exceed 2500)

Server Side

- firstname (len > 5 && len <= 100)
- surname (can be null || len <= 100 )
- age (>= 18 && <= 100)
- country (!null)
  mismatch in values e.g. antigua, barbuda, Congo (Congo-Brazzaville), and more
- notes (null || <=3000)

---

Mismatches between validation rules

HTML

- firstname:
    - mandatory
    - min length: 1

- surname:
    - optional

- age
    - mandatory
    - number
    - 18 - 80

- country:
    - mandatory
    - drop down list

- notes:
    - optional
    - max-length: 2000

Note: possible to bypass validation from javascript by creating elements with names earlier
JavaScript

- firstname:
    - mandatory
    - min length: 2
    - "Firstname provided is too short"
    - max length: 90
        - "Firstname provided is too long"

- surname:
  -optional
    - min length: 10 if it exists
    - "Surname provided is too short"
    - max length: 80 if it exists
    - "Surname provided is too long"

- age
    - optional
    - if exists then 16 - 100
    - "Age provided is not valid"

- country:
    - mandatory
    - validation that name does not have "." - invalidates Eswatini
    - "Please select a country from the list"

- notes:
    - optional
    - max-length: 2500
    - "Text is too long"
    - bug: notes error message is left behind and not cleared when error condition is cleared

Server

- firstname:
    - optional
    - min length: 4 - if it exists
    - max length: 100

- surname:
    - optional
    - max length: 100

- age:
    - optional
    - if exists then between 18 and 100

- notes:
    - optional
    - max-length: 3000 - if it exists

- country:
    - mandatory
    - checking list missing panama
    - check by 'does submitted country text contain item from country list'
        - note split some countries from main list into two
        - introduced a ")" error as well - should still match drop down, but fail if value amended

String []countries = {"Afghanistan",
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
"Côte d'Ivoire",
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
"Panama",
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