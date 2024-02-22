package kr.where.backend.search;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import kr.where.backend.api.IntraApiService;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.auth.authUser.AuthUser;
import kr.where.backend.group.GroupRepository;
import kr.where.backend.group.entity.Group;
import kr.where.backend.group.exception.GroupException;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberService;
import kr.where.backend.member.exception.MemberException;
import kr.where.backend.search.dto.ResponseSearchDTO;
import kr.where.backend.search.exception.SearchException;
import kr.where.backend.oauthtoken.OAuthTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {
    private static final String PATTERN = "^[0-9a-z]*$";
    private static final String TOKEN_NAME = "search";
    private static final int MAXIMUM_SIZE = 10;
    private static final int MINIMUM_LENGTH = 1;
    private static final int MAXIMUM_LENGTH = 10;
    private final MemberService memberService;
    private final IntraApiService intraApiService;
    private final OAuthTokenService oauthTokenService;
    private final GroupRepository groupRepository;

    /**
     * @param keyWord 찾을 검색 입력값
     * @return response로 변경하여 client 측에 전달 검색하고자 하는 입력값의 결과 10개를 반환 블랙홀에 빠지지 않은 카뎃을 필터로 걸러서 response DTO 생성
     * 입력 받은 값을 trim으로 공백을 없애주고, 대문자 영어가 들어와도 검색 가능하게 toLowerCase 적용
     */

    public List<ResponseSearchDTO> search(final String keyWord, final AuthUser authUser) {
        final String word = validateKeyWord(keyWord.trim().toLowerCase());
        final Member member = memberService.findOne(authUser.getIntraId())
                .orElseThrow(MemberException.NoMemberException::new);

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
        return keyWord.length() > MINIMUM_LENGTH && keyWord.length() < MAXIMUM_LENGTH;
    }

    private List<CadetPrivacy> findActiveCadets(final String word) {
        final List<CadetPrivacy> result = new ArrayList<>();

        int page = 1;
        while (true) {
            final List<CadetPrivacy> searchApiResult =
                    intraApiService.getCadetsInRange(oauthTokenService.findAccessToken(TOKEN_NAME), word, page);
            isActiveCadet(result, searchApiResult);
            if (searchApiResult.size() < MAXIMUM_SIZE || result.size() > 14) {
                break;
            }
            page += 1;
        }
        return result;
    }

    private void isActiveCadet(final List<CadetPrivacy> result, final List<CadetPrivacy> cadetPrivacies) {
        cadetPrivacies.stream().filter(CadetPrivacy::isActive).forEach(result::add);
    }

    private List<ResponseSearchDTO> responseOfSearch(final Member member, final List<CadetPrivacy> cadetPrivacies) {
        final Group group = groupRepository
                .findById(member.getDefaultGroupId())
                .orElseThrow(GroupException.NoGroupException::new);

        return cadetPrivacies
                .stream()
                .map(search -> memberService.findOne(search.getId())
                        .orElseGet(() -> memberService.createDisagreeMember(search)))
                .map(search -> new ResponseSearchDTO(group, search))
                .toList();
    }
}
