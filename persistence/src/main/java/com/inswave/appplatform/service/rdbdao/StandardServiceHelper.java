package com.inswave.appplatform.service.rdbdao;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.inswave.appplatform.service.domain.StandardDomain;
import com.inswave.appplatform.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.criteria.Predicate;
import java.time.ZonedDateTime;
import java.util.*;

@Slf4j
@Service
public class StandardServiceHelper {

    @Autowired
    @Qualifier("wemEntityManager")
    protected EntityManager                                      entityManager;
    @Autowired
    private   AnnotationConfigServletWebServerApplicationContext applicationContext;
    private   Map<String, Map<String, Object>>                   entityRepositoryMap = new HashMap<>(); // EntityName, [EntityClass, RepositoryClass]

    private final String   KEY_DOMAIN_CLASS        = "domainClass";
    private final String   KEY_REPOSITORY_INSTANCE = "repositoryInstance";
    private final String[] LIST_EXCLUDE_ENTITY     = { "LogEvent", "VIplist" };

    @PostConstruct
    public void postConstruct() {
        DefaultListableBeanFactory defaultListableBeanFactory = applicationContext.getDefaultListableBeanFactory();
        Repositories repositories = new Repositories(defaultListableBeanFactory);
        //        Repositories repositories = new Repositories(applicationContext.getBeanFactory());

        Iterator<Class<?>> it = repositories.iterator();
        while (it.hasNext()) {
            Class<?> domainClass = it.next();
            if (Arrays.asList(LIST_EXCLUDE_ENTITY).contains(domainClass.getSimpleName())) {   // 제외클래스
                continue;
            }
            //            RepositoryInformation repositoryInformation = repositories.getRepositoryInformationFor(domainClass).get();
            //            JpaEntityInformation jpaEntityInformation = JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager);
            //            EntityInformation entityInformation = repositories.getEntityInformationFor(domainClass);
            //            log.info("{} / {} / {} / {}",
            //                     domainClass.getPackage(),
            //                     jpaEntityInformation.getEntityName(),
            //                     jpaEntityInformation.getIdAttributeNames(),
            //                     jpaEntityInformation.getIdType().getName());

            Repository repository = (Repository) repositories.getRepositoryFor(domainClass).get();
            entityRepositoryMap.put(domainClass.getSimpleName(), new HashMap<String, Object>() {
                {
                    put(KEY_DOMAIN_CLASS, domainClass);
                    put(KEY_REPOSITORY_INSTANCE, repository);
                }
            });
        }
    }

    public Class findDomainClass(String entityName) {
        return (Class) entityRepositoryMap.get(entityName).get(KEY_DOMAIN_CLASS);
    }

    public JpaRepository findJpaRepository(String entityName) {
        return (JpaRepository) entityRepositoryMap.get(entityName).get(KEY_REPOSITORY_INSTANCE);
    }

    public SimpleJpaRepository createSimpleJpaRepository(String entityName) {
        return new SimpleJpaRepository((Class) entityRepositoryMap.get(entityName).get(KEY_DOMAIN_CLASS), entityManager);
    }

