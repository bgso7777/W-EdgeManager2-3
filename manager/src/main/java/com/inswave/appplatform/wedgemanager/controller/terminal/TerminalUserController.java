package com.inswave.appplatform.wedgemanager.controller.terminal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.service.rdbdao.StandardServiceHelper;
import com.inswave.appplatform.util.Crypto;
import com.inswave.appplatform.wedgemanager.domain.device.Device;
import com.inswave.appplatform.wedgemanager.domain.device.DeviceRepository;
import com.inswave.appplatform.wedgemanager.domain.organization.Department;
import com.inswave.appplatform.wedgemanager.domain.organization.DepartmentRepository;
import com.inswave.appplatform.wedgemanager.domain.terminal.wrapper.TerminalUserVO;
import com.inswave.appplatform.wedgemanager.domain.user.User;
import com.inswave.appplatform.wedgemanager.domain.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Predicate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/api/wem/terminal/user")
public class TerminalUserController {

    private       UserRepository       userRepository;
    private       DepartmentRepository departmentRepository;
    private       DeviceRepository     deviceRepository;
    private       String               aes256Secret;
    public static int                  TERMINAL_LOGIN_EXPIRATION_PERIOD = 600000;

    public TerminalUserController(UserRepository userRepository,
                                  DepartmentRepository departmentRepository,
                                  DeviceRepository deviceRepository,
                                  @Value("${wedgemanager.mattermost.api.aes256Secret:#{'mattermost.proxy!aes256Secret...'}}") String aes256Secret,
                                  @Value("${wedgemanager.terminal.login.expirationPeriod:600000}") int terminalExpirationPeriod) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.deviceRepository = deviceRepository;
        this.aes256Secret = aes256Secret;
        TERMINAL_LOGIN_EXPIRATION_PERIOD = terminalExpirationPeriod;
    }

    public ResponseEntity changeUserLoginStatus(String userId, LoginReportStatus loginReportStatus) {
        //TODO : 상태관리 갱신
        //TODO : 사용자 조회 시 로그인 상태 표시
        Optional<User> optionalUser = userRepository.findById(userId);
        optionalUser.ifPresent(user -> {
            switch (loginReportStatus) {
            case LOGIN:
                if (user.getLastLogoutDate() == null) {
                    if (user.getLastLoginDate() != null) {
                        user.setLastLogoutDate(user.getLastLoginDate());
                    } else {
                        user.setLastLogoutDate(ZonedDateTime.from(user.getCreateDate().toInstant()));
                    }
                }
                user.setLastLoginDate(ZonedDateTime.now());
                user.setLastLoginPingDate(ZonedDateTime.now());
                break;
            case LOGOUT:
                user.setLastLogoutDate(ZonedDateTime.now());
                break;
            case PING:
                user.setLastLoginPingDate(ZonedDateTime.now());
                break;
            }
            userRepository.save(user);
        });

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/health")
    public ResponseEntity loginHealth(@RequestBody JsonNode req) {
        String userId = req.get("userId").asText();
        return changeUserLoginStatus(userId, LoginReportStatus.PING);
    }

    @PostMapping(path = "/login")
    public ResponseEntity login(@RequestBody JsonNode req) {
        String userId = req.get("userId").asText();
        return changeUserLoginStatus(userId, LoginReportStatus.LOGIN);
    }

    @PostMapping(path = "/logout")
    public ResponseEntity logout(@RequestBody JsonNode req) {
        String userId = req.get("userId").asText();
        return changeUserLoginStatus(userId, LoginReportStatus.LOGOUT);
    }

    @PostMapping(path = "/select")
    public ResponseEntity<TerminalUserVO> select(@RequestBody JsonNode req) {
        String id = req.get("userId").asText();
        TerminalUserVO terminalUserVO = TerminalUserVO.from(userRepository.findById(id).orElse(null));
        if (terminalUserVO != null) {
            Optional<Department> optionalDepartment = departmentRepository.findById(terminalUserVO.getDepartment());
            optionalDepartment.ifPresent(department -> {
                terminalUserVO.setDepartName(department.getDepartName());
            });
        }
        return ResponseEntity.ok(terminalUserVO);
    }

    @PostMapping(path = "/select-all")
    public ResponseEntity<Map<String, Object>> selectAll(@RequestBody JsonNode req) {
        JsonNode where = req.get("where");
        JsonNode pageable = req.get("pageable");

        ArrayNode whereClauses = (ArrayNode) where;
        AtomicBoolean hasLoginFilter = new AtomicBoolean(false);
        AtomicBoolean hasLoginFilterValue = new AtomicBoolean(false);
        for (int i = 0, iL = whereClauses.size(); i < iL; i++) {
            JsonNode wr = whereClauses.get(i);
            String field = wr.get("field").asText();
            if (field.equalsIgnoreCase("loginYn")) {
                hasLoginFilter.set(true);
                hasLoginFilterValue.set(wr.get("value").asBoolean());
                whereClauses.remove(i);
                break;
            }
        }

        PageRequest pageRequest = StandardServiceHelper.toPageRequest(pageable);
        Specification specification = StandardServiceHelper.toSpecification(where);
        if (hasLoginFilter.get()) {
            ZonedDateTime expireDate = ZonedDateTime.now().minus(TerminalUserController.TERMINAL_LOGIN_EXPIRATION_PERIOD, ChronoUnit.MILLIS);
            specification = specification.and((root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();
                if (hasLoginFilterValue.get()) {
                    predicates.add(cb.and(cb.greaterThan(root.get("lastLoginPingDate"), expireDate),
                                          cb.greaterThan(root.get("lastLoginPingDate"), root.get("lastLogoutDate"))));
                } else {
                    predicates.add(cb.or(cb.isNull(root.get("lastLoginPingDate")),
                                         cb.lessThan(root.get("lastLoginPingDate"), expireDate),
                                         cb.greaterThan(root.get("lastLogoutDate"), root.get("lastLoginPingDate"))));
                }
                return cb.and(predicates.toArray(new Predicate[0]));
            });
        }
        Page<User> userList = userRepository.findAll(specification, pageRequest);

        List<TerminalUserVO> userVoList = userList.getContent()
                                                  .stream()
                                                  .map(TerminalUserVO::from)
                                                  .collect(Collectors.toList());

        List<Department> departments = departmentRepository.findAll();
        Map<String, String> departmentsMap = departments.stream()
                                                        .collect(Collectors.toMap(department -> department.getDepartCode(),
                                                                                  department -> department.getDepartName()));
        List<Device> devices = deviceRepository.findAll();
        Map<String, String> ipByUserId = devices.stream()
                                                .collect(Collectors.toMap(Device::getUserId, Device::getIp, (p1, p2) -> p1));

        userVoList.forEach(terminalUserVO -> {
            terminalUserVO.setDepartName(departmentsMap.get(terminalUserVO.getDepartment()));
            terminalUserVO.setTermIp(ipByUserId.get(terminalUserVO.getUserId()));
        });

        Map<String, Object> result = new HashMap<>();

        result.put(Constants.TAG_TABLE_ENTITY_ROWS, userVoList);
        result.put(Constants.TAG_PAGE_ROW_COUNT, userList.getTotalElements());
        result.put(Constants.TAG_PAGE_NUMBER, userList.getNumber());
        result.put(Constants.TAG_PAGE_SIZE, userList.getSize());
        result.put(Constants.TAG_PAGE_COUNT, userList.getTotalPages());

        return ResponseEntity.ok(result);
    }

    @PostMapping(path = "/save-all")
    public ResponseEntity saveAll(@RequestBody JsonNode req) throws Exception {
        ArrayNode deleted = (ArrayNode) req.get("deleted");
        ArrayNode updated = (ArrayNode) req.get("updated");
        ArrayNode inserted = (ArrayNode) req.get("inserted");

        if (deleted != null) {
            deleted.forEach(obj -> {
                String userId = obj.get("userId").asText();
                userRepository.deleteById(userId);
            });
        }
        if (updated != null) {
            updated.forEach(obj -> {
                String userId = obj.get("userId").asText();
                Optional<User> registeredUser = userRepository.findById(userId);
                User inputUser = User.from(obj);
                if (registeredUser.isPresent()) {
                    registeredUser.get().bind(inputUser);
                    registeredUser.get().setUpdateDate(new Date());
                    userRepository.save(registeredUser.get());
                }
            });
        }
        if (inserted != null) {
            inserted.forEach(obj -> {
                User inputUser = User.from(obj);
                inputUser.setCreateDate(new Date());
                userRepository.save(inputUser);
            });
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/insert")
    public ResponseEntity insert(@RequestBody JsonNode req) throws Exception {
        String userId = req.get("userId").asText();
        Optional<User> registeredUser = userRepository.findById(userId);
        if (registeredUser.isPresent()) {
            throw new Exception("UserId '" + userId + "' is already exists.");
        }
        User newUser = User.from(req);
        newUser.setCreateDate(new Date());

        return ResponseEntity.ok(userRepository.save(newUser));
    }

    @PostMapping(path = "/update")
    public ResponseEntity update(@RequestBody JsonNode req) {
        String userId = req.get("userId").asText();
        Optional<User> registeredUser = userRepository.findById(userId);
        if (registeredUser.isPresent()) {
            User updatedUser = User.from(req);
            registeredUser.get().bind(updatedUser);
            registeredUser.get().setUpdateDate(new Date());
            userRepository.save(registeredUser.get());
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/delete")
    public ResponseEntity delete(@RequestBody JsonNode req) {
        String id = req.get("userId").asText();
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/generate-token")
    public ResponseEntity<Map<String, Object>> generateToken(@RequestBody JsonNode req) {
        Map<String, Object> result = new HashMap<>();

        String email = req.get("email").asText();
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

enum LoginReportStatus {
    LOGIN, LOGOUT, PING
}