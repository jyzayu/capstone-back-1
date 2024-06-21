package capstone.be.domain.like.controller;

import capstone.be.domain.like.dto.LikeRequestDto;
import capstone.be.domain.like.service.LikeService;
import capstone.be.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/like")
public class LikeController {

    private final LikeService likeService;
    private final JwtProvider jwtProvider;

    @PostMapping("/{diaryId}")
    public ResponseEntity<?> insert(@PathVariable("diaryId") Long diaryId, HttpServletRequest httpServletRequest) throws Exception {
        String accessToken = jwtProvider.resolveToken(httpServletRequest);
        Long userId = Long.parseLong(jwtProvider.getSubjects(accessToken));
        likeService.insert(diaryId, userId);
        return ResponseEntity.ok().build();
    }

}