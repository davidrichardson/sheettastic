package sheetastic.sheets;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import sheettastic.sheets.Sheet;

import java.util.Optional;

public class TrimIt {

    private Sheet sheet;

    @Before
    public void buildUp(){
        sheet = new Sheet();
        sheet.addRow(new String[]{"a1","b1","c1"});
        sheet.addRow(new String[]{"a2","b2","c2"});
        sheet.addRow(new String[]{"a3","b3"});
    }

    @Test
    public void trimMiddleColumn(){
        Sheet expected = new Sheet();
        expected.addRow(new String[]{"a1","c1"});
        expected.addRow(new String[]{"a2","c2"});
        expected.addRow(new String[]{"a3"});

        sheet.deleteColumn(1);

        Assert.assertEquals(expected,sheet);
    }

    @Test
    public void trimAfterMiddleColumn(){
        Sheet expected = new Sheet();
        expected.addRow(new String[]{"a1","b1"});
        expected.addRow(new String[]{"a2","b2"});
        expected.addRow(new String[]{"a3","b3"});

        sheet.deleteColumnsPastIndex(1);

        Assert.assertEquals(expected,sheet);
    }

    @Test
    public void lastNonEmptyColumn(){
        sheet = new Sheet();
        sheet.addRow(new String[]{"a1"," ","b1"});
        sheet.addRow(new String[]{"a2",null,"b2"});
        sheet.addRow(new String[]{"a3","","b3","",null,"     "});

        Optional<Integer> expectedLastNonEmptyColumn = Optional.of(2);

        Assert.assertEquals(expectedLastNonEmptyColumn,sheet.indexOfLastNonEmptyColumn());
    }

    @Test
    public void removeEmptyRows(){
        sheet = new Sheet();
        sheet.addRow(new String[]{"a1"," ","b1"});
        sheet.addRow(new String[]{});
        sheet.addRow(new String[]{"",null,"     "});

        Sheet expected = new Sheet();
        expected.addRow(new String[]{"a1"," ","b1"});

        sheet.removeEmptyRows();

        Assert.assertEquals(expected,sheet);
    }

    @Test
    public void trimToUsedSpace(){
        sheet = new Sheet();
        sheet.addRow(new String[]{"a1"," ","c1",null, null, null});
        sheet.addRow(new String[]{"a2","b2","c2"});
        sheet.addRow(new String[]{"",null,"     "});

        Sheet expected = new Sheet();
        expected.addRow(new String[]{"a1"," ","c1"});
        expected.addRow(new String[]{"a2","b2","c2"});
        expected.addRow(new String[]{"",null,"     "});

        sheet.removeColumnsPastLastNonEmpty();

        Assert.assertEquals(expected,sheet);
    }


}
