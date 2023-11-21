package kr.where.backend.exception.json;

import kr.where.backend.exception.CustomException;

public class JsonException extends CustomException {
    public JsonException(final JsonErrorCode jsonErrorCode) {
        super(jsonErrorCode);
    }

    public static class DeserializeException extends JsonException {
        public DeserializeException() {
            super(JsonErrorCode.DESERIALIZE_FAIL);
        }
    }
}
