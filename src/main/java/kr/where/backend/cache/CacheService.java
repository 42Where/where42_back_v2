package kr.where.backend.cache;

import kr.where.backend.api.IntraApiService;
import kr.where.backend.api.json.CadetPrivacy;
import kr.where.backend.oauthtoken.OAuthTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CacheService {
    final IntraApiService intraApiService;
    final OAuthTokenService oauthTokenService;
    private static final String TOKEN_NAME = "search";
    private static final int MAXIMUM_SIZE = 10;

    @Cacheable(key = "#word", value = "searchCache", cacheManager = "redisCacheManager", unless = "#result.isEmpty()")
    public List<CadetPrivacy> getSearchCacheResult(final String word) {
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
}
