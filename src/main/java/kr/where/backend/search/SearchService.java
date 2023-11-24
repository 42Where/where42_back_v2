package kr.where.backend.search;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import kr.where.backend.api.IntraApiService;
import kr.where.backend.member.DTO.Seoul42;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberService;
import kr.where.backend.search.exception.SearchException;
import kr.where.backend.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {
    private static final String END_WORD = "z";
    private static final String PATTERN = "^[a-zA-Z]*$";
    private final MemberService memberService;
    private final IntraApiService intraApiService;
    private final TokenService tokenService;


    public List<ResponseSearch> search(final String keyWord) {
        final String word = validateKeyWord(keyWord.trim().toLowerCase());

        return responseOfSearch(findActiveCadets(word));
    }

    private String validateKeyWord(final String keyWord) {
        if (keyWord.isEmpty() || !isContainOnlyEnglish(keyWord)) {
            throw new SearchException.InvalidKeyWordException();
        }

        return keyWord.trim().toLowerCase();
    }

    private boolean isContainOnlyEnglish(final String keyWord) {
        return Pattern.matches(PATTERN, keyWord);
    }

    private List<Seoul42> findActiveCadets(final String word) {
        final List<Seoul42> result = new ArrayList<>();

        do {
            isActiveCadet(result,
                    intraApiService
                            .get42UsersInfoInRange(tokenService.findAccessToken("search"), word, word + END_WORD));
        } while(result.size() > 10);

        return result;
    }

    private void isActiveCadet(final List<Seoul42> result, final List<Seoul42> responses) {
        for (Seoul42 response : responses) {
            if (response.isActive()) {
                result.add(response);
            }
            if (result.size() > 9) {
                break;
            }
        }
    }

    private List<ResponseSearch> responseOfSearch(final List<Seoul42> result) {
        final List<ResponseSearch> responseSearches = new ArrayList<>();

        for (Seoul42 search : result) {
            final Member member = memberService.findOne(search.getId());
            if (member != null) {
                responseSearches.add(ResponseSearch.of(member));
                continue ;
            }
            responseSearches.add(memberService.createNotMember(search));
        }

        return responseSearches;
    }
}
