/**
 * Moved code from index into here to make it easier to re-use on different platforms
 */

var phrases = {

    "start" : ["", "Of course", "I honestly believe", "I really do think"],
    "im_not" : ["evil", "good", "nasty", "unpleasant",
        "rude", "arrogant", "bad", "pedantic"],
    "i_am" : ["I'm", "I am"],
    "o_just" : ["", "#just"],

    "just" : ["just", "only"],
    "o_just" : ["", "#just"],

    "that_way!" : ["look like it!", "act that way!"],
    "that_way?" : ["look like it?", "act that way?"],

    "i_do_this" : ["#i_am #just doing this", "I #just do this", "I #just act this way", "I #just pretend to be"],
    "for_you" : ["for you", "for your own good", "to help you", "for your benefit"],
    "just_because" : ["because I care!", "because I do!", "because I can!", "because... just BECAUSE!",
        "because I know what is best for you!", "because THEY said so!", "because THEY made me!", "because #i_am_so_much_more !"],
    "because_i_care" : ["because I care about", "because I love", "because I value"],
    "care_about" : ["the project", "the world", "your sanity", "you", "your career"],
    "than_this" : ["you do", "you seem to", "your beliefs", "your sanity"],

    "tagline" : ["", "How about you?", "Don't you?", "And you will too!", "And I think you will too!", "As will you!"],
    "lol" : ["", "Bwahahaha!", "Bwahaha!", "Bwahahahahahah!", "Bwah*COUGH*ha!", "lol.", "lols.", "LOL!"],

    "optional_lol" : ["", "#lol"],
    "optional_tagline" : ["", "#tagline"],

    "excla_stop" : ["!", "."],

    "o_so": ["", "so"],
    "o_now": ["", "now"],
    "o_much": ["", "much"],
    "better_than": ["better than", "more than", "far beyond"],

    "i_am_so_much_more" : ["#i_am #o_so GOOD", "#i_am #o_so EEEEVIL", "#i_am #o_so heavenly",
        "#better_than_that_now",
        "I'm like a #thingy ... waiting to #do_something"],

    "better_than_that_now" : ["#i_am #o_so #o_much #better_than that #o_now"],

    "thingy" : ["butterly", "pancake", "open book", "flower", "planet", "special thing", "door", "new opportunity", "egg", "smile", "laugh"],
    "do_something" : ["explode", "be explored", "close", "open", "flower", "be found", "be caught", "run away", "dance", "sing"],

    "be_good" : ["good", "evil", "nasty", "splendid", "unpleasant", "joyful",
        "playful", "weird", "random", "helpful", "attentive",
        "pedantic", "obtuse", "excellent", "critical"],
    "be_adj" : ["seemingly", "absolutely", "totally", "slightly", "fully",
        "completely", "overly", "unduly", "unnecessarily"],

    "other_be_s" : ["the \"E\" word", "true", "yourself", "somebody else", "otherworldly", "something else", "it", "them", "that thing", "that other thing", "whatever it takes"],

    "be_goodly" : ["#be_good", "#be_adj #be_good"],
    "be_all_goodly" : ["#be_good", "#be_adj #be_good", "#other_be_s"],

    "to_them" : ["", "to THEM", "to everyone", "to yourself", "to someone special",
        "to them all", "to your boss", "to someone random", "to a stranger",
        "to the system", "to a requirement", "to a function", "to a feature", "to the database",
        "to the data",
        "to each other", "to nobody",
        "to the people who live under the stairs that only appear when you are looking but not when anyone else is looking"],

    "be_when": ["today", "now", "every day", "when they least expect it", "at all times", "all the time", "when no-one is looking"],
    "be_because" : ["you can", "you know you should", "you should", "its important", "I say so", "...just BECAUSE"],

    "look" : ["look", "appear", "seem"],
    "might_could" : ["might", "could"],

    "could_be" : ["#might_could be"],
    "must_be" :  [ "must be", "has to be"],
    "have_to_be" :  [ "must be", "have to be"],
    "reason_be" : ["#could_be", "#must_be"],
    "this_instead" : ["these pants", "my after shave", "the beard", "my hair",
        "the weather", "the sandwich I ate for lunch", "what I had for breakfast",
        "the time of year"
    ],
    "it_must_be" : ["", "It #reason_be #this_instead !", "Do you think it #could_be #this_instead ?"],

    "suppose" : ["suppose", "guess", "can see how"],
    "suppose_might_be": ["I #suppose I #could_be", "I #suppose I #have_to_be"],
    "act" : ["act", "be"],
    "more_or_less" : ["more", "less"],
    "o_more_or_less" : ["", "#more_or_less"],
    "frequently" : ["frequently", "often"],
    "whatever_it_takes" : ["doing whatever it takes",
        "thinking about it",
        "dreaming",
        "putting in the hours",
        "wondering how to be that",
        "living the dream",
        "practising",
        "doing this for your own good"],
    "what_I_am" :  ["necessary", "essential", "fundamental", "here", "standing over there", "a mirror to your soul", "free"],
    "something" : ["whatever it takes", "something", "anything", "not move", "not look behind you",
        "nothing", "not touch that", "the time warp again", "something else", "that thing you do", "what everyone else does",
        "what no-one would think of", "that thing you forgot to do", "... nope I've forgotten what I was thinking"],
    "dont_break_them" : ["rules", "systems", "laws", "procedures", "processes"],
    "bend_them" : ["bend them", "go around them", "bypass them", "push them", "stress them", "test them", "do just enough to conform",
        "show them for what they are", "brandish the sword of truth", "divulge that which no-one else will state",
        "lay open their rotten inner core", "debunk them",
        "play by their spirit, not their written lore",
        "reveal their ambiguities", "expose their flaws", "make clear their errors"],
    "whats_it_all_about" : ["bugs", "feedback", "testing", "scripts", "coverage",
        "requirements", "process", "people", "design", "questions",
        "questioning", "psychology", "hacking", "conditions", "test conditions",
        "test cases", "test scripts", "test coverage", "automation", "automating"],
    "baby" : ["", ", baby" , ", honey", ", friend", ", my friend", ", brother",
        ", isn't it?" , ", my child", ", because people love that kind of thing", ", you know"],

    "I_read_that" : ["" , "I'm sure I read that #read_that_in #excla_stop #optional_lol", "#optional_lol"],
    "read_that_in" : ["in Cosmo", "on twitter", "on the internet", "in a book",
        "in a magazine", "in some grafitti", "scrawled on a bathroom wall",
        "in a fortune cookie", "in some tea leaves", "in my horoscope", "in the paper",
        "in an exam syllabus", "in a standard", "in my spam folder"],


    "i_resolve_to_prefix" : ["I will", "I resolve to", "I will try to", "I suppose I could",
        "I think I might just", "I made a resolution to", "I resolved to"
    ],

    "find_more_of_this" : ["bugs", "ways to test this", "ways to explore this", "alternatives",
        "paths", "risks", "models", "ways to view this", "examples", "evidence"],

    "find_more_prefix" : ["find more", "provide alternative", "supply compelling"],


    "o_test_more_prefix" : ["learn to", "",  "practice so that I can", "learn to use my tools to"],
    "test_more" : ["more", "harder", "better", "effectively", "efficiently", "intelligently", "well"],
    "question_more" : ["question more", "scrutinize"],
    "q_more_of_this" : ["requirements", "ambiguity"],

    "exploitable_thing" : ["problem", "risk", "issue", "bug", "defect", "feature"],
    "exploitable_verb" : ["exploit", "explore", "pursue", "investigate", "follow"],
    "exploitable_way" : ["further", "more", "diligently", "in depth", "in detail", "tenaciously"],
    "write_down" : ["write down what I", "create a log of what I"],
    "write_downable" : ["test", "explore", "question", "decide", "do", "think", "learn"],
    "test_heck" : ["heck", "life", "essence", "riches", "lifeblood", "source", "mystery", "wonder", "magic"],

    "i_resolve_to_body" : ["#find_more_prefix #find_more_of_this", "#o_test_more_prefix test #test_more",
        "#question_more #q_more_of_this", "#exploitable_verb this #exploitable_thing further",
        "#write_down #write_downable", "test the #test_heck out of this system"
    ],

    "like_it" : ["like it", "to be", "thus", "so", "thusly"],

    "im_not_in_this_business" : ["Confidence", "Confidence Building", "'nice'", "Happy", "Illusion", "Hope", "Prayer", "Therapy", "Psychic", "'I want you to like me'"],
    "sure" : ["sure", "sure you're sure", "certain", "really sure", "absolutely sure"],
    "aboutthat" : ["", "about that"],
    "im_in_this_business" : ["Fear", "Reality", "Clarity", "Information", "Honesty", "Shattered Illusion",
        "Insight", "Risk", '"are you #sure #aboutthat ?"', "Change", "Doubt", "Uncertainty",
        "Exploration", "Bug", "Defect", "Problem"],
    "conman" : ["Con Man", "Confidence Man", "Grifter", "Hustler", "Card Shark"],
    "magicman" : ["Magician", "Illusionist", "Prestidigitator"],
    "magicbusiness" : ["Illusion", "Magic"],
    "person_in_this_business" : ["#conman is in the Confidence", "#magicman is in the #magicbusiness"],
    "me_or_someone_in_business" : ["#start I'm not in the #im_not_in_this_business", "A #person_in_this_business"],

}

