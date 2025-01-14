package nextstep.subway.path.ui;

import nextstep.subway.auth.domain.AuthenticationPrincipal;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.application.MapService;
import nextstep.subway.path.dto.PathResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PathController {

    private MapService mapService;

    public PathController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/paths")
    public ResponseEntity<PathResponse> findShortestPath(@AuthenticationPrincipal(required = false) LoginMember loginMember, @RequestParam Long source, @RequestParam Long target) {
        return ResponseEntity.ok(mapService.findShortestPath(loginMember, source, target));
    }
}
