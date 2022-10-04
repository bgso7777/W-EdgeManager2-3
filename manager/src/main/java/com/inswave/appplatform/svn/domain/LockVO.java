package com.inswave.appplatform.svn.domain;

import com.inswave.appplatform.util.DateUtil;
import lombok.*;
import org.apache.subversion.javahl.types.Lock;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LockVO {
    private String owner;
    private String path;
    private String token;
    private String comment;
    private String creationDate;
    private String expirationDate;

    public static LockVO from(Lock lock) {
        Date cdt = lock.getCreationDate();
        Date edt = lock.getExpirationDate();
        return LockVO.builder()
                     .owner(lock.getOwner())
                     .path(lock.getPath())
                     .token(lock.getToken())
                     .comment(lock.getComment())
                     .creationDate(cdt == null ? null : DateUtil.getDate(cdt, "yyyy-MM-dd hh:mm:ss"))
                     .expirationDate(edt == null ? null : DateUtil.getDate(edt, "yyyy-MM-dd hh:mm:ss"))
                     .build();
    }
}
