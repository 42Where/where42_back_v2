package kr.where.backend.member;

import kr.where.backend.api.json.hane.HaneResponseDto;

import java.util.List;
import java.util.Map;

public interface MemberCustomRepository {

    void updateMemberInOrOutStatus(List<HaneResponseDto> haneResponseDtos);
}
