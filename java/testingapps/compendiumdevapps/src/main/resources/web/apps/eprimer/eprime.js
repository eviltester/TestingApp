	function inEPrimeOutputFormat(aWord){
		return '<span class="ep_violation">' + aWord + "</span>";
	}
	
	function inPossibleEPrimeOutputFormat(aWord){
		return '<span class="ep_warning">' + aWord + "</span>";
	}

	function isDiscouragedWord(aWord){

		var discouragedWords = new Array();
		discouragedWords['be'] = 'be';
		discouragedWords['being'] = 'being';
		discouragedWords['been'] = 'been';
		discouragedWords['am'] = 'am';
		discouragedWords["isn't"] = "isn't";
		discouragedWords["are"] = "are";
		discouragedWords["aren't"] = "aren't";
		discouragedWords["was"] = "was";
		discouragedWords["wasn't"] = "wasn't";
		discouragedWords["were"] = "were";
		discouragedWords["weren't"] = "weren't";
		discouragedWords["is"] = "is";
		discouragedWords["ain't"] = "ain't";
		discouragedWords["i'm"] = "i'm";
		discouragedWords["amn't"] = "amn't";
		
            
                return (discouragedWords[aWord.toLowerCase()]==aWord.toLowerCase());
		
	}	
	

	function formatIfFailsEPrimeCheck(aWord){
		
		if (isDiscouragedWord(aWord.toLowerCase())){
			discouragedWordCount++;
			return inEPrimeOutputFormat(aWord);
		}
			
		if (isApostropheEprime(aWord)){
			possibleViolationCount++;
			return inPossibleEPrimeOutputFormat(aWord);
		}

		return aWord;

		
	}
	
	function isApostropheEprime(aWord){
	
		var discouragedEndings = new Array();
		discouragedEndings["'s"] = 2;
		
		var apostropheAt = aWord.indexOf("'")
		if (apostropheAt == -1)
			return false;
			
		var wordEnding = aWord.substr(apostropheAt);
		if (typeof(discouragedEndings[wordEnding.toLowerCase()]) == 'number')
			return true;
			
		return false;		
	}	
	
	
	
	// warning not TDD code below
	
	var currChar = 0;
	var processingWord = "";
	var outputText = ""
	var discouragedWordCount = 0;
	var possibleViolationCount = 0;
	var wordCount = 0;

	function displayStats(){
		document.getElementById("wordCount").innerHTML = wordCount;
		document.getElementById("discouragedWordCount").innerHTML = discouragedWordCount;
		document.getElementById("possibleViolationCount").innerHTML = possibleViolationCount;
	}
	
	function resetGlobalVariables(){
		currChar = 0;
		processingWord = "";
		outputText = ""
		discouragedWordCount = 0;
		possibleViolationCount = 0;
		wordCount = 0;
	}
	
	function processWordIfNotEmpty(){
		if (processingWord.length > 0){
			// process word
			outputText = outputText + formatIfFailsEPrimeCheck(processingWord);
			processingWord="";
		}
	}
		
	function convertToHTMLIfRequired(c){
		if(c=='<')
			return '&lt;';

		if(c=='>')
			return '&gt;';
			
		return c;
	}				

	
    function checkThisTextForEprime(textToProcess) {
	
		var validChars = "abcdefghijklmnopqrstuvwxyz'ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		var textLength = textToProcess.length;
		resetGlobalVariables();
	
		outputText = "<p>";
		wordCount = 0;
				
		var processingSpaces=true;
		
		for (currChar=0; currChar<textLength; currChar++){
			c = textToProcess.charAt(currChar);

			if(c=='\n'){
				outputText = outputText + "</p><p>";
				c='';
			}

			
			if(processingSpaces==true && c!=' '){
				wordCount++;
				processingSpaces = false;
			}
			
			if(processingSpaces==false && c==' '){
				processingSpaces = true;
			}
			
			
			if (validChars.indexOf(c) == -1)
			{
				processWordIfNotEmpty();
				outputText = outputText + convertToHTMLIfRequired(c);
			}else
			{
				processingWord = processingWord + c;
			}
		}
		
		processWordIfNotEmpty();
		
		outputText = outputText + "</p>";
		return outputText;
    }
	
	function processInputText(){
		checkThisTextForEprime(document.getElementById("inputtext").value);
		writeTheOutputTextToTheScreen();
		displayStats();

	}
	
	function writeTheOutputTextToTheScreen(){
		document.getElementById("eprimeoutput").innerHTML = outputText;
                //alert("["+document.getElementById("eprimeoutput").innerHTML+"]");
	}
	
	