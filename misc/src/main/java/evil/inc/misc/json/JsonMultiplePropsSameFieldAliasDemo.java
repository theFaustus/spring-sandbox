package evil.inc.misc.json;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

/**
 * Demonstrates how Jackson resolves a field that may appear under multiple
 * JSON property names (<code>type</code> or <code>streetType</code>).
 *
 * <p>The class defines alias and property mapped to different JSON keys, both assigning
 * to the same field.
 *
 */
public class JsonMultiplePropsSameFieldAliasDemo {

    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Street {

        @JsonProperty("id")
        private String id;

        @JsonProperty("type")
        @JsonAlias("streetType")
        private StreetType type;

        @JsonProperty("name")
        private String name;

        @JsonProperty("number")
        private String number;

        @JsonProperty("zipCode")
        private String zipCode;

        @Override
        public String toString() {
            return "id=" + id +
                   ", type=" + type +
                   ", name=" + name +
                   ", number=" + number +
                   ", zip=" + zipCode;
        }
    }

    @Setter
    @Getter
    public static class StreetType {
        private String code;

        @Override
        public String toString() {
            return code;
        }
    }

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        String json1 = "{ \"id\": \"1\", \"type\": { \"code\": \"A\" } }";
        String json2 = "{ \"id\": \"2\", \"streetType\": { \"code\": \"B\" } }";
        String json3 = "{ \"id\": \"2\", \"streetType\": { \"code\": \"B\" }, \"type\": { \"code\": \"A\" } }";
        String json4 = "{ \"id\": \"2\", \"type\": { \"code\": \"A\" }, \"streetType\": { \"code\": \"B\" } }";

        Street r1 = mapper.readValue(json1, Street.class);
        Street r2 = mapper.readValue(json2, Street.class);
        Street r3 = mapper.readValue(json3, Street.class);
        Street r4 = mapper.readValue(json4, Street.class);

        System.out.println("From 'type' < { \"id\": \"1\", \"type\": { \"code\": \"A\" } } >:       " + r1);
        System.out.println("From 'streetType' < { \"id\": \"2\", \"streetType\": { \"code\": \"B\" } } >: " + r2);
        System.out.println("From 'streetType' and 'type' - Last one wins < { \"id\": \"2\", \"streetType\": { \"code\": \"B\" }, \"type\": { \"code\": \"A\" } } >: " + r3);
        System.out.println("From 'type' and 'streetType' - Last one wins < { \"id\": \"2\", \"type\": { \"code\": \"A\" }, \"streetType\": { \"code\": \"B\" } } >: " + r4);
    }
}
