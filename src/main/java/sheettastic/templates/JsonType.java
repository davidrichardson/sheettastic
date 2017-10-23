package sheettastic.templates;

import org.json.JSONObject;

/**
 * Created by Dave on 18/10/2017.
 */
public enum JsonType {

    String,
    IntegerNumber,
    FloatNumber,
    Boolean;

    public void addValueToDocument(String fieldName, String value, JSONObject document){
        switch (this){
            case String:
                document.put(fieldName,value);
                break;
            case IntegerNumber:
                try {
                    long longVal = Long.parseLong(value);
                    document.put(fieldName,longVal);
                }
                catch (NumberFormatException e){
                    document.put(fieldName,value);
                }
                break;
            case FloatNumber:
                try {
                    double doubleVal = Double.parseDouble(value);
                    document.put(fieldName,doubleVal);
                }
                catch (NumberFormatException e){
                    document.put(fieldName,value);
                }
                break;
            case Boolean:
                try {
                    boolean boolVal = java.lang.Boolean.parseBoolean(value);
                    document.put(fieldName,boolVal);
                }
                catch (NumberFormatException e){
                    document.put(fieldName,value);
                }
                break;
            default:
                throw new IllegalArgumentException("cannot add value for type: "+this.name());
        }

    }
}