var sentences = [

    "#start I'm not #im_not , #i_do_this #for_you #excla_stop #optional_lol",
    "#start I'm not #im_not , #i_do_this #for_you #o_just #just_because #optional_lol",
    "#start I'm not #im_not , #i_do_this #for_you #because_i_care #care_about !",
    "#start I'm not #im_not , #i_do_this #for_you #because_i_care #care_about more than #than_this ! #optional_lol",
    "#start I'm not #im_not , #i_do_this #for_you #because_i_care #care_about more than #than_this ! #tagline",
    // Of course I'm not evil, do I look evil?
    "#start I'm not #im_not ... Do I #look #im_not ?",
    //Do I look evil?
    "Do I #look #im_not ? #it_must_be #optional_lol",
    // Do I look evil? I'm so much more than that
    "Do I #look #im_not ? #better_than_that_now #excla_stop #optional_lol",

    // Do I look evil ? I suppose I might be!
    "Do I #look #im_not ? #suppose_might_be #excla_stop #optional_lol",
    // because I care
    "#o_just #just_because",

    "#start I'm not #im_not , do I #that_way? #optional_lol",
    "#start I'm not #im_not , I #just #that_way! #optional_tagline #optional_lol",

    // I'm not good, I'm better than that
    "#start I'm not #im_not , #i_am_so_much_more #excla_stop #optional_tagline #optional_lol",

    "Be #be_goodly #to_them #excla_stop #optional_lol",
    "Be #be_goodly #to_them #be_when #excla_stop #optional_lol",
    "Be #be_goodly #to_them #be_when because #be_because #excla_stop #optional_lol",
    //Act More Weird
    "#act #o_more_or_less #be_good #excla_stop #optional_lol",
    //Act More Weird More Frequently
    "#act #more_or_less #be_good #more_or_less #frequently #excla_stop #optional_lol",
    "#act #more_or_less #be_good now #excla_stop #optional_lol",
    //Act More Weird More Frequently to them
    "#act #more_or_less #be_good #more_or_less #frequently #to_them #excla_stop #optional_lol",
    "#act #more_or_less #be_good now, #to_them #excla_stop #optional_lol",
    "#act #more_or_less #be_good #to_them #more_or_less #frequently #excla_stop #optional_lol",
    // Are you a good little tester?
    "Are you a #be_goodly little tester?",
    // I'm not Evil, I'm just doing whatever it takes
    "#start I'm not #im_not , I'm just #whatever_it_takes #excla_stop #optional_lol",
    // I'm not evil, I'm necessary
    "#start I'm not #im_not , I'm #what_I_am #excla_stop #optional_lol",
    // Be the "E" word
    // Be whatever it takes!
    // Be good
    "Be #be_all_goodly #excla_stop #optional_lol",
    // It might seem Evil, but I do this because I care
    "It #might_could #look #im_not , but #i_do_this #just_because",
    // Do whatever it takes!
    "Do #something #excla_stop",
    //I don't break the rules, I bend them
    "I don't break the #dont_break_them , I #bend_them #excla_stop",

    // Testing is all about Bugs, Baby!
    "Testing is all about #whats_it_all_about #baby #excla_stop #I_read_that",

    //"I resolve to Find more Bugs"
    "#i_resolve_to_prefix #i_resolve_to_body",
    "I never say ' #i_resolve_to_body ' , I do #excla_stop #optional_lol",

    //Of course I'm not evil... Do I look like it?
    "#start I'm not #im_not ... Do I #look #like_it ?",

    // I'm not in the confidence business, I'm in the fear business
    "#me_or_someone_in_business business, I'm in the #im_in_this_business business #excla_stop #optional_lol",


    // TODO:
    // I'm not laughing 'at' you
    // If someone left a door open, would you walk in? Of course you would
    //Well... What if I just... oops ... wasn't me... quick leg it!
    // I dont want to settle for 'good' testing
    // If "System Testing is a necessary Evil" then "Evil Testing is a necessary system"
    // Not all testers are evil, just the good ones
    // Go on. Break it. You know you want to.
    // OK! To be honest, I do it 'cause it's fun.
]