    protected List toEntityList(String tableEntityName, JsonNode jsonNode) {
        ObjectMapper mapper = new ObjectMapper();
        List rtn = new ArrayList();
        try {
            Class clazz = findDomainClass(tableEntityName);
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            rtn = mapper.readValue(jsonNode.toString(), listType);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return rtn;
    }

    public static Specification toSpecification(JsonNode jsonNode) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            ArrayNode whereClauses = (ArrayNode) jsonNode;
            whereClauses.forEach(where -> {
                String operator = where.get("operator").asText();
                String field = where.get("field").asText();
                switch (operator) {
                case "isEmpty":
                    predicates.add(cb.isEmpty(root.get(field)));
                    break;
                case "isNotEmpty":
                    predicates.add(cb.isNotEmpty(root.get(field)));
                    break;
                case "isNull":
                    predicates.add(cb.isNull(root.get(field)));
                    break;
                case "isNotNull":
                    predicates.add(cb.isNotNull(root.get(field)));
                    break;
                case "equal":
                    predicates.add(cb.equal(root.get(field), where.get("value").asText()));
                    break;
                case "equalBoolean":
                    predicates.add(cb.equal(root.get(field), where.get("value").asBoolean()));
                    break;
                case "like":
                    predicates.add(cb.like(root.get(field), where.get("value").asText()));
                    break;
                case "notEqual":
                    predicates.add(cb.notEqual(root.get(field), where.get("value").asText()));
                    break;
                case "greaterThan":
                    predicates.add(cb.greaterThan(root.get(field), where.get("value").asText()));
                    break;
                case "greaterThanOrEqualTo":
                    predicates.add(cb.greaterThanOrEqualTo(root.get(field), where.get("value").asText()));
                    break;
                case "lessThan":
                    predicates.add(cb.lessThan(root.get(field), where.get("value").asText()));
                    break;
                case "lessThanOrEqualTo":
                    predicates.add(cb.lessThanOrEqualTo(root.get(field), where.get("value").asText()));
                    break;
                case "between":
                    predicates.add(cb.between(root.get(field), where.get("value").get("startValue").asText(), where.get("value").get("endValue").asText()));
                    break;
                case "betweenDatetime":
                    predicates.add(cb.between(root.get(field), DateUtil.getDate(where.get("value").get("startValue").asText()), DateUtil.getDate(where.get("value").get("endValue").asText())));
                    break;
                case "greaterThanDatetime":
                    predicates.add(cb.greaterThan(root.get(field), DateUtil.getDate(where.get("value").asText())));
                    break;
                case "greaterThanOrEqualToDatetime":
                    predicates.add(cb.greaterThanOrEqualTo(root.get(field), DateUtil.getDate(where.get("value").asText())));
                    break;
                case "lessThanDatetime":
                    predicates.add(cb.lessThan(root.get(field), DateUtil.getDate(where.get("value").asText())));
                    break;
                case "lessThanOrEqualToDatetime":
                    predicates.add(cb.lessThanOrEqualTo(root.get(field), DateUtil.getDate(where.get("value").asText())));
                    break;
                }
            });
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static PageRequest toPageRequest(JsonNode jsonNode) {
        Integer page = jsonNode.get("page").asInt(0);             // page : 검색을 원하는 페이지 번호(0부터 시작)
        Integer size = jsonNode.get("size").asInt(Integer.MAX_VALUE);       // size : 한페이지 개수
        if (size == -1) {
            size = Integer.MAX_VALUE;  // size -1 설정 시 1개 행 최대 값 조회 : 실제로는 페이지당 최대행 수가 Integer.MAX_VALUE로 동작
        }
        Sort sort = toSort(jsonNode.get("sort"));                           // sort : 정렬방식(sort=ename, asc&sort=sal, desc)
        return PageRequest.of(page, size, sort);
    }

    public static Sort toSort(JsonNode jsonNode) {
        ArrayNode sortList = (ArrayNode) jsonNode;
        List<Sort.Order> orderList = new ArrayList<>();
        if (sortList != null) {
            sortList.forEach(sort -> {
                String column = sort.get("column").asText();
                String direction = sort.get("direction").asText("ASC");
                if (column != null) {
                    orderList.add(new Sort.Order(direction.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC, column));
                }
            });
        }

        if (orderList.size() == 0) {
            orderList.add(new Sort.Order(Sort.Direction.DESC, "createDate"));
        }
        return Sort.by(orderList);
    }

    protected void insertAudit(List<Object> inserted, String userId, String userName) {
        inserted.forEach(o -> {
            StandardDomain sd = (StandardDomain) o;
            sd.setCreateDate(ZonedDateTime.now());
            sd.setCreateUserName(userName);
            sd.setCreateUserId(userId);
        });
    }

    protected void updateAudit(List<Object> updated, String userId, String userName) {
        updated.forEach(o -> {
            StandardDomain sd = (StandardDomain) o;
            sd.setUpdateDate(ZonedDateTime.now());
            sd.setUpdateUserName(userName);
            sd.setUpdateUserId(userId);
        });
    }
}
