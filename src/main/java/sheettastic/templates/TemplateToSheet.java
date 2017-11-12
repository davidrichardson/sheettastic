package sheettastic.templates;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sheettastic.sheets.Row;
import sheettastic.sheets.Sheet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class TemplateToSheet implements Converter<Template,Sheet>{

    public Sheet convert(Template template) {

        Sheet sheet = new Sheet();
        List<Row> rows = new ArrayList<>();

        Row headerRow = new Row(
                template
                        .getColumnCaptures()
                        .entrySet()
                        .stream()
                        .flatMap(entry -> Stream.concat(
                                Stream.of(entry.getKey()),
                                entry.getValue().expectedColumnHeaders().stream()
                        ))
                        .collect(Collectors.toList())
        );
        rows.add(headerRow);

        sheet.addRow(headerRow);
        sheet.setHeaderRowIndex(0);
        sheet.setTemplate(template);

        sheet.setSheetName(template.getName()+"_template");

        return sheet;
    }

}
