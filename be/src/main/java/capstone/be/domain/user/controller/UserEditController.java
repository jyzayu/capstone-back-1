
package capstone.be.domain.user.controller;

import capstone.be.domain.user.dto.EmailEditDto;
import capstone.be.domain.user.dto.NicknameEditDto;
import capstone.be.domain.user.service.EditUserService;
import capstone.be.global.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user/update")
public class UserEditController {
    private final JwtProvider jwtProvider;
    private final EditUserService editUserService;

    //닉네임 변경
    @PatchMapping("/nickname")
    public ResponseEntity<NicknameEditDto> updateNickname(@RequestBody NicknameEditDto request, HttpServletRequest tokenRequest) {
        String accessToken = jwtProvider.resolveToken(tokenRequest);
        // 엑세스 토큰으로 userId를 받아서 유저 찾기
        Long userId = Long.parseLong(jwtProvider.getSubjects(accessToken));

        NicknameEditDto newNickname = editUserService.editNickname(userId, request.getNickname());

        return ResponseEntity.ok(newNickname);
    }

    //이메일 변경
    @PatchMapping("/email")
    public ResponseEntity<EmailEditDto> updateEmail(@RequestBody EmailEditDto request, HttpServletRequest tokenRequest) {
        String accessToken = jwtProvider.resolveToken(tokenRequest);
        Long userId = Long.parseLong(jwtProvider.getSubjects(accessToken));

        EmailEditDto newEmail = editUserService.editEmail(userId, request.getEmail());

        return ResponseEntity.ok(newEmail);
    }

}