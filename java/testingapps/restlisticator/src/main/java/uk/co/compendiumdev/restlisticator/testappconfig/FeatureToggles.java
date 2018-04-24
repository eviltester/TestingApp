package uk.co.compendiumdev.restlisticator.testappconfig;


public enum FeatureToggles {

    // this class is purely because we are a test app and
    // I want to be able to switch things on and off at runtime

        BUG_001_FIXED, // the create POST message does not return a list id in the location header
        BUG_002_FIXED, // the create POST message does not return list in the response message
        BUG_003_FIXED, // create POST message does not have created date and amended date in payload
        BUG_004_FIXED, // when not fixed then the system does not return WWW-Authenticate header on 401
        BUG_005_FIXED, // when not fixed then any authenticated user can partial POST and PATCH lists
        BUG_006_FIXED, // when not fixed then an unauthenticated user can get the feature toggles
        BUG_007_FIXED, // when not fixed then a PATCH can create LIST entity
        BUG_008_FIXED, // when not fixed then a POST to a non-existant Lists/guid can not create the entity
        BUG_009_FIXED, // when not fixed then a 201 actually returns 200 - this is 'debatable'
        BUG_010_FIXED, // when not fixed then a 404 actually returns 400 - this is 'debatable'
        BUG_011_FIXED, // when not fixed then a content-type is not set in the response header and defaults to text/html which is incorrect
        BUG_012_FIXED, // when not fixed then apikey length check is minimum 9 instead of minimum 10
        ;

    private boolean state=true;

        public void toggleState(){
            this.state = !state;
        }

        public boolean getState(){
            return this.state;
        }

    public void setState(Boolean state) {
        this.state = state;
    }

    public static void toggleAll(Boolean state){
        for(FeatureToggles toggle : FeatureToggles.values()){
            toggle.setState(state);
        }
    }
}
