// naively converted from Java

function StringCounterStringCreator(){

    this.string="";

    this.append = function(nextPart) {
        this.string = this.string + nextPart;
    }
    this.toString = function() {
        return this.string;
    }

    this.finished = function(){
        // OK it is finished - just added this to complete the interface,
        // not required for a buffered object like this - more important for a streaming object
    }
};

function CounterStringRangeStruct(space, minX, maxX) {
    this.spaceValueInRangeTakes=space;
    this.minValInRange=minX;
    this.maxValInRange=maxX;
};

function CounterStringRangeIterator(range, minValInRange) {
    this.range=range;
    this.nextValueFromRange = minValInRange;


    this.hasAnotherValueInRange = function() {
        return (this.nextValueFromRange <= this.range.maxValInRange);
    };

    this.getNextValueFromRange = function() {

        retVal = this.nextValueFromRange;

        if(this.nextValueFromRange > this.range.maxValInRange){
            Console.og("Tried to exceed range limit of " + this.range.maxValInRange + " by using " + this.nextValueFromRange);
            exit();
        }

        this.nextValueFromRange += this.range.spaceValueInRangeTakes;

        return retVal;
    };
};

function CounterStringRangeCreator(lengthOfCounterString, limiter) {
    this.lengthOfCounterString = lengthOfCounterString;
    this.limiter = limiter;

    this.getListOfRangeStructs = function() {

        // assumes limiter is a single char
        theSpacer = this.limiter; //getSingleCharSpacer(limiter);

        ranges = [];

        maxNumberOfDigits = this.getLengthOfNumberInString(this.lengthOfCounterString, theSpacer);


        lengthOfNumberInString = maxNumberOfDigits;

        highestNextNumberOfDigitsNumberIs= this.lengthOfCounterString;


        do {
            maxNumberWithXDigits = highestNextNumberOfDigitsNumberIs;


            lowestXCharNumberIs = this.calculateLowestXDigitNumberForThisRange(highestNextNumberOfDigitsNumberIs, theSpacer);

            ranges.unshift(new CounterStringRangeStruct(lengthOfNumberInString, lowestXCharNumberIs, maxNumberWithXDigits));

            highestNextNumberOfDigitsNumberIs = lowestXCharNumberIs - lengthOfNumberInString;

            lengthOfNumberInString--;



        } while (lengthOfNumberInString > 1);



        return ranges;
    };


    this.calculateLowestXDigitNumberForThisRange = function(maxNumberWithXDigits, spacer) {

        displayLengthInStringWithLimiterAppended = this.getLengthOfNumberInString(maxNumberWithXDigits, spacer);

        //*3*5*7*10*13*16*19*22*25*28*31*34*37*40*43*46*49*52*55*58*61*64*67*70*73*76*79*82*85*88*91*94*97*101*105*109*113*117*121*125*
        // e.g. 125 has 3 chars
        // and display length with limiter would be 4 i.e. 125*
        numberHasXChars = maxNumberWithXDigits.toString().length;

        // length - lowest x digit number
        // lowest 2 digit number is 10 to the power of (2-1) = 10
        // e.g. lowest 3 digit number is 10 to the power of (3-1) = 100
        lowestXDigitNumber = Math.pow(10, (numberHasXChars - 1));

        // what is the gap between our highest range number and the lowest in the range?
        // e.g. 125 - 100 = 25
        differenceOfThisDigitValues = maxNumberWithXDigits - lowestXDigitNumber;

        // how many X digit numbers are in this range? (integer division)
        // e.g. 25/4 = 6  how many 4 character numbers fit in 25? == 6  125*
        // force an integer division with Match.floor
        numberOfThisDigitValues = Math.floor(differenceOfThisDigitValues/displayLengthInStringWithLimiterAppended);

        // but we already displayed one
        numberOfThisDigitValues++;

        // 125 - (4*7) == 97
        lowestXCharNumberIs = maxNumberWithXDigits - (displayLengthInStringWithLimiterAppended * numberOfThisDigitValues);
        if(lowestXCharNumberIs==0){
            // if it is 0 then it is really 2 because we can't have 0 as the lowest number
            lowestXCharNumberIs=2;
        }

        // length of 97 with spacer is 3
        lengthOfLowestAsCounterString = this.getLengthOfNumberInString(lowestXCharNumberIs, spacer);

        // 3 != 4 so 97 is not lowest, lowest must be 97 + 4 = 101
        if (lengthOfLowestAsCounterString != displayLengthInStringWithLimiterAppended) {
            //it is not an X char number so next X char number is lowest + lengthOfNumberInString
            lowestXCharNumberIs = lowestXCharNumberIs + displayLengthInStringWithLimiterAppended;
        }

        return lowestXCharNumberIs;
    };

    this.getLengthOfNumberInString = function(valueOfLastCounter, theSpacer) {
        numberOfDigitsExample = valueOfLastCounter.toString() + theSpacer;
        return numberOfDigitsExample.length;
    };
};


function CounterString() {

    var maxLimiter=1;

    this.create = function(lengthOfCounterString, limiter){

        limiter = typeof limiter !== 'undefined' ? limiter : "*";

        return this.createWith(lengthOfCounterString, limiter, new StringCounterStringCreator()).toString();

    };

    this.createWith = function (lengthOfCounterString, limiter, creator){
        var theSpacer = this.getSingleCharSpacer(limiter);

        ranges = this.createCounterStringRangesFor(lengthOfCounterString, theSpacer);

            // iterate through the ranges to build the string
            for(range in ranges){
                ranger = new CounterStringRangeIterator(ranges[range], ranges[range].minValInRange);
                while(ranger.hasAnotherValueInRange()){
                    creator.append(this.getCounterStringRepresentationOfNumber(ranger.getNextValueFromRange(), theSpacer));
                }
            }
            creator.finished();

        return creator;
    };

    this.getSingleCharSpacer = function(limiter){
        // if we have been given a limiter then use the first character of it
        if(limiter!=null){
            if(limiter.length>0) {
                return limiter.substring(0, this.maxLimiter);
            }
        }

        return "*";
    };


    this.getCounterStringRepresentationOfNumber = function(outX, theSpacer) {
        // if we are outputting the number 1 then output just the spacer
        if (outX == 1) {
            return theSpacer;
        }

        return outX + theSpacer;
    };


    this.createCounterStringRangesFor = function(lengthOfCounterString, limiter) {

        return new CounterStringRangeCreator(lengthOfCounterString, limiter).getListOfRangeStructs();

    };
}