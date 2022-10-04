package com.inswave.appplatform.service;

import com.inswave.appplatform.wedgemanager.domain.organization.Department;
import com.inswave.appplatform.wedgemanager.domain.organization.DepartmentRepository;
import com.inswave.appplatform.wedgemanager.domain.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import net.bis5.mattermost.client4.ApiResponse;
import net.bis5.mattermost.client4.MattermostClient;
import net.bis5.mattermost.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

@Slf4j
@Service
public class MattermostService {

    private String               serverUrl;
    private String               clientLoginId;
    private String               clientLoginPassword;
    private String               logLevel;
    private UserRepository       userRepository;
    private DepartmentRepository departmentRepository;

    public MattermostService(@Value("${wedgemanager.mattermost.api.serverUrl:#{'http://localhost:8065'}}")
                             String serverUrl,
                             @Value("${wedgemanager.mattermost.api.logLevel:#{null}}")
                             String logLevel,
                             @Value("${wedgemanager.mattermost.api.clientLoginId:#{'admin'}}")
                             String clientLoginId,
                             @Value("${wedgemanager.mattermost.api.clientLoginPassword:#{'admin'}}")
                             String clientLoginPassword,
                             UserRepository userRepository,
                             DepartmentRepository departmentRepository
    ) {
        this.serverUrl = serverUrl;
        this.logLevel = logLevel;
        this.clientLoginId = clientLoginId;
        this.clientLoginPassword = clientLoginPassword;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    @PostConstruct
    public void postConstruct() {
        //        printUsers();
        //        printTeams();
        //        printTeamMembers();
        //        printChannels();
        //
        //        syncUsers();
        //        syncTeams();
    }

    public MattermostClient getClient() {
        MattermostClient.MattermostClientBuilder builder = MattermostClient.builder();
        builder.url(serverUrl)
               .ignoreUnknownProperties();
        if (logLevel != null) {
            builder.logLevel(Level.parse(logLevel));
        }

        MattermostClient client = builder.build();
        client.login(clientLoginId, clientLoginPassword);
        return client;
    }

    public synchronized void syncUsers() {
        UserList mattermostUsers = getUsers();
        List<com.inswave.appplatform.wedgemanager.domain.user.User> terminalUsers = userRepository.findAll();
        List<com.inswave.appplatform.wedgemanager.domain.user.User> unregisteredUsers = new ArrayList<>();
        terminalUsers.forEach(terminalUser -> {
            boolean isRegistered = false;
            for (int i = 0; i < mattermostUsers.size(); i++) {
                User mattermostUser = mattermostUsers.get(i);

                String email = terminalUser.getEmail();
                if (!StringUtils.isEmpty(email)) {
                    if (mattermostUser.getEmail().equalsIgnoreCase(email)) {    // 이메일 기준으로 등록여부 판별
                        isRegistered = true;
                        break;
                    }
                } else {
                    isRegistered = true;    // 메일주소 입력안된 건은 무시
                    break;
                }
            }
            if (!isRegistered) {
                unregisteredUsers.add(terminalUser);
            }
        });
        createUsers(unregisteredUsers);
    }

    public synchronized void syncTeams() {
        TeamList mattermostTeams = getTeams();
        List<Department> departments = departmentRepository.findAll();
        List<Department> unregisteredTeams = new ArrayList<>();
        departments.forEach(department -> {
            boolean isRegistered = false;
            for (int i = 0; i < mattermostTeams.size(); i++) {
                Team mattermostTeam = mattermostTeams.get(i);

                String departName = department.getDepartName();
                if (!StringUtils.isEmpty(departName)) {
                    if (mattermostTeam.getName().equalsIgnoreCase(departName)) {
                        isRegistered = true;
                        break;
                    }
                } else {
                    isRegistered = true;    // 메일 없는경우 무시
                    break;
                }
            }
            if (!isRegistered) {
                unregisteredTeams.add(department);
            }
        });
        createTeams(unregisteredTeams);
    }

    public synchronized void syncChannels(String teamId) {

    }

    public void createUsers(List<com.inswave.appplatform.wedgemanager.domain.user.User> terminalUsers) {
        MattermostClient client = getClient();
        terminalUsers.forEach(terminalUser -> {
            User user = terminalUser.to();
            if (user != null) {
                client.createUser(user);
            }
        });
        client.close();
    }

    // 부서를 팀으로 사용할 경우 : 팀(부서) -> 채널(부서)
    public void createTeams(List<Department> departments) {
        MattermostClient client = getClient();
        departments.forEach(department -> {
            Team team = department.to();
            if (team != null) {
                client.createTeam(team);
            }
        });
        client.close();
    }

    // 부서를 채널로 사용할 경우 : 팀(회사) -> 채널(부서)
    public void createChannels(String teamId, List<Department> departments) {
        MattermostClient client = getClient();
        departments.forEach(department -> {
            Channel channel = department.to(teamId, ChannelType.Open);
            if (channel != null) {
                client.createChannel(channel);
            }
        });
        client.close();
    }

    public UserList getUsers() {
        MattermostClient client = getClient();
        ApiResponse<UserList> apiResponse = client.getUsers();
        UserList userList = apiResponse.readEntity();
        client.close();
        return userList;
    }

    public TeamList getTeams() {
        MattermostClient client = getClient();
        ApiResponse<TeamList> apiResponse = client.getAllTeams();
        TeamList teamList = apiResponse.readEntity();
        client.close();
        return teamList;
    }

    public TeamMemberList getTeamMembers(String teamId) {
        MattermostClient client = getClient();
        ApiResponse<TeamMemberList> apiResponse = client.getTeamMembers(teamId);
        TeamMemberList teamMemberList = apiResponse.readEntity();
        client.close();
        return teamMemberList;
    }

    public ChannelList getChannels(String teamId) {
        MattermostClient client = getClient();
        ApiResponse<ChannelList> apiResponse = client.getPublicChannelsForTeam(teamId);
        ChannelList channelList = apiResponse.readEntity();
        client.close();
        return channelList;
    }

    public User login(String loginId, String password) {
        MattermostClient client = getClient();

        ApiResponse<User> apiResponse = client.login(loginId, password);
        User user = apiResponse.readEntity();
        client.close();

        return user;
    }

    public Boolean logout(String userId) {
        MattermostClient client = getClient();

        ApiResponse<Boolean> apiResponse = client.revokeAllActiveSessionForUser(userId);
        Boolean succ = apiResponse.readEntity();
        client.close();

        return succ;
    }

    public void printUsers() {
        getUsers().forEach(user -> {
            log.info("user : {}", user.toString());
        });
    }

    public void printTeams() {
        getTeams().forEach(team -> {
            log.info("team : {}", team.toString());
        });
    }

    public void printTeamMembers() {
        getTeams().forEach(team -> {
            getTeamMembers(team.getId()).forEach(teamMember -> {
                log.info("teamMember : {}", teamMember.toString());
            });
        });
    }

    public void printChannels() {
        getTeams().forEach(team -> {
            getChannels(team.getId()).forEach(channel -> {
                log.info("channel : {}", channel.toString());
            });
        });
    }

    public User getUserByEmail(String email) {
        MattermostClient client = getClient();

        ApiResponse<User> apiResponse = client.getUserByEmail(email);
        User user = apiResponse.readEntity();
        client.close();

        return user;
    }

    public User getUserByUsername(String username) {
        MattermostClient client = getClient();

        ApiResponse<User> apiResponse = client.getUserByUsername(username);
        User user = apiResponse.readEntity();
        client.close();

        return user;
    }

    public UserAccessToken getUserAccessToken(String userId, String description) {
        MattermostClient client = getClient();

        ApiResponse<UserAccessToken> apiResponse = client.createUserAccessToken(userId, description);
        UserAccessToken userAccessToken = apiResponse.readEntity();
        client.close();

        return userAccessToken;
    }

    public TeamMember addTeamMember(String teamName, String userName) {
        MattermostClient client = getClient();

        ApiResponse<Team> apiResponseTeam = client.getTeamByName(teamName);
        Team team = apiResponseTeam.readEntity();
        ApiResponse<User> apiResponseUser = client.getUserByUsername(userName);
        User user = apiResponseUser.readEntity();

        ApiResponse<TeamMember> apiResponse = client.addTeamMember(new TeamMember(team.getId(), user.getId()));
        TeamMember teamMember = apiResponse.readEntity();
        client.close();

        return teamMember;
    }

    public boolean isTeamExists(String teamName) {
        MattermostClient client = getClient();

        ApiResponse<Team> apiResponse = client.getTeamByName(teamName);
        Team team = apiResponse.readEntity();
        client.close();

        if (team != null || team.getId() != null) {
            return true;
        }
        return false;
    }
}
