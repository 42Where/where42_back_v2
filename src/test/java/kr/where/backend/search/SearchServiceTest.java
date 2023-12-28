package kr.where.backend.search;


import kr.where.backend.member.MemberService;
import kr.where.backend.search.exception.SearchException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback
public class SearchServiceTest {

    @Autowired
    SearchService searchService;
    @Autowired
    MemberService memberService;

    @DisplayName("유효하지 않은 keyword값이 들어왔을 경우 예외처리")
    @ValueSource(strings = {"수환", "a", "", "%%"})
    @ParameterizedTest
    public void invalidKeyWord(final String keyWord) {
        //then
        assertThatThrownBy(() -> searchService.search(1, keyWord))
                .isInstanceOf(SearchException.class);

        assertThatThrownBy(() -> searchService.search(1, keyWord))
                .isInstanceOf(SearchException.class);
    }

//    @DisplayName("올바른 keyWord의 검색 결과 확인")
//    @Test
//    public void getCadetPrivacy() {
//        //given
//        memberService.createAgreeMember(CadetPrivacy.createForTest(22224L, "jonhan", "c1r1s1",
//                "image", true, "2022-10-31"), Hane.createForTest("IN"));
//
//        //when
//        List<ResponseSearch> result = searchService.search(22224L, "jn");
//
//        //then
//        result.stream().forEach(System.out::println);
//    }
}