package sheettastic.sheets;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Component
public class SheetCsvMessageConverter extends AbstractHttpMessageConverter<Sheet> {

    public static final MediaType MEDIA_TYPE = new MediaType("text", "csv", Charset.forName("utf-8"));

    public SheetCsvMessageConverter(){
        super(MEDIA_TYPE);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return Sheet.class.equals(clazz);
    }


    @Override
    protected void writeInternal(Sheet sheet, HttpOutputMessage output) throws IOException, HttpMessageNotWritableException {
        output.getHeaders().setContentType(MEDIA_TYPE);
        output.getHeaders().set("Content-Disposition", "attachment; filename=\"" + sheet.getSheetName() + ".csv\"");

        OutputStream outputStream = output.getBody();

        Writer out = new BufferedWriter(new OutputStreamWriter(outputStream));
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.EXCEL);

        for (Row row : sheet.getRows()) {
            printer.printRecord(row.getCells());
        }

        printer.close();
    }

    @Override
    protected Sheet readInternal(Class<? extends Sheet> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {

        Sheet sheet = new Sheet();
        InputStream inputStream = inputMessage.getBody();
        Reader in = new InputStreamReader(inputStream);
        CSVParser csvParser = CSVFormat.EXCEL.parse(in);


        for (CSVRecord record : csvParser) {
            List<String> row = new ArrayList<>(record.size());
            record.forEach(row::add);

            sheet.addRow(row);
        }

        return sheet;
    }
}
