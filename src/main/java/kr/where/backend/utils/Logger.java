package kr.where.backend.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Slf4j
public enum Logger {

    LOGIN("[INFO] : '{}님이 로그인 하였습니다."),
    LOGOUT("[INFO] : '{}님이 로그아웃 하였습니다."),
    UPDATE_GROUP_NAME("[INFO] : '{}'님이 그룹이름을 '{}로 변경하였습니다."),
    CREATE_GROUP("[INFO] : '{}'님이 그룹 '[{}]'을 생성하였습니다."),
    DELETE_GROUP("[INFO] : '{}'님이 그룹 '[{}]'을 삭제했습니다"),

    INTRA_API_FALLBACK("[INTRA API] : Cadet Privacy fallback {}");

    public final String value;

    Logger(String value) {
        this.value = value;
    }

    public static void main(String[] args) {
        log.info(Logger.LOGIN.value, "suhwpark", "where42");
    }
}


