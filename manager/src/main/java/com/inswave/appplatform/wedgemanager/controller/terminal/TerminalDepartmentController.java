package com.inswave.appplatform.wedgemanager.controller.terminal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.inswave.appplatform.Constants;
import com.inswave.appplatform.service.rdbdao.StandardServiceHelper;
import com.inswave.appplatform.wedgemanager.domain.organization.Department;
import com.inswave.appplatform.wedgemanager.domain.organization.DepartmentRepository;
import com.inswave.appplatform.wedgemanager.domain.organization.Organization;
import com.inswave.appplatform.wedgemanager.domain.organization.OrganizationRepository;
import com.inswave.appplatform.wedgemanager.domain.terminal.wrapper.TerminalDepartmentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/api/wem/terminal/department")
public class TerminalDepartmentController {

    private DepartmentRepository   departmentRepository;
    private OrganizationRepository organizationRepository;

    public TerminalDepartmentController(DepartmentRepository departmentRepository, OrganizationRepository organizationRepository) {
        this.departmentRepository = departmentRepository;
        this.organizationRepository = organizationRepository;
    }

    @PostMapping(path = "/select")
    public ResponseEntity<TerminalDepartmentVO> select(@RequestBody JsonNode req) {
        String departCode = req.get("departCode").asText();
        return ResponseEntity.ok(TerminalDepartmentVO.from(departmentRepository.findById(departCode).orElse(null)));
    }

    @PostMapping(path = "/select-all")
    public ResponseEntity<Map<String, Object>> selectAll(@RequestBody JsonNode req) {
        JsonNode pageable = req.get("pageable");
        PageRequest pageRequest = StandardServiceHelper.toPageRequest(pageable);
        Page<Department> departments = departmentRepository.findAll(pageRequest);

        List<TerminalDepartmentVO> departmentVOs = departments.getContent().stream().map(TerminalDepartmentVO::from).collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();

        result.put(Constants.TAG_TABLE_ENTITY_ROWS, departmentVOs);
        result.put(Constants.TAG_PAGE_ROW_COUNT, departments.getTotalElements());
        result.put(Constants.TAG_PAGE_NUMBER, departments.getNumber());
        result.put(Constants.TAG_PAGE_SIZE, departments.getSize());
        result.put(Constants.TAG_PAGE_COUNT, departments.getTotalPages());

        return ResponseEntity.ok(result);
    }

    @PostMapping(path = "/save-all")
    public ResponseEntity saveAll(@RequestBody JsonNode req) throws Exception {
        ArrayNode deleted = (ArrayNode) req.get("deleted");
        ArrayNode updated = (ArrayNode) req.get("updated");
        ArrayNode inserted = (ArrayNode) req.get("inserted");

        if (deleted != null) {
            deleted.forEach(obj -> {
                String departCode = obj.get("departCode").asText();
                departmentRepository.deleteById(departCode);
            });
        }
        if (updated != null) {
            updated.forEach(obj -> {
                String departCode = obj.get("departCode").asText();
                Optional<Department> registeredDept = departmentRepository.findById(departCode);
                Department inputDept = Department.toMap(obj);
                if (registeredDept.isPresent()) {
                    registeredDept.get().bind(inputDept);
                    registeredDept.get().setUpdateDate(new Date());
                    departmentRepository.save(registeredDept.get());
                }
            });
        }
        if (inserted != null) {
            inserted.forEach(obj -> {
                Department inputDept = Department.toMap(obj);
                inputDept.setCreateDate(new Date());
                departmentRepository.save(inputDept);
            });
        }
        saveOrganization();

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/insert")
    public ResponseEntity insert(@RequestBody JsonNode req) throws Exception {
        String departCode = req.get("departCode").asText();
        Optional<Department> registeredDept = departmentRepository.findById(departCode);
        if (registeredDept.isPresent()) {
            throw new Exception("DepartCode '" + departCode + "' is already exists.");
        }
        Department newDept = Department.toMap(req);
        newDept.setCreateDate(new Date());

        departmentRepository.save(newDept);
        saveOrganization();

        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/update")
    public ResponseEntity update(@RequestBody JsonNode req) {
        String departCode = req.get("departCode").asText();
        Optional<Department> registeredDept = departmentRepository.findById(departCode);
        if (registeredDept.isPresent()) {
            Department updatedDept = Department.toMap(req);
            registeredDept.get().bind(updatedDept);
            registeredDept.get().setUpdateDate(new Date());
            departmentRepository.save(registeredDept.get());
        }
        saveOrganization();
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "/delete")
    public ResponseEntity delete(@RequestBody JsonNode req) {
        String departCode = req.get("departCode").asText();
        departmentRepository.deleteById(departCode);
        return ResponseEntity.ok().build();
    }

    public void saveOrganization() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode deptTreeNode = mapper.createObjectNode();

        deptTreeNode.set("dlt_orgList", mapper.valueToTree(toMapList(selectFlat())));
        String deptTreeNodeStr = null;
        try {
            deptTreeNodeStr = mapper.writeValueAsString(deptTreeNode);
            Optional<Organization> org0 = organizationRepository.findById(0);
            if (org0.isPresent()) {
                organizationRepository.updateOrganizationById(0, deptTreeNodeStr);
            } else {
                organizationRepository.save(Organization.builder().id(0).org(deptTreeNodeStr).build());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public List<Map> toMapList(List<Department> departments) {
        return departments.stream().map(Department::toMap).collect(Collectors.toList());
    }

    public List<Department> selectFlat() {
        Department tree = selectTree();
        List<Department> flat = new ArrayList<>();
        flatten(tree, flat);
        return flat;
    }

    public Department selectTree() {
        List<Department> departments = departmentRepository.findAll(Sort.by("departName"));
        Optional<Department> root = departments.stream()
                                               .filter(department -> department.getUpperCode().equals("root"))
                                               .findFirst();
        linkChildren(departments, root.get());
        return root.get();
    }

    public void flatten(Department parent, List<Department> flat) {
        flat.add(parent);
        if (parent.getChildren() != null) {
            parent.getChildren().forEach(child -> {
                flatten(child, flat);
            });
        }
    }

    public void linkChildren(List<Department> all, Department parent) {

        List<Department> children = all.stream()
                                       .filter(department -> department.getUpperCode().equals(parent.getDepartCode()))
                                       .sorted((o1, o2) -> (o1.getDepartName()).compareToIgnoreCase(o2.getDepartName()))
                                       .collect(Collectors.toList());
        parent.setChildren(children);

        for (int i = 0; i < children.size(); i++) {
            linkChildren(all, children.get(i));
        }
    }
}
