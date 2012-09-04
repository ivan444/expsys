package imaing.expsys.server.api;

import imaing.expsys.server.api.APIExtendedResponse.Status;

import java.io.IOException;
import java.io.OutputStream;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JSONObjectMapper {
	private final ObjectMapper mapper;
	
	public JSONObjectMapper() {
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		mapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, true);
		mapper.getSerializerProvider().setNullValueSerializer(new NullSerializer());
	}
	
	private class NullSerializer extends JsonSerializer<Object> {
		public void serialize(Object value, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException {
			jgen.writeString("");
		}
	}
	
	public <T> T read(String content, TypeReference<T> valueTypeRef) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(content, valueTypeRef);
	}
	
	public <T> T read(String json, Class<T> valueType) throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(json, valueType);
	}
	
	public void write(OutputStream out, Object obj) throws JsonGenerationException, JsonMappingException, IOException {
		mapper.writeValue(out, obj);
	}
	
	public <P> void writeResponse(OutputStream out, Status status, String msg, P payload) throws JsonGenerationException, JsonMappingException, IOException {
		mapper.writeValue(out, new APIExtendedResponse<P>(status, msg, payload));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void writeResponse(OutputStream out, Status status, String msg) throws JsonGenerationException, JsonMappingException, IOException {
		mapper.writeValue(out, new APIExtendedResponse(status, msg, null));
	}
}
