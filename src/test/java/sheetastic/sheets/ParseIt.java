package sheetastic.sheets;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sheettastic.sheets.CsvStreamToSheetConverter;
import sheettastic.sheets.Sheet;

import java.io.*;

/**
 * Created by Dave on 21/10/2017.
 */

public class ParseIt {

    private FileInputStream inputStream;
    private CsvStreamToSheetConverter csvStreamToSheetConverter;

    @Before
    public void buildUp() throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("test.csv").getFile());
        inputStream  = new FileInputStream(file);

        csvStreamToSheetConverter = new CsvStreamToSheetConverter();
    }

    @After
    public void after() throws IOException {
        if (inputStream != null){
            inputStream.close();
        }
    }

    @Test
    public void simpleConvertTest() throws IOException {
        Sheet expected = new Sheet();
        expected.addRow(new String[]{"a","b","c"});
        expected.addRow(new String[]{"1","2","3"});

        Sheet actual = new Sheet();
        csvStreamToSheetConverter.convert(actual,inputStream);

        Assert.assertEquals(expected,actual);
        System.out.println(actual);
    }
}
