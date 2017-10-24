package sheettastic.sheets;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dave on 21/10/2017.
 */
@Component
public class CsvStreamToSheetConverter {

    public void convert(Sheet sheet, InputStream inputStream) throws IOException {


        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

        Reader in = new InputStreamReader(inputStream);

        CSVParser csvParser = CSVFormat.EXCEL.parse(in);


        for (CSVRecord record : csvParser) {
            List<String> row = new ArrayList<>(record.size());
            record.forEach(row::add);

            sheet.addRow(row);
        }

    }




}
