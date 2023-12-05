package kr.where.backend.api.exception;

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
