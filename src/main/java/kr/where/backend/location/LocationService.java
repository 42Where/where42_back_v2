package kr.where.backend.location;

import kr.where.backend.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    @Transactional
    public void create(final Member member, final String imac) {
        Location location = new Location(member, imac);

        locationRepository.save(location);

        member.setLocation(location);
    }
}
