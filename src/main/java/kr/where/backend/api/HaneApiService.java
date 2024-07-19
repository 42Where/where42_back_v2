package kr.where.backend.api;

import java.util.ArrayList;
import java.util.List;
import kr.where.backend.api.exception.RequestException;
import kr.where.backend.api.http.HttpHeader;
import kr.where.backend.api.http.HttpResponse;
import kr.where.backend.api.http.UriBuilder;
import kr.where.backend.api.json.hane.Hane;
import kr.where.backend.api.json.hane.HaneRequestDto;
import kr.where.backend.api.json.hane.HaneResponseDto;
import kr.where.backend.member.Member;
import kr.where.backend.oauthtoken.OAuthTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HaneApiService {
	private final OAuthTokenService oauthTokenService;
	private static final String HANE_TOKEN = "hane";

	/**
	 * hane api 호출하여 in, out state 반환
	 */
	public Hane getHaneInfo(final String name, final String token) {
		try {
			return JsonMapper.mapping(HttpResponse.getMethod(HttpHeader.requestHaneInfo(token), UriBuilder.hane(name)),
				Hane.class);
		} catch (final RequestException exception) {
			log.warn("[hane] {} : {}", name, exception.toString());
			return new Hane();
		}
	}

	@Transactional
	public void updateInClusterForMainPage(final Member member) {
		if (member.isAgree()) {
			member.setInCluster(getHaneInfo(member.getIntraName(), oauthTokenService.findAccessToken(HANE_TOKEN)));

			log.info("member {}의 inCluster가 변경되었습니다", member.getIntraName());
		}
	}

	public List<HaneResponseDto> getHaneListInfo(final List<HaneRequestDto> haneRequestDto, final String token) {
		try {
			return JsonMapper.mappings(HttpResponse.postMethod(HttpHeader.requestHaneListInfo(haneRequestDto, token),
					UriBuilder.hane("where42All")), HaneResponseDto[].class);
		} catch (final RequestException exception) {
			log.warn("[hane] : {}", exception.toString());
			return new ArrayList<>();
		}
	}

	@Transactional
	public void updateMemberInOrOutState(final Member member, final String state) {
		member.setInCluster(Hane.create(state));
	}
}
