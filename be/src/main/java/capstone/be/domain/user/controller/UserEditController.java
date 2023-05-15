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

//Todo : 회원 탈퇴는 uri 에 맞게 SignController로 옮겨야 할 것 같음.(일단 코드 리뷰할 때 보기 편하도록 한 파일에 넣어뒀음
// + 유저 정보 delete 후 토큰은 어떻게 처리해야 하는지..?
// 유저를 찾을 수 없을 때 발생하는 exception이 에러코드에 적힌 AUTH_010에 해당하는 것 같다. yml에 적힌 이름을 바꾸는 게 좋지 않을까?

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

    //회원 탈퇴 -> 일단 deltet로 유저 정보만 삭제하도록 구현
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(HttpServletRequest request){
        String accessToken = jwtProvider.resolveToken(request);
        Long userId = Long.parseLong(jwtProvider.getSubjects(accessToken));
        editUserService.deleteUser(userId);

        return ResponseEntity.ok("");
    }
}
