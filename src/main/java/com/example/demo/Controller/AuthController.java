package com.example.demo.Controller;

import com.example.demo.Service.AuthService;
import com.example.demo.dto.auth.FindIdResponseDto;
import com.example.demo.dto.auth.LoginDto;
import com.example.demo.dto.auth.SmsDto;
import com.example.demo.dto.jwt.TokenDto;
import com.example.demo.dto.jwt.TokenReqDto;
import com.example.demo.dto.member.MemberRequestDto;
import com.example.demo.dto.member.MemberResponseDto;
import io.micrometer.core.instrument.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Api(tags = "AuthController : 로그인/회원가입 관련 컨트롤러")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "회원가입")
    @ApiResponse(code = 200, message = "회원가입 성공")
    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto requestDto) {
        return ResponseEntity.ok(authService.signup(requestDto));
    }
    @Operation(summary = "로그인")
    @ApiResponse(code = 200, message = "로그인 성공")
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginrequest) {
        return ResponseEntity.ok(authService.login(loginrequest));
    }

    @Operation(summary = "로그아웃")
    @GetMapping("/logout")
    @ApiResponse(code = 200 ,message = "로그아웃")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/";
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenReqDto tokenRequestDto) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDto));
    }

    @GetMapping("/check/sendSMS")
    public @ResponseBody ResponseEntity<SmsDto> sendSMS(@RequestParam(value="to") String to){
        return ResponseEntity.ok(authService.PhoneNumberCheck(to));
    }

    @GetMapping("/check/verifySMS")
    public @ResponseBody ResponseEntity<SmsDto> verifySMS(@RequestParam(value="to") String to,@RequestParam(value="code") String code){
        return ResponseEntity.ok(authService.verifySms(code,to));
    }

    @GetMapping("/findId")
    @ApiOperation(value = "아이디 찾기")
    public @ResponseBody ResponseEntity<Boolean> findID(@RequestParam(value="to") String phonenumber) {
        return ResponseEntity.ok((authService.findID(phonenumber)));
    }

    @GetMapping("/findId/veritfySMS")
    public @ResponseBody ResponseEntity<FindIdResponseDto> findID(@RequestParam(value="to") String to, @RequestParam(value="code") String code){
        return ResponseEntity.ok(authService.findIdverifySms(code,to));
    }
}