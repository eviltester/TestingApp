package com.javafortesters.pulp.domain;

import com.javafortesters.pulp.domain.objects.PulpBook;
import com.javafortesters.pulp.reader.CsvReader;
import com.javafortesters.pulp.reader.forseries.SavageReader;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class DataReaderTest{

        @Test
        public void canReadSavage(){
            CsvReader reader = new CsvReader("/data/pulp/doc_savage_test.csv");
            reader.read();

            Assert.assertEquals(5, reader.numberOfLines());

            String fields[] = reader.getLines(0).split("\",\"");
            Assert.assertEquals(4, fields.length);

            Assert.assertEquals("Lester Dent / Will Murray", reader.getLineField(0,0));
            Assert.assertEquals("The Angry Canary", reader.getLineField(0,1));
            Assert.assertEquals("Jul / Aug, 1948", reader.getLineField(0,2));
            Assert.assertEquals("1948", reader.getLineField(0,3));

        }

        @Test
        public void canUseSavageReader() {
            SavageReader reader = new SavageReader("/data/pulp/doc_savage_test.csv");

            Assert.assertEquals(5, reader.numberOfLines());

            PulpBook book = reader.getBook(0);

            Assert.assertEquals("Kenneth Robeson", book.getHouseAuthorIndex());
            Assert.assertEquals("Street And Smith", book.getPublisherIndex());
            Assert.assertEquals("Lester Dent", book.getAuthorIndexes().get(0));
            Assert.assertEquals("Will Murray", book.getAuthorIndexes().get(1));
            Assert.assertEquals("The Angry Canary", book.getTitle());
            Assert.assertEquals("Jul / Aug, 1948", book.getSeriesId());
            Assert.assertEquals(1948, book.getPublicationYear());

        }

        @Test
        public void canReadAuthorNamesFromSavageReader(){

            SavageReader reader = new SavageReader("/data/pulp/doc_savage_test.csv");

            List<String> authorNames = reader.getAuthorNames();
            Assert.assertEquals(3, authorNames.size());
            Assert.assertTrue(authorNames.contains("Kenneth Robeson"));
            Assert.assertTrue(authorNames.contains("Lester Dent"));
            Assert.assertTrue(authorNames.contains("Will Murray"));

        }



}
