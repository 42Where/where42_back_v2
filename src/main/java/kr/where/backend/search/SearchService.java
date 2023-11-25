package kr.where.backend.search;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import kr.where.backend.api.IntraApiService;
import kr.where.backend.api.mappingDto.CadetPrivacy;
import kr.where.backend.group.GroupService;
import kr.where.backend.group.entity.Group;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberService;
import kr.where.backend.member.exception.MemberException;
import kr.where.backend.search.dto.ResponseSearch;
import kr.where.backend.search.exception.SearchException;
import kr.where.backend.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {
    private static final String PATTERN = "^[0-9a-zA-Z]*$";
    private static final String TOKEN_NAME = "search";
    private static final int MAXIMUM_SIZE = 10;
    private static final int MINIMUM_LENGTH = 2;
    private final MemberService memberService;
    private final IntraApiService intraApiService;
    private final TokenService tokenService;
    private final GroupService groupService;

    /**
     * @param keyWord 찾을 검색 입력값
     * @return response로 변경하여 client 측에 전달 검색하고자 하는 입력값의 결과 10개를 반환 블랙홀에 빠지지 않은 카뎃을 필터로 걸러서 response DTO 생성
     * 입력 받은 값을 trim으로 공백을 없애주고, 대문자 영어가 들어와도 검색 가능하게 toLowerCase 적용
     */

    public List<ResponseSearch> search(final Long intraId, final String keyWord) {
        final String word = validateKeyWord(keyWord.trim().toLowerCase());
        final Member member = memberService.findOne(intraId).orElseThrow(MemberException.NoMemberException::new);

        return responseOfSearch(member, findActiveCadets(word));
    }

    private String validateKeyWord(final String keyWord) {
        if (keyWord.isEmpty() || !isContainOnlyEnglishAndDigit(keyWord)) {
            throw new SearchException.InvalidContextException();
        }
        if (!validateLength(keyWord)) {
            throw new SearchException.InvalidLengthException();
        }
        return keyWord;
    }

    private boolean isContainOnlyEnglishAndDigit(final String keyWord) {
        return Pattern.matches(PATTERN, keyWord);
    }

    private boolean validateLength(final String keyWord) {
        return keyWord.length() < MINIMUM_LENGTH;
    }

    private List<CadetPrivacy> findActiveCadets(final String word) {
        final List<CadetPrivacy> result = new ArrayList<>();

        do {
            isActiveCadet(result,
                    intraApiService
                            .getCadetsInRange(tokenService.findAccessToken(TOKEN_NAME), word));
        } while (result.size() < MAXIMUM_SIZE);

        return result;
    }

    private void isActiveCadet(final List<CadetPrivacy> result, final List<CadetPrivacy> cadetPrivacies) {
        cadetPrivacies.stream()
                .filter(CadetPrivacy::isActive)
                .limit(MAXIMUM_SIZE - result.size())
                .forEach(result::add);
    }

    private List<ResponseSearch> responseOfSearch(final Member member, final List<CadetPrivacy> cadetPrivacies) {
        final Group group = groupService.findOneGroupById(member.getDefaultGroupId());

        return cadetPrivacies.stream()
                .map(search -> memberService.findOne(search.getId())
                        .orElse(memberService.createFlashMember(search)))
                .map(search -> ResponseSearch.of(group, search))
                .toList();
    }
}
