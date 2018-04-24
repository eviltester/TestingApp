package uk.co.compendiumdev.restlisticator.domain;


import org.junit.Assert;
import org.junit.Test;
import uk.co.compendiumdev.restlisticator.domain.app.TheListicator;
import uk.co.compendiumdev.restlisticator.domain.list.ListicatorList;

public class TheListicatorTest {

    @Test
    public void canCreateListicator(){
        TheListicator app = new TheListicator();

        Assert.assertEquals(0,app.listCount());

        app.addList(new ListicatorList("first List", "this is my list"));

        Assert.assertEquals(1,app.listCount());

    }
}