function randomNumberUpTo(theVal){
    return Math.floor(Math.random() * theVal)
}
function getRandomValueFromArray(theArray) {
    return theArray[randomNumberUpTo(theArray.length)];
}

function getSentence() {
    return getRandomValueFromArray(sentences);
}

function expand_text(text_to_expand){

    var slen = text_to_expand.length;

    var processedSentence = "";
    var word = "";
    var isHash = false;

    for (var pos = 0; pos < slen; pos++)
    {
        var c = text_to_expand.charAt(pos);

        if(c == "#"){
            isHash = true;
            c=""; // abandon hash we don't want it in the word
        }

        if(c!=" "){
            word = word + c;
        }

        if(c==" " || (pos+1)==slen ){ // space or last char
            // process word
            //console.log(word);

            if(isHash){
                //console.log("EXPAND IT");
                var expandThis = phrases[word];
                if(expandThis===undefined){
                    console.log("Cannot find key: " +  word);
                }
                word = expand_text(getRandomValueFromArray(expandThis));
            }

            //console.log(word);
            processedSentence = processedSentence + word + " ";

            word = "";
            isHash=false;
        }
    }

    return processedSentence;
}

function removeExtraSpaces(text_to_process){

    var slen = text_to_process.length;

    var processedSentence = "";
    var c ="";
    var last_c ="";

    for (var pos = 0; pos < slen; pos++)
    {
        c = text_to_process.charAt(pos);

        if(c == " " && last_c ==" "){
            c=""; // abandon it
        }else{
            last_c = c;
        }

        processedSentence = processedSentence + c;

    }

    return processedSentence;
}

