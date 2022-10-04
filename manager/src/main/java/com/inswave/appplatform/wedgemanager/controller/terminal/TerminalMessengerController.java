package com.inswave.appplatform.wedgemanager.controller.terminal;

import com.fasterxml.jackson.databind.JsonNode;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.mattermost.service.MattermostService;
import com.inswave.appplatform.util.Crypto;
import com.inswave.appplatform.wedgemanager.domain.organization.Department;
import com.inswave.appplatform.wedgemanager.domain.organization.DepartmentRepository;
import com.inswave.appplatform.wedgemanager.domain.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.model.User;
import net.bis5.mattermost.model.UserAccessToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/api/msg")
public class TerminalMessengerController {

    private String               aes256Secret;
    private MattermostService    mattermostService;
    private UserRepository       userRepository;
    private DepartmentRepository departmentRepository;

    public TerminalMessengerController(@Value("${wedgemanager.mattermost.api.aes256Secret:#{'mattermost.proxy!aes256Secret...'}}") String aes256Secret,
                                       MattermostService mattermostService,
                                       UserRepository userRepository,
                                       DepartmentRepository departmentRepository) {
        this.aes256Secret = aes256Secret;
        this.mattermostService = mattermostService;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping("/terminal-msg-login")
    public String login(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        String token = servletRequest.getParameter("token");
        if (token != null && token.length() > 0) {
            try {
                String email = Crypto.decryptAES256(token, aes256Secret);

                User user = mattermostService.getUserByEmail(email);
                UserAccessToken userAccessToken = mattermostService.getUserAccessToken(user.getId(), "Terminal Access Token");
                String cookie_MMAUTHTOKEN = userAccessToken.getToken();
                String cookie_MMUSERID = user.getId();
                Cookie mmauthtoken = new Cookie("MMAUTHTOKEN", cookie_MMAUTHTOKEN);
                mmauthtoken.setPath("/");

                Cookie mmuserid = new Cookie("MMUSERID", cookie_MMUSERID);
                mmuserid.setPath("/");

                servletResponse.addCookie(mmauthtoken);
                servletResponse.addCookie(mmuserid);

                servletResponse.sendRedirect("/");  // Cookie 들고 다시 로그인 시도.
                return "Login success";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "Login Failed";
    }

    @GetMapping("/terminal-msg-logout")
    public String logout(HttpServletRequest servletRequest, HttpServletResponse servletResponse) {
        String token = servletRequest.getParameter("token");
        try {
            String email = Crypto.decryptAES256(token, aes256Secret);
            User user = mattermostService.getUserByEmail(email);
            mattermostService.logout(user.getId());

            Cookie mmauthtoken = new Cookie("MMAUTHTOKEN", null);
            mmauthtoken.setPath("/");

            Cookie mmuserid = new Cookie("MMUSERID", null);
            mmuserid.setPath("/");

            servletResponse.addCookie(mmauthtoken);
            servletResponse.addCookie(mmuserid);

            return "Logout success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Logout Failed";
    }

    @PostMapping(path = "/generate-token")
    public ResponseEntity<Map<String, Object>> generateToken(@RequestBody JsonNode req) {
        Map<String, Object> result = new HashMap<>();

        String userId = req.get("userId").asText();
        String userName = req.get("userName").asText();
        String masterBranchCode = req.get("masterBranchCode").asText();
        String branchCode = req.get("branchCode").asText();
        String branchName = req.get("branchName").asText();
        String email = req.get("email").asText();

        // Team 등록 (관리서버 && 메신저)
        Optional<Department> optionalDepartment = departmentRepository.findByDepartName(branchName);
        if (!optionalDepartment.isPresent()) {
            Department newDepartment = departmentRepository.save(Department.builder()
                                                                           .departName(branchName)
                                                                           .departCode(branchCode)
                                                                           .departLevel(2)
                                                                           .upperCode("000000")    // KPIC 기준
                                                                           .build());
            //            if (!mattermostService.isTeamExists(branchName)) {
            mattermostService.createTeams(new ArrayList<Department>() {
                {
                    add(newDepartment);
                }
            });
            //            }
        }

        // 사용자 등록 (관리서버 && 메신저)
        Optional<com.inswave.appplatform.wedgemanager.domain.user.User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            com.inswave.appplatform.wedgemanager.domain.user.User newUser = userRepository.save(com.inswave.appplatform.wedgemanager.domain.user.User.builder()
                                                                                                                                                     .email(email)
                                                                                                                                                     .name(userName)
                                                                                                                                                     .userId(userId)
                                                                                                                                                     .department(branchCode)
                                                                                                                                                     .build());
            //            User user = mattermostService.getUserByEmail(email);
            //            if (user == null || user.getId() == null) {
            mattermostService.createUsers(new ArrayList<com.inswave.appplatform.wedgemanager.domain.user.User>() {
                {
                    add(newUser);
                }
            });
            mattermostService.addTeamMember(branchName, userId);
            //            }
        }

        try {
            result.put("token", Crypto.encryptAES256(email, aes256Secret));
            result.put(Constants.TAG_RESULT, Constants.RESULT_SUCESS);
        } catch (Exception e) {
            e.printStackTrace();
            result.put(Constants.TAG_RESULT, Constants.RESULT_FAIL);
            result.put(Constants.TAG_ERROR, Constants.ERROR_SERVICE_PROCESS_CODE);
            result.put(Constants.TAG_ERROR_DESCRIPTION, e.getMessage());
        }

        return ResponseEntity.ok(result);
    }
}
