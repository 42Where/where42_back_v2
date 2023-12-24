package kr.where.backend.location;

import kr.where.backend.location.dto.ResponseLocationDto;
import kr.where.backend.location.dto.UpdateCustomLocationDto;
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
    public ResponseLocationDto updateCustomLocation(final UpdateCustomLocationDto updateCustomLocationDto) {
        final Member member = memberRepository.findByIntraId(updateCustomLocationDto.getIntraId()).orElseThrow(MemberException.NoMemberException::new);
        final Location location = locationRepository.findByMember(member);

        location.setCustomLocation(updateCustomLocationDto.getCustomLocation());

        return ResponseLocationDto.builder().location(location).build();
    }
}