function removeSpacesAtStart(text_to_process){

    var slen = text_to_process.length;

    var processedSentence = "";
    var hitText=false;

    for (var pos = 0; pos < slen; pos++)
    {
        c = text_to_process.charAt(pos);

        if(c == " " && hitText===false){
            c=""; // abandon it
        }else{
            hitText=true;
        }

        processedSentence = processedSentence + c;

    }

    return processedSentence;
}

//http://stackoverflow.com/questions/1026069/how-do-i-make-the-first-letter-of-a-string-uppercase-in-javascript
function capitalizeFirstLetter(text_to_process) {
    return text_to_process.charAt(0).toUpperCase() + text_to_process.slice(1);
}

function removeSpacesBeforePunctuation(text_to_process){

    var slen = text_to_process.length;

    var processedSentence = "";
    var spaceToAdd=false;

    var punctuation = /[\.\,\!,\?]/;

    for (var pos = 0; pos < slen; pos++)
    {
        c = text_to_process.charAt(pos);

        if(c == " "){
            spaceToAdd=true;
            c=""; // abandon it
        }else{
            // is it punctuation?
            if(punctuation.test(c)){
                spaceToAdd=false;
            }

            if(spaceToAdd){
                processedSentence = processedSentence + " ";
                spaceToAdd=false;
            }

            processedSentence = processedSentence + c;
        }
    }
    return processedSentence;
}




function process_sentence(sentence){
    var processedSentence = expand_text(sentence);
    processedSentence = removeExtraSpaces(processedSentence);
    processedSentence = removeSpacesAtStart(processedSentence);
    processedSentence = removeSpacesBeforePunctuation(processedSentence);
    processedSentence = capitalizeFirstLetter(processedSentence);
    return processedSentence;
}

function random_sentence(){

    var sentence = getSentence();

    return process_sentence(sentence);
}


