package sheettastic.templates;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.*;

@Component
public class TemplateRepository {

    public TemplateRepository() {
        populate();
    }

    private Map<String, Template> templatesByName = new LinkedHashMap<>();

    public List<Template> fetchAll() {
        return new ArrayList<>(templatesByName.values());
    }


    public Template fetchById(String name) {
        Assert.notNull(name);
        return templatesByName.get(name);
    }

    public void save(Template template) {
        Assert.notNull(template.getName());
        Assert.notNull(template);
        templatesByName.put(template.getName(), template);
    }

    private void populate() {

        Arrays.asList(
                sample(),
                protocol()
        ).forEach(t -> this.save(t));
    }

    private Template sample() {
        Template sampleTemplate = base("sample");


        sampleTemplate
                .add("taxon",
                        FieldCapture.builder().fieldName("taxon").build()
                )
                .add("taxon id",
                        FieldCapture.builder().fieldName("taxon id").fieldType(JsonFieldType.IntegerNumber).build()
                );

        return sampleTemplate;
    }

    private Template protocol() {
        return base("protocol");
    }


    private Template base(String name) {
        Template template = Template.builder()
                .name(name)
                .targetType(name)
                .build();

        template
                .add(
                        "alias",
                        FieldCapture.builder().fieldName("alias").build()
                )
                .add("title",
                        FieldCapture.builder().fieldName("title").build()
                )
                .add(
                        "description",
                        FieldCapture.builder().fieldName("description").build()
                );
        ;

        template.setDefaultCapture(
                AttributeCapture.builder().build()
        );

        return template;
    }


}
