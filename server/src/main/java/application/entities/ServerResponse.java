package application.entities;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.io.IOException;

@JsonSerialize(using = ServerResponse.ServerResponseSerializer.class)
public abstract class ServerResponse {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public String toString() {
        try {
            return OBJECT_MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return super.toString();
        }
    }

    public static <T> ServerDataResponse<T> data(T data) {
        return new ServerDataResponse<>(data);
    }

    public static ServerErrorResponse error(int code, String message) {
        return new ServerErrorResponse(code, message);
    }

    public static class ServerDataResponse<T> extends ServerResponse {
        private T data;

        public ServerDataResponse(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }
    }

    public static class ServerErrorResponse extends ServerResponse {
        private int code;
        private String message;

        public ServerErrorResponse(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }

    static class ServerResponseSerializer extends JsonSerializer<ServerResponse> {
        @Override
        public void serialize(ServerResponse value, JsonGenerator gen,
                              SerializerProvider serializers) throws IOException {
            gen.writeStartObject();
            if (value instanceof ServerDataResponse) {
                gen.writeObjectField("data", ((ServerDataResponse<?>) value).getData());
            } else {
                gen.writeObjectFieldStart("error");
                gen.writeNumberField("code", ((ServerErrorResponse) value).getCode());
                gen.writeStringField("message", ((ServerErrorResponse) value).getMessage());
                gen.writeEndObject();
            }
            gen.writeEndObject();
        }
    }
}
