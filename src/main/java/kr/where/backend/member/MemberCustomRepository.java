package kr.where.backend.member;

import java.util.Map;

public interface MemberCustomRepository {

    void updateMemberInOrOutStatus(Map<String, String> intraNameStateMap);
}
