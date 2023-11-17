package kr.where.backend.suhwparkException.json;

import kr.where.backend.suhwparkException.CustomException;

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
