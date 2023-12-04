package kr.where.backend.utils;

import lombok.extern.slf4j.Slf4j;

public enum Logger {
    LOGIN("[INFO] : '{}님이 로그인 하였습니다."),
    LOGOUT("[INFO] : '{}님이 로그아웃 하였습니다."),
    ADMIN_LOGIN("[ADMIN] : 관리자 '{}' 님이 로그인 하였습니다."),
    ADMIN_LOGOUT("[ADMIN] : 관리자 '{}' 님이 로그아웃 하였습니다."),
    CHECK_MAIN_PAGE("[INFO] : '{}'님이 메인페이지를 조회하였습니다."),
    CREATE_MEMBER("[INFO] : 새로운 멤버'{}'님이 등록되었습니다."),
    UPDATE_MEMBER_MSG("[INFO] : '{}'님이 상태메세지를 {}로 변경하였습니다."),
    UPDATE_MEMBER_LOCATION("[INFO] : '{}'님의 자리가 {}에서 {}로 변경되었습니다."),
    UPDATE_MEMBER_CUSTOM_LOCATION("[INFO] : '{}'님이 {}로 수동 설정하였습니다."),
    UPDATE_GROUP_NAME("[INFO] : '{}'님이 그룹이름을 '{}로 변경하였습니다."),
    CREATE_GROUP("[INFO] : '{}'님이 그룹 '[{}]'을 생성하였습니다."),
    DELETE_GROUP("[INFO] : '{}'님이 그룹 '[{}]'을 삭제했습니다"),
    UPDATE_GROUPMEMBER("[INFO] : '{}'님이 '{}님을 친구 추가 하였습니다."),
    DELETE_GROUPMEMBER("[INFO] : '{}'번 그룹에서 {}님이 삭제 되었습니다."),
    SEARCH_CADET("[INFO] : '{}' 검색어로 요청 되었습니다."),
    UPDATE_IMG("[INFO] : 이미지 업데이트를 시작합니다."),
    GET_OAUTH_TOKEN("[OAuth] : Intra OAuth Token이 발급되었습니다."),
    FAIL_REQUSET_OAUTH_TOKEN("[OAuth] : Intra OAuth Token 발급 요청 횟수가 초과되었습니다."),
    CREATE_TOKEN("[TOKEN] : {} 토큰이 생성되었습니다."),
    DELETE_TOKEN("[TOKEN] : {} 토큰이 삭제되었습니다."),
    UPDATE_TOKEN("[TOKEN] : {} 토큰이 갱신 되었습니다."),
    EXPIRE_TOKEN("[TOKEN] : {} 토큰이 만료되었습니다."),
    INTRA_API_FALLBACK("[INTRA API] : Cadet Privacy fallback {}"),
    HANE_API_ERROR("[HANE API] : Hane Api 오류가 발생하였습니다."),
    EXCEPTION("[EXCEPTION] : {}");

    public final String msg;

    Logger(String msg) {
        this.msg = msg;
    }


    //이런식으로 사용하면 돠지 않을까 싶습니다~
//    @Slf4j
//    public static void main(String[] args) {
//        log.info(Logger.LOGIN.msg, "suhwpark", "where42");
//    }
}


