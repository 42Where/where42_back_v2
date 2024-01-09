package kr.where.backend.location;

import kr.where.backend.auth.authUserInfo.AuthUserInfo;
import kr.where.backend.location.dto.ResponseLocationDTO;
import kr.where.backend.location.dto.UpdateCustomLocationDTO;
import kr.where.backend.member.Member;
import kr.where.backend.member.MemberRepository;
import kr.where.backend.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void create(final Member member, final String imac) {
        Location location = new Location(member, imac);

        locationRepository.save(location);

        member.setLocation(location);
    }

    @Transactional
    public ResponseLocationDTO updateCustomLocation(
            final UpdateCustomLocationDTO updateCustomLocationDto,
            final AuthUserInfo authUser) {
        final Member member = memberRepository.findByIntraId(authUser.getIntraId()).orElseThrow(MemberException.NoMemberException::new);
        final Location location = locationRepository.findByMember(member);

        location.setCustomLocation(updateCustomLocationDto.getCustomLocation());

        return ResponseLocationDTO.builder().location(location).build();
    }
}
