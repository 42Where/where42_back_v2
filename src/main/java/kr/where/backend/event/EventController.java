package kr.where.backend.event;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventController {
    @PostMapping("/event")
    public int checkEvent(HttpServletRequest req, HttpServletResponse res,
                          @CookieValue(value = "ID", required = false) String key, @RequestParam String param) {
        String token42 = tokenService.findAccessToken(res, key);
        Member member = memberService.findBySessionWithToken(req, token42);
        String place;
        if (param.equals("khq5n6r7"))
            place = "A";
        else if (param.equals("ui3zzgbl"))
            place = "B";
        else if (param.equals("0z3oprmj"))
            place = "C";
        else if (param.equals("7cz3ex68"))
            place = "D";
        else if (param.equals("z51m8l8e"))
            place = "E";
        else if (param.equals("barh6z2c"))
            place = "F";
        else if (param.equals("4rq01zpy"))
            place = "G";
        else if (param.equals("gfppifr1"))
            place = "H";
        else if (param.equals("olsmt445"))
            place = "I";
        else if (param.equals("t8w0193r"))
            place = "J";
        else
            throw new NotFoundException();
        int ret = eventService.checkEvent(member.getName(), place);
        return ret;
    }
}
