package kr.where.backend.search;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import kr.where.backend.api.IntraApiService;
import kr.where.backend.member.dto.Seoul42;
import kr.where.backend.member.MemberService;
import kr.where.backend.search.dto.ResponseSearch;
import kr.where.backend.search.exception.SearchException;
import kr.where.backend.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {
    private static final String PATTERN = "^[a-zA-Z]*$";
    private static final String TOKEN_NAME = "search";
    private static final int MAXIMUM_SIZE = 9;
    private static final int MINIMUM_LENGTH = 2;
    private final MemberService memberService;
    private final IntraApiService intraApiService;
    private final TokenService tokenService;

    /**
     *
     * @param keyWord 찾을 검색 입력값
     * @return response로 변경하여 client 측에 전달
     * 검색하고자 하는 입력값의 결과 10개를 반환
     * 블랙홀에 빠지지 않은 카뎃을 필터로 걸러서 response DTO 생성
     */

    public List<ResponseSearch> search(final String keyWord) {
        final String word = validateKeyWord(keyWord.trim().toLowerCase());

        return responseOfSearch(findActiveCadets(word));
    }

    private String validateKeyWord(final String keyWord) {
        if (keyWord.isEmpty() || !isContainOnlyEnglish(keyWord)) {
            throw new SearchException.InvalidContextException();
        }
        if (!validateLength(keyWord)) {
            throw new SearchException.InvalidLengthException();
        }
        return keyWord;
    }

    private boolean isContainOnlyEnglish(final String keyWord) {
        return Pattern.matches(PATTERN, keyWord);
    }

    private boolean validateLength(final String keyWord) {
        return keyWord.length() < MINIMUM_LENGTH;
    }

    private List<Seoul42> findActiveCadets(final String word) {
        final List<Seoul42> result = new ArrayList<>();

        do {
            isActiveCadet(result,
                    intraApiService
                            .get42UsersInfoInRange(tokenService.findAccessToken(TOKEN_NAME), word));
        } while(result.size() > MAXIMUM_SIZE);

        return result;
    }

    private void isActiveCadet(final List<Seoul42> result, final List<Seoul42> responses) {
        for (Seoul42 response : responses) {
            if (response.isActive()) {
                result.add(response);
            }
            if (result.size() > MAXIMUM_SIZE) {
                break;
            }
        }
    }

    private List<ResponseSearch> responseOfSearch(final List<Seoul42> result) {

         return result.stream()
                .map(search -> memberService.findOne(search.getId())
                        .orElse(() -> memberService.createNotMember(search)))
                .map(ResponseSearch::of)
                .toList();
    }
}
